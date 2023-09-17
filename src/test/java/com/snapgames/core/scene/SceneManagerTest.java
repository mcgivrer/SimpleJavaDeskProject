package com.snapgames.core.scene;

import com.snapgames.core.App;
import com.snapgames.core.service.ServiceManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SceneManagerTest {
    SceneManager scm;
    App app;

    @BeforeEach
    void setUp() {
        app = new TestApp();
        scm = new SceneManager(app);
    }

    @AfterEach
    void tearDown() {
        scm = null;
    }

    @Test
    void initialize() {
        scm.initialize(null);
    }

    @Test
    void add() {
        TestScene ts = new TestScene(app, "test_scene");
        scm.add(ts);
        assertEquals(ts, scm.getScene("test_scene"));
    }

    @Test
    void testActivate() {
        TestScene ts = new TestScene(app, "test_scene");
        scm.add(ts);
        scm.activate();
        assertEquals(ts, scm.getCurrent());
    }

    @Test
    void testActivateByName() {
        TestScene ts1 = new TestScene(app, "test_scene_1");
        TestScene ts2 = new TestScene(app, "test_scene_2");
        scm.add(ts1);
        scm.add(ts2);
        scm.activate("test_scene_2");
        assertEquals(ts2, scm.getCurrent());
    }

    @Test
    void dispose() {
        TestScene ts1 = new TestScene(app, "test_scene_1");
        TestScene ts2 = new TestScene(app, "test_scene_2");
        scm.add(ts1);
        scm.add(ts2);
        scm.dispose();
        assertNull(scm.getScene("test_scene_1"));
        assertNull(scm.getScene("test_scene_2"));
    }
}