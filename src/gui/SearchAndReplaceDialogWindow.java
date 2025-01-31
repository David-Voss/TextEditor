package gui;

import controller.SearchAndReplaceManager;

import javax.swing.*;
import java.awt.*;

public class SearchAndReplaceDialogWindow extends JDialog {

    JPanel searchPanel;
    JTextField searchField;
    JTextField replaceField;
    JPanel caseSensitivePanel;
    JCheckBox caseSensitiveCheckBox;
    JButton searchButton;
    JButton replaceButton;

    public SearchAndReplaceDialogWindow(JFrame parent) {
        super(parent, "Suchen und Ersetzen", false);

        setLayout(new BorderLayout());
        setSize(300, 150);

        // Search and replace area
        this.searchPanel = new JPanel(new GridLayout(2,2,0,5));

        searchPanel.add(new JLabel("Suchen:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);

        searchPanel.add(new JLabel("Ersetzen:"));
        replaceField = new JTextField(20);
        searchPanel.add(replaceField);

        add(searchPanel, BorderLayout.NORTH);

        // Case-sensitive Checkbox
        this.caseSensitivePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        caseSensitiveCheckBox = new JCheckBox("Groß- / Kleinschreibung beachten");
        caseSensitivePanel.add(caseSensitiveCheckBox);

        add(caseSensitivePanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        searchButton = new JButton("Suchen");
        buttonPanel.add(searchButton);

        replaceButton = new JButton("Ersetzen");
        buttonPanel.add(replaceButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void showSearchAndReplaceDialog(JFrame parent) {
        setVisible(true);
        setDialogWindowLocation(parent);
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

    // Centres the dialogue relative to the parent window.
    //  If parent is 'null', the window is placed in the centre of the screen.
    private void setDialogWindowLocation (JFrame parent) {
        if (parent != null) {
            int x = parent.getX() + (parent.getWidth() - getWidth()) / 2;
            int y = parent.getY() + (parent.getHeight() - getHeight()) / 2;
            setLocation(x, y);
        } else {
            setLocationRelativeTo(null); // Fallback zur Bildschirmmitte
        }
    }

}
