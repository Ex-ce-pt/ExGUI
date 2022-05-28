package ExGUI.lib.ExEvent;

import java.util.Set;
import java.util.HashSet;
import java.awt.Container;
import java.awt.event.*;
import java.time.LocalTime;
import java.time.Duration;

public final class EventSystem implements KeyListener, MouseListener, MouseMotionListener {

    private static final EventSystem SINGLETON = new EventSystem();

    private EventSystem() {}

    private Object mutex = this;
    private final Set<Integer> pressedKeys = new HashSet<>();
    private byte mouseButtons = 0b0000000;
    private MouseEvent movementPoint = null;
    private MouseEvent dragPoint = null;

    private final MouseMovementController mouseMovementController = new MouseMovementController(this);

    public void bind(Container container) {
        if (container == null) return;
        container.addKeyListener(this);
        container.addMouseListener(this);
        container.addMouseMotionListener(this);
    }

    public void unbind(Container container) {
        if (container == null) return;
        container.removeKeyListener(this);
        container.removeMouseListener(this);
        container.removeMouseMotionListener(this);
    }

    public boolean isKeyPressed(int code) {
        synchronized (mutex) {
            return pressedKeys.contains(code);
        }
    }

    public boolean isMouseButtonPressed(int code) {
        synchronized (mutex) {
            return ((mouseButtons >> (code - 1)) & 1) == 1;
        }
    }

    public MouseEvent getMovementPoint() {
        return movementPoint;
    }

    public MouseEvent getDragPoint() {
        return dragPoint;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        synchronized (mutex) {
            pressedKeys.add(e.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        synchronized (mutex) {
            pressedKeys.remove(e.getKeyCode());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        synchronized (mutex) {
            mouseButtons |= (1 << (e.getButton() - 1));
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        synchronized (mutex) {
            if (isMouseButtonPressed(e.getButton()))
                mouseButtons ^= (1 << (e.getButton() - 1));

            dragPoint = null;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        synchronized (mutex) {
            dragPoint = e;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        synchronized (mutex) {
            movementPoint = e;
            mouseMovementController.prevMovementTime = LocalTime.now();
        }
    }

    public void setMutex(Object mutex) {
        if (mutex != null) this.mutex = mutex;
    }

    public static EventSystem getEventSystem() { return SINGLETON; }



    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    private static final class MouseMovementController {

        private LocalTime prevMovementTime;
        private final EventSystem eventSystem;

        public MouseMovementController(EventSystem eventSystem) {
            this.eventSystem = eventSystem;
            this.prevMovementTime = LocalTime.now();
            new Thread(() -> {
                while(eventSystem != null) {
                    try {
                        synchronized (eventSystem) {
                            if (Duration.between(prevMovementTime, LocalTime.now()).toMillis() >= 13) {
                                eventSystem.movementPoint = null;
                            }

                        }
                        Thread.sleep(13);
                    } catch(InterruptedException ignored) {}
                }
            }).start();
        }

    }

}
