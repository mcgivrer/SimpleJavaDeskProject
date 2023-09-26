package com.snapgames.core.gfx;

import com.snapgames.core.App;
import com.snapgames.core.behavior.Behavior;
import com.snapgames.core.entity.Camera;
import com.snapgames.core.entity.Entity;
import com.snapgames.core.gfx.plugins.GameObjectRendererPlugin;
import com.snapgames.core.gfx.plugins.GaugeObjectRenderingPlugin;
import com.snapgames.core.gfx.plugins.TextObjectRendererPlugin;
import com.snapgames.core.io.InputHandler;
import com.snapgames.core.physic.PhysicEngine;
import com.snapgames.core.scene.Scene;
import com.snapgames.core.scene.SceneManager;
import com.snapgames.core.service.Service;
import com.snapgames.core.utils.Configuration;
import com.snapgames.core.utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The {@link Renderer} service implementation provides the {@link Scene}
 * draw capability to feed the game window with fancy graphics.
 * <p>
 * It provides a {@link Renderer#draw(App, Scene, Map)}
 * method to draw the current active {@link Scene} from the parent {@link App}.
 * It draws everything in a three frame screen buffer strategy (by default).
 * It also instantiates the App window (as a JFrame) to draw rendered buffer in.
 *
 * @author Frédéric Delorme
 * @since 1.0.0
 */
public class Renderer extends JPanel implements Service {

    private final App app;

    // Graphics Properties
    JFrame frame;
    public BufferedImage screenBuffer;
    private Rectangle2D screenResolution;
    private Dimension windowSize;
    private Graphics2D gr;

    private final Map<Class<? extends Entity<?>>, RendererPlugin<? extends Entity<?>>> plugins = new HashMap<>();

    /**
     * Create the {@link Renderer} services for the {@link App} instance.
     * <p>
     * It defines the basic rendering plugins for the 3 basic Entity implementations:
     * <ul>
     *     <li>{@link com.snapgames.core.entity.GameObject}: the {@link GameObjectRendererPlugin},</li>
     *     <li>{@link com.snapgames.core.entity.TextObject}: the {@link TextObjectRendererPlugin},</li>
     *     <li>and {@link com.snapgames.core.entity.GaugeObject}: the {@link GaugeObjectRenderingPlugin}.</li>
     *     </ul>
     *
     * @param app the parent {@link App} instance.
     */
    public Renderer(App app) {
        this.app = app;
        addPlugin(new GameObjectRendererPlugin());
        addPlugin(new TextObjectRendererPlugin());
        addPlugin(new GaugeObjectRenderingPlugin());
    }

    /**
     * add a new {@link RendererPlugin} implementation for a customized {@link Entity}.
     *
     * @param plugin the new {@link RendererPlugin} implementation.
     * @return the updated Renderer (fluent API).
     */
    public Renderer addPlugin(RendererPlugin<? extends Entity<?>> plugin) {
        plugins.put(plugin.getEntityClass(), plugin);
        return this;
    }


    /**
     * Create the {@link App} window and attach the {@link InputHandler} service instance to.
     *
     * @param app          the parent {@link App} instance.
     * @param inputHandler the {@link InputHandler} management service instance to be linked to Window.
     */
    public void createWindow(App app, InputHandler inputHandler) {

        frame = new JFrame(String.format("%s(%s) - %s", app.name, app.version, App.messages.getString("app.title")));
        this.setPreferredSize(windowSize);
        frame.setPreferredSize(windowSize);
        frame.setLayout(new GridLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setFocusTraversalKeysEnabled(true);
        frame.setContentPane(this);
        frame.pack();
        frame.addKeyListener(inputHandler);
        frame.setVisible(true);
        frame.createBufferStrategy(3);

        screenBuffer = new BufferedImage(screenResolution.getBounds().width, screenResolution.getBounds().height,
                BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Request to draw the {@link Scene} with all its active {@link Entity}.
     *
     * @param app   the parent {@link App} instance.
     * @param scene the {@link Scene} instance to be drawn
     * @param stats the data statistical map fed and maintained by the services.
     */
    public void draw(App app, Scene scene, Map<String, Object> stats) {

        Graphics2D g = screenBuffer.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // clear screen
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, screenBuffer.getWidth(), screenBuffer.getHeight());

        // draw play area limit
        if (app.isDebugLevelMin(1)) {
            drawPlayAreaLimits(g);
        }
        // draw all entities
        drawEntities(g, stats);

        // draw camera viewport
        if (app.isDebugLevelMin(2)) {
            drawCameraViewport(g);
        }

        // render scene-specific things
        scene.render(app, g, this, stats);

        g.dispose();

        // copy rendering buffer to Window.
        copyBufferToWindow(stats);

    }

    private void copyBufferToWindow(Map<String, Object> stats) {
        Graphics2D g2 = (Graphics2D) frame.getBufferStrategy().getDrawGraphics();
        this.gr = g2;
        //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2.drawImage(screenBuffer,
                0, frame.getInsets().top, frame.getWidth(), frame.getHeight(),
                0, 0, screenBuffer.getWidth(), screenBuffer.getHeight(),
                null);
        if (App.getDebug()) {
            g2.setColor(Color.ORANGE);
            g2.setFont(getFont().deriveFont(11.0f));
            g2.drawString(
                    StringUtils.prepareStatsString(stats,
                            "[ ", " ]", " | "),
                    4, (int) frame.getHeight() - 10);
        }
        frame.getBufferStrategy().show();
        g2.dispose();
    }

    private void drawCameraViewport(Graphics2D g) {

        SceneManager sceneManager = app.getSceneManager();
        Camera currentCamera = sceneManager.getCurrent().getCurrentCamera();
        moveToCameraPointOfView(g, currentCamera, -1);
        g.setColor(Color.BLUE);
        g.draw(currentCamera.getViewport());
        moveToCameraPointOfView(g, currentCamera, 1);
    }

    private void drawPlayAreaLimits(Graphics2D g) {

        SceneManager sceneManager = app.getSceneManager();
        PhysicEngine pe = app.getPhysicEngine();
        Rectangle2D playArea = pe.getWorld().getPlayArea();

        if (Optional.ofNullable(playArea).isPresent()) {
            Camera currentCamera = sceneManager.getCurrent().getCurrentCamera();
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

        SceneManager sceneManager = app.getSceneManager();
        Scene scene = sceneManager.getCurrent();
        Camera currentCamera = sceneManager.getCurrent().getCurrentCamera();

        long count = scene.getEntities().size();
        stats.put("5_objCount", count);

        long rendererObj = scene.getEntities().stream()
                .filter(Entity::isActive)
                .filter(e -> !(e instanceof Camera))
                .filter(currentCamera::isInViewPort)
                .count();
        stats.put("6_objRendered", rendererObj);


        scene.getEntities().stream()
                .filter(Entity::isActive)
                .filter(e -> !(e instanceof Camera))
                .filter(e -> currentCamera.isInViewPort(e) || e.isStickToCamera())
                .sorted(Comparator.comparingInt(e -> ((Entity<?>) e).getPriority()).reversed())
                .forEach(e -> {
                    if (!e.isStickToCamera()) {
                        moveToCameraPointOfView(g, currentCamera, -1);
                    }
                    drawEntity(g, scene, e);
                    if (!e.isStickToCamera()) {
                        moveToCameraPointOfView(g, currentCamera, 1);
                    }
                });
    }

    private void drawEntity(Graphics2D g, Scene scene, Entity<?> e) {
        if (plugins.containsKey(e.getClass())) {
            RendererPlugin<?> plugin = plugins.get(e.getClass());
            plugin.draw(this, g, scene, e);
            e.setRenderedBy(plugin);
        }
        e.getBehaviors().forEach(b -> ((Behavior) b).draw(this, g, scene, e));
    }

    public void drawText(Graphics2D g, Font pauseFont, String pauseText, int x, int y, Color textColor,
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

    public void drawText(Graphics2D g, Font pauseFont, String pauseText) {
        int fontHeight = g.getFontMetrics().getHeight();
        drawText(g,
                pauseFont,
                pauseText,
                (int) (screenBuffer.getWidth() * 0.5),
                (int) ((screenBuffer.getHeight() + fontHeight) * 0.5),
                Color.WHITE,
                Color.BLACK, 2);
    }

    public void moveToCameraPointOfView(Graphics2D g, Entity<?> cam, double i) {
        g.translate(cam.getPosition().x * i, cam.getPosition().y * i);
    }

    public void setWindowSize(Dimension dimension) {
        this.windowSize = dimension;
    }

    public void setScreenResolution(Rectangle2D res) {
        this.screenResolution = res;
    }

    public Graphics2D getBufferGraphics() {
        return gr;
    }

    public Rectangle2D getScreenResolution() {
        return screenResolution;
    }

    public Dimension getWindowSize() {
        return windowSize;
    }

    public void dispose() {
        if (Optional.ofNullable(frame).isPresent()) {
            frame.dispose();
        }
    }

    public void initialize(Configuration configuration) {
        this.windowSize = configuration.windowSize;
        this.screenResolution = configuration.bufferResolution;
    }

    public BufferedImage getScreenBuffer() {
        return screenBuffer;
    }
}
