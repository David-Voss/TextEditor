package controller;

import gui.TextEditorGUI;
import gui.SearchAndReplaceDialogWindow;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class TextEditorController implements ActionListener {

    private final TextEditorGUI gui;
    private File currentFile = null;
    private final UndoManager undoManager = new UndoManager();
    private final SearchAndReplaceDialogWindow searchAndReplaceDialogWindow;
    private final SearchAndReplaceManager searchAndReplaceManager;
    private final ToolBarManager toolBarManager;

    public TextEditorController(TextEditorGUI gui) {
        this.gui = gui;
        this.searchAndReplaceDialogWindow = new SearchAndReplaceDialogWindow(gui);
        this.searchAndReplaceManager = new SearchAndReplaceManager(gui, searchAndReplaceDialogWindow);
        this.toolBarManager = new ToolBarManager(gui.getToolBar(), this, searchAndReplaceManager);

        //this.statusBarManager = new StatusBarManager(gui.getStatusBar(), gui.getTextArea());
        initialiseShortcuts();
        initialiseUndoManager();
        updateUndoRedoState();
        initialiseListeners();
    }

    public void initialiseShortcuts() {
        // Shortcuts 'File' menu
        gui.getNewFileItem().setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
        gui.getOpenFileItem().setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
        gui.getSaveFileItem().setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
        gui.getSaveFileAsItem().setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        gui.getPrintDocumentItem().setAccelerator(KeyStroke.getKeyStroke('P', InputEvent.CTRL_DOWN_MASK));

        // Shortcuts 'Edit' menu
        gui.getUndoItem().setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));
        gui.getRedoItem().setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        gui.getWebSearchItem().setAccelerator(KeyStroke.getKeyStroke('F', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        gui.getSearchAndReplaceItem().setAccelerator(KeyStroke.getKeyStroke('F', InputEvent.CTRL_DOWN_MASK));
        gui.getDateTimeItem().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
    }

    private void initialiseListeners() {
        gui.getTextArea().addCaretListener(e -> updateWebSearchItemStatus());

        // Listeners 'File' menu
        gui.getNewFileItem().addActionListener(this);
        gui.getNewFileItem().setActionCommand("new");

        gui.getOpenFileItem().addActionListener(this);
        gui.getOpenFileItem().setActionCommand("open");

        gui.getSaveFileItem().addActionListener(this);
        gui.getSaveFileItem().setActionCommand("save");

        gui.getSaveFileAsItem().addActionListener(this);
        gui.getSaveFileAsItem().setActionCommand("save_as");

        gui.getPrintDocumentItem().addActionListener(this);
        gui.getPrintDocumentItem().setActionCommand("print");

        // Listeners 'Edit' menu
        gui.getUndoItem().addActionListener(this);
        gui.getUndoItem().setActionCommand("undo");

        gui.getRedoItem().addActionListener(this);
        gui.getRedoItem().setActionCommand("redo");

        gui.getWebSearchItem().addActionListener(this);
        gui.getWebSearchItem().setActionCommand("web_search");

        gui.getSearchAndReplaceItem().addActionListener(this);
        gui.getSearchAndReplaceItem().setActionCommand("search_and_replace_dialog");

        gui.getDateTimeItem().addActionListener(this);
        gui.getDateTimeItem().setActionCommand("date/time");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        switch (actionCommand) {
            // 'File' menu actions
            case "new":
                createNewFile();
                break;
            case "open":
                openFile();
                break;
            case "save":
                saveFile();
                break;
            case "save_as":
                saveFileAs();
                break;
            case "print":
                printDocument();
                break;
            // 'Edit' menu actions
            case "undo":
                undo();
                break;
            case "redo":
                redo();
                break;
            case "web_search":
                webSearch();
                break;
            case "search_and_replace_dialog":
                searchAndReplaceDialogWindow.showSearchAndReplaceDialog(gui);
                break;
            // search() / replace() -> SearchAndReplaceManager
            case "date/time":
                dateTime();
                break;
            default:
                break;
        }
    }

    private void updateTitle() {
        if (currentFile != null) {
            gui.setTitle("Texteditor | " + currentFile.getName());
        } else {
            gui.setTitle("Texteditor | Unbenannt");
        }
    }

    //// 'File' menu methods / functions

    protected void openFile() {
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

    protected void createNewFile() {
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

    protected void saveFile() {
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

    protected void printDocument() {
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

    //// 'Edit' menu methods / functions

    private void initialiseUndoManager() {
        gui.getTextArea().getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undoManager.addEdit(e.getEdit());
                updateUndoRedoState();
            }
        });
    }

    public void updateUndoRedoState() {
        gui.getUndoItem().setEnabled(undoManager.canUndo());
        gui.getRedoItem().setEnabled(undoManager.canRedo());
    }

    protected void undo() {
        if (undoManager.canUndo()) {
            undoManager.undo();
            updateUndoRedoState();
        } else {
            JOptionPane.showMessageDialog(
                    gui,
                    "Es gibt nichts zum Rückgängig machen.",
                    "Rückgängig",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    protected void redo() {
        if (undoManager.canRedo()) {
            undoManager.redo();
            updateUndoRedoState();
        } else {
            JOptionPane.showMessageDialog(
                    gui,
                    "Es gibt nichts zum Wiederherstellen.",
                    "Wiederherstellen",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    protected void webSearch() {
        String selectedText = gui.getTextArea().getSelectedText();
        String query = URLEncoder.encode(selectedText, StandardCharsets.UTF_8);
        String webSearchUrl = "https://www.google.com/search?q=" + query;

        try {
            Desktop.getDesktop().browse(URI.create(webSearchUrl));
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(
                    gui,
                    "Fehler beim Öffnen des Browsers.",
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateWebSearchItemStatus() {
        boolean hasSelection = gui.getTextArea().getSelectedText() != null;
        gui.getWebSearchItem().setEnabled(hasSelection);
    }

    private void dateTime() {
        JTextArea textArea = gui.getTextArea();
        int cursorPosition = textArea.getCaretPosition();

        // Get system locale and timezone
        Locale systemLocale = Locale.getDefault();
        ZoneId systemTimeZone = ZoneId.systemDefault();

        // Format for date & time
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(systemLocale);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(systemLocale);

        // Merge date & time
        ZonedDateTime dateTimeNow = ZonedDateTime.now(systemTimeZone);
        String dateTimeString = dateTimeNow.format(dateFormatter) + " " + dateTimeNow.format(timeFormatter);

        try {
            textArea.getDocument().insertString(cursorPosition, dateTimeString, null);
        } catch (BadLocationException exception) {
            JOptionPane.showMessageDialog(
                    gui,
                    "Fehler beim Einfügen von Datum und Uhrzeit",
                    "Datum/Uhrzeit Fehler",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}