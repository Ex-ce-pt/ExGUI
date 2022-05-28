package ExGUI.misc;

import java.awt.Dimension;

import ExGUI.widget.Widget;

public abstract class WindowHeader {

    protected String title;
    protected Widget widget;
    protected Dimension windowSize;

    public WindowHeader(Widget widget, String title) {
        this.widget = widget;
        this.title = title;
    }

    public abstract void render(java.awt.Graphics2D g);

}
