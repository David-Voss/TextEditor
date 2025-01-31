package gui;

import javax.swing.*;
import java.awt.*;

/**
 * The status bar for the text editor.
 * This class extends {@link JPanel} and provides real-time information
 * about the cursor position, character count, and word count.
 */
public class TextEditorStatusBar extends JPanel {

    private final JLabel cursorPositionLabel;
    private final JLabel wordCountLabel;
    private final JLabel charCountLabel;

    /**
     * Constructs the status bar and initialises all labels.
     */
    public TextEditorStatusBar() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS)); // Align horizontally
        setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5)); // Internal spacing

        cursorPositionLabel = new JLabel("Ze 1, Sp 1");
        add(cursorPositionLabel);
        addSeparator();

        charCountLabel = new JLabel("0 Zeichen");
        add(charCountLabel);
        addSeparator();

        wordCountLabel = new JLabel("0 Wörter");
        add(wordCountLabel);
        addSeparator();
    }

    /**
     * Adds a vertical separator with spacing to the status bar.
     */
    private void addSeparator() {
        add(Box.createHorizontalStrut(10)); // Adds spacing
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(2, 15)); // Set width & height
        add(separator);
        add(Box.createHorizontalStrut(10)); // Adds spacing
    }

    /**
     * Updates the cursor position display.
     *
     * @param line   The current line number.
     * @param column The current column number.
     */
    public void updateCursorPosition(int line, int column) {
        cursorPositionLabel.setText("Ze " + line + ", Sp " + column);
    }

    /**
     * Updates the character and word count display.
     *
     * @param charCount The number of characters in the document.
     * @param wordCount The number of words in the document.
     */
    public void updateTextInfo(int charCount, int wordCount) {
        String wordLabel = (wordCount == 1) ? "1 Wort" : wordCount + " Wörter";
        wordCountLabel.setText(wordLabel);
        charCountLabel.setText(charCount + " Zeichen");
    }

    // Status bar getter
    public JLabel getCursorPositionLabel() { return cursorPositionLabel; }
    public JLabel getCharCountLabel() { return charCountLabel; }
    public JLabel getWordCountLabel() { return wordCountLabel; }
}
