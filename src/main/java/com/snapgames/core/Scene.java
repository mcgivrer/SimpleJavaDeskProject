package com.snapgames.core;

import java.awt.*;
import java.util.Collection;
import java.util.Map;

public interface Scene {
    void create(App app);

    void dispose(App app);

    void input(App app);

    void update(App app, double elapsed, Map<String, Object> stats);

    void render(App app, Graphics2D g, Map<String, Object> stats);

    String getName();

    public Collection<Entity> getEntities();

    Entity getEntity(String name);

    Camera getCurrentCamera();
}
