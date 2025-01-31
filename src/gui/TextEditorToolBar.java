package gui;

import javax.swing.*;
import java.awt.*;

/**
 * The toolbar for the text editor.
 * This class extends {@link JToolBar} and provides quick-access buttons
 * for common actions such as file operations, undo/redo, printing,
 * web search, and text search.
 */
public class TextEditorToolBar extends JToolBar{

    private final JButton newFileButton;
    private final JButton openFileButton;
    private final JButton saveFileButton;
    private final JButton printDocumentButton;
    private final JButton undoButton;
    private final JButton redoButton;
    private final JButton webSearchButton;
    private final JTextField searchField;

    /**
     * Constructs the toolbar and initialises all buttons and components.
     */
    public TextEditorToolBar() {
        setFloatable(false); // Prevents the toolbar from being moved

        // Initialising buttons with icons and tooltips
        this.newFileButton = createButton("assets/icons/file-regular.png", "Neues Dokument erstellen");
        add(newFileButton);

        this.openFileButton = createButton("assets/icons/folder-open-regular.png", "Datei öffnen");
        add(openFileButton);

        this.saveFileButton = createButton("assets/icons/floppy-disk-regular.png", "Speichern");
        add(saveFileButton);

        addSeparator();

        this.printDocumentButton = createButton("assets/icons/print-solid.png", "Drucken");
        add(printDocumentButton);

        addSeparator();

        this.undoButton = createButton("assets/icons/rotate-left-solid.png", "Rückgängig");
        add(undoButton);

        this.redoButton = createButton("assets/icons/rotate-right-solid.png", "Wiederherstellen");
        add(redoButton);

        addSeparator();

        this.webSearchButton = createButton("assets/icons/globe-solid.png", "Markierten Text im Internet suchen");
        add(webSearchButton);

        addSeparator();

        // Creating the search field with an icon
        this.searchField = new JTextField(15);
        searchField.setMaximumSize(new Dimension(200, 25));
        searchField.setToolTipText("Suchen");

        JLabel searchIcon = new JLabel(loadIcon("assets/icons/magnifying-glass-solid.png", 24, 24));
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        add(searchPanel);
    }

    // Getter methods for toolbar buttons and components
    public JButton getNewFileButton() { return newFileButton; }
    public JButton getOpenFileButton() { return openFileButton; }
    public JButton getSaveFileButton() { return saveFileButton; }
    public JButton getPrintDocumentButton() { return printDocumentButton; }
    public JButton getUndoButton() { return undoButton; }
    public JButton getRedoButton() { return redoButton; }
    public JButton getWebSearchButton() { return webSearchButton; }
    public JTextField getSearchField() { return searchField; }

    /**
     * Creates a button with an icon and tooltip.
     *
     * @param iconPath The path to the icon file.
     * @param tooltip  The tooltip text for the button.
     * @return The created JButton.
     */
    private JButton createButton(String iconPath, String tooltip) {
        JButton button = new JButton(loadIcon(iconPath, 24, 24));
        button.setToolTipText(tooltip);
        return button;
    }

    /**
     * Loads and scales an icon to the specified dimensions.
     *
     * @param path  The file path of the icon.
     * @param width The desired width.
     * @param height The desired height.
     * @return A scaled {@link ImageIcon}.
     */
    private ImageIcon loadIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}
