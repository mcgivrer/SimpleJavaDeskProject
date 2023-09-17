package com.snapgames.core.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceManagerTest {
    ServiceManager sm;

    @BeforeEach
    void setUp() {
        sm = ServiceManager.get();
        sm.dispose();
    }

    @AfterEach
    void tearDown() {
        sm.dispose();
        sm = null;
    }

    @Test
    void get() {
        assertNotNull(ServiceManager.get());
    }

    @Test
    void add() {
        sm.add(new TestService());
        TestService ts = sm.find(TestService.class);
        assertNotNull(ts);
        assertEquals(TestService.class, ts.getServiceName());
    }

    @Test
    void initialize() {
        sm.add(new TestService());
        sm.initialize(null);
        TestService ts = sm.find(TestService.class);
        assertTrue(ts.isInitialized());
    }

    @Test
    void dispose() {
        sm.add(new TestService());
        sm.dispose();
        TestService ts = sm.find(TestService.class);
        assertNull(ts);
    }
}