package com.snapgames.core.gfx.particles;

import com.snapgames.core.entity.EntityType;
import com.snapgames.core.entity.GameObject;

import java.util.ArrayList;
import java.util.List;

public class ParticleSystemBuilder {

    private GameObject object;
    private int nbParticles;
    private List<ParticleBehavior> behaviors = new ArrayList<>();

    public static ParticleSystemBuilder create(String name) {
        ParticleSystemBuilder psb = new ParticleSystemBuilder();
        psb.object = new GameObject(name).setEntityType(EntityType.NONE);
        return psb;
    }

    public ParticleSystemBuilder setSize(int nb) {
        this.object.addAttribute("maxParticle", nb);
        return this;
    }

    public ParticleSystemBuilder add(ParticleBehavior pb) {
        this.behaviors.add(pb);
        return this;
    }

    public GameObject build() {
        for (int i = 0; i < object.getAttribute("maxParticle", 0); i++) {
            object.addChild(
                    new GameObject("PS_" + object.getName() + "_" + i)
                            .setStickToParent(true)
                            .setInheritGraphicsFromParent(true)
                            .addBehaviors(behaviors)
            );

        }
        return this.object;
    }
}
