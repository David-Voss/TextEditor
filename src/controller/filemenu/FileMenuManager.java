package controller.filemenu;

import controller.TextEditorMainController;
import gui.TextEditorMainGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileMenuManager {

    private final TextEditorMainGUI gui;
    private final TextEditorMainController mainController;
    private File currentFile = null;

    public FileMenuManager(TextEditorMainGUI gui, TextEditorMainController mainController) {
        this.gui = gui;
        this.mainController = mainController;
    }

    //// 'File' menu methods / functions

    public void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Datei öffnen");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Textdateien (*.txt)", "txt"));

        int userSelection = fileChooser.showOpenDialog(gui);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();

            try {
                String content = new String(java.nio.file.Files.readAllBytes(currentFile.toPath()));
                gui.getTextArea().setText(content);
                mainController.updateTitle(currentFile);

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

    public void createNewFile() {
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
        mainController.updateTitle(currentFile);
    }

    public void saveFile() {
        if (currentFile == null) {
            saveFileAs();
        } else {
            writeFile(currentFile);
        }
    }

    public void saveFileAs() {
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
            mainController.updateTitle(currentFile);

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

    public void printDocument() {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setJobName(gui.getTitle());

        printerJob.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex > 0) {
                    return Printable.NO_SUCH_PAGE;
                }

                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                try {
                    // Prints the entire content
                    JTextArea textArea = gui.getTextArea();
                    textArea.printAll(graphics);
                } catch (Exception exception) {
                    return Printable.NO_SUCH_PAGE;
                }
                return Printable.PAGE_EXISTS;
            }
        });

        boolean canPrint = printerJob.printDialog(); // Display print dialogue
        if (canPrint) {
            try {
                printerJob.print();
            } catch (PrinterException exception) {
                JOptionPane.showMessageDialog(
                        gui, "Druckfehler:\n" + exception.getMessage(),
                        "Druckfehler",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
