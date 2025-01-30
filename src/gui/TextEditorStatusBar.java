package gui;

import javax.swing.*;
import java.awt.*;

public class TextEditorStatusBar extends JPanel {
    private final JLabel cursorPositionLabel;
    private final JLabel wordCountLabel;
    private final JLabel charCountLabel;

    public TextEditorStatusBar() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS)); // Horizontal ausrichten
        setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5)); // Innenabstand

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

    private void addSeparator() {
        add(Box.createHorizontalStrut(10)); // Abstand
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(2, 15)); // Breite & Höhe setzen
        add(separator);
        add(Box.createHorizontalStrut(10)); // Abstand
    }

    public void updateCursorPosition(int line, int column) {
        cursorPositionLabel.setText("Ze " + line + ", Sp " + column);
    }

    public void updateTextInfo(int charCount, int wordCount) {
        String wordLabel = (wordCount == 1) ? "1 Wort" : wordCount + " Wörter";
        wordCountLabel.setText(wordLabel);
        charCountLabel.setText(charCount + " Zeichen");
    }
}
