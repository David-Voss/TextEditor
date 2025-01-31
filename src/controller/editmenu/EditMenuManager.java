package controller.editmenu;

import controller.TextEditorController;
import gui.TextEditorGUI;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class EditMenuManager {

    private final TextEditorGUI gui;
    private final TextEditorController textEditorController;
    private final UndoManager undoManager = new UndoManager();
    private File currentFile = null;

    public EditMenuManager(TextEditorGUI gui, TextEditorController textEditorController) {
        this.gui = gui;
        this.textEditorController = textEditorController;

        initialiseUndoManager();
        updateUndoRedoState();
    }

    private void initialiseUndoManager() {
        gui.getTextArea().getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undoManager.addEdit(e.getEdit());
                updateUndoRedoState();
            }
        });
    }

    public void updateUndoRedoState() {
        gui.getUndoItem().setEnabled(undoManager.canUndo());
        gui.getRedoItem().setEnabled(undoManager.canRedo());
    }

    public void undo() {
        if (undoManager.canUndo()) {
            undoManager.undo();
            updateUndoRedoState();
        } else {
            JOptionPane.showMessageDialog(
                    gui,
                    "Es gibt nichts zum Rückgängig machen.",
                    "Rückgängig",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    public void redo() {
        if (undoManager.canRedo()) {
            undoManager.redo();
            updateUndoRedoState();
        } else {
            JOptionPane.showMessageDialog(
                    gui,
                    "Es gibt nichts zum Wiederherstellen.",
                    "Wiederherstellen",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    public void webSearch() {
        String selectedText = gui.getTextArea().getSelectedText();
        String query = URLEncoder.encode(selectedText, StandardCharsets.UTF_8);
        String webSearchUrl = "https://www.google.com/search?q=" + query;

        try {
            Desktop.getDesktop().browse(URI.create(webSearchUrl));
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(
                    gui,
                    "Fehler beim Öffnen des Browsers.",
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void updateWebSearchItemStatus() {
        boolean hasSelection = gui.getTextArea().getSelectedText() != null;
        gui.getWebSearchItem().setEnabled(hasSelection);
    }

    public void dateTime() {
        JTextArea textArea = gui.getTextArea();
        int cursorPosition = textArea.getCaretPosition();

        // Get system locale and timezone
        Locale systemLocale = Locale.getDefault();
        ZoneId systemTimeZone = ZoneId.systemDefault();

        // Format for date & time
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(systemLocale);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(systemLocale);

        // Merge date & time
        ZonedDateTime dateTimeNow = ZonedDateTime.now(systemTimeZone);
        String dateTimeString = dateTimeNow.format(dateFormatter) + " " + dateTimeNow.format(timeFormatter);

        try {
            textArea.getDocument().insertString(cursorPosition, dateTimeString, null);
        } catch (BadLocationException exception) {
            JOptionPane.showMessageDialog(
                    gui,
                    "Fehler beim Einfügen von Datum und Uhrzeit",
                    "Datum/Uhrzeit Fehler",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
