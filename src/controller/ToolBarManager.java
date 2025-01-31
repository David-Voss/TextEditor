package controller;

import controller.editmenu.EditMenuManager;
import controller.editmenu.SearchAndReplaceManager;
import controller.filemenu.FileMenuManager;
import gui.TextEditorToolBar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolBarManager implements ActionListener {
    private final TextEditorToolBar toolBar;
    private final FileMenuManager fileMenuManager;
    private final EditMenuManager editMenuManager;
    private final SearchAndReplaceManager searchAndReplaceManager;

    public ToolBarManager(TextEditorToolBar toolBar, FileMenuManager fileMenuManager, EditMenuManager editMenuManager, SearchAndReplaceManager searchAndReplaceManager) {
        this.toolBar = toolBar;
        this.fileMenuManager = fileMenuManager;
        this.editMenuManager = editMenuManager;
        this.searchAndReplaceManager = searchAndReplaceManager;
        initialiseToolBarListeners();
    }

    private void initialiseToolBarListeners() {
        toolBar.getNewFileButton().addActionListener(this);
        toolBar.getNewFileButton().setActionCommand("new");

        toolBar.getOpenFileButton().addActionListener(this);
        toolBar.getOpenFileButton().setActionCommand("open");

        toolBar.getSaveFileButton().addActionListener(this);
        toolBar.getSaveFileButton().setActionCommand("save");

        toolBar.getPrintDocumentButton().addActionListener(this);
        toolBar.getPrintDocumentButton().setActionCommand("print");

        toolBar.getUndoButton().addActionListener(this);
        toolBar.getUndoButton().setActionCommand("undo");

        toolBar.getRedoButton().addActionListener(this);
        toolBar.getRedoButton().setActionCommand("redo");

        toolBar.getWebSearchButton().addActionListener(this);
        toolBar.getWebSearchButton().setActionCommand("web_search");

        toolBar.getSearchField().addActionListener(this);
        toolBar.getSearchField().setActionCommand("search_field");
    }

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
        }
    }
}
