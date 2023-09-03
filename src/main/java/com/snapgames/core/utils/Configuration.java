package com.snapgames.core.utils;

import com.snapgames.core.App;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Properties;

public class Configuration {

    private String name;
    private String version;
    private Properties config = new Properties();
    public boolean debug;
    public int debugLevel;
    public boolean testMode;
    public Dimension windowSize;
    public Rectangle2D bufferResolution;
    public Rectangle2D playArea;

    public Configuration(App app, String configFilePath) {
        try {
            config.load(App.class.getResourceAsStream(configFilePath));
            config.forEach((k, v) -> setConfigValueFrom((String) k, (String) v));
        } catch (IOException e) {
            System.err.printf(">> <!> Unable to read confguration file %s%n", e.getMessage());
        } finally {
            System.out.printf(">> %s (%s)%n", name, version);
        }
    }

    private void setConfigValueFrom(String key, String value) {
        switch (key) {
            case "app.main.name" -> {
                this.name = value;
            }
            case "app.main.version" -> {
                this.version = value;
            }
            case "app.debug", "d", "debug" -> {
                this.debug = Boolean.valueOf(value);
                System.out.printf(">> <#> configuration debug set to %s%n",
                        this.debug ? "true" : "false");
            }
            case "app.debug.level", "dl", "debugLevel" -> {
                this.debugLevel = Integer.parseInt(value);
                System.out.printf(">> <#> configuration debuglevel set to %d%n", debugLevel);
            }
            case "app.window.size" -> {
                String[] dim = value.split("x");
                this.windowSize = new Dimension(
                        Integer.parseInt(dim[0]),
                        Integer.parseInt(dim[1]));
            }
            case "app.screen.resolution" -> {
                String[] dim = value.split("x");
                this.bufferResolution = new Rectangle2D.Double(
                        0, 0,
                        Double.parseDouble(dim[0]),
                        Double.parseDouble(dim[1]));
            }
            case "app.physic.play.area" -> {
                String[] dim = value.split("x");
                this.playArea = new Rectangle2D.Double(
                        0, 0,
                        Double.parseDouble(dim[0]),
                        Double.parseDouble(dim[1]));
            }
            case "app.test.mode" -> {
                this.testMode = Boolean.parseBoolean(value);
            }
            default -> System.err.printf("argument/confguration key '%s' unknown%n", key);
        }
    }


    public void parseCLIArguments(String[] args) {
        for (String arg : args) {
            System.out.printf("arg : %s%n", arg);
            String[] values = arg.split("=");
            setConfigValueFrom(values[0], values[1]);
        }
    }
}
