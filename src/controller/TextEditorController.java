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
        initializeShortcuts();
        initializeListeners();
    }

    public void initializeShortcuts() {
        gui.getNewFileItem().setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
        gui.getSaveFileItem().setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
        gui.getSaveFileAsItem().setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
    }

    private void initializeListeners() {
        gui.getNewFileItem().addActionListener(this);
        gui.getNewFileItem().setActionCommand("new");

        gui.getSaveFileItem().addActionListener(this);
        gui.getSaveFileItem().setActionCommand("save");

        gui.getSaveFileAsItem().addActionListener(this);
        gui.getSaveFileAsItem().setActionCommand("save as");
        //gui.getUndoItem().addActionListener(this);
       // gui.getSearchWordItem().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Object source = e.getSource();
        String actionCommand = e.getActionCommand();

        switch (actionCommand) {
            case "new":
                gui.getTextArea().setText("");
                break;
            case "save":
                System.out.println("\"Speichern\"-Funktion noch nicht implementiert.");
                break;
            case "save as":
                System.out.println("\"Speichern unter\"-Funktion noch nicht implementiert.");
            default:
                break;
        }

        /*if (source == gui.getNewFileItem()) {
            gui.getTextArea().setText("");
        }

        if (source == gui.getSaveFileItem()) {
            System.out.println("'Speichern'-Funktion noch nicht implementiert.");
        }*/
    }
}
