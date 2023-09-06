package com.snapgames.demo.test001;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

import com.snapgames.core.App;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AppConfigTest {
    App app;

    @BeforeEach
    public void setup() {
        app = new App("/test-config.properties");
        app.run(new String[]{});
    }

    @AfterEach
    public void ending() {
        app = null;
    }

    @Test
    public void testIfConfigurationDebugSet() {
        assertTrue(app.getDebug());
    }

    @Test
    public void testIfConfigurationDebugLevelSet() {
        assertEquals(4, app.getDebugLevel());
    }

    @Test
    public void testIfConfigurationScreenResolutionSet() {
        assertEquals(new Rectangle2D.Double(0, 0, 320, 200), app.getRenderer().getScreenResolution());
    }

    @Test
    public void testIfConfigurationWindowSize() {
        assertEquals(new Dimension(640, 480), app.getRenderer().getWindowSize());
    }
}
