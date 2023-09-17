package com.snapgames.core.service;

import com.snapgames.core.utils.Configuration;

public class TestService implements Service {
    private boolean initialized;

    @Override
    public void initialize(Configuration app) {
        this.initialized = true;
    }

    @Override
    public void dispose() {
        this.initialized = false;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
}
