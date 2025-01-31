package controller.editmenu;

import gui.SearchAndReplaceDialogWindow;
import gui.TextEditorMainGUI;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Manages the search and replace functionality in the text editor.
 * Handles user interactions from the search and replace dialogue, performs text searches,
 * replaces occurrences, and highlights matches.
 */
public class SearchAndReplaceManager implements ActionListener {

    private final TextEditorMainGUI gui;
    private final JTextArea textArea;
    private final SearchAndReplaceDialogWindow dialogWindow;
    private int lastMatchIndex = -1;
    private boolean hasSearchFunctionBeenCalled = false;

    /**
     * Constructs the search and replace manager.
     *
     * @param gui          The main GUI of the text editor.
     * @param dialogWindow The search and replace dialogue window.
     */
    public SearchAndReplaceManager(TextEditorMainGUI gui, SearchAndReplaceDialogWindow dialogWindow) {
        this.gui = gui;
        this.textArea = gui.getTextArea();
        this.dialogWindow = dialogWindow;

        initialiseSearchAndReplaceListeners();
    }

    /**
     * Registers event listeners for the search and replace dialog.
     */
    private void initialiseSearchAndReplaceListeners() {
        dialogWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeSearchDialog();
            }
        });

        dialogWindow.getRootPane().registerKeyboardAction(
                e -> closeSearchDialog(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        textArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                clearHighlights();
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });

        registerEnterKeyListener(dialogWindow.getReplaceField(), "replace");
        registerEnterKeyListener(dialogWindow.getSearchField(), "search");
        registerEnterKeyListener(dialogWindow.getSearchButton(), "search");
        registerEnterKeyListener(dialogWindow.getReplaceButton(), "replace");

        dialogWindow.getCaseSensitiveCheck().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    dialogWindow.getCaseSensitiveCheck().setSelected(!dialogWindow.getCaseSensitiveCheck().isSelected());
                }
            }
        });

        // Allow navigation between buttons using arrow keys
        registerArrowKeyListener(dialogWindow.getSearchButton(), KeyEvent.VK_RIGHT, dialogWindow.getReplaceButton());
        registerArrowKeyListener(dialogWindow.getReplaceButton(), KeyEvent.VK_LEFT, dialogWindow.getSearchButton());

        dialogWindow.setFocusTraversalPolicy(new LayoutFocusTraversalPolicy());

        dialogWindow.getSearchButton().addActionListener(this);
        dialogWindow.getSearchButton().setActionCommand("search");

        dialogWindow.getReplaceButton().addActionListener(this);
        dialogWindow.getReplaceButton().setActionCommand("replace");
    }

    /**
     * Registers an Enter key listener to trigger an action.
     *
     * @param component The component to listen for key presses.
     * @param action    The action command to execute.
     */
    private void registerEnterKeyListener(JComponent component, String action) {
        component.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    actionPerformed(new ActionEvent(component, ActionEvent.ACTION_PERFORMED, action));
                }
            }
        });
    }

    /**
     * Registers an arrow key listener to navigate between buttons.
     *
     * @param source      The button where the key event originates.
     * @param keyCode     The arrow key code (e.g., KeyEvent.VK_LEFT or KeyEvent.VK_RIGHT).
     * @param destination The button to transfer focus to.
     */
    private void registerArrowKeyListener(JButton source, int keyCode, JButton destination) {
        source.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == keyCode) {
                    destination.requestFocus();
                }
            }
        });
    }

    /**
     * Handles search and replace actions triggered by the user.
     *
     * @param e The action event triggered by user interaction.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "search":
                search(dialogWindow.getSearchField().getText(), dialogWindow.getCaseSensitiveCheck().isSelected());
                break;
            case "replace":
                replace(dialogWindow.getSearchField().getText(), dialogWindow.getReplaceField().getText(),
                        dialogWindow.getCaseSensitiveCheck().isSelected());
                break;
            default:
                System.out.println("Unbekannte Aktion: " + e.getActionCommand());
        }
    }

    /**
     * Closes the search dialogue and resets search-related states.
     */
    private void closeSearchDialog() {
        clearHighlights();
        resetMatchIndex();
        resetHasSearchFunctionBeenCalled();
        dialogWindow.dispose();
    }

    /**
     * Searches for the given term in the text area and highlights the first match.
     *
     * @param searchTerm      The text to search for.
     * @param isCaseSensitive Whether the search is case-sensitive.
     * @return The index of the match, or -1 if not found.
     */
    public int search(String searchTerm, boolean isCaseSensitive) {
        hasSearchFunctionBeenCalled = true;

        if (searchTerm == null || searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(gui, "Kein Suchbegriff eingegeben", "Leeres Suchfeld",
                    JOptionPane.WARNING_MESSAGE);
            return -1;
        }

        String textContent = textArea.getText();
        int currentCursorPosition = textArea.getCaretPosition();

        if (!isCaseSensitive) {
            searchTerm = searchTerm.toLowerCase();
            textContent = textContent.toLowerCase();
        }

        lastMatchIndex = textContent.indexOf(searchTerm, currentCursorPosition);

        if (lastMatchIndex == -1) {
            lastMatchIndex = textContent.indexOf(searchTerm);
            if (lastMatchIndex != -1) {
                JOptionPane.showMessageDialog(gui, "Am Ende des Dokuments angekommen. Suche beginnt von vorne",
                        "Suchen", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(gui, "Keine Treffer", "Suchen", JOptionPane.INFORMATION_MESSAGE);
                resetHasSearchFunctionBeenCalled();
                return -1;
            }
        }

        highlightText(lastMatchIndex, lastMatchIndex + searchTerm.length());
        textArea.setCaretPosition(lastMatchIndex + searchTerm.length());
        return lastMatchIndex;
    }

    /**
     * Replaces the currently highlighted search term with the given replacement.
     *
     * @param searchTerm      The text to search for.
     * @param replaceTerm     The text to replace the found term with.
     * @param isCaseSensitive Whether the search is case-sensitive.
     */
    public void replace(String searchTerm, String replaceTerm, boolean isCaseSensitive) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(gui, "Kein Suchbegriff eingegeben", "Leeres Suchfeld",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!hasSearchFunctionBeenCalled) {
            lastMatchIndex = search(searchTerm, isCaseSensitive);
            if (lastMatchIndex == -1) return;
            return;
        }

        String selectedText = textArea.getText().substring(lastMatchIndex, lastMatchIndex + searchTerm.length());

        if ((isCaseSensitive && selectedText.equals(searchTerm)) ||
                (!isCaseSensitive && selectedText.equalsIgnoreCase(searchTerm))) {
            textArea.replaceRange(replaceTerm, lastMatchIndex, lastMatchIndex + searchTerm.length());
        }

        lastMatchIndex = search(searchTerm, isCaseSensitive);
    }

    /**
     * Highlights a section of text in the text area.
     *
     * @param start The starting index of the highlight.
     * @param end   The ending index of the highlight.
     */
    private void highlightText(int start, int end) {
        try {
            Highlighter highlighter = textArea.getHighlighter();
            Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);
            highlighter.removeAllHighlights();
            highlighter.addHighlight(start, end, painter);
            textArea.setCaretPosition(end);
        } catch (BadLocationException exception) {
            exception.printStackTrace();
        }
    }

    public void clearHighlights() { textArea.getHighlighter().removeAllHighlights(); }
    public void resetMatchIndex() { lastMatchIndex = -1; }
    public void resetHasSearchFunctionBeenCalled() { hasSearchFunctionBeenCalled = false; }
}
