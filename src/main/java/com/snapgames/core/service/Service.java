package com.snapgames.core.service;

import com.snapgames.core.App;
import com.snapgames.core.utils.Configuration;

/**
 * Define a standard API for a {@link Service} to be managed by the {@link ServiceManager}.
 *
 * @author Frédéric Delorme
 * @since 1.0.1
 */
public interface Service {
    /**
     * Retrieve the name (class) of the service.
     *
     * @return
     */
    default Class<? extends Service> getServiceName() {
        return this.getClass();
    }

    /**
     * Initialize the {@link Service} implementation with the parent {@link App} instance.
     *
     * @param app
     */
    default void initialize(Configuration app) {

    }

    /**
     * A default dispose implementation for unnecessary Service dispose.
     */
    default void dispose() {

    }
}
