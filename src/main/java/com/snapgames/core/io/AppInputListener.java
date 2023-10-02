package com.snapgames.core.io;

import com.snapgames.core.App;

import java.awt.event.KeyEvent;

public class AppInputListener implements InputListener {
    private final App app;

    public AppInputListener(App app) {
        this.app = app;
    }

    public void onKeyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE -> {
                app.setExit(true);
            }
            case KeyEvent.VK_P, KeyEvent.VK_PAUSE -> {
                app.setPause(!app.isPaused());
            }
            case KeyEvent.VK_D -> {
                app.setDebugLevel(app.getDebugLevel() + 1 < 10 ? app.getDebugLevel() + 1 : 0);
                app.setDebug(app.getDebugLevel() != 0);
            }
            case KeyEvent.VK_Z -> {
                app.getSceneManager().getCurrent().reset(app);
            }
        }
    }
}
