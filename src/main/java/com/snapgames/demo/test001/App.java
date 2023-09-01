package com.snapgames.demo.test001;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * A basic CLI java application.
 *
 * @author Frédéric Delorme
 * @since 1.0.0
 */
public class App extends JPanel implements KeyListener {
    private Properties config = new Properties();
    private static final ResourceBundle messages = ResourceBundle.getBundle("i18n/messages");
    private static final int FPS = 60;

    // internal attributes
    private String name = "Application Test001";
    private String version = "1.0.0";
    private boolean exit;

    // Configuration attribute values.
    private Boolean debug;
    private int debugLevel;
    private Rectangle2D screenResolution;
    private Dimension windowSize;

    private Rectangle2D playArea;

    // Graphics Properties
    JFrame frame;
    BufferedImage screenBuffer;
    private Camera currentCamera;

    private Map<String, Entity> entities = new HashMap<>();
    private boolean pause;

    // I/O attributes
    boolean[] keys = new boolean[1024];
    boolean[] prevKeys = new boolean[1024];
    private World world;
    private boolean testMode;

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

    /**
     * Read configuration values from the <code>config.properties</code> file.
     *
     * @param configurationFilepath path to the configuration file to be loaded.
     */
    private void initialize(String configurationFilepath) {

        try {
            config.load(App.class.getResourceAsStream(configurationFilepath));
            config.forEach((k, v) -> setConfigValueFrom((String) k, (String) v));
        } catch (IOException e) {
            System.err.printf(">> <!> Unable to read confguration file %s%n", e.getMessage());
        } finally {
            System.out.printf(">> %s (%s)%n", name, version);
            System.out.printf(">> Window %s%n", messages.getString("app.title"));
            System.out.printf(">> Running on JRE %s%n", System.getProperty("java.version"));
        }
    }

    private void setConfigValueFrom(String key, String value) {
        switch (key) {
            case "app.main.name" -> {
                this.name = value;
            }
            case "app.main.version" -> {
                this.version = value;
            }
            case "app.debug", "d", "debug" -> {
                this.debug = Boolean.valueOf(value);
                System.out.printf(">> <#> configuration debug set to %s%n",
                        this.debug ? "true" : "false");
            }
            case "app.debug.level", "dl", "debugLevel" -> {
                this.debugLevel = Integer.parseInt(value);
                System.out.printf(">> <#> configuration debuglevel set to %d%n", debugLevel);
            }
            case "app.window.size" -> {
                String[] dim = value.split("x");
                this.windowSize = new Dimension(
                        Integer.parseInt(dim[0]),
                        Integer.parseInt(dim[1]));
            }
            case "app.screen.resolution" -> {
                String[] dim = value.split("x");
                this.screenResolution = new Rectangle2D.Double(
                        0, 0,
                        Double.parseDouble(dim[0]),
                        Double.parseDouble(dim[1]));
            }
            case "app.physic.play.area" -> {
                String[] dim = value.split("x");
                this.playArea = new Rectangle2D.Double(
                        0, 0,
                        Double.parseDouble(dim[0]),
                        Double.parseDouble(dim[1]));
            }
            case "app.test.mode" -> {
                this.testMode = Boolean.parseBoolean(value);
            }
            default -> System.err.printf("argument/confguration key '%s' unknown%n", key);
        }
    }

    /**
     * Run the application and start by displaying CLI args.
     *
     * @param args all the Java CLI arguments.
     */
    public void run(String[] args) {
        parseCLIArguments(args);
        createWindow();
        loop();
        dispose();
    }

    private void parseCLIArguments(String[] args) {
        for (String arg : args) {
            System.out.printf("arg : %s%n", arg);
            String[] values = arg.split("=");
            setConfigValueFrom(values[0], values[1]);
        }
    }

