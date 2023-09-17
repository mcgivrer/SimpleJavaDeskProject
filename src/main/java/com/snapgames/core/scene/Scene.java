package com.snapgames.core.scene;

import com.snapgames.core.App;
import com.snapgames.core.entity.Camera;
import com.snapgames.core.entity.Entity;
import com.snapgames.core.io.InputHandler;
import com.snapgames.core.physic.World;

import java.awt.*;
import java.util.Collection;
import java.util.Map;

public interface Scene {
    void create(App app);

    default void load() {
    }

    void dispose(App app);

    void input(App app, InputHandler ih);

    void update(App app, double elapsed, Map<String, Object> stats);

    void render(App app, Graphics2D g, com.snapgames.core.gfx.Renderer r, Map<String, Object> stats);

    String getName();

    public Collection<Entity> getEntities();

    Entity getEntity(String name);

    Camera getCurrentCamera();

    void reset(App app);

    String treeToString();

    World getWorld();
}
