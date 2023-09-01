package com.snapgames.core;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class Entity {

    private static int index = 0;
    int id = index++;

    String name = "entity_" + id;
    double x;
    double y;

    int w;
    int h;

    public double dx;
    public double dy;

    Color color;
    Color fillColor;
    BufferedImage image;

    boolean active;

    public int priority;

    public long life;
    public long duration = -1;

    public Material material;
    public double mass = 1.0;

    private Double bbox;
    private boolean stickToCamera;

    private Entity() {
        this.active = true;
    }

    public Entity(String name) {
        this();
        this.name = name;
    }

    public Entity setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Entity setSize(int w, int h) {
        this.w = w;
        this.h = h;
        return this;
    }

    public Entity setSpeed(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
        return this;
    }

    public Entity setColor(Color c) {
        this.color = c;
        return this;
    }

    public Entity setFillColor(Color fc) {
        this.fillColor = fc;
        return this;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return this.active;
    }

    public Entity setActive(boolean a) {
        this.active = a;
        return this;
    }

    public Entity stickToCamera(boolean sticky) {
        this.stickToCamera = sticky;
        return this;
    }

    public int getPriority() {
        return priority;
    }

    public Rectangle2D getBBox() {
        return bbox;
    }

    public void update(double elapsed) {
        x += Math.signum(dx) * Math.max(Math.abs(dx), 0.0);
        y += Math.signum(dy) * Math.max(Math.abs(dy), 0.0);

        if (Optional.ofNullable(material).isPresent()) {
            dx *= material.roughness;
            dy *= material.roughness / mass;
        }
        if (duration != -1 && life > duration) {
            setActive(false);
        } else {
            life += elapsed;
        }

        this.bbox = new Rectangle2D.Double(this.x, this.y, this.w, this.h);
    }

    public Entity setMaterial(Material m) {
        this.material = m;
        return this;
    }

    public Entity setImage(BufferedImage img) {
        this.image = img;
        return this;
    }

    public boolean isStickToCamera() {
        return stickToCamera;
    }

    public Entity setMass(double mass) {
        this.mass = mass;
        return this;
    }

    public Entity setPriority(int p) {
        this.priority = p;
        return this;
    }
}