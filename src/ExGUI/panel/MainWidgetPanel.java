package ExGUI.panel;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.util.List;

import ExGUI.ExGUI;

public class MainWidgetPanel implements ComponentListener {

    private JPanel panel;
    private Dimension size;
    private final Thread mainloop;

    private final LayoutManager lastLayout;

    private final List<WidgetPanel> widgetPanels;

    public MainWidgetPanel(JPanel panel, Dimension size) {
        attachPanel(panel, size);
        widgetPanels = new ArrayList<>();
        lastLayout = panel.getLayout();
        panel.setLayout(new BorderLayout());
        mainloop = new Thread(() -> {
            try {
                while(true) {
                    if (this.panel != null) panel.repaint();
                    Thread.sleep(50);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
        mainloop.start();

        if (ExGUI.getMode() == ExGUI.Mode.DEBUG) {
            widgetPanels.add(new WidgetPanel(this, WidgetPanel.Type.ROOT));
            panel.add(widgetPanels.get(0), BorderLayout.CENTER);
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        size = panel.getSize();
    }

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}

    // setters & getters

    public void attachPanel(JPanel panel, Dimension size) {
        disposePanel();

        this.panel = panel;
        Dimension d = this.panel.getSize();
        if (d.width == 0 && d.height == 0) this.size = size;

        if (panel != null) {
            /*
            this.panel.addKeyListener(this);
            this.panel.addMouseListener(this);
            this.panel.addMouseMotionListener(this);
            this.panel.addMouseWheelListener(this);
            this.panel.addFocusListener(this);
            */
            this.panel.addComponentListener(this);
        }

    }

    public void disposePanel() {
        if (panel != null) {
            /*
            panel.removeKeyListener(this);
            panel.removeMouseListener(this);
            panel.removeMouseMotionListener(this);
            panel.removeMouseWheelListener(this);
            panel.removeFocusListener(this);
             */
            panel.removeComponentListener(this);

            panel.setLayout(lastLayout);
            panel.removeAll();
        }
        panel = null;
        size = null;
    }

    public JPanel getPanel() {
        return panel;
    }

}
