package com.snapgames.demo.test001;

public class Material {
    String name;
    double density;
    double elasticity;
    double roughness;

    public Material(String name, double d, double e, double r) {
        this.name = name;
        this.density = d;
        this.elasticity = e;
        this.roughness = r;
    }
}
