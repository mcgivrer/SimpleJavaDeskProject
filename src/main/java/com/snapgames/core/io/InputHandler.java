package com.snapgames.core.io;

import com.snapgames.core.App;
import com.snapgames.core.service.Service;
import com.snapgames.core.utils.Configuration;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This {@link InputHandler} service provides an action manager when any key is pressed/released,
 * and is based on the Java {@link KeyListener}.
 *
 * @author Frédéric Delorme
 * @since 1.0.0
 */
public class InputHandler implements Service, KeyListener {

    private final App app;
    // I/O attributes
    boolean[] keys = new boolean[1024];
    boolean[] prevKeys = new boolean[1024];
    private boolean ctrlKeyPressed;

    Collection<InputListener> listeners = new CopyOnWriteArrayList<>();

    /**
     * Create the {@link InputHandler} service attached to the parent {@link App} instance.
     *
     * @param app
     */
    public InputHandler(App app) {
        this.app = app;
    }

    /**
     * Add a new {@link InputListener} implementation to the InputHandler manager.
     *
     * @param il the InputListener to be added to the InputHandler internal list.
     */
    public void add(InputListener il) {
        listeners.add(il);
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
        listeners.forEach(il -> il.onKeyPressed(e));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        prevKeys[e.getKeyCode()] = keys[e.getKeyCode()];
        keys[e.getKeyCode()] = false;
        ctrlKeyPressed = e.isControlDown();
        listeners.forEach(il -> il.onKeyReleased(e));
    }

    /**
     * Retrieve the current state of a key according to its internal {@link KeyEvent} code.
     *
     * @param keyCode the code of the key to check status.
     * @return true if pressed, else false.
     */
    public boolean getKeys(int keyCode) {
        return keys[keyCode];
    }

    /**
     * Return status of the CTRL key.
     *
     * @return true if pressed, else false.
     */
    public boolean isControlKeyPressed() {
        return this.ctrlKeyPressed;
    }
}
