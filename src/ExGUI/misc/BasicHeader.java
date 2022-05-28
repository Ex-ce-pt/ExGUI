package ExGUI.misc;

import java.awt.*;

import ExGUI.ExGUI;
import ExGUI.widget.Widget;
import ExGUI.icons.IconStore;
import ExGUI.lib.ExEvent.EventSystem;

public class BasicHeader extends WindowHeader {

    private boolean cursorOverCross;

    public BasicHeader(Widget widget) {
        this(widget, "");
    }

    public BasicHeader(Widget widget, String title) {
        super(widget, title);
        cursorOverCross = false;
    }

    @Override
    public void render(Graphics2D g) {
        windowSize = widget.getSize();
        {
            Point p = MouseInfo.getPointerInfo().getLocation();
            javax.swing.SwingUtilities.convertPointFromScreen(p, widget);
            cursorOverCross = (p.x >= (windowSize.getWidth() - IconStore.ICON_SIZE) && p.x <= windowSize.getWidth()) &&
                    (p.y >= 0 && p.y <= IconStore.ICON_SIZE);
        }

        if (ExGUI.getTheme() == ExGUI.Theme.LIGHT) {

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, (int) windowSize.getWidth(), 20);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Consolas", Font.PLAIN, 12));
            g.drawLine(0, 20, (int) windowSize.getWidth(), 20);
            g.drawString(title, 10, 15);
            if (cursorOverCross) {
                g.setColor(Color.lightGray);
                g.fillRect((int) (windowSize.getWidth() - IconStore.ICON_SIZE), 0, IconStore.ICON_SIZE, IconStore.ICON_SIZE);
            }
            g.drawImage(IconStore.getIcon("cross"), (int) (windowSize.getWidth() - IconStore.ICON_SIZE), 0, null);

        } else if (ExGUI.getTheme() == ExGUI.Theme.DARK) {


        }
    }


}
