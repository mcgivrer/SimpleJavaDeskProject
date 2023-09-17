package com.snapgames.core.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@link ResourceManager} is the resource store where to load all needed resources.
 * <p>
 * Sample usage :
 *
 * <pre>
 *  // at initialization
 * ResourceManager.add("/images/my_image.png");
 * ResourceManager.add("/font/my_font.ttf");
 *
 * // later to use resource :
 * BufferedImage img = ResourceManager.get("images/my_image.png");
 * Font font = ResourceManager.get("images/my_font.ttf");
 * </pre>
 *
 * @author Frédéric Delorme
 * @Since 0.1.4
 */
public class ResourceManager {
    private static final Map<String, Object> resources = new ConcurrentHashMap<>();

    public static void add(String path, Object obj) {
        resources.put(path, obj);
    }

    public static <T> T get(String path) {
        return (T) resources.get(path);
    }

    public static <T> T add(String path) {
        String ext = path.substring(path.lastIndexOf('.') + 1, path.length());
        switch (ext.toLowerCase()) {
            case "png", "jpg", "gif" -> {
                BufferedImage img = null;
                try {
                    img = ImageIO.read(ResourceManager.class.getResourceAsStream(path));
                } catch (IOException e) {
                    System.err.printf(">> ERR : Unable to read image file at %s : %s%n", path, e.getMessage());
                }
                add(path, img);
                return (T) img;
            }
            case "ttf" -> {
                Font font = null;
                if (!resources.containsKey(path)) {
                    try {
                        InputStream stream = ResourceManager.class.getResourceAsStream(path);
                        font = Font.createFont(Font.TRUETYPE_FONT, stream);
                        if (font != null) {
                            add(path, font);
                        }
                    } catch (FontFormatException | IOException e) {
                        System.err.printf("Unable to read font at %s : %s%n", path, e.getMessage());
                    }
                    return (T)font;
                }
                if (Optional.ofNullable(font).isPresent()) {
                    resources.put(path, font);
                }
            }
            default -> {
                System.err.printf(">> ERR : Resource reader for %s not already implemented%n", path);
            }
        }
        return null;
    }
}
