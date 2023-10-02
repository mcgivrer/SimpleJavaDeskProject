## Mapping action

As for now, in terms of controls, we just detect some keys pressed or released event and then du direction action.

But if we think multiplatform for our code, it would be easier to get some way to map some internal application action
to external input events.

So defining a real mapping between keys, mouse or controller events, mapped to some virtual actions, and using those
actions into our code, will let us adapt the code to any other platform.

Let's bring this level of abstraction to our `InputHandler`, and rename it as `ActionHandler`.

### An Action

If we dive into our real needs, a platform framework, we only need a minimal number of actions, according to what
already exists in the standard
game console controllers:

| Action              | Description       |
|---------------------|-------------------|
| HOME                | Display Home      |
| START               | Start action      |
| Capture             | Require a capture |
| OPTIONS             | Display Options   |
| UP                  | move up           |
| DOWN                | move down         |
| LEFT                | move left         |
| RIGHT               | move right        |
| Button X            | a 1st action      |
| Button Y            | a 2nd action      |
| Button A            | a 3rd action      |
| Button B            | a 4th action      |
| Trigger UPPER LEFT  | a 5th action      |
| Trigger LOWER LEFT  | a 5th action      |
| Trigger UPPER RIGHT | a 5th action      |
| Trigger LOWER LEFT  | a 5th action      |


So we can start by mapping keyboard events to those identified actions.

```java
public enum ACTIONS{
    HOME,
    START,
    CAPTURE,
    OPTIONS,
    UP,DOWN,LEFT,RIGHT,
    A,B,X,Y,
    TRIG_UPPER_LEFT,
    TRIG_LOWER_LEFT,
    TRIG_UPPER_RIGHT,
    TRIG_LOWER_RIGHT;
}
```

and we may start a new ActionHandler:

```java
import com.snapgames.core.App;
import com.snapgames.core.service.Service;

import java.awt.*;

public class ActionHandler implements Service {
    private Map<ACTIONS, Event> actions = new HashMap<>();

    public ActionHandler(App app) {
        
    }
}
```