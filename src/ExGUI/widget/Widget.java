package ExGUI.widget;

import javax.swing.*;
import java.awt.*;

import ExGUI.icons.IconStore;
import ExGUI.misc.WindowHeader;
import ExGUI.lib.ExEvent.EventSystem;

public abstract class Widget extends JPanel {

    protected boolean decorated;
    protected boolean closing;

    protected WindowHeader header;

    public Widget(Dimension size) {
        this();
        setPreferredSize(size);
    }

    public Widget() {
        decorated = true;
        closing = false;
        header = null;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (decorated && EventSystem.getEventSystem().isMouseButtonPressed(java.awt.event.MouseEvent.BUTTON1)) {
            Point p = MouseInfo.getPointerInfo().getLocation();
            javax.swing.SwingUtilities.convertPointFromScreen(p, this);
            if ((p.x >= (getWidth() - IconStore.ICON_SIZE) && p.x <= getWidth()) &&
                (p.y >= 0 && p.y <= IconStore.ICON_SIZE)) {
                closing = true;
            }
        }
    }

    protected void renderHeader(java.awt.Graphics2D g) {
        if (header != null) header.render(g);
    }

    public void setDecorated(boolean decorated) { this.decorated = decorated; }

    public boolean isDecorated() { return decorated; }

    public boolean needsToClose() { return closing; }

}
