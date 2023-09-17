package com.snapgames.core.service;

import com.snapgames.core.utils.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceManager {
    private final Map<Class<? extends Service>, Service> services;
    private static ServiceManager instance;

    private ServiceManager() {
        services = new HashMap<>();
    }

    public static ServiceManager get() {
        if (instance == null) {
            instance = new ServiceManager();
        }
        return instance;
    }

    public ServiceManager add(Service service) {
        services.put(service.getServiceName(), service);
        return this;
    }

    public void initialize(Configuration c) {
        for (Service s : services.values()) {
            s.initialize(c);
        }
    }

    public void dispose() {
        for (Service s : services.values()) {
            s.dispose();
        }
        services.clear();
    }


    public <T extends Service> T find(Class<? extends Service> className) {
        return (T) services.get(className);
    }
}
