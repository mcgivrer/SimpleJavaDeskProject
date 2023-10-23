package com.snapgames.demo.test001;

import com.snapgames.core.App;
import com.snapgames.demo.test001.scenes.DemoScene;

public class DemoApp001 extends App {
    @Override
    public void createScene() {
        DemoScene scene = new DemoScene(this);
        getSceneManager().add(scene);
    }

    public static void main(String[] args) {
        DemoApp001 app = new DemoApp001();
        app.run(args);
    }
}
