package controller;

import gui.SearchAndReplaceDialogWindow;
import gui.TextEditorGUI;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SearchAndReplaceManager implements ActionListener {

    private final TextEditorGUI gui;
    private final JTextArea textArea;
    private final SearchAndReplaceDialogWindow dialogWindow;
    private int lastMatchIndex = -1;
    private boolean hasSearchFunctionBeenCalled = false;

    public SearchAndReplaceManager(TextEditorGUI gui, SearchAndReplaceDialogWindow dialogWindow) {
        this. gui = gui;
        this.textArea = gui.getTextArea();
        this.dialogWindow = dialogWindow;

        initialiseSearchAndReplaceListeners();
    }

    private void initialiseSearchAndReplaceListeners() {
        // Window listeners
        dialogWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                clearHighlights();
                resetMatchIndex();
                resetHasSearchFunctionBeenCalled();
            }
        });

        dialogWindow.getSearchButton().addActionListener(this); // <- Listeners in die richtigen Klassen verschieben!
        dialogWindow.getSearchButton().setActionCommand("search");

        dialogWindow.getReplaceButton().addActionListener(this);
        dialogWindow.getReplaceButton().setActionCommand("replace");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "search":
                search(dialogWindow.getSearchField().getText(), dialogWindow.getCaseSensitiveCheck().isSelected());
                break;
            case "replace":
                replace(
                        dialogWindow.getSearchField().getText(),
                        dialogWindow.getReplaceField().getText(),
                        dialogWindow.getCaseSensitiveCheck().isSelected()
                );
                break;
            default:
                System.out.println("Unbekannte Aktion: " + command);
        }
    }

    public int search(String searchTerm, boolean isCaseSensitive) {
        hasSearchFunctionBeenCalled = true;

        if (searchTerm == null || searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(
                    gui,
                    "Kein Suchbegriff eingegeben",
                    "Leeres Suchfeld",
                    JOptionPane.WARNING_MESSAGE
            );
            return -1;
        }

        String textContent = textArea.getText();
        int currentCursorPosition = textArea.getCaretPosition(); // Current cursor position

        if (!isCaseSensitive) {
            searchTerm = searchTerm.toLowerCase();
            textContent = textContent.toLowerCase();
        }

        // Search from the current cursor position
        lastMatchIndex = textContent.indexOf(searchTerm, currentCursorPosition);

        // If no match was found, start from the beginning of the document
        if (lastMatchIndex == -1) {
            lastMatchIndex = textContent.indexOf(searchTerm);
            if (lastMatchIndex != -1) {
                JOptionPane.showMessageDialog(
                        gui,
                        "Am Ende des Dokuments angekommen. Suche beginnt von vorne",
                        "Suchen",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        gui,
                        "Keine Treffer",
                        "Suchen",
                        JOptionPane.INFORMATION_MESSAGE
                );
                resetHasSearchFunctionBeenCalled(); // Prevents replace() to cause an exception, when it tries to replace a not existing word.
                return -1;
            }
        }

        // Highlight the text found
        highlightText(lastMatchIndex, lastMatchIndex + searchTerm.length());
        textArea.setCaretPosition(lastMatchIndex + searchTerm.length());
        return lastMatchIndex;
    }

    protected void replace(String searchTerm, String replaceTerm, boolean isCaseSensitive) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(
                    gui,
                    "Kein Suchbegriff eingegeben",
                    "Leeres Suchfeld",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // check if search() has already been called
        if (!hasSearchFunctionBeenCalled) {
            lastMatchIndex = search(searchTerm, isCaseSensitive);
            if (lastMatchIndex == -1) return; // If no match was found, end the method
            return; // **Important:** End the method here so that the user sees the first result.
        }

        // Get the actually marked word from the JTextArea
        String selectedText = textArea.getText().substring(lastMatchIndex, lastMatchIndex + searchTerm.length());

        // Check whether the selected word matches searchTerm
        if ((isCaseSensitive && selectedText.equals(searchTerm)) || (!isCaseSensitive && selectedText.equalsIgnoreCase(searchTerm))) {
            textArea.replaceRange(replaceTerm, lastMatchIndex, lastMatchIndex + searchTerm.length());
        }

        // Search for the next match
        lastMatchIndex = search(searchTerm, isCaseSensitive);
    }

    private void highlightText(int start, int end) {
        try {
            Highlighter highlighter = textArea.getHighlighter();
            Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);

            // Remove all previous markings
            highlighter.removeAllHighlights();

            // Add new marking
            highlighter.addHighlight(start, end, painter);

            // Move cursor to found position
            textArea.setCaretPosition(end);
        } catch (BadLocationException exception) {
            exception.printStackTrace();
        }
    }

    public void clearHighlights() {
        Highlighter highlighter = textArea.getHighlighter();
        highlighter.removeAllHighlights();
    }

    public void resetMatchIndex() {
        lastMatchIndex = -1;
    }

    public void resetHasSearchFunctionBeenCalled() {
        hasSearchFunctionBeenCalled = false;
    }

}
