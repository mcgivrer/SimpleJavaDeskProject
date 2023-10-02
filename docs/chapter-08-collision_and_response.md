## Collision and response

Basic collision handling (for now). Here, you can implement a more complex collision response, like the impulse
response, or simply push the objects out of collision.
For this illustration, let's do something simple: repel the objects.

```java
public class CollisionDetection {
    private List<GameObject> gameObjects;
    private Vector2D gravity = new Vector2D(0, -9.81); // Default gravity

    private List<PhysicZone> physicZones; // Zones with various physical properties (friction, etc.)

    public void update(double dt) {
        // Apply forces to each GameObject
        for (GameObject obj : gameObjects) {

            // If the object is dynamic, it's affected by gravity and other forces
            if (obj.getState() == GameObject.State.DYNAMIC) {
                obj.applyForce(gravity.scale(obj.getMass()));  // F = m * a

                // Apply effects from physical zones
                for (PhysicZone zone : physicZones) {
                    if (zone.isInside(obj)) {
                        // Apply friction as a force opposite to the velocity
                        Vector2D frictionForce = obj.getVelocity().scale(-1).normalize().scale(zone.getFriction());
                        obj.applyForce(frictionForce);
                        // You can also add other effects here based on the zone's properties
                    }
                }

            }

            // Move the object based on the applied forces
            obj.move(dt);
        }

        // Check for collisions and apply appropriate responses
        for (int i = 0; i < gameObjects.size(); i++) {
            for (int j = i + 1; j < gameObjects.size(); j++) {
                GameObject obj1 = gameObjects.get(i);
                GameObject obj2 = gameObjects.get(j);

                if (obj1.isColliding(obj2)) {
                    handleCollision(obj1, obj2);
                }
            }
        }
    }

    private void handleCollision(GameObject obj1, GameObject obj2) {

        Vector2D collisionNormal = obj1.getPosition().subtract(obj2.getPosition()).normalize();
        obj1.setPosition(obj1.getPosition().add(collisionNormal.scale(0.1)));
        obj2.setPosition(obj2.getPosition().subtract(collisionNormal.scale(0.1)));
    }

    // Constructor, getters, setters...
}
```
