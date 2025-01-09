package gui;

import javax.swing.*;
import java.awt.*;

public class TextEditorGUI {

    JFrame frame = new JFrame("Texteditor");

    public TextEditorGUI() {
        frame.setLayout(new BorderLayout());
        frame.setSize(800,600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
