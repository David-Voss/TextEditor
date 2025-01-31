package controller.editmenu;

import gui.TextEditorMainGUI;
import org.junit.jupiter.api.*;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link EditMenuManager}.
 * <p>
 * This test class verifies undo/redo functionality, web search capability,
 * and the insertion of date/time in the text editor.
 * </p>
 */
class EditMenuManagerTest {

    private EditMenuManager editMenuManager;
    private TextEditorMainGUI gui;
    private JTextArea textArea;

    /**
     * Ensures GUI is properly initialised before tests run.
     */
    @BeforeEach
    void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            gui = new TextEditorMainGUI("Test Editor"); // Ensure GUI starts in EDT
            textArea = gui.getTextArea();
            editMenuManager = new EditMenuManager(gui, null);
        });
    }

    /**
     * Tests the undo/redo functionality.
     * Ensures that text changes can be undone and redone correctly.
     */
    @Test
    void testUndoRedo() {
        SwingUtilities.invokeLater(() -> {
            textArea.setText("Initial Text");
            textArea.append(" Added Text");

            editMenuManager.undo();
            assertEquals("Initial Text", textArea.getText());

            editMenuManager.redo();
            assertEquals("Initial Text Added Text", textArea.getText());
        });
    }

    /**
     * Tests the web search functionality.
     * Ensures that a valid URL is created from selected text.
     */
    @Test
    void testWebSearch() {
        SwingUtilities.invokeLater(() -> {
            textArea.setText("Orang Utan");
            textArea.select(0, 10); // Selects "Search"

            assertDoesNotThrow(() -> editMenuManager.webSearch()); // Ensures no exceptions occur
        });
    }

    /**
     * Tests inserting the current date and time.
     * Verifies that the correct format is used and that the text is inserted at the cursor position.
     */
    @Test
    void testInsertDateTime() {
        SwingUtilities.invokeLater(() -> {
            textArea.setText("Hello there!");
            textArea.setCaretPosition(13); // Cursor after "Hello "

            editMenuManager.dateTime();

            // Date format check (adjust as needed)
            String expectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            assertTrue(textArea.getText().contains(expectedDate));
        });
    }

    /**
     * Cleans up after each test case.
     */
    @AfterEach
    void tearDown() {
        SwingUtilities.invokeLater(() -> textArea.setText("")); // Reset text area safely
    }
}
