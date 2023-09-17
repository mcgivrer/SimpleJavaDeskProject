package com.snapgames.core.entity;

import com.snapgames.core.gfx.particles.ParticleBehavior;

import java.util.List;

public class GameObject extends Entity<GameObject> {
    public GameObject(String name) {
        super(name);
    }

    public GameObject addBehaviors(List<ParticleBehavior> behaviors) {
        this.getBehaviors().addAll(behaviors);
        return this;
    }
}
