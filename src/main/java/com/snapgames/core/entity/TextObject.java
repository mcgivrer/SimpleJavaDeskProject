package com.snapgames.core.entity;

import com.snapgames.core.physic.Material;

import java.awt.*;

public class TextObject extends Entity<TextObject> {
    private String text;
    private Object value;
    private Font font;

    private Color textColor;
    private Color shadowColor;

    public TextObject(String name) {
        super(name);
        setMass(1.0);
        setMaterial(Material.DEFAULT);
    }

    public Object getValue() {
        return this.value;
    }

    public String getText() {
        if (text.contains("%")) {
            return String.format(text, value);
        }
        return text;
    }

    public TextObject withText(String text) {
        this.text = text;
        return this;
    }

    public TextObject withValue(Object value) {
        this.value = value;
        return this;
    }

    public TextObject withTextColor(Color textColor) {
        this.textColor = textColor;
        return this;
    }

    public TextObject withShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
        return this;
    }

    public TextObject withFont(Font font) {
        this.font = font;
        return this;
    }

    public Color getShadowColor() {
        return shadowColor;
    }

    public Color getTextColor() {
        return textColor;
    }

    public Font getFont() {
        return font;
    }

}
