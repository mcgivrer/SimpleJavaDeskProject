package com.snapgames.core.entity;

import com.snapgames.core.physic.Material;
import com.snapgames.core.physic.Vector2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Entity {

    private static int index = 0;
    int id = index++;

    private String name = "entity_" + id;

    private EntityType type = EntityType.RECTANGLE;

    private Vector2D pos = new Vector2D();
    private Vector2D size = new Vector2D();

    private Vector2D vel = new Vector2D();
    private Vector2D acc = new Vector2D();
    List<Vector2D> forces = new ArrayList<>();

    public Color color;
    public Color fillColor;
    public BufferedImage image;

    public boolean active;

    public int priority;

    public double life;
    public long duration = -1;

    public Material material;
    public double mass = 1.0;

    private Double bbox;
    private boolean stickToCamera;
    private Map<String, Object> attributes = new ConcurrentHashMap<>();

    private Entity() {
        this.active = true;
    }

    public Entity(String name) {
        this();
        this.name = name;
    }

    public Entity addForce(Vector2D f) {
        this.forces.add(f);
        return this;
    }

    public Entity setPosition(double x, double y) {
        this.pos = new Vector2D(x, y);
        return this;
    }

    public Entity setPosition(Vector2D p) {
        this.pos = p;
        return this;
    }

    public Entity setSize(int w, int h) {
        this.size = new Vector2D(w, h);
        return this;
    }

    public Entity setSpeed(double dx, double dy) {
        this.vel = new Vector2D(dx, dy);
        return this;
    }

    public Entity setAcceleration(double ax, double ay) {
        this.acc = new Vector2D(ax, ay);
        return this;
    }

    public Entity setAcceleration(Vector2D acc) {
        this.acc = acc;
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

        if (duration != -1 && life > duration) {
            setActive(false);
        } else {
            life += elapsed;
        }
        updateBBox();
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

    public <T> Entity addAttribute(String attrName, T attrValue) {
        attributes.put(attrName, attrValue);
        return this;
    }

    public <T> T getAttribute(String attrName, T v) {
        return (T) attributes.get(attrName);
    }

    public EntityType getType() {
        return this.type;
    }

    public Entity setEntityType(EntityType t) {
        this.type = t;
        return this;
    }

    public Vector2D getVelocity() {
        return vel;
    }


    public Vector2D getPosition() {
        return pos;
    }

    public Entity setVelocity(Vector2D v) {
        this.vel = v;
        return this;
    }

    public Vector2D getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pos=" + pos +
                ", vel=" + vel +
                ", acc=" + acc +
                ", active=" + active +
                ", mass=" + mass +
                ", material=" + material +
                ", forces=" + forces +
                '}';
    }

    public List<Vector2D> getForces() {
        return this.forces;
    }

    public Material getMaterial() {
        return material;
    }

    public Vector2D getAcceleration() {
        return acc;
    }

    public void updateBBox() {
        bbox = new Rectangle2D.Double(pos.x, pos.y, this.size.x, this.size.y);
    }
}