package gui;

import javax.swing.*;
import java.awt.*;

public class TextEditorGUI extends JFrame {

    //JFrame frame;
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem openFileItem;
    JMenuItem newFileItem;
    JMenuItem saveFileItem;
    JMenuItem saveFileAsItem;
    JMenu editMenu;
    JMenuItem undoItem;
    JMenuItem searchWordItem;

    JTextArea textArea;

    public TextEditorGUI(String title) {
        super(title);
        //frame = new JFrame("Texteditor");
        setLayout(new BorderLayout());
        setSize(600,800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //// Creating the menu bar
        this.menuBar =new JMenuBar();


        // Creating the file menu
        this.fileMenu = new JMenu("Datei");
        menuBar.add(fileMenu);

        this.openFileItem = new JMenuItem("Öffnen");
        fileMenu.add((openFileItem));

        this.newFileItem = new JMenuItem("Neu");
        fileMenu.add(newFileItem);

        this.saveFileItem = new JMenuItem("Speichern");
        fileMenu.add(saveFileItem);

        this.saveFileAsItem = new JMenuItem("Speichern als");
        fileMenu.add(saveFileAsItem);


        // Creating the edit menu
        this.editMenu = new JMenu("Bearbeiten");
        menuBar.add(editMenu);

        this.undoItem = new JMenuItem("Rückgängig");
        editMenu.add(undoItem);

        this.searchWordItem = new JMenuItem("Suchen");
        editMenu.add(searchWordItem);

        // Adding the meu bar to JFrame
        setJMenuBar(menuBar);


        //// Creating the Toolbar
        JToolBar toolBar = new JToolBar();
        JButton newButton = new JButton(new ImageIcon("assets/icons/file-regular.png"));
        newButton.setToolTipText("Neue Datei erstellen");
        toolBar.add(newButton);

        // Adding the toolbar to JFrame
        //frame.add(toolBar, BorderLayout.NORTH);


        //// Creating the text area
        this.textArea = new JTextArea();
        textArea.setLineWrap(true); // <- Implementing manual switching on and off later.

        // Adding the text area to JFrame
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        //pack(); //Is not used, because a certain size is needed, when opening the window.

        setVisible(true);
    }

    public JMenuItem getFileMenu() {
        return fileMenu;
    }

    public JMenuItem getOpenFileItem() {
        return openFileItem;
    }

    public JMenuItem getNewFileItem() {
        return newFileItem;
    }

    public JMenuItem getSaveFileItem() {
        return saveFileItem;
    }

    public JMenuItem getSaveFileAsItem() {
        return saveFileAsItem;
    }

    public JMenu getEditMenu() {
        return editMenu;
    }

    public JMenuItem getUndoItem() {
        return undoItem;
    }

    public JMenuItem getSearchWordItem() {
        return searchWordItem;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

}
