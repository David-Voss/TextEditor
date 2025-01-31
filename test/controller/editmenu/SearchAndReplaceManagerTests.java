package controller.editmenu;

import gui.SearchAndReplaceDialogWindow;
import gui.TextEditorMainGUI;
import org.junit.jupiter.api.*;

import javax.swing.*;
import javax.swing.text.Highlighter;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link SearchAndReplaceManager}.
 * <p>
 * This test class verifies the search, replace, and highlight functionality
 * within the text editor.
 * </p>
 */
class SearchAndReplaceManagerTest {

    private SearchAndReplaceManager searchAndReplaceManager;
    private TextEditorMainGUI gui;
    private JTextArea textArea;
    private SearchAndReplaceDialogWindow dialogWindow;

    /**
     * Ensures GUI is properly initialised before tests run.
     */
    @BeforeEach
    void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            gui = new TextEditorMainGUI("Test Editor");
            textArea = gui.getTextArea();
            dialogWindow = new SearchAndReplaceDialogWindow(gui);
            searchAndReplaceManager = new SearchAndReplaceManager(gui, dialogWindow);
        });
    }

    /**
     * Tests the search functionality.
     * Ensures that a term can be found and highlighted.
     */
    @Test
    void testSearch() {
        SwingUtilities.invokeLater(() -> {
            textArea.setText("This is a sample text with a keyword.");
            int index = searchAndReplaceManager.search("keyword", false);

            assertTrue(index >= 0, "Search should find the term.");
            assertEquals(28, index, "The term should be found at index 28.");
        });
    }

    /**
     * Tests the replace functionality.
     * Ensures that a found term is correctly replaced.
     */
    @Test
    void testReplace() {
        SwingUtilities.invokeLater(() -> {
            textArea.setText("Replace this test with an example.");
            searchAndReplaceManager.replace("test", "example", false);

            assertEquals("Replace this example with an example.", textArea.getText());
        });
    }

    /**
     * Tests the highlighting functionality.
     * Ensures that searched terms are correctly highlighted.
     */
    @Test
    void testHighlightText() {
        SwingUtilities.invokeLater(() -> {
            textArea.setText("Highlight this word.");
            searchAndReplaceManager.search("Highlight", false);

            Highlighter.Highlight[] highlights = textArea.getHighlighter().getHighlights();
            assertTrue(highlights.length > 0, "Text should be highlighted.");
        });
    }

    /**
     * Cleans up after each test case.
     */
    @AfterEach
    void tearDown() {
        SwingUtilities.invokeLater(() -> {
            textArea.setText("");
            searchAndReplaceManager.clearHighlights();
        });
    }
}
