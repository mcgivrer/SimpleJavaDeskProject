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
        double time = elapsed * 0.025;

        if (app.isDebugLevelMin(9)) {
            System.out.printf("=> Start Update --- %n = Elapsed: %fs%n", time);
        }
        scene.getEntities().stream()
                .filter(Entity::isActive)
                .sorted(Comparator.comparingInt(Entity::getPriority).reversed())
                .forEach(e -> {
                    if (!(e instanceof Camera) && !e.isStickToCamera()) {
                        applyWorldConstrains(e, time);
                        updateEntity(e, time);
                        constrainsEntityToPlayArea(e);
                    }
                    e.update(elapsed);
                    if (app.isDebugLevelMin(9) && app.isDebugFiltered(e.getName())) {
                        System.out.printf("|  -> entity: %s%n", e.toString());
                    }

                });
        scene.update(app, time, stats);
        if (app.isDebugLevelMin(4)) {
            System.out.println("|_ End Update ---");
        }
    }

    private void updateEntity(Entity entity, double elapsed) {
        // apply gravity
        applyWorldConstrains(entity, elapsed);
        // compute acceleration
        entity.setAcceleration(entity.getAcceleration().addAll(entity.getForces()));
        entity.setAcceleration(entity.getAcceleration().multiply(
                (entity.getMaterial() != null ? entity.getMaterial().density : 1.0) * entity.mass));
        // compute velocity
        double roughness = (entity.getMaterial() != null) ? entity.getMaterial().roughness : 1.0;
        entity.setVelocity(entity.getVelocity().add(entity.getAcceleration().multiply(elapsed * elapsed * 0.5)).multiply(roughness));

        // compute position
        entity.setPosition(entity.getPosition().add(entity.getVelocity().multiply(elapsed)));
        entity.getForces().clear();
        entity.updateBBox();

    }

    private void applyWorldConstrains(Entity e, double elapsed) {
        e.addForce(new Vector2D(0, -world.getGravity()));
    }

    private void constrainsEntityToPlayArea(Entity e) {
        Rectangle2D playArea = world.getPlayArea();
        if (Optional.ofNullable(playArea).isPresent() && !playArea.contains(e.getBBox())) {
            if (e.getPosition().x < playArea.getMinX() || e.getPosition().x + (int) e.getSize().x > playArea.getMaxX()) {
                if (e.getPosition().x < playArea.getMinX())
                    e.getPosition().x = playArea.getMinX();
                if (e.getPosition().x + (int) e.getSize().x > playArea.getMaxX())
                    e.getPosition().x = playArea.getMaxX() - (int) e.getSize().x;

                e.getVelocity().x = -e.getVelocity().x * e.material.elasticity;
            }
            if (e.getPosition().y < playArea.getMinY() || e.getPosition().y + (int) e.getSize().y > playArea.getMaxY()) {
                if (e.getPosition().y < playArea.getMinY())
                    e.getPosition().y = playArea.getMinY();
                if (e.getPosition().y + (int) e.getSize().y > playArea.getMaxY())
                    e.getPosition().y = playArea.getMaxY() - (int) e.getSize().y;

                e.getVelocity().y = -e.getVelocity().y * e.material.elasticity;
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
