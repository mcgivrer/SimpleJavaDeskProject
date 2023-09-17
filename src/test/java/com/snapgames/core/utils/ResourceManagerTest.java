package com.snapgames.core.utils;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class ResourceManagerTest {

    @Test
    void add() {
        ResourceManager.add("test_obj_1", .009);
        double value = ResourceManager.get("test_obj_1");
        assertEquals(.009, value, 0.0001);
    }


    @Test
    void testAddAndGetImage() {
        BufferedImage img1 = ResourceManager.add("/images/backgrounds/forest.jpg");
        BufferedImage img2 = ResourceManager.get("/images/backgrounds/forest.jpg");

        assertEquals(img1, img2);
    }

    @Test
    void testAddAndGetFont() {
        Font font1 = ResourceManager.add("/fonts/tino/Tinos for Powerline.ttf");
        Font font2 = ResourceManager.get("/fonts/tino/Tinos for Powerline.ttf");

        assertEquals(font1, font2);
    }
}