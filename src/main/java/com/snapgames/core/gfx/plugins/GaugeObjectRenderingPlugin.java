package com.snapgames.core.gfx.plugins;

import com.snapgames.core.entity.Entity;
import com.snapgames.core.entity.GaugeObject;
import com.snapgames.core.gfx.Renderer;
import com.snapgames.core.gfx.RendererPlugin;
import com.snapgames.core.scene.Scene;

import java.awt.*;

public class GaugeObjectRenderingPlugin implements RendererPlugin<GaugeObject> {
    @Override
    public Class<GaugeObject> getEntityClass() {
        return GaugeObject.class;
    }

    @Override
    public void draw(Renderer r, Graphics2D g, Scene s, Entity e) {
        GaugeObject go = (GaugeObject) e;
        // draw shadow
        g.setColor(go.getShadowColor());
        g.fillRect((int) go.getPosition().x + 3, (int) go.getPosition().y + 3, (int) go.getSize().x-2, (int) go.getSize().y-2);
        // draw background
        g.setColor(go.getFillColor());
        g.fillRect((int) go.getPosition().x, (int) go.getPosition().y, (int) go.getSize().x, (int) go.getSize().y);

        double unitValue = (go.getSize().x - 2) / (go.getMaxValue() - go.getMinValue());
        // draw gauge background
        g.setColor(go.getGaugeColor().darker().darker());
        g.fillRect((int) go.getPosition().x + 1, (int) go.getPosition().y + 1, (int) (go.getMaxValue() * unitValue), (int) go.getSize().y - 2);

        // draw gauge
        g.setColor(go.getGaugeColor());
        g.fillRect((int) go.getPosition().x + 1, (int) go.getPosition().y + 1, (int) (go.getCurrentValue() * unitValue), (int) go.getSize().y - 2);
        g.setColor(go.getGaugeColor().brighter());
        g.fillRect((int) go.getPosition().x + 1, (int) go.getPosition().y + 1, (int) (go.getCurrentValue() * unitValue), 1);
    }
}
