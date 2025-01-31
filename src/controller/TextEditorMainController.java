package controller;

import controller.editmenu.EditMenuManager;
import controller.editmenu.SearchAndReplaceManager;
import controller.filemenu.FileMenuManager;
import gui.TextEditorMainGUI;
import gui.SearchAndReplaceDialogWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * The main controller for the text editor.
 * This class manages user interactions by handling menu actions, shortcuts,
 * and toolbar functionalities.
 */
public class TextEditorMainController implements ActionListener {

    private final TextEditorMainGUI gui;
    private final FileMenuManager fileMenuManager;
    private final EditMenuManager editMenuManager;
    private final SearchAndReplaceDialogWindow searchAndReplaceDialogWindow;
    private final SearchAndReplaceManager searchAndReplaceManager;
    private final ToolBarManager toolBarManager;

    /**
     * Constructs the main controller for the text editor.
     * It initialises menu managers, shortcut keys, and event listeners.
     *
     * @param gui The main graphical user interface of the text editor.
     */
    public TextEditorMainController(TextEditorMainGUI gui) {
        this.gui = gui;
        this.fileMenuManager = new FileMenuManager(gui, this);
        this.editMenuManager = new EditMenuManager(gui, this);
        this.searchAndReplaceDialogWindow = new SearchAndReplaceDialogWindow(gui);
        this.searchAndReplaceManager = new SearchAndReplaceManager(gui, searchAndReplaceDialogWindow);
        this.toolBarManager = new ToolBarManager(gui.getToolBar(), fileMenuManager, editMenuManager, searchAndReplaceManager);

        initialiseShortcuts();
        initialiseListeners();
    }

    /**
     * Sets up keyboard shortcuts for the application's menus.
     */
    public void initialiseShortcuts() {
        // Shortcuts 'File' menu
        gui.getNewFileItem().setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
        gui.getOpenFileItem().setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
        gui.getSaveFileItem().setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
        gui.getSaveFileAsItem().setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        gui.getPrintDocumentItem().setAccelerator(KeyStroke.getKeyStroke('P', InputEvent.CTRL_DOWN_MASK));

        // Shortcuts 'Edit' menu
        gui.getUndoItem().setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));
        gui.getRedoItem().setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        gui.getWebSearchItem().setAccelerator(KeyStroke.getKeyStroke('G', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        gui.getSearchItem().setAccelerator(KeyStroke.getKeyStroke('F', InputEvent.CTRL_DOWN_MASK));
        gui.getSearchAndReplaceItem().setAccelerator(KeyStroke.getKeyStroke('F', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        gui.getDateTimeItem().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
    }

    /**
     * Registers action listeners for menu items and updates web search status.
     */
    private void initialiseListeners() {
        // Update web search item status dynamically
        gui.getTextArea().addCaretListener(e -> editMenuManager.updateWebSearchItemStatus());

        // Register listeners for 'File' menu actions
        addMenuAction(gui.getNewFileItem(), "new");
        addMenuAction(gui.getOpenFileItem(), "open");
        addMenuAction(gui.getSaveFileItem(), "save");
        addMenuAction(gui.getSaveFileAsItem(), "save_as");
        addMenuAction(gui.getPrintDocumentItem(), "print");

        // Register listeners for 'Edit' menu actions
        addMenuAction(gui.getUndoItem(), "undo");
        addMenuAction(gui.getRedoItem(), "redo");
        addMenuAction(gui.getWebSearchItem(), "web_search");
        addMenuAction(gui.getSearchItem(), "simple_search");
        addMenuAction(gui.getSearchAndReplaceItem(), "search_and_replace_dialog");
        addMenuAction(gui.getDateTimeItem(), "date/time");
    }

    /**
     * Adds an action listener to a menu item with a specific action command.
     *
     * @param menuItem The menu item to which the listener is added.
     * @param command  The action command for event handling.
     */
    private void addMenuAction(JMenuItem menuItem, String command) {
        menuItem.addActionListener(this);
        menuItem.setActionCommand(command);
    }

    /**
     * Handles menu and toolbar actions triggered by the user.
     *
     * @param e The action event triggered by user interaction.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        switch (actionCommand) {
            // 'File' menu actions
            case "new":
                fileMenuManager.createNewFile();
                break;
            case "open":
                fileMenuManager.openFile();
                break;
            case "save":
                fileMenuManager.saveFile();
                break;
            case "save_as":
                fileMenuManager.saveFileAs();
                break;
            case "print":
                fileMenuManager.printDocument();
                break;
            // 'Edit' menu actions
            case "undo":
                editMenuManager.undo();
                break;
            case "redo":
                editMenuManager.redo();
                break;
            case "web_search":
                editMenuManager.webSearch();
                break;
            case "search_and_replace_dialog":
                searchAndReplaceDialogWindow.showSearchAndReplaceDialog(gui);
                break;
            case "simple_search":
                gui.getToolBar().getSearchField().requestFocus();
                break;
            // search()/replace() -> SearchAndReplaceManager
            case "date/time":
                editMenuManager.dateTime();
                break;
            default:
                break;
        }
    }

    /**
     * Updates the title of the editor window based on the current file.
     *
     * @param currentFile The currently opened or saved file.
     */
    public void updateTitle(File currentFile) {
        if (currentFile != null) {
            gui.setTitle("Texteditor | " + currentFile.getName());
        } else {
            gui.setTitle("Texteditor | Unbenannt");
        }
    }
}