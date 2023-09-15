package com.snapgames.core.gfx;

import com.snapgames.core.entity.Entity;

import java.awt.*;

public interface RendererPlugin<T extends Entity> {
    Class<T> getEntityClass();

    void draw(Renderer r, Graphics2D g, Entity e);

}
