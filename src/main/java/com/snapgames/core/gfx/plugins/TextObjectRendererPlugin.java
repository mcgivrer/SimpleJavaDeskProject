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
        if (Optional.ofNullable(t.getShadowColor()).isPresent()) {
            g.setColor(t.getShadowColor());
            g.drawString(t.getText(), (int) t.getPosition().x + 2, (int) t.getPosition().y + 2);
        }
        g.setColor(t.getTextColor());
        g.drawString(t.getText(), (int) t.getPosition().x, (int) t.getPosition().y);
    }
}