    private void createWindow() {
        frame = new JFrame(String.format("%s(%s) - %s", name, version, messages.getString("app.title")));
        this.setPreferredSize(windowSize);
        frame.setPreferredSize(windowSize);
        frame.setLayout(new GridLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setFocusTraversalKeysEnabled(true);
        frame.setContentPane(this);
        frame.pack();
        frame.addKeyListener(this);
        frame.setVisible(true);
        frame.createBufferStrategy(3);

        screenBuffer = new BufferedImage(screenResolution.getBounds().width, screenResolution.getBounds().height,
                BufferedImage.TYPE_INT_ARGB);
    }

    private void loop() {
        createScene();
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
        while (!exit && !testMode) {
            current = System.currentTimeMillis();

            input();
            if (!pause) {
                update(elapsed, stats);
                internalTime += elapsed;
                updates++;
            }
            render(stats);
            frames++;
            elapsed = System.currentTimeMillis() - previous;
            cumulatedTime += elapsed;
            if (cumulatedTime > 1000) {
                fps = frames;
                ups = updates;
                updates = 0;
                frames = 0;
                cumulatedTime = 0;
            }
            int wait = (int) ((FPS / 1000) - elapsed > 0 ? (FPS / 1000) - elapsed : 1);
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                System.err.println("unable to wait for some milliseconds");
            }
            previous = current;
            stats.put("1_debug", debug ? "ON" : "off");
            stats.put("2_debugLevel", debugLevel);
            stats.put("3_ups", ups);
            stats.put("3_fps", fps);
            stats.put("4_time", StringUtils.formatDuration(internalTime));
        }
    }

    private void createScene() {
        this.world = new World(-0.981);
        Entity player = new Entity("player")
                .setColor(Color.GREEN.darker())
                .setFillColor(Color.GREEN)
                .setPosition(screenBuffer.getWidth() * 0.5, screenBuffer.getHeight() * 0.5)
                .setSize(16, 16)
                .setPriority(1)
                .setMass(50.0)
                .setMaterial(new Material("body", 0.9, 0.78, 0.97));

        addEntity(player);

        addEnemies(100);

        Camera cam01 = new Camera("cam01")
                .setViewport(new Rectangle2D.Double(0, 0, 300, 200))
                .setTarget(player)
                .setTweenfactor(0.002);
        addEntity(cam01);

    }

    private void addEnemies(int nbEnemies) {
        for (int i = 0; i < nbEnemies; i++) {
            Entity enemy = new Entity("enemy_" + i)
                    .setColor(Color.RED.darker())
                    .setFillColor(Color.RED)
                    .setPosition(playArea.getWidth() * Math.random(), playArea.getHeight() * Math.random() * 0.4)
                    .setSize(8, 8)
                    .setPriority(i + 10)
                    .setMass(10.0)
                    .setMaterial(new Material("enemy", 1.0, 0.98, 0.99));

            addEntity(enemy);
        }
    }

    private void addEntity(Entity entity) {
        entities.put(entity.getName(), entity);
        if (entity instanceof Camera) {
            this.currentCamera = (Camera) entity;
        }
    }

    private void input() {
        Entity player = entities.get("player");

        double speed = 0.5;
        if (keys[KeyEvent.VK_UP]) {
            player.dy = -speed;
        }
        if (keys[KeyEvent.VK_DOWN]) {
            player.dy = speed;
        }
        if (keys[KeyEvent.VK_LEFT]) {
            player.dx = -speed;
        }
        if (keys[KeyEvent.VK_RIGHT]) {
            player.dx = speed;
        }

    }

    private void update(long elapsed, Map<String, Object> stats) {
        entities.values().stream()
                .filter(Entity::isActive)
                .sorted(Comparator.comparingInt(Entity::getPriority).reversed())
                .forEach(e -> {
                    e.update(elapsed);
                    applyWorldConstrains(e, elapsed);
                    if (!(e instanceof Camera || e.isStickToCamera()))
                        constrainsEntityToPlayArea(e);
                });
    }

