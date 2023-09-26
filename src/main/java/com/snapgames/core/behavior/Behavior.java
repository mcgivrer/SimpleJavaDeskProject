package com.snapgames.core.behavior;

import com.snapgames.core.entity.Entity;
import com.snapgames.core.gfx.Renderer;
import com.snapgames.core.scene.Scene;

import java.awt.*;

/**
 * The interface {@link Behavior} provide a way
 * to create new updates or dow capabilities to any {@link Entity} linked to.
 *
 * @author Frédéric Delorme
 * @since 1.0.2
 */
public interface Behavior {

    /**
     * Implements new update processing for the concerned {@link Entity}.
     *
     * @param scene   the contextual {@link Scene} where this Behavior is processed.
     * @param e       the concerned Entity by this Behavior
     * @param elapsed the elapsed time (in ms) since the previous call.
     */
    default void update(Scene scene, Entity<?> e, double elapsed) {
        // nothing specific to draw by default.
    }

    /**
     * Implements new draw processing for the concerned {@link Entity}.
     *
     * @param r     the {@link Renderer} service calling this drawing operation
     * @param g     the {@link Graphics2D} API instance used to draw.
     * @param scene the contextual {@link Scene} where this {@link Behavior} is processed.
     * @param e     the concerned {@link Entity} by this {@link Behavior}
     */
    default void draw(Renderer r, Graphics2D g, Scene scene, Entity<?> e) {
        // nothing specific to draw by default.
    }
}
