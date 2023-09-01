package com.snapgames.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SceneManager {

    private final App app;
    private Map<String, Scene> scenes = new HashMap<>();
    private Scene currentScene;

    public SceneManager(App app) {
        this.app = app;
    }

    public Scene getCurrent() {
        return currentScene;
    }

    public void add(Scene scene) {
        this.scenes.put(scene.getName(), scene);
        if (Optional.ofNullable(currentScene).isEmpty()) {
            this.currentScene = scene;
        }
    }

    public Scene activate() {
        if (Optional.ofNullable(currentScene).isPresent()) {
            this.currentScene.create(app);
        }
        return currentScene;
    }

    public Scene activate(String sceneName) {
        if (scenes.containsKey(sceneName)) {
            if (Optional.ofNullable(currentScene).isPresent()) {
                currentScene.dispose(app);
            }
            Scene scn = scenes.get(sceneName);
            scn.create(app);
            this.currentScene = scn;
        }
        return currentScene;
    }

    public void dispose() {
        for (Scene scn : scenes.values()) {
            scn.dispose(app);
        }
    }
}
