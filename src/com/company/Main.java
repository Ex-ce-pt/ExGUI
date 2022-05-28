package com.company;

import javax.swing.JFrame;
import javax.swing.JPanel;
import ExGUI.*;

import java.awt.*;

public class Main {

    public enum ProgramMode {
        DEFAULT,
        DEBUG
    }

    private static ProgramMode runMode;

    public static final Object GLOBAL_MUTEX = new Object();
    public static final JFrame frame = new JFrame();

    public static void main(String[] args) {

        if (args.length != 0 && args[0].equals("DEBUG")) runMode = ProgramMode.DEBUG;
        else runMode = ProgramMode.DEFAULT;

        frame.setTitle((runMode == ProgramMode.DEFAULT) ? "ExGUI - DEFAULT" : "ExGUI - DEBUG");
        frame.setLayout(new BorderLayout());
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setFocusable(true);

        ExGUI.load();
        ExGUI.setMutex(GLOBAL_MUTEX);
        ExGUI.setMode((runMode == ProgramMode.DEFAULT) ? ExGUI.Mode.DEFAULT : ExGUI.Mode.DEBUG);
        ExGUI.setTheme(ExGUI.Theme.LIGHT);
        var widgetPanel = ExGUI.createContext(frame);

        System.out.printf("Running %s\n", ExGUI.getVersion());

        frame.setVisible(true);

    }

}
