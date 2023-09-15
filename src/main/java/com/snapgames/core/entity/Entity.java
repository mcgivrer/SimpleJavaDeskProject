package com.snapgames.core.entity;

import com.snapgames.core.behavior.Behavior;
import com.snapgames.core.gfx.RendererPlugin;
import com.snapgames.core.physic.Material;
import com.snapgames.core.physic.Vector2D;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Entity<T> {

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

    private Shape bbox = new Rectangle2D.Double();
    private boolean stickToCamera;
    private Map<String, Object> attributes = new ConcurrentHashMap<>();
    private List<Behavior> behaviors = new ArrayList<>();
    private boolean contact;
    private RendererPlugin renderedByPlugin;

    private Entity() {
        this.active = true;
    }

    public Entity(String name) {
        this();
        this.name = name;
    }

    public static int getIndex() {
        return index;
    }

    public T addForce(Vector2D f) {
        this.forces.add(f);
        return (T) this;
    }

    public T addBehavior(Behavior e) {
        this.behaviors.add(e);
        return (T) this;
    }


    public T setPosition(double x, double y) {
        this.pos = new Vector2D(x, y);
        return (T) this;
    }

    public T setPosition(Vector2D p) {
        this.pos = p;
        return (T) this;
    }

    public T setSize(int w, int h) {
        this.size = new Vector2D(w, h);
        return (T) this;
    }

    public T setSpeed(double dx, double dy) {
        this.vel = new Vector2D(dx, dy);
        return (T) this;
    }

    public T setAcceleration(double ax, double ay) {
        this.acc = new Vector2D(ax, ay);
        return (T) this;
    }

    public T setAcceleration(Vector2D acc) {
        this.acc = acc;
        return (T) this;
    }

    public T setColor(Color c) {
        this.color = c;
        return (T) this;
    }

    public T setFillColor(Color fc) {
        this.fillColor = fc;
        return (T) this;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return this.active;
    }

    public T setActive(boolean a) {
        this.active = a;
        return (T) this;
    }

    public T stickToCamera(boolean sticky) {
        this.stickToCamera = sticky;
        return (T) this;
    }

    public int getPriority() {
        return priority;
    }

    public Shape getBBox() {
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

    public T setMaterial(Material m) {
        this.material = m;
        return (T) this;
    }

    public T setImage(BufferedImage img) {
        this.image = img;
        return (T) this;
    }

    public boolean isStickToCamera() {
        return stickToCamera;
    }

    public T setMass(double mass) {
        this.mass = mass;
        return (T) this;
    }

    public T setPriority(int p) {
        this.priority = p;
        return (T) this;
    }

    public <Y> T addAttribute(String attrName, Y attrValue) {
        attributes.put(attrName, attrValue);
        return (T) this;
    }

    public <Y> Y getAttribute(String attrName, Y v) {
        if (!attributes.containsKey(attrName)) {
            addAttribute(attrName, v);
        }
        return (Y) attributes.get(attrName);
    }

    public EntityType getType() {
        return this.type;
    }

    public T setEntityType(EntityType t) {
        this.type = t;
        return (T) this;
    }

    public Vector2D getVelocity() {
        return vel;
    }


    public Vector2D getPosition() {
        return pos;
    }

    public T setVelocity(Vector2D v) {
        this.vel = v;
        return (T) this;
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
        switch (type) {
            case LINE, RECTANGLE -> {
                bbox = new Rectangle2D.Double(pos.x, pos.y, this.size.x, this.size.y);
            }
            case DOT, ELLIPSE -> {
                bbox = new Ellipse2D.Double(pos.x, pos.y, this.size.x, this.size.y);

            }
        }
    }

    public List<Behavior> getBehaviors() {
        return this.behaviors;
    }

    public T setContact(boolean c) {
        this.contact = c;
        return (T) this;
    }

    public boolean getContact() {
        return contact;
    }

    public void setRenderedBy(RendererPlugin plugin) {
        this.renderedByPlugin = plugin;
    }
}