package com.snapgames.core.scene;

import com.snapgames.core.App;
import com.snapgames.core.service.Service;
import com.snapgames.core.utils.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The {@link SceneManager} service implementation provides the global mechanism to support {@link Scene} lifecycle.
 * <p>
 * You can add any Scene implementation to the SceneManager internal list through
 * the {@link SceneManager#add(Scene)} method.
 *
 * @author Frédéric Delorme
 * @since 1.0.1
 */
public class SceneManager implements Service {

    private final App app;
    private Map<String, Scene> scenes = new HashMap<>();
    private Scene currentScene;

    /**
     * Create the {@link SceneManager} service instance attached to the parent {@link App} instance.
     *
     * @param app the parent {@link App} instance to link this {@link SceneManager} instance with.
     */
    public SceneManager(App app) {
        this.app = app;
    }

    /**
     * Add a new {@link Scene} implementation to the manager.
     * <p>
     * If the scene is the first one, it is considered as the default activated one.
     *
     * @param scene the {@link Scene} implementation to add to the manager.
     */
    public void add(Scene scene) {
        this.scenes.put(scene.getName(), scene);
        if (Optional.ofNullable(currentScene).isEmpty()) {
            scene.load();
            this.currentScene = scene;
        }
    }

    /**
     * Activate the current {@link Scene} instance.
     * <p>
     * Call the {@link Scene#create(App)} method to create all the scene internal entities and behaviors.
     */
    public void activate() {
        if (Optional.ofNullable(currentScene).isPresent()) {
            // set pause off (for restarting Scene)
            app.setPause(false);
            this.currentScene.create(app);
        }
    }

    /**
     * request to activate the {@link Scene} instance having this sceneMane.
     *
     * @param sceneName the name of the {@link Scene} to be activated.
     * @return the current activated {@link Scene}.
     */
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

    /**
     * force the current active Scene.
     *
     * @param scene the Scene instance to be the current one (without activating it).
     */
    public void setCurrentScene(Scene scene) {
        this.currentScene = scene;
    }

    /**
     * Return the current active {@link Scene} instance.
     *
     * @return
     */
    public Scene getCurrent() {
        return currentScene;
    }

    /**
     * Dispose all the {@link Scene} managed by this service, and clear the internal {@link Scene} list.
     */
    public void dispose() {
        for (Scene scn : scenes.values()) {
            scn.dispose(app);
        }
        scenes.clear();
    }

    /**
     * Get a known {@link Scene} by its sceneName.
     *
     * @param sceneName the name of the {@link Scene} instance to be retrieved.
     * @return the {@link Scene} instance to be retrieved from the {@link SceneManager}.
     */
    public Scene getScene(String sceneName) {
        return scenes.get(sceneName);
    }
}
