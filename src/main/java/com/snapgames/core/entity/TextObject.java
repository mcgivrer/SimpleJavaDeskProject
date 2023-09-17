package com.snapgames.core.entity;

import com.snapgames.core.physic.Material;

import java.awt.*;

public class TextObject extends Entity<TextObject> {
    private String text;
    private Object value;
    private Font font;

    private TextAlign textAlign;

    private Color textColor;
    private Color shadowColor;
    private int borderWidth;

    public TextObject(String name) {
        super(name);
        setMass(1.0);
        setMaterial(Material.DEFAULT);
        setTextAlign(TextAlign.LEFT);
    }

    public void setTextAlign(TextAlign textAlign) {
        this.textAlign = textAlign;
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

    public TextObject withTextAlign(TextAlign t) {
        this.textAlign = t;
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

    public TextAlign getTextAlign() {
        return textAlign;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public TextObject withBorderWidth(int bw) {
        this.borderWidth = bw;
        return this;
    }

    public TextObject withBorderColor(Color bc) {
        this.color = bc;
        return this;
    }
}
