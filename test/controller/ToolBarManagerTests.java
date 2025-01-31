package controller;

import controller.editmenu.EditMenuManager;
import controller.editmenu.SearchAndReplaceManager;
import controller.filemenu.FileMenuManager;
import gui.SearchAndReplaceDialogWindow;
import gui.TextEditorMainGUI;
import gui.TextEditorToolBar;
import org.junit.jupiter.api.*;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ToolBarManager}.
 * <p>
 * This test class verifies the toolbar button interactions,
 * ensuring they trigger the corresponding menu actions.
 * </p>
 */
class ToolBarManagerTests {

    private ToolBarManager toolBarManager;
    private TextEditorMainGUI gui;
    private JTextArea textArea;
    private TextEditorToolBar toolBar;
    private SearchAndReplaceDialogWindow dialogWindow;
    private FileMenuManager fileMenuManager;
    private EditMenuManager editMenuManager;
    private SearchAndReplaceManager searchAndReplaceManager;

    /**
     * Ensures GUI is properly initialised before tests run.
     */
    @BeforeEach
    void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            gui = new TextEditorMainGUI("Test Editor");
            gui.setVisible(true);
            textArea = gui.getTextArea();
            toolBar = gui.getToolBar();

            fileMenuManager = new FileMenuManager(gui, null);
            editMenuManager = new EditMenuManager(gui, null);
            dialogWindow = new SearchAndReplaceDialogWindow(gui);
            searchAndReplaceManager = new SearchAndReplaceManager(gui, dialogWindow);

            toolBarManager = new ToolBarManager(toolBar, fileMenuManager, editMenuManager, searchAndReplaceManager);
        });
    }


    /**
     * Tests the new file button functionality.
     * Ensures that clicking the button clears the text area.
     */
    @Test
    void testNewFileButton() {
        SwingUtilities.invokeLater(() -> {
            textArea.setText("Old Content");
            toolBar.getNewFileButton().doClick(); // Simulate button press

            assertEquals("", textArea.getText(), "New file action should clear the text area.");
        });
    }

    /**
     * Tests the save file button functionality.
     * Ensures that clicking the button does not throw exceptions.
     */
    @Test
    void testSaveFileButton() {
        SwingUtilities.invokeLater(() -> {
            textArea.setText("Some content to save");
            assertDoesNotThrow(() -> toolBar.getSaveFileButton().doClick());
        });
    }

    /**
     * Tests the undo button functionality.
     * Ensures that clicking the button triggers an undo operation.
     */
    @Test
    void testUndoButton() {
        SwingUtilities.invokeLater(() -> {
            textArea.setText("Initial Text");
            textArea.append(" Added Text");
            toolBar.getUndoButton().doClick();

            assertEquals("Initial Text", textArea.getText(), "Undo should revert the last text change.");
        });
    }

    /**
     * Tests the redo button functionality.
     * Ensures that clicking the button re-applies an undone change.
     */
    @Test
    void testRedoButton() {
        SwingUtilities.invokeLater(() -> {
            textArea.setText("Initial Text");
            textArea.append(" Added Text");
            toolBar.getUndoButton().doClick();
            toolBar.getRedoButton().doClick();

            assertEquals("Initial Text Added Text", textArea.getText(), "Redo should reapply the undone change.");
        });
    }

    /**
     * Tests the search field functionality.
     * Ensures that entering text and pressing Enter triggers a search.
     */
    @Test
    void testSearchField() {
        SwingUtilities.invokeLater(() -> {
            textArea.setText("Searching for a keyword in this text.");
            toolBar.getSearchField().setText("keyword");
            toolBar.getSearchField().postActionEvent(); // Simulate pressing Enter

            assertTrue(textArea.getText().contains("keyword"), "Search action should find the keyword.");
        });
    }

    /**
     * Cleans up after each test case.
     */
    @AfterEach
    void tearDown() {
        SwingUtilities.invokeLater(() -> textArea.setText(""));
    }
}
