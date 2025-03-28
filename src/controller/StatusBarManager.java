package controller;

import gui.TextEditorStatusBar;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Manages the status bar updates for the text editor.
 * This class listens for caret movements and document changes,
 * updating the cursor position, word count, and character count in real-time.
 */
public class StatusBarManager {

    private final TextEditorStatusBar statusBar;
    private final JTextArea textArea;

    /**
     * Constructs the status bar manager and registers event listeners.
     *
     * @param statusBar The status bar instance to be updated.
     * @param textArea  The text area whose events are being monitored.
     */
    public StatusBarManager(TextEditorStatusBar statusBar, JTextArea textArea) {
        this.statusBar = statusBar;
        this.textArea = textArea;

        textArea.addCaretListener(new CursorPositionUpdater());
        textArea.getDocument().addDocumentListener(new TextUpdater());
    }

    /**
     * Listens for caret position changes and updates the cursor position in the status bar.
     */
    private class CursorPositionUpdater implements CaretListener {
        @Override
        public void caretUpdate(CaretEvent e) {
            try {
                int caretPos = e.getDot();
                int line = textArea.getLineOfOffset(caretPos) + 1;
                int column = caretPos - textArea.getLineStartOffset(line - 1) + 1;
                statusBar.updateCursorPosition(line, column);
            } catch (Exception exception) {
                statusBar.updateCursorPosition(1, 1); // Default position in case of error
            }
        }
    }

    /**
     * Listens for document changes and updates the word and character count in the status bar.
     */
    private class TextUpdater implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) { infoUpdater(); }
        @Override
        public void removeUpdate(DocumentEvent e) { infoUpdater(); }
        @Override
        public void changedUpdate(DocumentEvent e) { infoUpdater(); }

        /**
         * Updates the text statistics displayed in the status bar.
         */
        private void infoUpdater() {
            String text = textArea.getText();
            statusBar.updateTextInfo(text.length(), countWords(text));
        }
    }

    /**
     * Counts the number of words in the given text.
     *
     * @param text The text to analyse.
     * @return The number of words detected in the text.
     */
    private int countWords(String text) {
        return text.trim().isEmpty() ? 0 : text.trim().split("\\s+").length;
    }

    /**
     * Updates the cursor position in the status bar.
     *
     * @param line   The current line number of the cursor (1-based index).
     * @param column The current column number of the cursor (1-based index).
     */
    public void updateCursorPosition(int line, int column) {
        statusBar.updateCursorPosition(line, column);
    }

    /**
     * Updates the text information displayed in the status bar.
     *
     * @param charCount The total number of characters in the text.
     * @param wordCount The total number of words in the text.
     */
    public void updateTextInfo(int charCount, int wordCount) {
        statusBar.updateTextInfo(charCount, wordCount);
    }
}
