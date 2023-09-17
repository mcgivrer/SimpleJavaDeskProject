package com.snapgames.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.snapgames.core.App;
import com.snapgames.core.service.ServiceManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AppTest {
    App app;

    @BeforeEach
    public void setup() {
        app = new App("/test-config.properties");
    }

    @AfterEach
    public void ending() {

        app = null;
        ServiceManager.get().dispose();
    }

    @Test
    public void testIfApplicationRunOnVersion() {
        assertEquals("1.0.0", app.getVersion());
    }

    @Test
    public void testIfApplicationHasRightTitle() {
        assertEquals("Application Test001", app.getName());
    }

}
