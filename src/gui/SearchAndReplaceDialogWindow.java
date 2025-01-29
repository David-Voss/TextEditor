package gui;

import controller.SearchAndReplaceManager;

import javax.swing.*;
import java.awt.*;

public class SearchAndReplaceDialogWindow extends JDialog {

    JPanel searchPanel;
    JTextField searchField;
    JTextField replaceField;
    JCheckBox caseSensitiveCheckBox;
    JButton searchButton;
    JButton replaceButton;

    public SearchAndReplaceDialogWindow(JFrame parent, SearchAndReplaceManager searchAndReplaceManager) {
        super(parent, "Suchen und Ersetzen", false);

        setLayout(new BorderLayout());
        setSize(400, 200);
        setLocationRelativeTo(parent);

        // Search and replace area
        this.searchPanel = new JPanel(new GridLayout(3,2,5,5));

        searchPanel.add(new JLabel("Suchen:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);

        searchPanel.add(new JLabel("Ersetzen:"));
        replaceField = new JTextField(20);
        searchPanel.add(replaceField);

        caseSensitiveCheckBox = new JCheckBox("Groß- / Kleinschreibung beachten");
        searchPanel.add(caseSensitiveCheckBox);

        add(searchPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        searchButton = new JButton("Suchen");
        buttonPanel.add(searchButton);

        replaceButton = new JButton("Ersetzen");
        buttonPanel.add(replaceButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // WindowListener - Removes highlights when closing the window
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                searchAndReplaceManager.clearHighlights();
                searchAndReplaceManager.resetMatchIndex();
                searchAndReplaceManager.resetHasSearchFunctionBeenCalled();
            }
        });
    }

    public void showSearchAndReplaceDialog() {
        setVisible(true);
    }

    public JPanel getSearchPanel() {
        return searchPanel;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JTextField getReplaceField() {
        return replaceField;
    }

    public JCheckBox getCaseSensitiveCheck() {
        return caseSensitiveCheckBox;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public JButton getReplaceButton() {
        return replaceButton;
    }

}
