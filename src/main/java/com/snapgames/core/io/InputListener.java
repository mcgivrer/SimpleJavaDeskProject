package com.snapgames.core.io;

import java.awt.event.KeyEvent;

public interface InputListener {
    default void onKeyPressed(KeyEvent e) {
    }

    default void onKeyReleased(KeyEvent e) {
    }
}
