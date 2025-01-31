package controller;

import gui.TextEditorStatusBar;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class StatusBarManager {
    private final TextEditorStatusBar statusBar;
    private final JTextArea textArea;

    public StatusBarManager(TextEditorStatusBar statusBar, JTextArea textArea) {
        this.statusBar = statusBar;
        this.textArea = textArea;

        textArea.addCaretListener(new CursorPositionUpdater());
        textArea.getDocument().addDocumentListener(new TextUpdater());
    }

    private class CursorPositionUpdater implements CaretListener {
        @Override
        public void caretUpdate(CaretEvent e) {
            try {
                int caretPos = e.getDot();
                int line = textArea.getLineOfOffset(caretPos) + 1;
                int column = caretPos - textArea.getLineStartOffset(line - 1) + 1;
                statusBar.updateCursorPosition(line, column);
            } catch (Exception exception) {
                statusBar.updateCursorPosition(1, 1);
            }
        }
    }

    private class TextUpdater implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) { updateTextInfo(); }
        @Override
        public void removeUpdate(DocumentEvent e) { updateTextInfo(); }
        @Override
        public void changedUpdate(DocumentEvent e) { updateTextInfo(); }

        private void updateTextInfo() {
            String text = textArea.getText();
            statusBar.updateTextInfo(text.length(), countWords(text));
        }
    }

    private int countWords(String text) {
        return (int) text.trim().split("\\s+").length;
    }
}
