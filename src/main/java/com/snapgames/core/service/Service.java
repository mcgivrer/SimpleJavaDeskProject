package com.snapgames.core.service;

import com.snapgames.core.App;
import com.snapgames.core.utils.Configuration;

public interface Service {
    default String getName() {
        return this.getClass().getName();
    }

    void initialize(Configuration app);

    void dispose();
}
