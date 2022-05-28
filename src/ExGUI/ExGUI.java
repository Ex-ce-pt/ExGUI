package ExGUI;

import ExGUI.panel.MainWidgetPanel;
import ExGUI.icons.IconStore;

import ExGUI.lib.ExEvent.EventSystem;

public final class ExGUI {

    public enum Mode {
        DEFAULT,
        DEBUG
    }

    public enum Theme {
        LIGHT,
        DARK
    }

    private static final long VERSION = 0;
    private static boolean loaded = false;
    private static Mode mode = Mode.DEFAULT;
    private static Theme theme = Theme.LIGHT;
    private static Object ExGUIMutex = new Object();

    private ExGUI() {}

    public static MainWidgetPanel createContext(javax.swing.JFrame frame) {
        if (loaded) {
            frame.getContentPane().setSize(frame.getSize());
            return createContext((javax.swing.JPanel) frame.getContentPane());
        } else return null;
    }

    public static MainWidgetPanel createContext(javax.swing.JPanel panel) {
        if (loaded) {
            EventSystem.getEventSystem().bind(panel);
            return new MainWidgetPanel(panel, panel.getSize());
        } else return null;
    }

    public static void load() {
        loaded = true;
        IconStore.loadIcons();
        EventSystem.getEventSystem().setMutex(ExGUIMutex);
    }

    public static void setMode(Mode mode) {
        ExGUI.mode = mode;
    }

    public static void setTheme(Theme theme) { ExGUI.theme = theme; }

    public static void setMutex(Object exGUIMutex) { ExGUIMutex = exGUIMutex; }

    public static Mode getMode() {
        return mode;
    }

    public static Theme getTheme() { return theme; }

    public static String getVersion() {
        return "ExGUI v." + VERSION / 100;
    }

}
