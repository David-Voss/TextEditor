package controller.editmenu;

import controller.TextEditorMainController;
import gui.TextEditorMainGUI;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * Manages edit menu functionalities, including undo/redo operations,
 * web search, and inserting the current date and time.
 */
public class EditMenuManager {

    private final TextEditorMainGUI gui;
    private final TextEditorMainController mainController;
    private final UndoManager undoManager = new UndoManager();

    /**
     * Constructs the edit menu manager.
     *
     * @param gui            The main GUI of the text editor.
     * @param mainController The main controller handling application logic.
     */
    public EditMenuManager(TextEditorMainGUI gui, TextEditorMainController mainController) {
        this.gui = gui;
        this.mainController = mainController;

        initialiseUndoManager();
        updateUndoRedoState();
    }

    /**
     * Initialises the undo manager and registers a listener to track document changes.
     */
    private void initialiseUndoManager() {
        gui.getTextArea().getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undoManager.addEdit(e.getEdit());
                updateUndoRedoState();
            }
        });
    }

    /**
     * Updates the enabled state of the undo and redo menu items.
     */
    public void updateUndoRedoState() {
        gui.getUndoItem().setEnabled(undoManager.canUndo());
        gui.getRedoItem().setEnabled(undoManager.canRedo());
    }

    /**
     * Performs an undo operation if possible.
     * Displays an information message if there is nothing to undo.
     */
    public void undo() {
        if (undoManager.canUndo()) {
            undoManager.undo();
            updateUndoRedoState();
        } else {
            showInfoDialog("Rückgängig", "Es gibt nichts zum Rückgängig machen");
        }
    }

    /**
     * Performs a redo operation if possible.
     * Displays an information message if there is nothing to redo.
     */
    public void redo() {
        if (undoManager.canRedo()) {
            undoManager.redo();
            updateUndoRedoState();
        } else {
            showInfoDialog("Wiederherstellen", "Es gibt nichts zum Wiederherstellen");
        }
    }

    /**
     * Performs a web search using the selected text.
     * Opens a browser with a Google search for the selected text.
     */
    public void webSearch() {
        String selectedText = gui.getTextArea().getSelectedText();
        if (selectedText == null || selectedText.isEmpty()) {
            showInfoDialog("Websuche", "Kein Text ausgewählt");
            return;
        }

        String query = URLEncoder.encode(selectedText, StandardCharsets.UTF_8);
        String webSearchUrl = "https://www.google.com/search?q=" + query;

        try {
            Desktop.getDesktop().browse(URI.create(webSearchUrl));
        } catch (IOException exception) {
            showErrorDialog("Fehler beim Öffnen des Browsers", exception);
        }
    }

    /**
     * Enables or disables the web search menu item based on text selection.
     */
    public void updateWebSearchItemStatus() {
        boolean hasSelection = gui.getTextArea().getSelectedText() != null;
        gui.getWebSearchItem().setEnabled(hasSelection);
    }

    /**
     * Inserts the current date and time at the cursor position.
     * Uses system locale and timezone settings to format the date and time.
     */
    public void dateTime() {
        JTextArea textArea = gui.getTextArea();
        int cursorPosition = textArea.getCaretPosition();

        Locale systemLocale = Locale.getDefault();
        ZoneId systemTimeZone = ZoneId.systemDefault();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(systemLocale);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(systemLocale);

        ZonedDateTime dateTimeNow = ZonedDateTime.now(systemTimeZone);
        String dateTimeString = dateTimeNow.format(dateFormatter) + " " + dateTimeNow.format(timeFormatter);

        try {
            textArea.getDocument().insertString(cursorPosition, dateTimeString, null);
        } catch (BadLocationException exception) {
            showErrorDialog("Fehler beim Einfügen von Datum und Uhrzeit", exception);
        }
    }

    /**
     * Displays an error message dialogue.
     *
     * @param title     The title of the error message.
     * @param exception The exception containing the error details.
     */
    private void showErrorDialog(String title, Exception exception) {
        JOptionPane.showMessageDialog(gui, title + ":\n" + exception.getMessage(),
                "Fehler", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays an information message dialogue.
     *
     * @param title   The title of the message.
     * @param message The content of the message.
     */
    private void showInfoDialog(String title, String message) {
        JOptionPane.showMessageDialog(gui, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
