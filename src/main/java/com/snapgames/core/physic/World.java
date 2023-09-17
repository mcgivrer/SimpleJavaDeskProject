package com.snapgames.core.physic;

import com.snapgames.core.entity.Entity;
import com.snapgames.core.entity.Node;

import java.awt.geom.Rectangle2D;

/**
 * A {@link World} object to describe the contextual environment in which the {@link Entity} evolves in.
 *
 * @author Frédéric Delorme
 * @since 1.0.0
 */
public class World extends Node {
    private final Vector2D gravity;

    public Rectangle2D playArea;

    public World(double gravity) {

        this(new Vector2D(0.0, gravity), new Rectangle2D.Double(0, 0, 320, 200));
    }

    public World(Vector2D gravity, Rectangle2D playArea) {
        this.gravity = gravity;
        this.playArea = playArea;
    }

    public Vector2D getGravity() {
        return this.gravity;
    }


    public void setPlayArea(Rectangle2D pa) {
        this.playArea = pa;
    }

    public Rectangle2D getPlayArea() {
        return this.playArea;
    }
}
