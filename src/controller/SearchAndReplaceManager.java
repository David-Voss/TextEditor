package controller;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class SearchAndReplaceManager {

    private final JTextArea textArea;
    private int lastMatchIndex = -1;
    private boolean hasSearchFunctionBeenCalled = false;

    public SearchAndReplaceManager(JTextArea textArea) {
        this.textArea = textArea;
    }

    public int search(String searchTerm, boolean isCaseSensitive) {
        hasSearchFunctionBeenCalled = true;

        if (searchTerm == null || searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Kein Suchbegriff eingegeben.",
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
                        null,
                        "Am Ende des Dokuments angekommen. Suche beginnt von vorne.",
                        "Suchen",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Keine Treffer.",
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

    public void replace(String searchTerm, String replaceTerm, boolean isCaseSensitive) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Kein Suchbegriff eingegeben.", "Leeres Suchfeld", JOptionPane.WARNING_MESSAGE);
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

            // Place the cursor behind the replaced word
            //textArea.setCaretPosition(lastMatchIndex + replaceTerm.length());
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
