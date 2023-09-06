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

        setPosition(getPosition().add(target.getPosition().substract(target.getSize().multiply(0.5)).substract(new Vector2D(viewport.getWidth() * 0.5, viewport.getHeight() * 0.5).substract(this.getPosition()))));

        /**
         this.pos.x += (target.pos.x - (target.w * 0.5) - ((viewport.getWidth()) * 0.5) - this.pos.x)
         * tf * Math.min(elapsed, 10);
         this.pos.y += (target.pos.y - (target.h * 0.5) - ((viewport.getHeight()) * 0.5) - this.pos.y)
         * tf * Math.min(elapsed, 10);
         **/

        this.viewport.setRect(
                this.getPosition().x + target.w * 0.5,
                this.getPosition().y + target.h * 0.5,
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
}
