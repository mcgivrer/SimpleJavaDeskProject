package com.snapgames.core.physic;

import com.snapgames.core.behavior.Behavior;
import com.snapgames.core.entity.Entity;

/**
 * This {@link CollisionResponseBehavior} interface provides an API to manage response on collision for Entity.
 * <p>
 * The {@link Entity} o1 colliding {@link Entity} o2 according to a {@link Vector2D} normal can add this collision
 * {@link Behavior} variant to manage how the O& must react.
 *
 * @author Frédéric Delorme
 * @since 1.0.3
 */
public interface CollisionResponseBehavior extends Behavior {

    /**
     * define implementation of a response for {@link Entity} o1 to the collision with the  {@link Entity} o2.
     *
     * @param o1     the object of collision
     * @param o2     the colliding object
     * @param normal the penetration vector between these 2 objects.
     */
    void response(Entity<?> o1, Entity<?> o2, Vector2D normal);

}
