package com.snapgames.core.scene;

import com.snapgames.core.App;
import com.snapgames.core.entity.Camera;
import com.snapgames.core.entity.Entity;
import com.snapgames.core.entity.Node;
import com.snapgames.core.physic.World;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractScene implements Scene {

    protected App app;
    private Camera currentCamera;

    private World world;

    protected AbstractScene(App app, String name) {
        super(name);
        this.app = app;
    }


    public void addEntity(Entity entity) {
        entities.put(entity.getName(), entity);
        if (entity instanceof Camera) {
            this.currentCamera = (Camera) entity;
        }
    }

    public Camera getCamera() {
        return this.currentCamera;
    }

    @Override
    public Camera getCurrentCamera() {
        return currentCamera;
    }

    @Override
    public Collection<Entity> getEntities() {
        return entities.values();
    }

    public Entity getEntity(String name) {
        return entities.get(name);
    }


    public void setWorld(World w) {
        this.world = w;
    }

    public World getWorld() {
        return this.world;
    }
}
