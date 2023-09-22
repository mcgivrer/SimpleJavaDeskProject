package com.snapgames.core.physic;

import com.snapgames.core.behavior.Behavior;
import com.snapgames.core.entity.Entity;

public interface CollisionResponseBehavior extends Behavior {

    void response(Entity<?> o1, Entity<?> o2, Vector2D normal);

}
