## Behavior

The main complexity in an application it to resist during the development to the copy/paste easy-way-of-doing-thing.
THe right way to go is to open the implementation to re-usability. The `Behavior` pattern let you implement in a
common way a standard process according to a defined interface.

Behavior is this interface.

And then, in the concerned services, the Behavior must be processed.

### The Behavior interface

With this extension pattern, we wat to extend the capabilities of our `Entity`. We will be able to update and draw the
assigned `Entity`.

```java
public interface Behavior {
    default void update(Scene scene, Entity e, double elapsed) {

    }

    default void draw(Renderer r, Graphics2D g, Scene scene, Entity e) {
        // nothing specific to draw by default.
    }
}
```

In this Interface proposition, some default implementation with no activity, letting you implementing only the required
method on your own `Behavior` flavor.

e.G.: An EnemyTrackingBehavior implementing the Behavior interface allows an enemy Entity to track a target,
and if this target goes closer than a sensor area. AS soon as this sensor area is raised, the assigned enemy Entity
starts
following with a certain level of tracking force the target, until this one target goes away from the sensor area.

```java
public class EnemyTrackingBehavior implements Behavior {

    private final double sensorDiameter;
    private final double force;
    private boolean sensor;

    public EnemyTrackingBehavior(double sensorDiameter, double force) {
        this.sensorDiameter = sensorDiameter;
        this.force = force;
    }

    @Override
    public void update(Scene scene, Entity e, double elapsed) {
        Entity player = scene.getEntity("player");
        if (player.getPosition().add(player.getSize().multiply(0.5)).distance(e.getPosition()) < this.sensorDiameter * 0.5) {
            e.addForce(player.getPosition().substract(e.getPosition()).multiply(force));
            sensor = true;
        } else {
            sensor = false;
        }
    }
}
```

Usage :

```java
import com.snapgames.demo.test001.behaviors.EnemyTrackingBehavior;

public class DemoScene extends AbstractScene {
    //...
    public void create(App app) {
        //...
        Entity enemy1 = new Enemey("enemy").addBehavior(new EnemyTrackingBehavior(40.0, 0.15));
        addEntity(enemy1);
        //...
    }
}
```

We can also add some visual information for debugging purpose :

```java
public class EnemyTrackingBehavior implements Behavior {

    private final double sensorDiameter;
    private final double force;
    private boolean sensor;

    public EnemyTrackingBehavior(double sensorDiameter, double force) {
        this.sensorDiameter = sensorDiameter;
        this.force = force;
    }

    @Override
    public void update(Scene scene, Entity e, double elapsed) {
        //...
    }

    @Override
    public void draw(Renderer r, Graphics2D g, Scene scene, Entity e) {
        if (App.getDebug() && App.isDebugLevelAtLeast(2)) {
            if (sensor) {
                g.setColor(Color.ORANGE);
            } else {
                g.setColor(Color.GRAY);
            }
            g.drawArc(
                    (int) (e.getPosition().x - sensorDiameter * 0.5),
                    (int) (e.getPosition().y - sensorDiameter * 0.5),
                    (int) sensorDiameter,
                    (int) sensorDiameter,
                    0, 360);
        }
    }
}
```

So as soon as the sensor is triggered, the sensor area is displayed in orange, while it is drawn in gray, if the debug
activated level is at least of level 2.

<figure>
    <img src="images/capture-test-002-behavior-sensor.png" alt="The enemies are enhanced with the EnemyTrackingBehavior implementation"/>
    <figcaption>firgure 1 - The enemies are enhanced with the EnemyTrackingBehavior implementation</figcaption>
</figure>

