package controller;

import gui.TextEditorGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

public class TextEditorController implements ActionListener {

    private final TextEditorGUI gui;

    public TextEditorController(TextEditorGUI gui) {
        this.gui = gui;
        shortcuts();
        initializeListeners();
    }

    public void shortcuts() {
        gui.getNewFileItem().setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
    }

    private void initializeListeners() {
        gui.getNewFileItem().addActionListener(this);
        gui.getSaveFileItem().addActionListener(this);
        gui.getUndoItem().addActionListener(this);
        gui.getSearchWordItem().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == gui.getNewFileItem()) {
            gui.getTextArea().setText("");
        }

        if (source == gui.getSaveFileItem()) {
            System.out.println("'Speichern'-Funktion noch nicht implementiert.");
        }
    }
}
