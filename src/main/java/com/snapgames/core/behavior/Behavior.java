package com.snapgames.core.behavior;

import com.snapgames.core.entity.Entity;
import com.snapgames.core.gfx.Renderer;
import com.snapgames.core.scene.Scene;

import java.awt.*;

public interface Behavior {
    default void update(Scene scene, Entity e, double elapsed) {
        // nothing specific to draw by default.
    }

    default void draw(Renderer r, Graphics2D g, Scene scene, Entity e) {
        // nothing specific to draw by default.
    }
}
