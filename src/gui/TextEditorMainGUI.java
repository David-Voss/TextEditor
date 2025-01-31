package gui;

import controller.StatusBarManager;

import javax.swing.*;
import java.awt.*;

/**
 * The main graphical user interface (GUI) for the text editor.
 * This class extends {@link JFrame} and provides the main window,
 * including the menu bar, toolbar, text area, and status bar.
 */
public class TextEditorMainGUI extends JFrame {

    // Menu bar and its components
    JMenuBar menuBar;

    // File menu and its items
    JMenu fileMenu;
    JMenuItem openFileItem;
    JMenuItem newFileItem;
    JMenuItem saveFileItem;
    JMenuItem saveFileAsItem;
    JMenuItem printDocumentItem;

    // Edit menu and its items
    JMenu editMenu;
    JMenuItem undoItem;
    JMenuItem redoItem;
    JMenuItem webSearchItem;
    JMenuItem searchItem;
    JMenuItem searchAndReplaceItem;
    JMenuItem dateTimeItem;

    // Toolbar, text area, and status bar
    private final TextEditorToolBar toolBar;
    JTextArea textArea;
    private final TextEditorStatusBar statusBar;

    /**
     * Constructs the main GUI window of the text editor.
     *
     * @param title The title of the window.
     */
    public TextEditorMainGUI(String title) {
        super(title);
        setLayout(new BorderLayout());
        setSize(550,750);
        centerWindow();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creating the menu bar
        this.menuBar =new JMenuBar();

        // Creating the file menu
        this.fileMenu = new JMenu("Datei");
        menuBar.add(fileMenu);

        this.newFileItem = new JMenuItem("Neu");
        fileMenu.add(newFileItem);

        this.openFileItem = new JMenuItem("Öffnen");
        fileMenu.add((openFileItem));

        this.saveFileItem = new JMenuItem("Speichern");
        fileMenu.add(saveFileItem);

        this.saveFileAsItem = new JMenuItem("Speichern unter");
        fileMenu.add(saveFileAsItem);

        fileMenu.addSeparator();

        this.printDocumentItem = new JMenuItem("Drucken");
        fileMenu.add(printDocumentItem);

        // Creating the edit menu
        this.editMenu = new JMenu("Bearbeiten");
        menuBar.add(editMenu);

        this.undoItem = new JMenuItem("Rückgängig");
        editMenu.add(undoItem);

        this.redoItem = new JMenuItem("Wiederherstellen");
        editMenu.add(redoItem);

        editMenu.addSeparator();

        this.webSearchItem = new JMenuItem("Mit Google suchen");
        webSearchItem.setEnabled(false);
        editMenu.add(webSearchItem);

        editMenu.addSeparator();

        this.searchItem = new JMenuItem("Suchen");
        editMenu.add(searchItem);

        this.searchAndReplaceItem = new JMenuItem("Suchen/Ersetzen");
        editMenu.add(searchAndReplaceItem);

        editMenu.addSeparator();

        this.dateTimeItem = new JMenuItem("Datum/Uhrzeit");
        editMenu.add(dateTimeItem);

        // Adding the meu bar to JFrame
        setJMenuBar(menuBar);

        // Creating and adding the Toolbar
        toolBar = new TextEditorToolBar();
        add(toolBar, BorderLayout.NORTH);

        // Creating the text area
        this.textArea = new JTextArea();
        textArea.setLineWrap(true); // Manual switching to be implemented later

        // Adding the text area with a scroll pane
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Adding the status bar
        statusBar = new TextEditorStatusBar();
        new StatusBarManager(statusBar, textArea);
        add(statusBar, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Getter methods to allow access to menu items and components

    // File menu getter
    public JMenuItem getOpenFileItem() { return openFileItem; }
    public JMenuItem getNewFileItem() { return newFileItem; }
    public JMenuItem getSaveFileItem() { return saveFileItem; }
    public JMenuItem getSaveFileAsItem() { return saveFileAsItem; }

    // Edit menu getter
    public JMenuItem getPrintDocumentItem() { return printDocumentItem; }
    public JMenuItem getUndoItem() { return undoItem; }
    public JMenuItem getRedoItem() {  return redoItem; }
    public JMenuItem getWebSearchItem() { return webSearchItem; }
    public JMenuItem getSearchItem() { return searchItem; };
    public JMenuItem getSearchAndReplaceItem() { return searchAndReplaceItem; }
    public JMenuItem getDateTimeItem() { return dateTimeItem; }

    // Tool bar and textArea getter
    public TextEditorToolBar getToolBar() { return toolBar; }
    public JTextArea getTextArea() { return textArea; }

    /**
     * Centres the application window on the user's screen.
     */
    private void centerWindow() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);
    }
}