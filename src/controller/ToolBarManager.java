package controller;

import gui.TextEditorToolBar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolBarManager implements ActionListener {
    private final TextEditorToolBar toolBar;
    private final TextEditorController textEditorController;

    public ToolBarManager(TextEditorToolBar toolBar, TextEditorController textEditorController) {
        this.toolBar = toolBar;
        this.textEditorController = textEditorController;
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
                textEditorController.createNewFile();
                break;
            case "open":
                textEditorController.openFile();
                break;
            case "save":
                textEditorController.saveFile();
                break;
            case "print":
                textEditorController.printDocument();
                break;
            case "undo":
                textEditorController.undo();
                break;
            case "redo":
                textEditorController.redo();
                break;
            case "web_search":
                textEditorController.webSearch();
                break;
            case "search_field":
                textEditorController.search(toolBar.getSearchField().getText(), false);
                break;
        }
    }
}
