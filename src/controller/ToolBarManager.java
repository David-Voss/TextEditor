package controller;

import controller.editmenu.EditMenuManager;
import controller.editmenu.SearchAndReplaceManager;
import controller.filemenu.FileMenuManager;
import gui.TextEditorToolBar;

import java.awt.event.*;

/**
 * Manages the toolbar actions and connects them to the corresponding menu managers.
 * This class listens for user interactions with toolbar buttons and executes
 * the appropriate actions accordingly.
 */
public class ToolBarManager implements ActionListener {

    private final TextEditorToolBar toolBar;
    private final FileMenuManager fileMenuManager;
    private final EditMenuManager editMenuManager;
    private final SearchAndReplaceManager searchAndReplaceManager;

    /**
     * Constructs the toolbar manager and sets up event listeners for toolbar actions.
     *
     * @param toolBar                The toolbar instance containing interactive buttons.
     * @param fileMenuManager        The manager handling file-related actions.
     * @param editMenuManager        The manager handling edit-related actions.
     * @param searchAndReplaceManager The manager handling search and replace functionality.
     */
    public ToolBarManager(TextEditorToolBar toolBar, FileMenuManager fileMenuManager, EditMenuManager editMenuManager, SearchAndReplaceManager searchAndReplaceManager) {
        this.toolBar = toolBar;
        this.fileMenuManager = fileMenuManager;
        this.editMenuManager = editMenuManager;
        this.searchAndReplaceManager = searchAndReplaceManager;
        initialiseToolBarListeners();
    }

    /**
     * Registers action listeners for all toolbar buttons.
     */
    private void initialiseToolBarListeners() {
        addButtonListener(toolBar.getNewFileButton(), "new");
        addButtonListener(toolBar.getOpenFileButton(), "open");
        addButtonListener(toolBar.getSaveFileButton(), "save");
        addButtonListener(toolBar.getPrintDocumentButton(), "print");
        addButtonListener(toolBar.getUndoButton(), "undo");
        addButtonListener(toolBar.getRedoButton(), "redo");
        addButtonListener(toolBar.getWebSearchButton(), "web_search");

        // Register search field action
        toolBar.getSearchField().addActionListener(this);
        toolBar.getSearchField().setActionCommand("search_field");
    }

    /**
     * Adds an action listener to a button with a specific action command.
     *
     * @param button   The JButton to which the listener is added.
     * @param command  The action command associated with the button.
     */
    private void addButtonListener(javax.swing.JButton button, String command) {
        button.addActionListener(this);
        button.setActionCommand(command);
    }

    /**
     * Handles toolbar actions triggered by the user.
     *
     * @param e The action event triggered by user interaction.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        switch (actionCommand) {
            case "new":
                fileMenuManager.createNewFile();
                break;
            case "open":
                fileMenuManager.openFile();
                break;
            case "save":
                fileMenuManager.saveFile();
                break;
            case "print":
                fileMenuManager.printDocument();
                break;
            case "undo":
                editMenuManager.undo();
                break;
            case "redo":
                editMenuManager.redo();
                break;
            case "web_search":
                editMenuManager.webSearch();
                break;
            case "search_field":
                searchAndReplaceManager.search(toolBar.getSearchField().getText(), false);
                break;
            default:
                break;
        }
    }
}
