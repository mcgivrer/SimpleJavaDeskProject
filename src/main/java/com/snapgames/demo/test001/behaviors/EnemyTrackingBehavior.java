package com.snapgames.demo.test001.behaviors;

import com.snapgames.core.App;
import com.snapgames.core.behavior.Behavior;
import com.snapgames.core.entity.Entity;
import com.snapgames.core.gfx.Renderer;
import com.snapgames.core.scene.Scene;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class EnemyTrackingBehavior implements Behavior {

    private final double sensorDiameter;
    private final double force;
    private boolean sensor;

    public EnemyTrackingBehavior(double sensorDiameter, double force) {
        this.sensorDiameter = sensorDiameter;
        this.force = force;
    }

    @Override
    public void update(Scene scene, Entity e, double elapsed) {
        Entity player = scene.getEntity("player");
        Ellipse2D sensorArea = new Ellipse2D.Double(
                e.getPosition().x + e.getSize().x * 0.5 - sensorDiameter * 0.5,
                e.getPosition().y + e.getSize().y * 0.5 - sensorDiameter * 0.5,
                sensorDiameter, sensorDiameter);
        if (player.getPosition().add(player.getSize().multiply(0.5)).distance(e.getPosition()) < sensorDiameter) {
            e.addForce(player.getPosition().substract(e.getPosition()).multiply(force));
            sensor = true;
        } else {
            sensor = false;
        }
    }

    @Override
    public void draw(Renderer r, Graphics2D g, Scene scene, Entity e) {
        if (App.getDebug() && App.isDebugLevelAtLeast(2)) {
            Stroke back = g.getStroke();
            g.setStroke(new BasicStroke(0.4f));
            if (sensor) {
                g.setColor(new Color(0.8f, 0.4f, 0.0f, 0.5f));
            } else {
                g.setColor(new Color(0.4f, 0.4f, 0.4f, 0.5f));
            }
            g.fillArc(
                    (int) (e.getPosition().x + e.getSize().x * 0.5 - sensorDiameter * 0.5),
                    (int) (e.getPosition().y + e.getSize().y * 0.5 - sensorDiameter * 0.5),
                    (int) sensorDiameter,
                    (int) sensorDiameter,
                    0, 360);
            if (sensor) {
                g.setColor(Color.ORANGE);
            } else {
                g.setColor(Color.GRAY);
            }
            g.drawArc(
                    (int) (e.getPosition().x + e.getSize().x * 0.5 - sensorDiameter * 0.5),
                    (int) (e.getPosition().y + e.getSize().y * 0.5 - sensorDiameter * 0.5),
                    (int) sensorDiameter,
                    (int) sensorDiameter,
                    0, 360);
            g.setStroke(back);
        }
    }
}
