package com.snapgames.core.gfx;

import com.snapgames.core.entity.Entity;
import com.snapgames.core.scene.Scene;

import java.awt.*;

public interface RendererPlugin<T extends Entity<?>> {
    Class<T> getEntityClass();

    void draw(Renderer r, Graphics2D g, Scene s, Entity<?> e);

}
