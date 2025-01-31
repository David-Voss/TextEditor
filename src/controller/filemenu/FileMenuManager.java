package controller.filemenu;

import controller.TextEditorMainController;
import gui.TextEditorMainGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.print.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Manages file-related actions such as creating, opening, saving, and printing files.
 * Handles user interactions from the file menu and updates the editor accordingly.
 */
public class FileMenuManager {

    private final TextEditorMainGUI gui;
    private final TextEditorMainController mainController;
    private final JFileChooser fileChooser;
    private File currentFile = null;

    /**
     * Constructs the file menu manager.
     *
     * @param gui            The main GUI of the text editor.
     * @param mainController The main controller handling application logic.
     */
    public FileMenuManager(TextEditorMainGUI gui, TextEditorMainController mainController) {
        this.gui = gui;
        this.mainController = mainController;
        this.fileChooser = new JFileChooser();
        FileChooserConfigurator.configureFileChooser(fileChooser);
    }

    /**
     * Creates a new file, prompting the user to confirm if unsaved changes exist.
     * This method remains unchanged to preserve its intended behaviour.
     */
    public void createNewFile() {
        if (!gui.getTextArea().getText().isEmpty()) {
            Object[] options = {"Ja", "Nein"};
            JOptionPane optionPane = new JOptionPane(
                    "Ungespeicherte Änderungen gehen verloren. Fortfahren?",
                    JOptionPane.WARNING_MESSAGE,
                    JOptionPane.YES_NO_OPTION,
                    null,
                    options,
                    options[1] // Default to "Nein"
            );

            JDialog dialog = optionPane.createDialog(gui, "Neue Datei erstellen");
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            SwingUtilities.invokeLater(() -> {
                JButton noButton = getButton(dialog, "Nein");
                if (noButton != null) {
                    noButton.requestFocusInWindow();
                    dialog.getRootPane().setDefaultButton(noButton);
                }
            });

            dialog.getRootPane().registerKeyboardAction(
                    e -> {
                        JButton focusedButton = (dialog.getFocusOwner() instanceof JButton)
                                ? (JButton) dialog.getFocusOwner()
                                : getButton(dialog, "Nein");
                        if (focusedButton != null) {
                            focusedButton.doClick();
                        }
                    },
                    KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                    JComponent.WHEN_IN_FOCUSED_WINDOW
            );

            dialog.setVisible(true);

            Object selectedValue = optionPane.getValue();
            if ("Ja".equals(selectedValue)) {
                resetEditor();
            }
        } else {
            resetEditor();
        }
    }

    /**
     * Resets the editor by clearing the text area and resetting the current file reference.
     */
    private void resetEditor() {
        gui.getTextArea().setText("");
        currentFile = null;
        mainController.updateTitle(null);
    }

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

        if (fileChooser.showOpenDialog(gui) == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            try {
                String content = new String(java.nio.file.Files.readAllBytes(currentFile.toPath()));
                gui.getTextArea().setText(content);
                mainController.updateTitle(currentFile);
            } catch (IOException exception) {
                showErrorDialog("Fehler beim Öffnen der Datei", exception);
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

        if (fileChooser.showSaveDialog(gui) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().contains(".")) {
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
            showInfoDialog("Speichern erfolgreich", "Datei erfolgreich gespeichert:\n" + file.getAbsolutePath());
        } catch (IOException exception) {
            showErrorDialog("Fehler beim Speichern der Datei", exception);
        }
    }

    /**
     * Prints the current document.
     * Displays a print dialogue and sends the document to the printer.
     */
    public void printDocument() {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setJobName(gui.getTitle());

        printerJob.setPrintable((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }

            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

            try {
                gui.getTextArea().printAll(graphics);
            } catch (Exception exception) {
                return Printable.NO_SUCH_PAGE;
            }
            return Printable.PAGE_EXISTS;
        });

        boolean canPrint = printerJob.printDialog(); // Display print dialogue
        if (canPrint) {
            try {
                printerJob.print();
            } catch (PrinterException exception) {
                showErrorDialog("Druckfehler", exception);
            }
        }
    }

    /**
     * Displays an error message dialogue.
     *
     * @param title     The title of the error message.
     * @param exception The exception containing the error details.
     */
    private void showErrorDialog(String title, Exception exception) {
        JOptionPane.showMessageDialog(gui, title + ":\n" + exception.getMessage(),
                "Fehler", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays an information message dialogue.
     *
     * @param title   The title of the message.
     * @param message The content of the message.
     */
    private void showInfoDialog(String title, String message) {
        JOptionPane.showMessageDialog(gui, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
