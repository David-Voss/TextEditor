package gui;

import javax.swing.*;
import java.awt.*;

public class TextEditorToolBar extends JToolBar{
    private JButton newFileButton;
    private JButton openFileButton;
    private JButton saveFileButton;
    private JButton printDocumentButton;
    private JButton undoButton;
    private JButton redoButton;
    private JButton webSearchButton;
    private final JTextField searchField;



    public TextEditorToolBar() {
        setFloatable(false); // Toolbar nicht beweglich machen

        // Icons laden (Platzhalter, ggf. ersetzen)
        this.newFileButton = new JButton(loadIcon("assets/icons/file-regular.png", 24, 24));
        newFileButton.setToolTipText("Neues Dokument erstellen");
        add(newFileButton);

        this.openFileButton = new JButton(loadIcon("assets/icons/folder-open-regular.png", 24, 24));
        openFileButton.setToolTipText("Datei Öffnen");
        add(openFileButton);

        this.saveFileButton = new JButton(loadIcon("assets/icons/floppy-disk-regular.png", 24, 24));
        saveFileButton.setToolTipText("Speichern");
        add(saveFileButton);

        addSeparator();

        this.printDocumentButton = new JButton(loadIcon("assets/icons/print-solid.png", 24, 24));
        printDocumentButton.setToolTipText("Drucken");
        add(printDocumentButton);

        addSeparator();

        this.undoButton = new JButton(loadIcon("assets/icons/rotate-left-solid.png", 24, 24));
        undoButton.setToolTipText("Rückgängig");
        add(undoButton);

        this.redoButton = new JButton(loadIcon("assets/icons/rotate-right-solid.png", 24, 24));
        redoButton.setToolTipText("Wiederherstellen");
        add(redoButton);

        addSeparator();

        this.webSearchButton = new JButton(loadIcon("assets/icons/globe-solid.png", 24, 24));
        webSearchButton.setToolTipText("Markierten Text im Internet suchen");
        add(webSearchButton);

        addSeparator();

        this.searchField = new JTextField(15);
        searchField.setMaximumSize(new Dimension(200, 25)); // Feste Größe für das Suchfeld
        searchField.setToolTipText("Suchen");

        // Lupensymbol ins Suchfeld setzen
        JLabel searchIcon = new JLabel(loadIcon("assets/icons/magnifying-glass-solid.png", 24, 24));
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        add(searchPanel);
    }

    public JButton getNewFileButton() { return newFileButton; }
    public JButton getOpenFileButton() { return openFileButton; }
    public JButton getSaveFileButton() { return saveFileButton; }
    public JButton getPrintDocumentButton() { return printDocumentButton; }
    public JButton getUndoButton() { return undoButton; }
    public JButton getRedoButton() { return redoButton; }
    public JButton getWebSearchButton() { return webSearchButton; }
    public JTextField getSearchField() { return searchField; }

    private ImageIcon loadIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}
