package com.snapgames.core.io;

import com.snapgames.core.App;
import com.snapgames.core.service.Service;
import com.snapgames.core.utils.Configuration;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements Service, KeyListener {

    private final App app;
    // I/O attributes
    boolean[] keys = new boolean[1024];
    boolean[] prevKeys = new boolean[1024];
    private boolean ctrlKeyPressed;

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
        ctrlKeyPressed = e.isControlDown();
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        prevKeys[e.getKeyCode()] = keys[e.getKeyCode()];
        keys[e.getKeyCode()] = false;
        ctrlKeyPressed = e.isControlDown();
        app.processOnKeyReleased(e);
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

    @Override
    public void initialize(Configuration app) {
        // nothing to initialize (maybe later a Key mapping)
    }

    @Override
    public void dispose() {
        // nothing to release.
    }

    public boolean isControlKeyPressed() {
        return this.ctrlKeyPressed;
    }
}
