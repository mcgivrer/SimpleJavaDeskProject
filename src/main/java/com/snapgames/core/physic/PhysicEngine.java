package com.snapgames.core.physic;

import com.snapgames.core.App;
import com.snapgames.core.entity.Camera;
import com.snapgames.core.entity.Entity;
import com.snapgames.core.scene.Scene;
import com.snapgames.core.service.Service;
import com.snapgames.core.utils.Configuration;

import java.awt.geom.Rectangle2D;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

public class PhysicEngine implements Service {
    public World world;

    private final App app;

    public PhysicEngine(App app) {
        this.app = app;
        this.world = new World(-0.981, new Rectangle2D.Double(0, 0, 600, 600));
    }

    public void update(App app, Scene scene, double elapsed, Map<String, Object> stats) {
        scene.getEntities().stream()
                .filter(Entity::isActive)
                .sorted(Comparator.comparingInt(Entity::getPriority).reversed())
                .forEach(e -> {
                    e.update(elapsed);
                    applyWorldConstrains(e, elapsed);
                    if (!(e instanceof Camera || e.isStickToCamera()))
                        constrainsEntityToPlayArea(e);
                    if (e.material != null) {
                        e.dx = e.dx * e.material.roughness;
                        e.dy = e.dy * e.material.roughness;
                    }
                });
        scene.update(app, elapsed, stats);
    }

    private void applyWorldConstrains(Entity e, double elapsed) {
        e.dy += -world.getGravity() * elapsed * 0.005 * e.mass;

    }

    private void constrainsEntityToPlayArea(Entity e) {
        Rectangle2D playArea = world.getPlayArea();
        if (Optional.ofNullable(playArea).isPresent() && !playArea.contains(e.getBBox())) {
            if (e.x < playArea.getMinX() || e.x + e.w > playArea.getMaxX()) {
                if (e.x < playArea.getMinX())
                    e.x = playArea.getMinX();
                if (e.x + e.w > playArea.getMaxX())
                    e.x = playArea.getMaxX() - e.w;

                e.dx = -e.dx * e.material.elasticity;
            }
            if (e.y < playArea.getMinY() || e.y + e.h > playArea.getMaxY()) {
                if (e.y < playArea.getMinY())
                    e.y = playArea.getMinY();
                if (e.y + e.h > playArea.getMaxY())
                    e.y = playArea.getMaxY() - e.h;

                e.dy = -e.dy * e.material.elasticity;
            }
        }
    }


    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void initialize(Configuration configuration) {
        world.setPlayArea(configuration.playArea);
    }


    @Override
    public void dispose() {

    }
}
