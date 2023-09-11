package com.snapgames.demo.test001.io;

import com.snapgames.core.io.InputListener;
import com.snapgames.core.scene.Scene;
import com.snapgames.demo.test001.scenes.DemoScene;

import java.awt.event.KeyEvent;

public class DemoInputListener implements InputListener {

    private Scene scene;

    public DemoInputListener(Scene scene){
        this.scene = scene;
    }
    @Override
    public void onKeyReleased(KeyEvent e) {
        DemoScene dsc = (DemoScene)scene;
        switch(e.getKeyCode()){
            case KeyEvent.VK_PAGE_UP -> {
                dsc.addEnemies("enemy", 10, 5.0);
            }
            case KeyEvent.VK_DELETE ->{
                dsc.removeAllEntity( "enemy");

            }
        }
    }
}
