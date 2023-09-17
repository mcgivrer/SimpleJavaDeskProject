package com.snapgames.demo.test001.scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import com.snapgames.core.App;
import com.snapgames.core.entity.*;
import com.snapgames.core.gfx.Renderer;
import com.snapgames.core.io.InputHandler;
import com.snapgames.core.physic.Material;
import com.snapgames.core.physic.PhysicEngine;
import com.snapgames.core.physic.Vector2D;
import com.snapgames.core.physic.World;
import com.snapgames.core.scene.AbstractScene;
import com.snapgames.core.utils.ResourceManager;
import com.snapgames.demo.test001.behaviors.EnemyTrackingBehavior;
import com.snapgames.demo.test001.io.DemoInputListener;

public class DemoScene extends AbstractScene {

    int score = 0;
    int lives = 2;
    double maxEnergy = 100;
    double maxMana = 100;
    double energy = 70;
    double mana = 80;

    Color shadowColor = new Color(0.2f, 0.2f, 0.2f, 0.7f);

    BufferedImage heart;
    private boolean levelEnd;
    Font scoreFont;

    public DemoScene(App app) {
        super(app, "Demonstration Scene");
    }

    @Override
    public void load() {
        BufferedImage tiles = (BufferedImage) ResourceManager.add("/images/tiles01.png");
        heart = tiles.getSubimage(0, 6 * 16, 16, 16);

        Font textFont = (Font) ResourceManager.add("/fonts/tino/Tinos for Powerline.ttf");
        scoreFont = textFont.deriveFont(Font.BOLD, 22.0f);
    }

    @Override
    public void create(App app) {
        PhysicEngine physicEngine = app.getPhysicEngine();
        Renderer renderer = app.getRenderer();

        Rectangle2D playArea = physicEngine.getWorld().getPlayArea();
        score = 0;
        lives = 2;

        GameObject player = (GameObject) new GameObject("player")
                .setColor(Color.GREEN.darker())
                .setFillColor(Color.GREEN)
                .setPosition(playArea.getWidth() * 0.5, playArea.getHeight() * 0.5)
                .setSize(16, 16)
                .setPriority(1)
                .setMass(1.0)
                .setMaterial(new Material("body", 1.2, 0.78, 0.97))
                .addAttribute("speed", 5.0)
                .addAttribute("energy", maxEnergy)
                .addAttribute("mana", maxMana)
                .addAttribute("life", lives);

        addEntity(player);

        addEnemies("enemy", 10, 10.0);

        Camera cam01 = new Camera("cam01")
                .setViewport(new Rectangle2D.Double(0, 0, 300, 200))
                .setTarget(player)
                .setTweenFactor(0.02);
        addEntity(cam01);

        TextObject textScore = new TextObject("score")
                .setPosition(new Vector2D(renderer.getScreenBuffer().getWidth() - 10, 20))
                .withValue(score)
                .withText("%06d")
                .withFont(scoreFont)
                .withTextColor(Color.WHITE)
                .withShadowColor(shadowColor)
                .withTextAlign(TextAlign.RIGHT)
                .withBorderColor(Color.BLACK)
                .withBorderWidth(1)
                .stickToCamera(true)
                .setPriority(1);
        addEntity(textScore);

        TextObject textGameOver = new TextObject("gameOver")
                .setPosition(new Vector2D(renderer.getScreenBuffer().getWidth() * 0.5, renderer.getScreenBuffer().getHeight() * 0.5))
                .withText(App.messages.getString("app.message.game.over"))
                .withFont(scoreFont)
                .withTextColor(Color.WHITE)
                .withTextAlign(TextAlign.CENTER)
                .withShadowColor(shadowColor)
                .stickToCamera(true)
                .setActive(false)
                .setPriority(1)
                .withBorderWidth(2);
        addEntity(textGameOver);

        GameObject imgHeart = new GameObject("heart")
                .setPosition(new Vector2D(16, 10))
                .stickToCamera(true)
                .setImage(heart)
                .setPriority(2);
        addEntity(imgHeart);

        TextObject textLife = new TextObject("life")
                .setPosition(new Vector2D(26, 26))
                .withValue(lives)
                .withText("%d")
                .withFont(scoreFont.deriveFont(Font.BOLD, 12.0f))
                .withTextColor(Color.WHITE)
                .withShadowColor(shadowColor)
                .withBorderColor(Color.BLACK)
                .withBorderWidth(1)
                .stickToCamera(true)
                .setPriority(1);
        addEntity(textLife);

        GaugeObject gaugeEnergy = new GaugeObject("energy")
                .setPosition(36, 12)
                .setSize(50, 6)
                .withGaugeColor(Color.RED)
                .withMaxValue(100)
                .withMinValue(0)
                .withShadowColor(shadowColor)
                .stickToCamera(true)
                .setPriority(0)
                .setFillColor(Color.BLACK)
                .withCurrentValue(energy);
        addEntity(gaugeEnergy);

        GaugeObject gaugeMana = new GaugeObject("mana")
                .setPosition(36, 20)
                .setSize(50, 6)
                .withGaugeColor(Color.BLUE)
                .withMaxValue(100)
                .withMinValue(0)
                .withShadowColor(shadowColor)
                .stickToCamera(true)
                .setPriority(0)
                .setFillColor(Color.BLACK)
                .withCurrentValue(mana);
        addEntity(gaugeMana);

        app.getInputHandler().add(new DemoInputListener(this));
    }


