package com.snapgames.core.scene;

import com.snapgames.core.App;
import com.snapgames.core.service.Service;
import com.snapgames.core.utils.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SceneManager implements Service {

    private final App app;
    private Map<String, Scene> scenes = new HashMap<>();
    private Scene currentScene;

    public SceneManager(App app) {
        this.app = app;
    }

    @Override
    public void initialize(Configuration app) {
        // may be implements later a Scene loader.
    }

    public void add(Scene scene) {
        this.scenes.put(scene.getName(), scene);
        if (Optional.ofNullable(currentScene).isEmpty()) {
            this.currentScene = scene;
        }
    }

    public void activate() {
        if (Optional.ofNullable(currentScene).isPresent()) {
            this.currentScene.create(app);
        }
    }

    public Scene activate(String sceneName) {
        if (scenes.containsKey(sceneName)) {
            if (Optional.ofNullable(currentScene).isPresent()) {
                currentScene.dispose(app);
            }
            setCurrentScene(scenes.get(sceneName));
            activate();
        }
        return currentScene;
    }

    public void setCurrentScene(Scene scene) {
        this.currentScene = scene;
    }


    public Scene getCurrent() {
        return currentScene;
    }


    public void dispose() {
        for (Scene scn : scenes.values()) {
            scn.dispose(app);
        }
    }
}
