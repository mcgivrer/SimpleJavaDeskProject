package com.snapgames.core.scene;

import com.snapgames.core.App;
import com.snapgames.core.entity.Camera;
import com.snapgames.core.entity.Entity;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractScene implements Scene {

    protected App app;
    private Camera currentCamera;

    private Map<String, Entity> entities = new ConcurrentHashMap<>();

    protected AbstractScene(App app) {
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
    public Collection<Entity> getEntities() {
        return entities.values();
    }

    public Entity getEntity(String name) {
        return entities.get(name);
    }

    @Override
    public Camera getCurrentCamera() {
        return currentCamera;
    }
}
