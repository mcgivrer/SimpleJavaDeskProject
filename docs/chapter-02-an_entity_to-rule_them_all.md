## An Entity to rule them all

The way to manage all the objects to be displayed on screen is to define a common model; the `Entity`.

```java
public class Entity {
    public Vector2D position;
    public Vector2D velocity;
    public Vector2D acceleration;
    public Vector2D size;


}
```

This is the basic of our game engine. But we need more than just an entity, if we look at our game structure, we can
notice
that there are some hierarchy to create.

<figure>
    <img src="http://www.plantuml.com/plantuml/png/VOoXhiCm34Mvly8llE3HWga3OsFxWgon54jE5iKEwD-lQGghaUjxTOwygFbgBGdyMQH4MCHpXiY1C975Jz0nl5dbTpDIMCMhuhsKixrModJsHpQs3NQ8xj_XT1jqrfuAn-XasedB3fz-epfBpmiwlitK3vF2OVyInhumriWIOA4SMf8l" alt="the hierarchical view" width="350"/>
    <figCaption>The Hierarchical view from App to Entity</figCaption>
</figure>

The `Scene` is parent of `Entity`, while a layer is parent of an `Entity`, and `Entity` is also its own parent.
there is clearly a tree pattern:

```text
Scene
|_ Layer
   |_ Entity
      |_ Entity
```

All those object has a Parent (but the first), and one or more children.

We can create a common object that implement such tree hierarchy: the `Node`

### The Node class

The Node object can have the following implementation:

```java
class Node<T> {
    private static int index = 0;
    int id = index++;
    public boolean active;
    private String name = "node_" + id;
    public int priority;
    private Node<T> parent;
    private List<Node<T>> children = new CopyOnWriteArrayList<>();
}
```

where :

- _index_ is an internal counter starting from 0,
- _id_ is the unique identifier for this `Node`,
- _active_ is the flag corresponding to the active status of that `Node`,
- _name_ is the internal name for this `Node`,
- _parent_ is the parent `Node` or null if this is the root node,
- _children_ is a list of child `Node`s.
- _priority_ is the priority of that `Node` in its parent list.

We won't go through all the method, there are self-explanatory.

### Entity as a Node

So, our `Entity` will inherit from the node :

```java
public class Entity extends Node<Entity> {
    // geometrical attributes
    private Vector2D position;
    private Vector2D size;

    // Physic attributes
    private Vector2D velocity;
    private Vector2D acceleration;
    private List<Vector2D> forces = new ArrayList<>();
    public Material material;
    public double mass = 1.0;

    // collision attributes
    // the bounding box for this entity
    private Shape bBox = new Rectangle2D.Double();
    // the flag for contact detection status
    private boolean contact;

    // Drawing attributes
    private EntityType type = EntityType.RECTANGLE;
    public Color color;
    public Color fillColor;
    public BufferedImage image;
    private boolean stickToCamera;

    // lifecycle attributes
    public double life;
    public long duration = -1;

    // some dynamic attributes
    private Map<String, Object> attributes = new ConcurrentHashMap<>();

    // Update mechanism to be applied to this object
    private List<Behavior> behaviors = new ArrayList<>();

}
```

