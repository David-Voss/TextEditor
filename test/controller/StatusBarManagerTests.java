package controller;

import gui.TextEditorMainGUI;
import gui.TextEditorStatusBar;
import org.junit.jupiter.api.*;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link StatusBarManager}.
 * <p>
 * This test class ensures that the status bar updates correctly when the text area changes.
 * It verifies cursor position, word count, and character count updates.
 * </p>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Ensures tests run in order
class StatusBarManagerTest {

    private StatusBarManager statusBarManager;
    private TextEditorMainGUI gui;
    private JTextArea textArea;
    private TextEditorStatusBar statusBar;

    /**
     * Ensures GUI is properly initialised before tests run.
     */
    @BeforeEach
    void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            gui = new TextEditorMainGUI("Test Editor");
            gui.setVisible(true); // 💡 GUI sichtbar machen
            textArea = gui.getTextArea();
            statusBar = new TextEditorStatusBar();
            statusBarManager = new StatusBarManager(statusBar, textArea);
        });
    }

    /**
     * Tests if the cursor position is updated correctly.
     */
    @Test
    @Order(1)
    void testCursorPositionUpdate() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            textArea.setText("Hello\nWorld");
            textArea.setCaretPosition(8); // Cursor auf "r" in "World"

            statusBarManager.updateCursorPosition(2, 3);
            assertEquals("Ze 2, Sp 3", statusBar.getCursorPositionLabel().getText());
        });
    }

    /**
     * Tests if the character and word count update correctly.
     */
    @Test
    @Order(2)
    void testTextInfoUpdate() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            textArea.setText("This is a test.");
            statusBarManager.updateTextInfo(15, 4);

            assertEquals("15 Zeichen", statusBar.getCharCountLabel().getText());
            assertEquals("4 Wörter", statusBar.getWordCountLabel().getText());
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
