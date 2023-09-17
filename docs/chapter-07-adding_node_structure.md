## Adding Node structure

To get a better design of our Game system, and to prepare the implementation of the ParticleSystem,
a new hierarchical structure must be added:  the Node structure.

We want our Entity be able to be organized through a tree, and each branch of a tree can have multiple subbranches
itself.

This is where the Node concept will help a lot:

The current object organization is presented in the bellow diagram :

```text
Root (Scene)
|_ TextObject "score"
|_ TextObject "life"
|_ GaugeObject "energy"
|_ GaugeObject "mana"
|_ GameObject "player"
|_ GameObject "enemy_1"
|_ GameObject "enemy_2"
|_ GameObject "enemy_..."
|_ GameObject "enemy_n"
```

And we are going to switch a better organization

```text
Root (Scene)
|_ Layer "hud"
|  |_ TextObject "score"
|  |_ TextObject "life"
|  |_ GaugeObject "energy"
|  |_ GaugeObject "mana"
|_ Layer "objects"    
   |_ GameObject "player"
   |_ GameObject "enemy_1"
   |_ GameObject "enemy_2"
   |_ GameObject "enemy_..."
   |_ GameObject "enemy_n"
```

And each layer will be sortable on `Entity` priority.

This organization can br bring by a simple new class our all `Entity`, `Scene` and the future`Layer` will all inherit
from: `Node`

```text
Node : Scene "demoscene"
|_ Node : Layer "hud"
|  |_ Node : TextObject "score"
|  |_ Node : TextObject "life"
|  |_ Node : GaugeObject "energy"
|  |_ Node : GaugeObject "mana"
|_ Node : Layer "objects"    
   |_ Node : GameObject "player"
   |_ Node : GameObject "enemy_1"
   |_ Node : GameObject "enemy_2"
   |_ Node : GameObject "enemy_..."
   |_ Node : GameObject "enemy_n"
```

### The Node class

```java
public class Node {
    private static int index = 0;
    int id = index++;
    public boolean active;
    private String name = "node_" + id;
    private Node parent;
    private List<Node> children = new ArrayList<>();
}
```

All those `index`, `id`, `name`, and `active` attributes are coming from the Entity class.

We just add 2 new attributes :

- `parent` which will defined the parent node where this one is attached to,
- `children` will be the list of child node attached to this one.

### Enhancing classes

### The Entity

The `Entity` is simplified by removing the no more necessary attributes, because there moved to `Node`:

```java
public class Entity<T> extends Node {
    // removed index,name and id.
//...
}
```

### The Scene

We can also simplify the `Scene` by tweaking the `AbstractScene` class:

```java
public abstract class AbstractScene extends Node implements Scene {

    protected App app;
    private Camera currentCamera;

    // entities map is removed

    //...

    @Override
    public Collection<Entity> getEntities() {
        return getChild();
    }

    public Entity getEntity(String name) {
        return getChildNode(name);
    }
    //...
}

```

The getter for entities is replaced by the `Node#getChild()`, and the getter to retrieve One entity in the child is now
delegated to `Node#getChildNode(String)`

Here is the new method added to the Node :

```java
public class Node {
    //...
    public <T extends Node> Collection<T> getChild() {
        return (Collection<T>) children;
    }
    public <T extends Node> T getChildNode(String name) {
        return (T) getChild().stream().filter(e -> e.getName().equals(name)).findFirst().get();
    }
}
```

And that is totally transparent to the other services as we replace only the implementation of the `Scene` interface does not change.

That's all folk !