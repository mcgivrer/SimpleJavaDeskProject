package com.snapgames.core.gfx.particles;

import com.snapgames.core.entity.GameObject;

public class ParticleSystemBuilder {

    private GameObject object;
    private int nbParticles;

    public static ParticleSystemBuilder create(String name) {
        ParticleSystemBuilder psb = new ParticleSystemBuilder();
        psb.object = new GameObject(name);
        return psb;
    }

    public ParticleSystemBuilder setSize(int nb) {
        this.nbParticles = nb;
        return this;
    }

    public ParticleSystemBuilder add(ParticleBehavior pb) {
        return this;
    }

    public GameObject build() {
        return this.object;
    }
}
