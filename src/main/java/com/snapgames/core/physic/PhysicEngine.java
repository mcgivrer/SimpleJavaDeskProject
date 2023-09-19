package com.snapgames.core.physic;

import com.snapgames.core.App;
import com.snapgames.core.behavior.Behavior;
import com.snapgames.core.entity.Camera;
import com.snapgames.core.entity.Entity;
import com.snapgames.core.scene.Scene;
import com.snapgames.core.service.Service;
import com.snapgames.core.service.ServiceManager;
import com.snapgames.core.utils.Configuration;

import java.awt.geom.Rectangle2D;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

public class PhysicEngine implements Service {
    public World world;

    private final App app;
    private int collisionNb;
    private int detectionNb;

    public PhysicEngine(App app) {
        this.app = app;
        this.world = new World(new Vector2D(0.0, -0.981), new Rectangle2D.Double(0, 0, 600, 600));
    }

    public void update(App app, Scene scene, double elapsed, Map<String, Object> stats) {
        double time = elapsed * 0.025;
        collisionNb = 0;
        detectionNb = 0;
        if (Optional.ofNullable(scene.getWorld()).isPresent()) {
            this.world = scene.getWorld();
        }

        if (app.isDebugLevelMin(9)) {
            System.out.printf("=> Start Update --- %n = Elapsed: %fs%n", time);
        }
        scene.getEntities().stream()
                .filter(Entity::isActive)
                .sorted(Comparator.comparingInt(e -> ((Entity) e).getPriority()).reversed())
                .forEach(e -> {
                    if (!(e instanceof Camera) && !e.isStickToCamera()) {
                        applyWorldConstrains(e, time);
                        updateEntity(scene, e, time);
                        detectCollision(scene, e);
                        constrainsEntityToPlayArea(e);
                    }
                    e.update(elapsed);
                    if (app.isDebugLevelMin(9) && app.isDebugFiltered(e.getName())) {
                        System.out.printf("|  -> entity: %s%n", e.toString());
                    }
                });
        scene.update(app, time, stats);
        if (app.isDebugLevelMin(9)) {
            System.out.println("=> End Update ---");
        }
        stats.put("6_col", collisionNb);
        stats.put("6_detect", detectionNb);
    }

    private void detectCollision(Scene scene, Entity obj1) {
        SpacePartition sp = ServiceManager.get().find(SpacePartition.class);
        // TODO The next step is to partition space to reduce the number of collision comparisons. (Space Partitioning)

        obj1.setContact(false);
        for (Entity obj2 : sp.find(obj1)) {
            detectionNb++;
            if (!obj1.isStickToCamera() && !obj2.isStickToCamera() && obj1.getId() != obj2.getId() && obj1.getBBox().getBounds2D().intersects(obj2.getBBox().getBounds2D())) {
                if (obj1.getPosition().notEquals(0.0, 0.0) && obj1.getPosition().notEquals(0.0, 0.0)) {
                    Vector2D centerObj1 = new Vector2D(obj1.getBBox().getBounds2D().getCenterX(), obj1.getBBox().getBounds2D().getCenterY());
                    Vector2D centerObj2 = new Vector2D(obj2.getBBox().getBounds2D().getCenterX(), obj2.getBBox().getBounds2D().getCenterY());

                    Vector2D collisionNormal = centerObj1.subtract(centerObj2).normalize();

                    obj1.setPosition(obj1.getPosition().add(collisionNormal.multiply(0.1)));
                    obj2.setPosition(obj2.getPosition().subtract(collisionNormal.multiply(-0.1)));

                    double elasticityObj1 = obj1.getMaterial() != null ? obj1.getMaterial().elasticity : 1.0;
                    double elasticityObj2 = obj2.getMaterial() != null ? obj2.getMaterial().elasticity : 1.0;

                    obj1.addForce(obj2.getVelocity().multiply(0.5 * obj1.mass * elasticityObj1));
                    obj2.addForce(obj1.getVelocity().multiply(0.5 * obj2.mass * elasticityObj2));

                    obj1.setContact(true);
                    obj2.setContact(true);

                    collisionNb++;

                    // apply specific response 'CollisionResponseBehavior' if exists on concerned the Entity's
                    obj1.getBehaviors().stream()
                            .filter(b -> b instanceof CollisionResponseBehavior)
                            .forEach(b ->
                                    ((CollisionResponseBehavior) b)
                                            .response(obj1, obj2, collisionNormal));
                    obj2.getBehaviors().stream()
                            .filter(b -> b instanceof CollisionResponseBehavior)
                            .forEach(b ->
                                    ((CollisionResponseBehavior) b)
                                            .response(obj2, obj1, collisionNormal.negate()));
                }
            }
        }
    }

    private void updateEntity(Scene scene, Entity entity, double elapsed) {
        // apply gravity
        entity.getBehaviors().stream().forEach(b -> ((Behavior) b).update(scene, entity, elapsed));
        applyWorldConstrains(entity, elapsed);
        // compute acceleration
        entity.setAcceleration(entity.getAcceleration().addAll(entity.getForces()));
        entity.setAcceleration(entity.getAcceleration().multiply(
                        (entity.getMaterial() != null ? entity.getMaterial().density : 1.0) * entity.mass)
                .maximize(4.0)
                .thresholdToZero(0.001));
        // compute velocity
        double roughness = (entity.getMaterial() != null) ? entity.getMaterial().roughness : 1.0;
        entity.setVelocity(
                entity.getVelocity().add(
                                entity.getAcceleration()
                                        .multiply(elapsed * elapsed * 0.5))
                        .maximize(30.0)
                        .thresholdToZero(0.01));
        Vector2D friction = entity.getVelocity().multiply(-roughness);
        entity.setVelocity(entity.getVelocity().add(friction));
        // compute position
        entity.setPosition(entity.getPosition().add(entity.getVelocity().multiply(elapsed)));

        entity.getForces().clear();
        entity.updateBBox();

    }

    private void applyWorldConstrains(Entity e, double elapsed) {
        e.addForce(world.getGravity().negate());
    }

    private void constrainsEntityToPlayArea(Entity e) {
        Rectangle2D playArea = world.getPlayArea();
        if (Optional.ofNullable(playArea).isPresent() && !playArea.getBounds2D().contains(e.getBBox().getBounds2D())) {
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