    private void applyWorldConstrains(Entity e, long elapsed) {
        e.dy += -world.getGravity() * elapsed * 0.005 * e.mass;

    }

    private void constrainsEntityToPlayArea(Entity e) {
        if (Optional.ofNullable(playArea).isPresent() && !playArea.contains(e.getBBox())) {
            if (e.x < playArea.getMinX() || e.x + e.w > playArea.getMaxX()) {
                if (e.x < playArea.getMinX())
                    e.x = playArea.getMinX();
                if (e.x + e.w > playArea.getMaxX())
                    e.x = playArea.getMaxX() - e.w;

                e.dx = -e.dx * e.material.elasticity;
            }
            if (e.y < playArea.getMinY() || e.y + e.h > playArea.getMaxY()) {
                if (e.y < playArea.getMinY())
                    e.y = playArea.getMinY();
                if (e.y + e.h > playArea.getMaxY())
                    e.y = playArea.getMaxY() - e.h;

                e.dy = -e.dy * e.material.elasticity;
            }
        }
    }

    private void render(Map<String, Object> stats) {

        Graphics2D g = screenBuffer.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // clear screen
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, screenBuffer.getWidth(), screenBuffer.getHeight());

        // draw play area limit
        drawPlayAreaLimits(g);

        // draw all entities
        drawEntities(g, stats);

        // draw camera viewport
        drawCameraViewport(g);

        // display pause message if required
        drawHUD(g);

        g.dispose();

