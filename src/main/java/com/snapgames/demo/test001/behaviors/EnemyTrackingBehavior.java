package com.snapgames.demo.test001.behaviors;

import com.snapgames.core.App;
import com.snapgames.core.behavior.Behavior;
import com.snapgames.core.entity.Entity;
import com.snapgames.core.gfx.Renderer;
import com.snapgames.core.scene.Scene;

import java.awt.*;

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
        if (player.getPosition().add(player.getSize().multiply(0.5)).distance(e.getPosition()) < this.sensorDiameter * 0.5) {
            e.addForce(player.getPosition().substract(e.getPosition()).multiply(force));
            sensor = true;
        } else {
            sensor = false;
        }
    }

    @Override
    public void draw(Renderer r, Graphics2D g, Scene scene, Entity e) {
        if (App.getDebug() && App.isDebugLevelAtLeast(2)) {
            if (sensor) {
                g.setColor(Color.ORANGE);
            } else {
                g.setColor(Color.GRAY);
            }
            g.drawArc(
                    (int) (e.getPosition().x - sensorDiameter * 0.5),
                    (int) (e.getPosition().y - sensorDiameter * 0.5),
                    (int) sensorDiameter,
                    (int) sensorDiameter,
                    0, 360);
        }
    }
}
