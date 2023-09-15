package com.snapgames.core.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceManager {
    private static final Map<String, Object> resources = new ConcurrentHashMap<>();

    public static void add(String path, BufferedImage img) {
        resources.put(path, img);
    }

    public static <T> T get(String path) {
        return (T) resources.get(path);
    }

    public static <T> T add(String path) {
        String ext = path.substring(path.lastIndexOf('.')+1, path.length());
        switch (ext) {
            case "png", "jpg" -> {
                BufferedImage img = null;
                try {
                    img = ImageIO.read(ResourceManager.class.getResourceAsStream(path));
                } catch (IOException e) {
                    System.err.printf(">> ERR : Unable to read image file at %s : %s%n", path, e.getMessage());
                }
                add(path, img);
                return (T) img;
            }
            default -> {
                System.err.printf(">> ERR : Resource reader for %s not already implemented%n", path);
            }
        }
        return null;
    }
}
