package com.snapgames.core;

/**
 * A {@link World} object to describe the contextual environment in which the {@link Entity} evolves in.
 *
 * @author Frédéric Delorme
 * @since 1.0.0
 */
public class World {
    private final double gravity;

    public World(double gravity) {
        this.gravity = gravity;
    }

    public double getGravity() {
        return this.gravity;
    }
}
