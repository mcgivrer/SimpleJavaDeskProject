package com.snapgames.core.gfx.plugins;

import com.snapgames.core.entity.Entity;
import com.snapgames.core.entity.GameObject;
import com.snapgames.core.gfx.Renderer;
import com.snapgames.core.gfx.RendererPlugin;
import com.snapgames.core.scene.Scene;

import java.awt.*;

public class GameObjectRendererPlugin implements RendererPlugin<GameObject> {

    @Override
    public Class<GameObject> getEntityClass() {
        return GameObject.class;
    }

    @Override
    public void draw(Renderer r, Graphics2D g, Scene s, Entity entity) {
        GameObject e = (GameObject) entity;
        switch (e.getType()) {
            case DOT, RECTANGLE -> {
                if (e.fillColor != null) {
                    g.setColor(e.fillColor);
                    g.fillRect((int) e.getPosition().x, (int) e.getPosition().y, (int) e.getSize().x, (int) e.getSize().y);
                }
                if (e.color != null) {
                    g.setColor(e.color);
                    g.drawRect((int) e.getPosition().x, (int) e.getPosition().y, (int) e.getSize().x, (int) e.getSize().y);
                }
            }
            case ELLIPSE -> {
                if (e.fillColor != null) {
                    g.setColor(e.fillColor);
                    g.fillArc((int) e.getPosition().x, (int) e.getPosition().y, (int) e.getSize().x, (int) e.getSize().y, 0, 360);
                }
                if (e.color != null) {
                    g.setColor(e.color);
                    g.drawArc((int) e.getPosition().x, (int) e.getPosition().y, (int) e.getSize().x, (int) e.getSize().y, 0, 360);
                }
            }
            case LINE -> {
                if (e.color != null) {
                    g.setColor(e.color);
                    g.drawLine((int) e.getPosition().x, (int) e.getPosition().y, (int) e.getSize().x, (int) e.getSize().y);
                }
            }
        }
    }
}
