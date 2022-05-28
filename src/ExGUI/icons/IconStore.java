package ExGUI.icons;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

public final class IconStore {

    public static final int ICON_SIZE = 20;

    private static Map<String, BufferedImage> icons;

    private IconStore() {}

    public static void loadIcons() {
        icons = new HashMap<>();

        { // CROSS
            var img = new BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_ARGB);
            Graphics g = img.getGraphics();
            g.setColor(Color.BLACK);
            g.drawLine((int) (ICON_SIZE * 0.25), (int) (ICON_SIZE * 0.25), (int) (ICON_SIZE * 0.75), (int) (ICON_SIZE * 0.75));
            g.drawLine((int) (ICON_SIZE * 0.25), (int) (ICON_SIZE * 0.75), (int) (ICON_SIZE * 0.75), (int) (ICON_SIZE * 0.25));
            icons.put("cross", img);
        }

    }

    public static BufferedImage getIcon(String name) {
        return icons.get(name);
    }

}
