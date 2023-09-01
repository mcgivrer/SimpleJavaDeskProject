package com.snapgames.core;

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
        this.x += (target.x - (target.w * 0.5) - ((viewport.getWidth()) * 0.5) - this.x)
                * tf * Math.min(elapsed, 10);
        this.y += (target.y - (target.h * 0.5) - ((viewport.getHeight()) * 0.5) - this.y)
                * tf * Math.min(elapsed, 10);

        this.viewport.setRect(
                this.x + target.w * 0.5,
                this.y + target.h * 0.5,
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

    public Camera setTweenfactor(double tf) {
        this.tf = tf;
        return this;
    }

}
