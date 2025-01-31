package gui;

import javax.swing.*;
import java.awt.*;

/**
 * The search and replace dialogue window for the text editor.
 * This class extends {@link JDialog} and provides a user interface
 * for searching and replacing text within the document.
 */
public class SearchAndReplaceDialogWindow extends JDialog {

    private final JPanel searchPanel;
    private final JTextField searchField;
    private final JTextField replaceField;
    private final JPanel caseSensitivePanel;
    private final JCheckBox caseSensitiveCheckBox;
    private final JButton searchButton;
    private final JButton replaceButton;

    /**
     * Constructs the search and replace dialogue window.
     *
     * @param parent The parent frame to which this dialogue belongs.
     */
    public SearchAndReplaceDialogWindow(JFrame parent) {
        super(parent, "Suchen und Ersetzen", false);

        setLayout(new BorderLayout());
        setSize(300, 150);

        // Search and replace input fields
        this.searchPanel = new JPanel(new GridLayout(2, 2, 0, 5));

        searchPanel.add(new JLabel("Suchen:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);

        searchPanel.add(new JLabel("Ersetzen:"));
        replaceField = new JTextField(20);
        searchPanel.add(replaceField);

        add(searchPanel, BorderLayout.NORTH);

        // Case-sensitive checkbox
        this.caseSensitivePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        caseSensitiveCheckBox = new JCheckBox("Groß- / Kleinschreibung beachten");
        caseSensitivePanel.add(caseSensitiveCheckBox);

        add(caseSensitivePanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        searchButton = new JButton("Suchen");
        buttonPanel.add(searchButton);

        replaceButton = new JButton("Ersetzen");
        buttonPanel.add(replaceButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Displays the search and replace dialogue window.
     * Ensures the window is correctly positioned relative to the parent frame.
     *
     * @param parent The parent frame to position the dialogue.
     */
    public void showSearchAndReplaceDialog(JFrame parent) {
        setVisible(true);
        setDialogWindowLocation(parent);
    }

    // Getter methods for UI components
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

    /**
     * Centres the dialogue window relative to the parent frame.
     * If the parent is null, the dialogue is placed in the centre of the screen.
     *
     * @param parent The parent frame, or null if not available.
     */
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
