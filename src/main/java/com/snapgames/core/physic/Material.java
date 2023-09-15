package com.snapgames.core.physic;

public class Material {
    public static Material DEFAULT = new Material("default", 1.0, 0.0, 1.0);

    String name;
    public double density;
    public double elasticity;
    public double roughness;

    public Material(String name, double d, double e, double r) {
        this.name = name;
        this.density = d;
        this.elasticity = e;
        this.roughness = r;
    }

    @Override
    public String toString() {
        return "Material{" +
                "name='" + name + '\'' +
                ", density=" + density +
                ", elasticity=" + elasticity +
                ", roughness=" + roughness +
                '}';
    }
}
