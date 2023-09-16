package com.snapgames.core.entity;

import java.awt.*;

public class GaugeObject extends Entity<GaugeObject> {
    private Color shadowColor;
    private Color gaugeColor;
    private double minValue;
    private double maxValue;

    public Color getShadowColor() {
        return shadowColor;
    }

    public Color getGaugeColor() {
        return gaugeColor;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    private double currentValue;

    public GaugeObject(String name) {
        super(name);
    }

    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
    }

    public void setGaugeColor(Color gaugeColor) {
        this.gaugeColor = gaugeColor;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public GaugeObject withShadowColor(Color shadowColor) {
        setShadowColor(shadowColor);
        return this;
    }

    public GaugeObject withGaugeColor(Color gqugeColor) {
        setGaugeColor(gqugeColor);
        return this;
    }

    public GaugeObject withMinValue(double minValue) {
        setMinValue(minValue);
        return this;
    }

    public GaugeObject withMaxValue(double maxValue) {
        setMaxValue(maxValue);
        return this;
    }

    public GaugeObject withCurrentValue(double currentValue) {
        setCurrentValue(currentValue);
        return this;
    }
}
