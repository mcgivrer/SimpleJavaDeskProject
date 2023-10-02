package com.snapgames.core;

import com.snapgames.core.gfx.Renderer;
import com.snapgames.core.io.AppInputListener;
import com.snapgames.core.io.InputHandler;
import com.snapgames.core.io.action.ActionHandler;
import com.snapgames.core.loop.GameLoop;
import com.snapgames.core.loop.StandardGameLoop;
import com.snapgames.core.physic.PhysicEngine;
import com.snapgames.core.physic.SpacePartition;
import com.snapgames.core.scene.SceneManager;
import com.snapgames.core.service.ServiceManager;
import com.snapgames.core.utils.Configuration;
import com.snapgames.demo.test001.scenes.DemoScene;

import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * A basic CLI java application.
 *
 * @author Frédéric Delorme
 * @since 1.0.0
 */
public class App {
    public static final ResourceBundle messages = ResourceBundle.getBundle("i18n/messages");
    static final int FPS = 60;

    // internal attributes
    public String name = "Application Test001";
    public String version = "1.0.0";
    private boolean exit;
    private boolean testMode;
    // debug mode
    private static Boolean debug;
    private static int debugLevel;
    private String debugFilter;

    private boolean pause;

    // Configuration
    private Configuration configuration;
    // main GameLoop implementation
    GameLoop gameLoop;
    // Scene manager service
    private SceneManager sceneManager;
    // rendering service
    private Renderer renderer;
    // Physic processing engine
    private PhysicEngine physicEngine;
    // input manager (Keyboard & more to come)
    private InputHandler inputHandler;
    // Action handler
    private ActionHandler actionHandler;
    // manage play area as partitioned spaces to spread Entity through.
    private SpacePartition spacePartitioning;

    /**
     * This is the {@link App} class.
     */
    public App() {
        initialize("/config.properties");
    }

    /**
     * Create the App instance according to the defined configuration file.
     *
     * @param configFilePath the path to the new configuration file path to be
     *                       loaded at start.
     */
    public App(String configFilePath) {
        initialize(configFilePath);
    }

    public static boolean isDebugLevelAtLeast(int i) {
        return debugLevel >= i;
    }

    /**
     * Read configuration values from the <code>config.properties</code> file.
     *
     * @param configurationFilepath path to the configuration file to be loaded.
     */
    private void initialize(String configurationFilepath) {

        // load configuration
        configuration = new Configuration(configurationFilepath);
        if (!configuration.noInitialization) {
            // create services
            gameLoop = new StandardGameLoop();
            sceneManager = new SceneManager(this);
            inputHandler = new InputHandler(this);
            actionHandler = new ActionHandler(this);
            physicEngine = new PhysicEngine(this);
            spacePartitioning = new SpacePartition(this);

            renderer = new Renderer(this);

            ServiceManager.get()
                    .add(sceneManager)
                    .add(renderer)
                    .add(physicEngine)
                    .add(inputHandler)
                    .add(spacePartitioning)
                    .add(actionHandler);

            inputHandler.add(new AppInputListener(this));

            // initialize service against configuration
            ServiceManager.get().initialize(configuration);

            System.out.printf(">> Window %s%n", messages.getString("app.title"));
            System.out.printf(">> Running on JRE %s%n", System.getProperty("java.version"));

        } else {
            System.out.printf(">> No Initialization, Running on JRE %s%n", System.getProperty("java.version"));
        }
    }


    /**
     * Run the application and start by displaying CLI args.
     *
     * @param args all the Java CLI arguments.
     */
    public void run(String[] args) {
        configuration.parseCLIArguments(args);
        // retrieve App attributes value from Configuration.
        debug = configuration.debug;
        debugLevel = configuration.debugLevel;
        this.testMode = configuration.testMode;
        this.debugFilter = configuration.debugFilter;
        this.name = configuration.name;
        this.version = configuration.version;

        // create a window to display game
        renderer.createWindow(this, inputHandler);
        // start loop until exit request
        loop();
        // exit now !
        dispose();
    }


    private void loop() {
        createScene();
        sceneManager.activate();

        System.out.printf(sceneManager.getCurrent().treeToString());
        gameLoop.loop(this);
    }

    private void createScene() {
        DemoScene scene = new DemoScene(this);
        sceneManager.add(scene);
    }

    public void input() {
        actionHandler.update(this, 0);
        sceneManager.getCurrent().input(this, inputHandler);
    }

    public void update(long elapsed, Map<String, Object> stats) {
        physicEngine.update(this, sceneManager.getCurrent(), elapsed, stats);
        spacePartitioning.update(this, sceneManager.getCurrent(), elapsed, stats);

    }

    public void render(Map<String, Object> stats) {
        renderer.draw(this, sceneManager.getCurrent(), stats);
    }

    private void dispose() {
        ServiceManager.get().dispose();
    }

    /**
     * Retrieve the {@link App} title.
     *
     * @return a string with {@link App} title.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieve the {@link App} version.
     *
     * @return a String with the {@link App} verison.
     */
    public String getVersion() {
        return version;
    }

    public static boolean getDebug() {
        return debug;
    }

    public int getDebugLevel() {
        return debugLevel;
    }

    public void setExit(boolean e) {
        this.exit = e;
    }

    private boolean isExited() {
        return this.exit;
    }

    public void setPause(boolean p) {
        this.pause = p;
    }

    public boolean isPaused() {
        return this.pause;
    }

    /**
     * The standard main entry point for {@link App} java.
     *
     * @param args list of CLI arguments.
     */
    public static void main(String[] args) {
        App app = new App();
        app.run(args);
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public int getFPS() {
        return FPS;
    }

    public boolean isExit() {
        return exit;
    }

    public boolean isTestMode() {
        return testMode;
    }

    public boolean isDebugEnabled() {
        return debug;
    }

    public PhysicEngine getPhysicEngine() {
        return physicEngine;
    }

    public void setDebugLevel(int dl) {
        debugLevel = dl;
    }

    public void setDebug(boolean d) {
        debug = d;
    }

    public InputHandler getInputHandler() {
        return this.inputHandler;
    }

    public boolean isDebugLevelMin(int minimumDebugLevelRequired) {
        return debugLevel >= minimumDebugLevelRequired;
    }

    public boolean isDebugFiltered(String name) {
        return debugFilter.contains(name);
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }
}