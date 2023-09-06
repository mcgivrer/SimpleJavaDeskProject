package com.snapgames.core.loop;

import com.snapgames.core.App;
import com.snapgames.core.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class StandardGameLoop implements GameLoop {

    @Override
    public void loop(App app) {
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
        while (!app.isExit() && !app.isTestMode()) {
            current = System.currentTimeMillis();
            elapsed = System.currentTimeMillis() - previous;
            app.input();
            if (!app.isPaused()) {
                app.update(elapsed, stats);
                internalTime += elapsed;
                updates++;
            }
            app.render(stats);
            frames++;

            cumulatedTime += elapsed;
            if (cumulatedTime > 1000) {
                fps = frames;
                ups = updates;
                updates = 0;
                frames = 0;
                cumulatedTime = 0;
            }
            double timeFrame = 1000.0 / app.getFPS();
            int wait = (int) (timeFrame - elapsed > 0 ? timeFrame - elapsed : 1);
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                System.err.println("unable to wait for some milliseconds");
            }
            previous = current;
            stats.put("1_debug", app.isDebugEnabled() ? "ON" : "off");
            stats.put("2_debugLevel", app.getDebugLevel());
            stats.put("3_ups", ups);
            stats.put("3_fps", fps);
            stats.put("3_tf", timeFrame);
            stats.put("4_time", StringUtils.formatDuration(internalTime));
        }
    }

}
