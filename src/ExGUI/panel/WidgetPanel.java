package ExGUI.panel;

import java.awt.*;
import java.util.*;
import java.util.function.Consumer;

import ExGUI.widget.Widget;

public class WidgetPanel extends Widget {

    public enum Type {
        ROOT,
        ADDITIONAL
    }

    private final Type type;
    private final MainWidgetPanel mainWidgetPanel;
    private final LayoutRegistry registry;

    public WidgetPanel(MainWidgetPanel mainPanel, Type type) {
        decorated = false;

        this.type = type;
        this.mainWidgetPanel = mainPanel;
        registry = new LayoutRegistry();

        setLayout(new BorderLayout());

        if (ExGUI.ExGUI.getMode() == ExGUI.ExGUI.Mode.DEBUG) {
            setBackground(Color.LIGHT_GRAY);

            var first = new ExGUI.widget.ExCanvas(new Dimension(mainPanel.getPanel().getWidth() / 2, mainPanel.getPanel().getHeight()), g -> {
                g.drawOval(50, 50, 100, 100);
            });
            var second = new ExGUI.widget.ExCanvas(new Dimension(mainPanel.getPanel().getWidth() / 2, mainPanel.getPanel().getHeight()), g -> {
                g.drawRect(50, 50, 100, 100);
            });
            var third = new ExGUI.widget.ExCanvas(new Dimension(100, 100), g -> {
                g.setColor(Color.BLACK);
                g.drawString("Hello", 10, 10);
            });
            third.setDecorated(false);
            var fourth = new ExGUI.widget.ExFileTree(new Dimension(100, 100));

            attachWidget(first, BorderLayout.WEST);
            attachWidget(second, BorderLayout.EAST);
            attachWidget(third, BorderLayout.SOUTH);
            attachWidget(fourth, BorderLayout.NORTH);
        }
    }

    public void attachWidget(Widget widget, String place) {
        if (registry.isEmpty()) place = BorderLayout.CENTER;

        registry.setWidget(widget, place);
        add(widget, place);
    }

