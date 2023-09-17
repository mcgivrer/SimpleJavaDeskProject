package com.snapgames.core.gfx.plugins;

import com.snapgames.core.entity.Entity;
import com.snapgames.core.entity.TextObject;
import com.snapgames.core.gfx.Renderer;
import com.snapgames.core.gfx.RendererPlugin;
import com.snapgames.core.scene.Scene;

import java.awt.*;
import java.util.Optional;

public class TextObjectRendererPlugin implements RendererPlugin<TextObject> {
    @Override
    public Class<TextObject> getEntityClass() {
        return TextObject.class;
    }

    @Override
    public void draw(Renderer r, Graphics2D g, Scene s, Entity e) {
        TextObject t = (TextObject) e;
        if (Optional.ofNullable(t.getFont()).isPresent()) {
            g.setFont(t.getFont());
        }
        int offset = 0;
        switch (t.getTextAlign()) {
            case LEFT -> {
                offset = 0;
            }
            case RIGHT -> {
                offset = -(int) (g.getFontMetrics().stringWidth(t.getText()));
            }
            case CENTER -> {
                offset = -(int) (g.getFontMetrics().stringWidth(t.getText()) * .5);
            }
        }
        // draw shadow
        if (Optional.ofNullable(t.getShadowColor()).isPresent()) {
            g.setColor(t.getShadowColor());
            g.drawString(t.getText(), (int) t.getPosition().x + offset + 2, (int) t.getPosition().y + 2);
        }
        // draw border
        if (t.getBorderWidth() > 0) {
            g.setColor(t.getColor());
            for (int dx = -t.getBorderWidth(); dx < t.getBorderWidth(); dx += 1) {
                for (int dy = -t.getBorderWidth(); dy < t.getBorderWidth(); dy += 1) {
                    g.drawString(t.getText(), (int) t.getPosition().x + offset + dx, (int) t.getPosition().y + dy);
                }
            }
        }
        // draw text
        g.setColor(t.getTextColor());
        g.drawString(t.getText(), (int) t.getPosition().x + offset, (int) t.getPosition().y);
    }
}