    public void addEnemies(String entityRootName, int nbEnemies, double maxMass) {
        Rectangle2D playArea = app.getPhysicEngine().getWorld().getPlayArea();
        Material enemyMat = new Material(entityRootName + "_material", 0.5, 0.8, 1.0);
        for (int i = 0; i < nbEnemies; i++) {
            GameObject enemy = new GameObject(entityRootName + "_" + Entity.getIndex())
                    .setEntityType(EntityType.ELLIPSE)
                    .setColor(Color.RED.darker())
                    .setFillColor(Color.RED)
                    .setPosition(playArea.getWidth() * Math.random(), playArea.getHeight() * Math.random() * 0.4)
                    .setSize(8, 8)
                    .setPriority(i + 10)
                    .setMass(Math.random() * maxMass)
                    .setMaterial(enemyMat)
                    .addBehavior(new EnemyTrackingBehavior(40.0, 0.15));

            addEntity(enemy);
        }
    }

    @Override
    public void dispose(App app) {

    }

    @Override
    public void input(App app, InputHandler ih) {
        if (!levelEnd) {
            Entity player = getEntity("player");

            double speed = (double) player.getAttribute("speed", 0.5);
            if (ih.isControlKeyPressed()) {
                speed *= 4.0;
            }

            if (ih.getKeys(KeyEvent.VK_UP)) {
                player.getForces().add(new Vector2D(0, -speed));
            }
            if (ih.getKeys(KeyEvent.VK_DOWN)) {
                player.getForces().add(new Vector2D(0, speed));
            }
            if (ih.getKeys(KeyEvent.VK_LEFT)) {
                player.getForces().add(new Vector2D(-speed, 0));
            }
            if (ih.getKeys(KeyEvent.VK_RIGHT)) {
                player.getForces().add(new Vector2D(speed, 0));
            }
        }
    }

    public void removeAllEntity(String enemy) {
        Collection<Entity> tobeDeleted = app.getSceneManager().getCurrent().getEntities().stream().filter(e -> e.getName().contains(enemy)).collect(Collectors.toList());
        app.getSceneManager().getCurrent().getEntities().removeAll(tobeDeleted);
    }

    @Override
    public void update(App app, double elapsed, Map<String, Object> stats) {
        int life = (int) getEntity("player").getAttribute("life", 3);

        score = (int) getEntity("player").getAttribute("score", score);
        ((TextObject) getEntity("score")).withValue(score);

        ((TextObject) getEntity("life")).withValue(life);

        mana = (double) getEntity("player").getAttribute("mana", 0.0);
        energy = (double) getEntity("player").getAttribute("energy", 0.0);

        if (energy <= 0.0) {
            if (life - 1 >= 0) {
                getEntity("player").addAttribute("life", life - 1);
                getEntity("player").addAttribute("energy", maxEnergy);
            } else {
                levelEnd = true;
                getEntity("player").setFillColor(Color.RED);
            }
        }
        ((GaugeObject) getEntity("energy")).setCurrentValue(energy);
        ((GaugeObject) getEntity("mana")).setCurrentValue(mana);
        if (levelEnd) {
            getEntity("gameOver").setActive(true);
        }

    }

    @Override
    public void render(App app, Graphics2D g, Renderer r, Map<String, Object> stats) {
        // render HUD
        Font pauseFont = g.getFont().deriveFont(Font.BOLD, 16.0f);
        // draw "paused" message if required
        if (app.isPaused()) {
            g.setColor(new Color(0.1f, 0.3f, 0.6f, 0.6f));
            g.fillRect(0, (int) ((r.screenBuffer.getHeight() - 6) * 0.5), r.screenBuffer.getWidth(), 20);
            String pauseText = App.messages.getString("game.pause.text");
            r.drawText(g, pauseFont, pauseText);
        }
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void reset(App app) {
        getEntities().clear();
        create(app);
    }

    public void reset() {
        getEntities().clear();
        create(app);
    }

    public boolean isLevelEnded() {
        return levelEnd;
    }

    public void setLevelEnded(boolean le) {
        this.levelEnd = le;
    }
}
