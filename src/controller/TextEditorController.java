package controller;

import gui.TextEditorGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TextEditorController implements ActionListener {

    private final TextEditorGUI gui;
    private File currentFile = null;

    public TextEditorController(TextEditorGUI gui) {
        this.gui = gui;
        initializeShortcuts();
        initializeListeners();
    }

    public void initializeShortcuts() {
        gui.getOpenFileItem().setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
        gui.getNewFileItem().setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
        gui.getSaveFileItem().setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
        gui.getSaveFileAsItem().setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));

        gui.getUndoItem().setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));
        gui.getSearchWordItem().setAccelerator(KeyStroke.getKeyStroke('F', InputEvent.CTRL_DOWN_MASK));
    }

    private void initializeListeners() {
        gui.getOpenFileItem().addActionListener(this);
        gui.getOpenFileItem().setActionCommand("open");

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
            case "open":
                openFile();
                break;
            case "new":
                createNewFile();
                break;
            case "save":
                saveFile();
                break;
            case "save as":
                saveFileAs();
                break;
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

    private void updateTitle() {
        if (currentFile != null) {
            gui.setTitle("Texteditor | " + currentFile.getName());
        } else {
            gui.setTitle("Texteditor | Unbenannt");
        }
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Datei öffnen");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Textdateien (*.txt)", "txt"));

        int userSelection = fileChooser.showOpenDialog(gui);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();

            try {
                String content = new String(java.nio.file.Files.readAllBytes(currentFile.toPath()));
                gui.getTextArea().setText(content);

                updateTitle();

                JOptionPane.showMessageDialog(
                        gui,
                        "Datei erfolgreich geöffnet: \n" + currentFile.getAbsolutePath(),
                        "Datei geöffnet",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(
                        gui,
                        "Fehler beim Öffnen der Datei:\n" + exception.getMessage(),
                        "Fehler",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void createNewFile() {
        if (!gui.getTextArea().getText().isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(
                    gui,
                    "Ungespeicherte Änderungen gehen verloren. Fortfahren?",
                    "Neue Datei erstellen",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm != JOptionPane.YES_OPTION) return;
        }
        gui.getTextArea().setText("");
        currentFile = null;
        updateTitle();
    }

    private void saveFile() {
        if (currentFile == null) {
            saveFileAs();
        } else {
            writeFile(currentFile);
        }
    }

    private void saveFileAs() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Speichern unter");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Textdateien (*.txt)", "txt"));

        int userSelection = fileChooser.showSaveDialog(gui);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            if(!selectedFile.getName().contains(".")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
            }

            currentFile = selectedFile;
            writeFile(currentFile);
        }
    }

    private void writeFile(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(gui.getTextArea().getText());

            updateTitle();

            JOptionPane.showMessageDialog(
                    gui,
                    "Datei erfolgreich gespeichert:\n" + file.getAbsolutePath(),
                    "Speichern erfolgreich",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(
                    gui,
                    "Fehler beim Speichern der Datei:\n" + exception.getMessage(),
                    "Speicherfehler",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}