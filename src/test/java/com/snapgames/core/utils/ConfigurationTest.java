package com.snapgames.core.utils;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

import com.snapgames.core.physic.Vector2D;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConfigurationTest {
    Configuration config;

    @BeforeEach
    public void setup() {
        config = new Configuration("/test-config.properties");
    }

    @AfterEach
    public void ending() {
        config = null;
    }

    @Test
    public void testIfConfigurationDebugSet() {
        assertTrue(config.debug);
    }

    @Test
    public void testIfConfigurationDebugLevelSet() {
        assertEquals(4, config.debugLevel);
    }

    @Test
    public void testIfConfigurationScreenResolutionSet() {
        assertEquals(new Rectangle2D.Double(0, 0, 320, 200), config.bufferResolution);
    }

    @Test
    public void testIfConfigurationWindowSize() {
        assertEquals(new Dimension(640, 480), config.windowSize);
    }

    @Test
    public void testIfConfigurationGravitySet() {
        assertEquals(new Vector2D(0, -0.981), config.gravity);
    }

    @Test
    public void testIfConfigurationPlayAreaSet() {
        assertEquals(new Rectangle2D.Double(0, 0, 320, 200), config.playArea);
    }

    @Test
    public void testIfConfigurationSpaceMaxEntitySet() {
        assertEquals(10, config.maxEntitiesInSpace);
    }

    @Test
    public void testIfConfigurationSpaceDepthSet() {
        assertEquals(5, config.maxDepthLevelInSpace);
    }

}
