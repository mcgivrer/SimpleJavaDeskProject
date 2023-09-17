package com.snapgames.core.service;

import com.snapgames.core.App;
import com.snapgames.core.utils.Configuration;

public interface Service {
    default Class<? extends Service> getServiceName() {
        return this.getClass();
    }

    void initialize(Configuration app);

    void dispose();
}