        // copy rendering buffer to Window.
        copyBufferToWindow(stats);

    }

    private void drawHUD(Graphics2D g) {
        Font pauseFont = g.getFont().deriveFont(Font.BOLD, 16.0f);
        int score = 0, lives = 3;
        // draw Score
        drawText(g, pauseFont, String.format("%06d", score), screenBuffer.getWidth() - 40, 20, Color.WHITE, Color.BLACK,
                2);
        // draw life counter
        drawText(g, pauseFont.deriveFont(20.0f), "❦", 24, 22, Color.RED, Color.RED.darker(), 1);
        drawText(g, pauseFont, String.format("%d", lives), 32, 24, Color.WHITE, Color.BLACK, 2);
        // draw "paused" message if required
        if (isPaused()) {
            g.setColor(new Color(0.1f, 0.3f, 0.6f, 0.6f));
            g.fillRect(0, (int) ((screenBuffer.getHeight() - 14) * 0.5), screenBuffer.getWidth(), 20);
            String pauseText = messages.getString("game.pause.text");
            drawText(g, pauseFont, pauseText);
        }
    }

    private void copyBufferToWindow(Map<String, Object> stats) {
        Graphics2D g2 = (Graphics2D) frame.getBufferStrategy().getDrawGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2.setColor(Color.ORANGE);
        g2.setFont(getFont().deriveFont(11.0f));
        g2.drawImage(screenBuffer,
                0, frame.getInsets().top, windowSize.width, windowSize.height,
                0, 0, screenBuffer.getWidth(), screenBuffer.getHeight(),
                null);
        g2.drawString(StringUtils.prepareStatsString(stats, "[ ", " ]", " | "), 4, (int) windowSize.getHeight() - 10);
        frame.getBufferStrategy().show();
        g2.dispose();
    }

    private void drawCameraViewport(Graphics2D g) {
        moveToCameraPointOfView(g, currentCamera, -1);
        g.setColor(Color.BLUE);
        g.draw(currentCamera.viewport);
        moveToCameraPointOfView(g, currentCamera, 1);
    }

    private void drawPlayAreaLimits(Graphics2D g) {
        if (Optional.ofNullable(playArea).isPresent()) {
            moveToCameraPointOfView(g, currentCamera, -1);
            g.setColor(Color.DARK_GRAY);
            g.draw(playArea);
            for (int x = 0; x < playArea.getWidth(); x += 32) {
                g.drawRect(x, 0, 32, (int) (playArea.getHeight()));
            }
            for (int y = 0; y < playArea.getHeight(); y += 32) {
                g.drawRect(0, y, (int) (playArea.getWidth()), 32);
            }
            moveToCameraPointOfView(g, currentCamera, 1);
        }
    }

    private void drawEntities(Graphics2D g, Map<String, Object> stats) {
        long count = entities.values().stream()
                .count();
        long rendererObj = entities.values().stream()
                .filter(Entity::isActive)
                .filter(e -> !(e instanceof Camera))
                .count();
        stats.put("5_objCount", count);
        stats.put("6_objRendered", rendererObj);
        entities.values().stream()
                .filter(Entity::isActive)
                .filter(e -> !(e instanceof Camera))
                .sorted((a, b) -> Integer.compare(a.getPriority(), b.getPriority()))
                .forEach(e -> {
                    moveToCameraPointOfView(g, currentCamera, -1);
                    drawEntity(g, e);
                    moveToCameraPointOfView(g, currentCamera, 1);
                });
    }

    private void drawEntity(Graphics2D g, Entity e) {
        if (e.fillColor != null) {
            g.setColor(e.fillColor);
            g.fillRect((int) e.x, (int) e.y, e.w, e.h);
        }
        if (e.color != null) {
            g.setColor(e.color);
            g.drawRect((int) e.x, (int) e.y, e.w, e.h);
        }
    }

    private void drawText(Graphics2D g, Font pauseFont, String pauseText, int x, int y, Color textColor,
            Color shadowColor, int shadowDepth) {
        Font bckf = g.getFont();
        g.setFont(pauseFont);
        int offset = g.getFontMetrics().stringWidth(pauseText);
        // draw Shadow
        g.setColor(shadowColor);
        for (int d = 0; d <= shadowDepth; d += 1) {
            g.drawString(
                    pauseText,
                    (int) (x - offset * 0.5) + d,
                    (int) (y) + d);
        }
        // draw Text
        g.setColor(textColor);
        g.drawString(
                pauseText,
                (int) (x - offset * 0.5),
                y);
        g.setFont(bckf);
    }

    private void drawText(Graphics2D g, Font pauseFont, String pauseText) {
        int fontHeight = g.getFontMetrics().getHeight();
        drawText(g,
                pauseFont,
                pauseText,
                (int) (screenBuffer.getWidth() * 0.5),
                (int) ((screenBuffer.getHeight() + fontHeight) * 0.5),
                Color.WHITE,
                Color.BLACK, 2);
    }

    private void moveToCameraPointOfView(Graphics2D g, Entity cam, double i) {
        g.translate(cam.x * i, cam.y * i);
    }

    private void dispose() {
        if (frame != null) {
            frame.dispose();
        }
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

    public boolean getDebug() {
        return this.debug;
    }

    public int getDebugLevel() {
        return this.debugLevel;
    }

    public Rectangle2D getScreenResolution() {
        return screenResolution;
    }

    public Dimension getWindowSize() {
        return windowSize;
    }

    private void setExit(boolean e) {
        this.exit = e;
    }

    private boolean isExited() {
        return this.exit;
    }

    private void setPause(boolean p) {
        this.pause = p;
    }

    private boolean isPaused() {
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

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyPressed(KeyEvent e) {
        prevKeys[e.getKeyCode()] = keys[e.getKeyCode()];
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        prevKeys[e.getKeyCode()] = keys[e.getKeyCode()];
        keys[e.getKeyCode()] = false;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE -> {
                setExit(true);
            }
            case KeyEvent.VK_P, KeyEvent.VK_PAUSE -> {
                setPause(!isPaused());
            }
        }
    }

    private boolean isKeyReleased(int keyCode) {
        return prevKeys[keyCode] && !keys[keyCode];
    }

    private boolean isKeyPressed(int keyCode) {
        return !prevKeys[keyCode] && keys[keyCode];
    }
}