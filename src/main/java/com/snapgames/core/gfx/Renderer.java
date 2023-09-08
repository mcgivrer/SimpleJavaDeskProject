package com.snapgames.core.gfx;

import com.snapgames.core.App;
import com.snapgames.core.entity.Camera;
import com.snapgames.core.entity.Entity;
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
import java.util.Map;
import java.util.Optional;

public class Renderer extends JPanel implements Service {

    private final App app;

    // Graphics Properties
    JFrame frame;
    public BufferedImage screenBuffer;
    private Rectangle2D screenResolution;
    private Dimension windowSize;
    private Graphics2D gr;

    public Renderer(App app) {
        this.app = app;
    }


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

    public void draw(App app, Scene scene, Map<String, Object> stats) {
        SceneManager sceneManager = app.getSceneManager();

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

        // render scene specific things
        sceneManager.getCurrent().render(app, g, this, stats);

        g.dispose();

        // copy rendering buffer to Window.
        copyBufferToWindow(stats);

    }

    private void copyBufferToWindow(Map<String, Object> stats) {
        Graphics2D g2 = (Graphics2D) frame.getBufferStrategy().getDrawGraphics();
        this.gr = g2;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2.setColor(Color.ORANGE);
        g2.setFont(getFont().deriveFont(11.0f));
        g2.drawImage(screenBuffer,
                0, frame.getInsets().top, frame.getWidth(), frame.getHeight(),
                0, 0, screenBuffer.getWidth(), screenBuffer.getHeight(),
                null);
        if (app.getDebug()) {
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

        long count = scene.getEntities().size();
        long rendererObj = scene.getEntities().stream()
                .filter(Entity::isActive)
                .filter(e -> !(e instanceof Camera))
                .count();
        stats.put("5_objCount", count);
        stats.put("6_objRendered", rendererObj);

        Camera currentCamera = sceneManager.getCurrent().getCurrentCamera();

        scene.getEntities().stream()
                .filter(Entity::isActive)
                .filter(e -> !(e instanceof Camera))
                .sorted(Comparator.comparingInt(Entity::getPriority))
                .forEach(e -> {
                    moveToCameraPointOfView(g, currentCamera, -1);
                    drawEntity(g, scene, e);
                    moveToCameraPointOfView(g, currentCamera, 1);
                });
    }

    private void drawEntity(Graphics2D g, Scene scene, Entity e) {
        switch (e.getType()) {
            case DOT, RECTANGLE -> {
                if (e.fillColor != null) {
                    g.setColor(e.fillColor);
                    g.fillRect((int) e.getPosition().x, (int) e.getPosition().y, (int) e.getSize().x, (int) e.getSize().y);
                }
                if (e.color != null) {
                    g.setColor(e.color);
                    g.drawRect((int) e.getPosition().x, (int) e.getPosition().y, (int) e.getSize().x, (int) e.getSize().y);
                }
            }
            case ELLIPSE -> {
                if (e.fillColor != null) {
                    g.setColor(e.fillColor);
                    g.fillArc((int) e.getPosition().x, (int) e.getPosition().y, (int) e.getSize().x, (int) e.getSize().y, 0, 360);
                }
                if (e.color != null) {
                    g.setColor(e.color);
                    g.drawArc((int) e.getPosition().x, (int) e.getPosition().y, (int) e.getSize().x, (int) e.getSize().y, 0, 360);
                }
            }
            case LINE -> {
                if (e.color != null) {
                    g.setColor(e.color);
                    g.drawLine((int) e.getPosition().x, (int) e.getPosition().y, (int) e.getSize().x, (int) e.getSize().y);
                }
            }
        }
        e.getBehaviors().stream().forEach(b -> b.draw(this, g, scene, e));
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

    private void moveToCameraPointOfView(Graphics2D g, Entity cam, double i) {
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
        frame.dispose();
    }

    public void initialize(Configuration configuration) {
        this.windowSize = configuration.windowSize;
        this.screenResolution = configuration.bufferResolution;
    }
}
