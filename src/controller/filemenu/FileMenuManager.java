package controller.filemenu;

import controller.TextEditorMainController;
import gui.TextEditorMainGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
    private JFileChooser fileChooser;
    private File currentFile = null;

    public FileMenuManager(TextEditorMainGUI gui, TextEditorMainController mainController) {
        this.gui = gui;
        this.mainController = mainController;
        fileChooser = new JFileChooser();
        FileChooserConfigurator.configureFileChooser(fileChooser);
    }

    public void createNewFile() {
        if (!gui.getTextArea().getText().isEmpty()) {
            Object[] options = {"Ja", "Nein"};
            JOptionPane optionPane = new JOptionPane(
                    "Ungespeicherte Änderungen gehen verloren. Fortfahren?",
                    JOptionPane.WARNING_MESSAGE,
                    JOptionPane.YES_NO_OPTION,
                    null,
                    options,
                    options[1] // Standardmäßig "Nein" vorausgewählt
            );

            JDialog dialog = optionPane.createDialog(gui, "Neue Datei erstellen");
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            // Standardmäßig "Nein" als Default-Button setzen
            SwingUtilities.invokeLater(() -> {
                JButton noButton = getButton(dialog, "Nein");
                if (noButton != null) {
                    noButton.requestFocusInWindow();
                    dialog.getRootPane().setDefaultButton(noButton);
                }
            });

            // Enter-Handling: Bestätigt den aktuell fokussierten Button
            dialog.getRootPane().registerKeyboardAction(
                    e -> {
                        JButton focusedButton = (dialog.getFocusOwner() instanceof JButton)
                                ? (JButton) dialog.getFocusOwner()
                                : getButton(dialog, "Nein"); // Falls kein Button fokussiert ist, "Nein" nehmen
                        if (focusedButton != null) {
                            focusedButton.doClick();
                        }
                    },
                    KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                    JComponent.WHEN_IN_FOCUSED_WINDOW
            );

            dialog.setVisible(true);

            // Ergebnis auswerten
            Object selectedValue = optionPane.getValue();
            if ("Ja".equals(selectedValue)) {
                resetEditor();
            }
        } else {
            resetEditor();
        }
    }

    private void resetEditor() {
        gui.getTextArea().setText("");
        currentFile = null;
        mainController.updateTitle(null);
    }

    // Hilfsmethode, um einen Button anhand seines Textes zu finden
    private JButton getButton(JDialog dialog, String buttonText) {
        for (Component comp : dialog.getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                for (Component btn : ((JPanel) comp).getComponents()) {
                    if (btn instanceof JButton && ((JButton) btn).getText().equals(buttonText)) {
                        return (JButton) btn;
                    }
                }
            }
        }
        return null;
    }

    public void openFile() {
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

    public void saveFile() {
        if (currentFile == null) {
            saveFileAs();
        } else {
            writeFile(currentFile);
        }
    }

    public void saveFileAs() {
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
