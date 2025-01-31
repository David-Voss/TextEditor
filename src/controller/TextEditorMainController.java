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

public class TextEditorMainController implements ActionListener {

    private final TextEditorMainGUI gui;
    private final FileMenuManager fileMenuManager;
    private final EditMenuManager editMenuManager;
    private final SearchAndReplaceDialogWindow searchAndReplaceDialogWindow;
    private final SearchAndReplaceManager searchAndReplaceManager;
    private final ToolBarManager toolBarManager;

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
        gui.getSearchAndReplaceItem().setAccelerator(KeyStroke.getKeyStroke('F', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        gui.getDateTimeItem().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));

        // Shortcuts Toolbar
        //gui.getToolBar().getSearchField().setAccelerator(KeyStroke.getKeyStroke('F', InputEvent.CTRL_DOWN_MASK));
    }

    private void initialiseListeners() {
        gui.getTextArea().addCaretListener(e -> editMenuManager.updateWebSearchItemStatus());

        // Listeners 'File' menu
        gui.getNewFileItem().addActionListener(this);
        gui.getNewFileItem().setActionCommand("new");

        gui.getOpenFileItem().addActionListener(this);
        gui.getOpenFileItem().setActionCommand("open");

        gui.getSaveFileItem().addActionListener(this);
        gui.getSaveFileItem().setActionCommand("save");

        gui.getSaveFileAsItem().addActionListener(this);
        gui.getSaveFileAsItem().setActionCommand("save_as");

        gui.getPrintDocumentItem().addActionListener(this);
        gui.getPrintDocumentItem().setActionCommand("print");

        // Listeners 'Edit' menu
        gui.getUndoItem().addActionListener(this);
        gui.getUndoItem().setActionCommand("undo");

        gui.getRedoItem().addActionListener(this);
        gui.getRedoItem().setActionCommand("redo");

        gui.getWebSearchItem().addActionListener(this);
        gui.getWebSearchItem().setActionCommand("web_search");

        gui.getSearchAndReplaceItem().addActionListener(this);
        gui.getSearchAndReplaceItem().setActionCommand("search_and_replace_dialog");

        gui.getDateTimeItem().addActionListener(this);
        gui.getDateTimeItem().setActionCommand("date/time");
    }

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
            // search() / replace() -> SearchAndReplaceManager
            case "date/time":
                editMenuManager.dateTime();
                break;
            default:
                break;
        }
    }

    public void updateTitle(File currentFile) {
        if (currentFile != null) {
            gui.setTitle("Texteditor | " + currentFile.getName());
        } else {
            gui.setTitle("Texteditor | Unbenannt");
        }
    }
}