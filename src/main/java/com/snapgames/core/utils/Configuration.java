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
            config.entrySet().stream()
                    .filter(e -> e.getKey() != null)
                    .forEach(e -> setConfigValueFrom((String) e.getKey(), (String) e.getValue()));
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
                System.out.printf(">> <#> configuration Application name set to %s%n",
                        this.name);
            }
            case "app.main.version" -> {
                this.version = value;
                System.out.printf(">> <#> configuration Application Version set to %s%n",
                        this.version);
            }
            case "app.debug", "d", "debug" -> {
                this.debug = Boolean.valueOf(value);
                System.out.printf(">> <#> configuration Debug set to %s%n",
                        this.debug ? "true" : "false");
            }
            case "app.debug.level", "dl", "debugLevel" -> {
                this.debugLevel = Integer.parseInt(value);
                System.out.printf(">> <#> configuration Debug Level set to %d%n", debugLevel);
            }
            case "app.window.size" -> {
                String[] dim = value.split("x");
                this.windowSize = new Dimension(
                        Integer.parseInt(dim[0]),
                        Integer.parseInt(dim[1]));
                System.out.printf(">> <#> configuration Window Size set to %.2fx%.2f%n", windowSize.getWidth(), windowSize.getHeight());
            }
            case "app.screen.resolution" -> {
                String[] dim = value.split("x");
                this.bufferResolution = new Rectangle2D.Double(
                        0, 0,
                        Double.parseDouble(dim[0]),
                        Double.parseDouble(dim[1]));
                System.out.printf(">> <#> configuration Screen Resolution set to %.2fx%.2f%n", bufferResolution.getWidth(), bufferResolution.getHeight());
            }
            case "app.physic.play.area" -> {
                String[] dim = value.split("x");
                this.playArea = new Rectangle2D.Double(
                        0, 0,
                        Double.parseDouble(dim[0]),
                        Double.parseDouble(dim[1]));
                System.out.printf(">> <#> configuration Play Area set to %.2fx%2f%n", playArea.getWidth(), playArea.getHeight());
            }
            case "app.test.mode" -> {
                this.testMode = Boolean.parseBoolean(value);
                System.out.printf(">> <#> configuration Test Mode set to %s%n",
                        this.debug ? "true" : "false");
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
