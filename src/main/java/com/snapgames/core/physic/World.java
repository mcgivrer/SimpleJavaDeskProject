package com.snapgames.core.physic;

import com.snapgames.core.entity.Entity;

import java.awt.geom.Rectangle2D;

/**
 * A {@link World} object to describe the contextual environment in which the {@link Entity} evolves in.
 *
 * @author Frédéric Delorme
 * @since 1.0.0
 */
public class World {
    private final double gravity;

    public Rectangle2D playArea;

    public World(double gravity) {

        this(gravity, new Rectangle2D.Double(0, 0, 320, 200));
    }

    public World(double gravity, Rectangle2D playArea) {
        this.gravity = gravity;
        this.playArea = playArea;
    }

    public double getGravity() {
        return this.gravity;
    }


    public void setPlayArea(Rectangle2D pa) {
        this.playArea = pa;
    }

    public Rectangle2D getPlayArea() {
        return this.playArea;
    }
}
