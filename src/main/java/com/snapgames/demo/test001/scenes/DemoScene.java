package com.snapgames.demo.test001.scenes;

import com.snapgames.core.*;
import com.snapgames.test001.core.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.Map;

public class DemoScene extends AbstractScene {
    @Override
    public void create(App app) {
        app.world = new World(-0.981);
        Entity player = new Entity("player")
                .setColor(Color.GREEN.darker())
                .setFillColor(Color.GREEN)
                .setPosition(app.screenBuffer.getWidth() * 0.5, app.screenBuffer.getHeight() * 0.5)
                .setSize(16, 16)
                .setPriority(1)
                .setMass(50.0)
                .setMaterial(new Material("body", 0.9, 0.78, 0.97));

        addEntity(player);

        addEnemies(app, 100);

        Camera cam01 = new Camera("cam01")
                .setViewport(new Rectangle2D.Double(0, 0, 300, 200))
                .setTarget(player)
                .setTweenfactor(0.002);
        addEntity(cam01);
    }


    private void addEnemies(App app, int nbEnemies) {
        for (int i = 0; i < nbEnemies; i++) {
            Entity enemy = new Entity("enemy_" + i)
                    .setColor(Color.RED.darker())
                    .setFillColor(Color.RED)
                    .setPosition(app.playArea.getWidth() * Math.random(), app.playArea.getHeight() * Math.random() * 0.4)
                    .setSize(8, 8)
                    .setPriority(i + 10)
                    .setMass(10.0)
                    .setMaterial(new Material("enemy", 1.0, 0.98, 0.99));

            addEntity(enemy);
        }
    }

    @Override
    public void dispose(App app) {

    }

    @Override
    public void input(App app) {
        Entity player = getEntity("player");

        double speed = 0.5;
        if (app.getKeys(KeyEvent.VK_UP)) {
            player.dy = -speed;
        }
        if (app.getKeys(KeyEvent.VK_DOWN)) {
            player.dy = speed;
        }
        if (app.getKeys(KeyEvent.VK_LEFT)) {
            player.dx = -speed;
        }
        if (app.getKeys(KeyEvent.VK_RIGHT)) {
            player.dx = speed;
        }
    }

    @Override
    public void update(App app, double elapsed, Map<String, Object> stats) {

    }

    @Override
    public void render(App app, Graphics2D g, Map<String, Object> stats) {
        // render HUD
        Font pauseFont = g.getFont().deriveFont(Font.BOLD, 16.0f);
        int score = 0, lives = 3;
        // draw Score
        app.drawText(g, pauseFont, String.format("%06d", score), app.screenBuffer.getWidth() - 40, 20, Color.WHITE, Color.BLACK,
                2);
        // draw life counter
        app.drawText(g, pauseFont.deriveFont(20.0f), "❦", 24, 22, Color.RED, Color.RED.darker(), 1);
        app.drawText(g, pauseFont, String.format("%d", lives), 32, 24, Color.WHITE, Color.BLACK, 2);
        // draw "paused" message if required
        if (app.isPaused()) {
            g.setColor(new Color(0.1f, 0.3f, 0.6f, 0.6f));
            g.fillRect(0, (int) ((app.screenBuffer.getHeight() - 14) * 0.5), app.screenBuffer.getWidth(), 20);
            String pauseText = App.messages.getString("game.pause.text");
            app.drawText(g, pauseFont, pauseText);
        }
    }

    @Override
    public String getName() {
        return null;
    }
}