    public void tick() {
        boolean registryUpdated = false;

        for (var i : registry) {
            if (i != null && i.needsToClose()) {
                registryUpdated = true;
                registry.setWidget(null, registry.getPlace(i));
                remove(i);
            }
        }

        if (registryUpdated) {
            Map<String, Widget> pattern = updateRegistry();

            registry.setWidget(pattern.get(BorderLayout.CENTER), BorderLayout.CENTER);
            registry.setWidget(pattern.get(BorderLayout.NORTH), BorderLayout.NORTH);
            registry.setWidget(pattern.get(BorderLayout.SOUTH), BorderLayout.SOUTH);
            registry.setWidget(pattern.get(BorderLayout.EAST), BorderLayout.EAST);
            registry.setWidget(pattern.get(BorderLayout.WEST), BorderLayout.WEST);

            removeAll();
            if (pattern.get(BorderLayout.CENTER) != null) add(pattern.get(BorderLayout.CENTER), BorderLayout.CENTER);
            if (pattern.get(BorderLayout.NORTH) != null) add(pattern.get(BorderLayout.NORTH), BorderLayout.NORTH);
            if (pattern.get(BorderLayout.SOUTH) != null) add(pattern.get(BorderLayout.SOUTH), BorderLayout.SOUTH);
            if (pattern.get(BorderLayout.EAST) != null) add(pattern.get(BorderLayout.EAST), BorderLayout.EAST);
            if (pattern.get(BorderLayout.WEST) != null) add(pattern.get(BorderLayout.WEST), BorderLayout.WEST);

            updateUI();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        tick();

        g.setColor(Color.BLACK);

        for (var i : registry) {
            if (i != null) {
                g.drawLine(i.getX(), i.getY(), i.getX() + i.getWidth(), i.getY());
                g.drawLine(i.getX(), i.getY() + i.getHeight(), i.getX() + i.getWidth(), i.getY() + i.getHeight());
                g.drawLine(i.getX(), i.getY(), i.getX(), i.getY() + i.getHeight());
                g.drawLine(i.getX() + i.getWidth(), i.getY(), i.getX() + i.getWidth(), i.getY() + i.getHeight());
            }
        }
    }

    private Map<String, Widget> updateRegistry() {
        LayoutRegistry copy = null;
        try {
            copy = (LayoutRegistry) registry.clone();
        } catch (CloneNotSupportedException ignored) { }
        Map<String, Widget> pattern = new HashMap<>();

        // center
        if (copy.center == null) {
            if (copy.west == null) {
                if (copy.east == null) {
                    if (copy.south == null) {
                        pattern.put(BorderLayout.CENTER, copy.north);
                        copy.north = null;
                    } else {
                        pattern.put(BorderLayout.CENTER, copy.south);
                        copy.south = null;
                    }
                } else {
                    pattern.put(BorderLayout.CENTER, copy.east);
                    copy.east = null;
                }
            } else {
                pattern.put(BorderLayout.CENTER, copy.west);
                copy.west = null;
            }
        } else pattern.put(BorderLayout.CENTER, copy.center);

        pattern.put(BorderLayout.NORTH, copy.north);
        pattern.put(BorderLayout.SOUTH, copy.south);
        pattern.put(BorderLayout.EAST, copy.east);
        pattern.put(BorderLayout.WEST, copy.west);

        return pattern;
    }

    // setters & getters

    public Type getType() {
        return type;
    }


    private final class LayoutRegistry implements Iterable<Widget>, Cloneable {

        private final class RegistryIterator implements Iterator<Widget> {

            private final LayoutRegistry registry;
            private int pos;

            public RegistryIterator(LayoutRegistry registry) {
                this.registry = registry;
                this.pos = 0;
            }

            @Override
            public boolean hasNext() {
                return pos < 5;
            }

            @Override
            public Widget next() {
                if (hasNext()) return registry.getWidget(getPlaceByPos(pos++));
                return null;
            }

            private String getPlaceByPos(int pos) {
                switch(pos) {
                    case 0: return BorderLayout.CENTER;
                    case 1: return BorderLayout.NORTH;
                    case 2: return BorderLayout.SOUTH;
                    case 3: return BorderLayout.EAST;
                    case 4: return BorderLayout.WEST;
                    default: return null;
                }
            }

        }

        private Widget center, north, south, east, west;
        private RegistryIterator innerIterator;

        public LayoutRegistry() {
            center = null;
            north = null;
            south = null;
            east = null;
            west = null;
        }

        public boolean isEmpty() {
            return center == null && north == null && south == null && east == null && west == null;
        }

        public void setWidget(Widget widget, String place) {
            if (place.equals(BorderLayout.CENTER)) center = widget;
            else if (place.equals(BorderLayout.NORTH)) north = widget;
            else if (place.equals(BorderLayout.SOUTH)) south = widget;
            else if (place.equals(BorderLayout.EAST)) east = widget;
            else if (place.equals(BorderLayout.WEST)) west = widget;
        }

        public Widget getWidget(String place) {
            if (place.equals(BorderLayout.CENTER)) return center;
            else if (place.equals(BorderLayout.NORTH)) return north;
            else if (place.equals(BorderLayout.SOUTH)) return south;
            else if (place.equals(BorderLayout.EAST)) return east;
            else if (place.equals(BorderLayout.WEST)) return west;
            else return null;
        }

        public String getPlace(Widget widget) {
            if (widget == center) return BorderLayout.CENTER;
            else if (widget == north) return BorderLayout.NORTH;
            else if (widget == south) return BorderLayout.SOUTH;
            else if (widget == east) return BorderLayout.EAST;
            else if (widget == west) return BorderLayout.WEST;
            else return null;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            LayoutRegistry newReg = new LayoutRegistry();
            newReg.center = center;
            newReg.north = north;
            newReg.south = south;
            newReg.east = east;
            newReg.west = west;

            return newReg;
        }

        @Override
        public Iterator<Widget> iterator() {
            return new RegistryIterator(this);
        }

        @Override
        public void forEach(Consumer<? super Widget> action) {
            if (innerIterator == null) innerIterator = new RegistryIterator(this);
            if (innerIterator.hasNext()) action.accept(innerIterator.next());
        }

        @Override
        public Spliterator<Widget> spliterator() {
            return Iterable.super.spliterator();
        }

    }

}
