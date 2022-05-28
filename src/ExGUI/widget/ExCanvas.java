package ExGUI.widget;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class ExCanvas extends Widget {

    private Consumer<Graphics2D> render;

    public ExCanvas() {
        this(null, null);
    }

    public ExCanvas(Consumer<Graphics2D> render) {
        this(null, render);
    }

    public ExCanvas(Dimension size) {
        this(size, null);
    }

    public ExCanvas(Dimension size, Consumer<Graphics2D> render) {
        super(size);
        header = new ExGUI.misc.BasicHeader(this, "Canvas");
        this.render = render;
    }

    public void setRender(Consumer<Graphics2D> render) {
        this.render = render;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        if (render != null) render.accept(g2d);

        if (decorated) renderHeader(g2d);
    }

}
