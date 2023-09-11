package com.snapgames.core.entity;

import com.snapgames.core.physic.Vector2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Camera extends Entity {

    Entity target;
    Rectangle2D viewport;
    double tf;

    public Camera(String name) {
        super(name);
    }

    @Override
    public void update(double elapsed) {

        this.getPosition().x += (target.getPosition().x - (target.getSize().x * 0.5) - ((viewport.getWidth()) * 0.5) - this.getPosition().x)
                * tf * Math.min(elapsed, 10);
        this.getPosition().y += (target.getPosition().y - (target.getSize().y * 0.5) - ((viewport.getHeight()) * 0.5) - this.getPosition().y)
                * tf * Math.min(elapsed, 10);

        this.viewport.setRect(
                this.getPosition().x + target.getSize().x * 0.5,
                this.getPosition().y + target.getSize().y * 0.5,
                this.viewport.getWidth(),
                this.viewport.getHeight());
    }

    public Camera setTarget(Entity t) {
        this.target = t;
        return this;
    }

    public Camera setViewport(Rectangle2D vp) {
        this.viewport = vp;
        return this;
    }

    public Camera setTweenFactor(double tf) {
        this.tf = tf;
        return this;
    }

    public Shape getViewport() {
        return viewport;
    }

    public boolean isInViewPort(Entity e) {
        return viewport.intersects(e.getBBox().getBounds2D()) || viewport.contains(e.getBBox().getBounds2D());
    }
}
