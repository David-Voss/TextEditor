package gui;

import javax.swing.*;
import java.awt.*;

public class TextEditorGUI {

    JFrame frame;

    public TextEditorGUI() {
        frame = new JFrame("Texteditor");
        frame.setLayout(new BorderLayout());
        frame.setSize(800,600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creating the menu bar
        JMenuBar menuBar =new JMenuBar();

        // Creating the file menu
        JMenu fileMenu = new JMenu("Datei");
        menuBar.add(fileMenu);

        JMenuItem newItem = new JMenuItem("Neu");
        fileMenu.add(newItem);

        JMenuItem saveItem = new JMenuItem("Speichern");
        fileMenu.add(saveItem);

        // Creating the edit menu
        JMenu editMenu = new JMenu("Bearbeiten");
        menuBar.add(editMenu);

        JMenuItem undoItem = new JMenuItem("Rückgängig");
        editMenu.add(undoItem);

        JMenuItem searchItem = new JMenuItem("Suchen");
        editMenu.add(searchItem);

        // Creating the Toolbar
        JToolBar toolBar = new JToolBar();
        JButton newButton = new JButton(new ImageIcon("assets/icons/file-regular.png"));
        newButton.setToolTipText("Neue Datei erstellen");
        toolBar.add(newButton);

        // Adding the meu bar to JFrame
        frame.setJMenuBar(menuBar);

        // Adding the toolbar to JFrame
        frame.add(toolBar, BorderLayout.NORTH);

        frame.setVisible(true);
    }



}
