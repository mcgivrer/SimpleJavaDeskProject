package com.snapgames.demo.test001.scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
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
import com.snapgames.demo.test001.behaviors.EnemyTrackingBehavior;
import com.snapgames.demo.test001.io.DemoInputListener;

public class DemoScene extends AbstractScene {

    int score = 0, lives = 3;
    double maxEnergy = 100;
    double maxMana = 100;
    double energy = 70;
    double mana = 80;

    public DemoScene(App app) {
        super(app);
    }

    @Override
    public void create(App app) {
        PhysicEngine physicEngine = app.getPhysicEngine();
        Renderer renderer = app.getRenderer();

        Rectangle2D playArea = physicEngine.getWorld().getPlayArea();

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
                .addAttribute("mana", maxMana);

        addEntity(player);

        addEnemies("enemy", 10, 10.0);

        Camera cam01 = new Camera("cam01")
                .setViewport(new Rectangle2D.Double(0, 0, 300, 200))
                .setTarget(player)
                .setTweenFactor(0.02);
        addEntity(cam01);

        Font scoreFont = renderer.getFont().deriveFont(Font.BOLD, 18.0f);
        TextObject textScore = new TextObject("score")
                .setPosition(new Vector2D(renderer.getScreenBuffer().getWidth() - 80, 20))
                .withValue(score)
                .withText("%06d")
                .withFont(scoreFont)
                .withTextColor(Color.WHITE)
                .withShadowColor(Color.DARK_GRAY)
                .stickToCamera(true);
        addEntity(textScore);

        TextObject textLife = new TextObject("life")
                .setPosition(new Vector2D(32, 24))
                .withValue(lives)
                .withText("%d")
                .withFont(scoreFont.deriveFont(15.0f))
                .withTextColor(Color.WHITE)
                .withShadowColor(Color.DARK_GRAY)
                .stickToCamera(true);
        addEntity(textLife);

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
        Entity player = getEntity("player");

        double speed = (double) player.getAttribute("speed", 0.5);
        if (ih.isControlKeyPressed()) {
            speed *= 2.0;
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

    public void removeAllEntity(String enemy) {
        Collection<Entity> tobeDeleted = app.getSceneManager().getCurrent().getEntities().stream().filter(e -> e.getName().contains(enemy)).collect(Collectors.toList());
        app.getSceneManager().getCurrent().getEntities().removeAll(tobeDeleted);
    }

    @Override
    public void update(App app, double elapsed, Map<String, Object> stats) {
        ((TextObject) getEntity("score")).withValue(score);
        ((TextObject) getEntity("life")).withValue(lives);

        mana = (double) getEntity("player").getAttribute("mana", 0.0);
        energy = (double) getEntity("player").getAttribute("energy", 0.0);

    }

    @Override
    public void render(App app, Graphics2D g, Renderer r, Map<String, Object> stats) {
        // render HUD
        Font pauseFont = g.getFont().deriveFont(Font.BOLD, 16.0f);

        // draw life counter
        r.drawText(g, pauseFont.deriveFont(20.0f), "❦", 24, 22, Color.RED, Color.RED.darker(), 1);

        // draw energy
        g.setColor(Color.BLACK);
        g.fillRect(14, 26, 50 + 2, 3);
        g.setColor(Color.RED.darker());
        g.fillRect(15, 27, (int) maxEnergy / 2, 2);
        g.setColor(Color.RED);
        g.fillRect(15, 27, (int) energy / 2, 2);

        // draw mana
        g.setColor(Color.BLACK);
        g.fillRect(14, 30, 50 + 2, 3);
        g.setColor(Color.BLUE.darker().darker());
        g.fillRect(15, 31, (int) maxMana / 2, 2);
        g.setColor(Color.BLUE);
        g.fillRect(15, 31, (int) mana / 2, 2);


        // draw "paused" message if required
        if (app.isPaused()) {
            g.setColor(new Color(0.1f, 0.3f, 0.6f, 0.6f));
            g.fillRect(0, (int) ((r.screenBuffer.getHeight() - 14) * 0.5), r.screenBuffer.getWidth(), 20);
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
}
