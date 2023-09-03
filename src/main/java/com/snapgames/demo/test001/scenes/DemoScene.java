package com.snapgames.demo.test001.scenes;

import com.snapgames.core.*;
import com.snapgames.core.entity.Camera;
import com.snapgames.core.entity.Entity;
import com.snapgames.core.entity.EntityType;
import com.snapgames.core.gfx.Renderer;
import com.snapgames.core.io.InputHandler;
import com.snapgames.core.physic.Material;
import com.snapgames.core.physic.PhysicEngine;
import com.snapgames.core.physic.World;
import com.snapgames.core.scene.AbstractScene;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

public class DemoScene extends AbstractScene {

    int score = 0, lives = 3;
    int energy = 100;

    @Override
    public void create(App app) {
        PhysicEngine physicEngine = app.getPhysicEngine();

        World world = new World(-0.981);
        physicEngine.setWorld(world);
        Rectangle2D playArea = physicEngine.getWorld().getPlayArea();

        Entity player = new Entity("player")
                .setColor(Color.GREEN.darker())
                .setFillColor(Color.GREEN)
                .setPosition(playArea.getWidth() * 0.5, playArea.getHeight() * 0.5)
                .setSize(16, 16)
                .setPriority(1)
                .setMass(1.0)
                .setMaterial(new Material("body", 0.9, 0.78, 0.97))
                .addAttribute("speed", 0.8);

        addEntity(player);

        addEnemies(app, 100);

        Camera cam01 = new Camera("cam01")
                .setViewport(new Rectangle2D.Double(0, 0, 300, 200))
                .setTarget(player)
                .setTweenFactor(0.002);
        addEntity(cam01);
    }


    private void addEnemies(App app, int nbEnemies) {
        Rectangle2D playArea = app.getPhysicEngine().getWorld().getPlayArea();
        for (int i = 0; i < nbEnemies; i++) {
            Entity enemy = new Entity("enemy_" + i)
                    .setEntityType(EntityType.ELLIPSE)
                    .setColor(Color.RED.darker())
                    .setFillColor(Color.RED)
                    .setPosition(playArea.getWidth() * Math.random(), playArea.getHeight() * Math.random() * 0.4)
                    .setSize(8, 8)
                    .setPriority(i + 10)
                    .setMass(1.0)
                    .setMaterial(new Material("enemy", 1.0, 0.98, 0.99));

            addEntity(enemy);
        }
    }

    @Override
    public void dispose(App app) {

    }

    @Override
    public void input(App app, InputHandler ih) {
        Entity player = getEntity("player");

        double speed = player.getAttribute("speed", 0.5);
        if (ih.getKeys(KeyEvent.VK_UP)) {
            player.dy = -speed;
        }
        if (ih.getKeys(KeyEvent.VK_DOWN)) {
            player.dy = speed;
        }
        if (ih.getKeys(KeyEvent.VK_LEFT)) {
            player.dx = -speed;
        }
        if (ih.getKeys(KeyEvent.VK_RIGHT)) {
            player.dx = speed;
        }
    }

    @Override
    public void update(App app, double elapsed, Map<String, Object> stats) {

    }

    @Override
    public void render(App app, Graphics2D g, Renderer r, Map<String, Object> stats) {
        // render HUD
        Font pauseFont = g.getFont().deriveFont(Font.BOLD, 16.0f);

        // draw Score
        r.drawText(g, pauseFont, String.format("%06d", score), r.screenBuffer.getWidth() - 40, 20, Color.WHITE, Color.BLACK,
                2);

        // draw life counter
        r.drawText(g, pauseFont.deriveFont(20.0f), "❦", 24, 22, Color.RED, Color.RED.darker(), 1);
        r.drawText(g, pauseFont, String.format("%d", lives), 32, 24, Color.WHITE, Color.BLACK, 2);

        // draw energy
        g.setColor(Color.BLACK);
        g.fillRect(14, 26, 50 + 2, 3);
        g.setColor(Color.GREEN.darker());
        g.fillRect(15, 27, energy / 2, 2);
        // draw mana
        g.setColor(Color.BLACK);
        g.fillRect(14, 30, 50 + 2, 3);
        g.setColor(Color.BLUE);
        g.fillRect(15, 31, energy / 2, 2);


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
}
