package ExGUI.widget;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.*;
import java.awt.BorderLayout;

import java.io.File;

public class ExFileTree extends Widget {

    JScrollPane scroller;
    JTree tree;


    public ExFileTree(java.awt.Dimension size) {
        super(size);

        setLayout(new BorderLayout());

        tree = new JTree();


        scroller = new JScrollPane(tree);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scroller, BorderLayout.CENTER);
    }

    private DefaultMutableTreeNode getNode(String path) {
        File file = new File(path);

        DefaultMutableTreeNode node = new DefaultMutableTreeNode(file);
        if (file.isDirectory()) {
            if (file.listFiles() != null) {
                for (File i : file.listFiles()) {
                    node.add(getNode(i.getPath()));
                }
            }
        }

        return node;
    }


}
