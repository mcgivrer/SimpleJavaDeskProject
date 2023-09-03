package com.snapgames.core.physic;

import com.snapgames.core.App;
import com.snapgames.core.entity.Camera;
import com.snapgames.core.entity.Entity;
import com.snapgames.core.scene.Scene;
import com.snapgames.core.utils.Configuration;

import java.awt.geom.Rectangle2D;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

public class PhysicEngine {
    public Rectangle2D playArea;
    public World world;

    private final App app;

    public PhysicEngine(App app) {
        this.app = app;
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
                });
        scene.update(app, elapsed, stats);
    }

    private void applyWorldConstrains(Entity e, double elapsed) {
        e.dy += -world.getGravity() * elapsed * 0.005 * e.mass;

    }

    private void constrainsEntityToPlayArea(Entity e) {
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

    public void setPlayArea(Rectangle2D pa) {
        this.playArea = pa;
    }

    public Rectangle2D getPlayArea() {
        return this.playArea;
    }

    public World getWorld() {
        return world;
    }

    public PhysicEngine setWorld(World world) {
        this.world = world;
        return this;
    }

    public void initialize(Configuration configuration) {
        setPlayArea(configuration.playArea);
    }
}
