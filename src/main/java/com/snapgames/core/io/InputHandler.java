package com.snapgames.core.io;

import com.snapgames.core.App;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    private final App app;
    // I/O attributes
    boolean[] keys = new boolean[1024];
    boolean[] prevKeys = new boolean[1024];

    public InputHandler(App app) {
        this.app = app;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Nothing to implement here now.
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
                app.setExit(true);
            }
            case KeyEvent.VK_P, KeyEvent.VK_PAUSE -> {
                app.setPause(!app.isPaused());
            }
            case KeyEvent.VK_D -> {
                app.setDebugLevel(app.getDebugLevel() + 1 < 5 ? app.getDebugLevel() + 1 : 0);
                app.setDebug(app.getDebugLevel() != 0);
            }
        }
    }

    private boolean isKeyReleased(int keyCode) {
        return prevKeys[keyCode] && !keys[keyCode];
    }

    private boolean isKeyPressed(int keyCode) {
        return !prevKeys[keyCode] && keys[keyCode];
    }

    public boolean getKeys(int keyCode) {
        return keys[keyCode];
    }

}
