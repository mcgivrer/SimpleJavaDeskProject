package com.snapgames.core.io.action;

import com.snapgames.core.App;
import com.snapgames.core.entity.Node;
import com.snapgames.core.physic.Vector2D;
import com.snapgames.core.service.Service;
import com.snapgames.core.utils.Configuration;
import net.java.games.input.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.function.Function;

public class ActionHandler implements Service, KeyListener, MouseListener {
    private final App app;
    private Map<Integer, ACTION> keys = new HashMap<>();
    private Map<ACTION, Function> actionsPressed = new HashMap<>();
    private Map<ACTION, Function> actionsReleased = new HashMap<>();
    private Map<ACTION, List<Node>> actionOnNode = new HashMap<>();

    private List<InputActionListener> actionListeners = new ArrayList<>();

    private boolean[] actionsEnabled = new boolean[3000];

    private Node node;
    private Vector2D cursorPosition;
    private Vector2D cursorStartPosition;
    private Vector2D cursorEndPosition;
    private Controller[] controllers;

    public ActionHandler(App app) {
        this.app = app;
    }

    @Override
    public void initialize(Configuration app) {
        // load mapping from file

        // mapping keys (native id)
        keys.put(KeyEvent.VK_LEFT, ACTION.LEFT);
        keys.put(KeyEvent.VK_RIGHT, ACTION.RIGHT);
        keys.put(KeyEvent.VK_UP, ACTION.UP);
        keys.put(KeyEvent.VK_DOWN, ACTION.DOWN);
        keys.put(KeyEvent.VK_A, ACTION.A);
        keys.put(KeyEvent.VK_B, ACTION.B);
        keys.put(KeyEvent.VK_X, ACTION.X);
        keys.put(KeyEvent.VK_Y, ACTION.Y);
        keys.put(KeyEvent.VK_ESCAPE, ACTION.HOME);
        keys.put(KeyEvent.VK_F12, ACTION.OPTIONS);
        keys.put(KeyEvent.VK_F2, ACTION.START);
        keys.put(KeyEvent.VK_F3, ACTION.CAPTURE);
        keys.put(KeyEvent.VK_1, ACTION.TRIG_UPPER_LEFT);
        keys.put(KeyEvent.VK_2, ACTION.TRIG_LOWER_LEFT);
        keys.put(KeyEvent.VK_9, ACTION.TRIG_UPPER_RIGHT);
        keys.put(KeyEvent.VK_8, ACTION.TRIG_LOWER_RIGHT);

        // mapping Mouse (id+1000)
        keys.put(MouseEvent.BUTTON1 + 1000, ACTION.A);
        keys.put(MouseEvent.BUTTON2 + 1000, ACTION.B);
        keys.put(MouseEvent.BUTTON3 + 1000, ACTION.X);

        // mapping Controller (id+2000)
        // TODO implement the controller management.
        controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();


    }

    public ActionHandler register(Node n, ACTION a, ACTION_DIRECTION ad, Function f) {
        if (!actionOnNode.containsKey(a)) {
            actionOnNode.put(a, new ArrayList<>());
        }
        actionOnNode.get(a).add(n);
        switch (ad) {
            case PRESSED -> actionsPressed.put(a, f);
            case RELEASED -> actionsReleased.put(a, f);
        }

        return this;
    }

    public void execute(ACTION a, ACTION_DIRECTION ad) {
        if (actionOnNode.containsKey(a)) {
            List<Node> nodes = actionOnNode.get(a);
            switch (ad) {
                case PRESSED -> {
                    if (!actionsPressed.isEmpty() && Optional.ofNullable(node).isPresent()) {
                        for (Node node : nodes) {
                            actionsPressed.get(a).apply(node);
                        }
                    }
                }
                case RELEASED -> {
                    if (!actionsReleased.isEmpty() && Optional.ofNullable(node).isPresent()) {
                        for (Node node : nodes) {
                            actionsReleased.get(a).apply(node);
                        }
                    }
                }
            }
        }
    }


    public void update(App a, long e) {
        readControllersStates();
    }

