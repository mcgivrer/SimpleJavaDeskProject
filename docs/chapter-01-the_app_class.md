## The App Class

As proposed in the introduction, the main processing of a game is a loop.

The `App` class is the container of such loop.

<figure>
<img src="http://www.plantuml.com/plantuml/png/VOynRiOm28Ltdu9koT0BPAZIew0OjKWO8nihLKzVbAc9VddnW3_UzzYxO_DH9BtvDnK24sHD9Hvu_u1FC-43dQjke7IcVuF7Doixe_0lBUiD2-wAs-0BGy0QIZtejQCDdJYEHkYKf5Q3eSu2zCEU06QrRFYo1eFYeDN1S7JACRYjFPiCow6w5adpLD42e1ReDF14zc22dHoOkLlj5DmUu2Zo1m00" alt=""figure 5 - The App Loop"/>
<figCaption>The main application loop and its decorating functions</figCaption>
</figure>

It will decorate this loop with some important functions:

- `initialize()` initialization of the application,
- `run()` ti start the application
- `create()` to create the required object before looping,
- `dispose()` to release resources;

And in the Loop we will execute :

- `input()` to manage interactive input from player,
- `update()` to update all the internal objects according to time (we will implement a dedicated service to manage some
  Newton's physic laws),
- `render()` will draw all the beautiful objects to the application window, with some buffered mechanics.~~~~

### The GameLoop interface

And the way these looping methods are called is provided by
a new API `GameLoop` implementation:

```java
public interface GameLoop {

    void loop(App app);

}
```

By introducing such interface, we will be able to implement the presented Game loop in the introduction chapter:

1. a game loop in a constant frame rate
2. a game loop with a constant update rate.

Our first interest here will the constant frame rate with the `StandardGameLoop` implementation.

### A constant frame rate loop

A common implementation provides some internal time counter:

```java
public class StandardGameLoop implements GameLoop {

    @Override
    public void loop(App app) {
        long start = System.currentTimeMillis();
        long current;
        long previous = start;
        long internalTime = 0;
        long elapsed = 0;
        int frames = 0;
        int updates = 0;
        int fps = 0;
        int ups = 0;
        int cumulatedTime = 0;
        Map<String, Object> stats = new HashMap<>();

        while (!app.isExit() && !app.isTestMode()) {
            current = System.currentTimeMillis();
            elapsed = System.currentTimeMillis() - previous;
            app.input();
            if (!app.isPaused()) {
                app.update(elapsed, stats);
                internalTime += elapsed;
                updates++;
            }
            app.render(stats);
            frames++;

            cumulatedTime += elapsed;
            if (cumulatedTime > 1000) {
                fps = frames;
                ups = updates;
                updates = 0;
                frames = 0;
                cumulatedTime = 0;
            }
            double timeFrame = 1000.0 / app.getFPS();
            int wait = (int) (timeFrame - elapsed > 0 ? timeFrame - elapsed : 1);
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                System.err.println("unable to wait for some milliseconds");
            }
            previous = current;
        }
    }

}
```

We first define a bunch of internal variables:

```java
public class StandardGameLoop implements GameLoop {

    @Override
    public void loop(App app) {
        long start = System.currentTimeMillis();
        long current;
        long previous = start;
        long internalTime = 0;
        long elapsed = 0;
        int frames = 0;
        int updates = 0;
        int fps = 0;
        int ups = 0;
        int cumulatedTime = 0;
        Map<String, Object> stats = new HashMap<>();
        //...
    }
}
```

- The first variables `start`, `current` and `previous` will be:

    - the starting time,
    - the internal current one,
    - and the previous loop cycle time.

- `internalTime` will count the total elapsed game time,
- `elapsed` is the loop elapsed time.

the come some counters:

- `frames` counts the number of frames, to compute the frame per second rate in frame/s,
- `updates` counts the number of updates, to compute the update per second rate in update/s,
- `fps` and `ups` are the resulting rates to be eventually displayed on screen.

and ending with the `stats` map, containing some exposed statistics to the rest of the world.

Then the processing `loop`, taking care of 2 App state, exit and pause:

- the `pause` state to manage update and render ,
- and `exit` state to exit of the loop if the exit state is true.

The other processing consists of calling in the right sort order:

- the `App#input()`,
- the `App#update(elapsed,stats)`,
- and the `App#render(stats)`.

And the rest of the code maintains and computes

- `frames` and `updates` counters,
- `ups` and `fps` rates.

### the App implementation

```java
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
            physicEngine = new PhysicEngine(this);
            spacePartitioning = new SpacePartition(this);
            renderer = new Renderer(this);

            ServiceManager.get()
                    .add(sceneManager)
                    .add(renderer)
                    .add(physicEngine)
                    .add(inputHandler)
                    .add(spacePartitioning);

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
        debug = configuration.debug;
        debugLevel = configuration.debugLevel;
        this.testMode = configuration.testMode;
        this.debugFilter = configuration.debugFilter;
        this.name = configuration.name;
        this.version = configuration.version;

        loop();
        dispose();
    }


    private void loop() {
        createScene();
        gameLoop.loop(this);
    }

    private void createScene() {}

    public void input() {}

    public void update(long elapsed, Map<String, Object> stats) {}

    public void render(Map<String, Object> stats) {}

    private void dispose() {
    }
```