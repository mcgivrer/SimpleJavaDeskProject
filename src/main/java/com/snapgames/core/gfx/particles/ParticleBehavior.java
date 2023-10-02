package com.snapgames.core.gfx.particles;

import com.snapgames.core.behavior.Behavior;
import com.snapgames.core.entity.Entity;
import com.snapgames.core.entity.GameObject;
import com.snapgames.core.scene.Scene;

public interface ParticleBehavior extends Behavior {
    void create(Entity<?> e);

    @Override
    default void update(Scene scene, Entity<?> e, double elapsed) {
        if (e.getChild().size() < (int) e.getAttribute("maxParticle", 0)) {
            create(e);
        }
    }
}