    private void readControllersStates() {
        for (int i = 0; i < controllers.length; i++) {
            /* Remember to poll each one */
            controllers[i].poll();

            /* Get the controllers event queue */
            EventQueue queue = controllers[i].getEventQueue();
            Event event = new Event();
            /* For each object in the queue */
            while (queue.getNextEvent(event)) {
                /* Get event component */
                Component comp = event.getComponent();
                float value = event.getValue();
                String nature = comp.getName();
                String action = comp.getIdentifier().getName();
                switch (action.strip().toLowerCase()) {
                    case "x" -> {
                        if (value > 0) {
                            actionsEnabled[ACTION.UP.ordinal()] = true;
                            execute(ACTION.UP, ACTION_DIRECTION.PRESSED);
                        } else if (value < 0) {
                            execute(ACTION.DOWN, ACTION_DIRECTION.PRESSED);
                            actionsEnabled[ACTION.DOWN.ordinal()] = true;
                        } else {
                            actionsEnabled[ACTION.UP.ordinal()] = false;
                            actionsEnabled[ACTION.DOWN.ordinal()] = false;
                        }
                    }
                    case "y" -> {
                        if (value > 0) {
                            execute(ACTION.RIGHT, ACTION_DIRECTION.PRESSED);
                            actionsEnabled[ACTION.RIGHT.ordinal()] = true;
                        } else if (value < 0) {
                            execute(ACTION.LEFT, ACTION_DIRECTION.PRESSED);
                            actionsEnabled[ACTION.LEFT.ordinal()] = true;
                        } else {
                            actionsEnabled[ACTION.LEFT.ordinal()] = false;
                            actionsEnabled[ACTION.RIGHT.ordinal()] = false;
                        }
                    }
                    case "pov" -> {
                        actionsEnabled[ACTION.LEFT.ordinal()] = false;
                        actionsEnabled[ACTION.RIGHT.ordinal()] = false;
                        if (value == 0.25f) {
                            execute(ACTION.UP, ACTION_DIRECTION.PRESSED);
                            actionsEnabled[ACTION.UP.ordinal()] = true;
                        } else if (value == 0.75f) {
                            execute(ACTION.DOWN, ACTION_DIRECTION.PRESSED);
                            actionsEnabled[ACTION.DOWN.ordinal()] = true;
                        } else if (value == 1.0f) {
                            execute(ACTION.LEFT, ACTION_DIRECTION.PRESSED);
                            actionsEnabled[ACTION.LEFT.ordinal()] = true;
                        } else if (value == 0.50f) {
                            execute(ACTION.RIGHT, ACTION_DIRECTION.PRESSED);
                            actionsEnabled[ACTION.RIGHT.ordinal()] = true;
                        }
                    }
                    default -> {
                        // nothing
                    }
                }
                if (app.isDebugLevelMin(8)) {
                    System.out.printf(">> ActionHandler : %s : action:%s = %f%n", nature, action, value);
                }
            }
        }
    }

    @Override
    public void dispose() {
        // wil release resources.
    }

    @Override
    public void keyTyped(KeyEvent e) {


    }

    @Override
    public void keyPressed(KeyEvent e) {
        ACTION action = keys.containsKey(e.getKeyCode()) ? keys.get(e.getKeyCode()) : ACTION.NONE;
        if (action != ACTION.NONE && actionsPressed.containsKey(action)) {
            execute(action, ACTION_DIRECTION.PRESSED);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        ACTION action = keys.containsKey(e.getKeyCode()) ? keys.get(e.getKeyCode()) : ACTION.NONE;
        if (action != ACTION.NONE && actionsReleased.containsKey(action)) {
            execute(action, ACTION_DIRECTION.RELEASED);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.cursorPosition = new Vector2D(e.getPoint().x, e.getPoint().y);
        ACTION action = keys.containsKey(e.getButton() + 1000) ? keys.get(e.getButton() + 1000) : ACTION.NONE;
        if (action != ACTION.NONE && actionsPressed.containsKey(action)) {
            execute(action, ACTION_DIRECTION.PRESSED);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.cursorPosition = new Vector2D(e.getPoint().x, e.getPoint().y);
        ACTION action = keys.containsKey(e.getButton() + 1000) ? keys.get(e.getButton() + 1000) : ACTION.NONE;
        if (action != ACTION.NONE && actionsReleased.containsKey(action)) {
            execute(action, ACTION_DIRECTION.RELEASED);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.cursorStartPosition = new Vector2D(e.getPoint().x, e.getPoint().y);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.cursorEndPosition = new Vector2D(e.getPoint().x, e.getPoint().y);
    }
}
