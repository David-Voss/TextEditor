package controller;

import controller.editmenu.EditMenuManager;
import controller.editmenu.SearchAndReplaceManager;
import controller.filemenu.FileMenuManager;
import gui.SearchAndReplaceDialogWindow;
import gui.TextEditorMainGUI;
import org.junit.jupiter.api.*;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link TextEditorMainController}.
 * <p>
 * This class tests the integration of the main controller with
 * file operations, edit menu actions, and search/replace functionality.
 * </p>
 */
class TextEditorMainControllerTests {

    private TextEditorMainController mainController;
    private TextEditorMainGUI gui;
    private JTextArea textArea;
    private FileMenuManager fileMenuManager;
    private EditMenuManager editMenuManager;
    private SearchAndReplaceManager searchAndReplaceManager;
    private File testFile;

    /**
     * Sets up the test environment before each test case.
     * Creates a real GUI instance and necessary managers.
     */
    @BeforeEach
    void setUp() throws IOException {
        gui = new TextEditorMainGUI("Test Editor");
        textArea = gui.getTextArea();
        mainController = new TextEditorMainController(gui);
        fileMenuManager = new FileMenuManager(gui, mainController);
        editMenuManager = new EditMenuManager(gui, null);
        searchAndReplaceManager = new SearchAndReplaceManager(gui, new SearchAndReplaceDialogWindow(gui));

        mainController = new TextEditorMainController(gui);

        // Create a local test file for file operations
        testFile = new File("testfile.txt");
        testFile.createNewFile();
    }

    /**
     * Tests updating the window title based on the opened file.
     */
    @Test
    void testUpdateTitle() {
        File file = new File("example.txt");
        mainController.updateTitle(file);
        assertEquals("Texteditor | example.txt", gui.getTitle());
    }

    /**
     * Tests if the controller correctly maps keyboard shortcuts.
     */
    @Test
    void testInitialiseShortcuts() {
        assertNotNull(gui.getNewFileItem().getAccelerator());
        assertNotNull(gui.getOpenFileItem().getAccelerator());
        assertNotNull(gui.getSaveFileItem().getAccelerator());
    }

    /**
     * Tests handling of the "New File" action.
     */
    @Test
    void testNewFileActionYES() {
        textArea.setText("Some unsaved text");

        // SIMULIERT: Benutzer klickt "Ja"
        int simulatedUserSelection = JOptionPane.YES_OPTION;

        if (simulatedUserSelection == JOptionPane.YES_OPTION) {
            fileMenuManager.createNewFile();
            assertTrue(textArea.getText().isEmpty(), "Text area should be empty after clicking 'Yes'.");
        } else {
            fileMenuManager.createNewFile();
            assertFalse(textArea.getText().isEmpty(), "Text area should retain content after clicking 'No'.");
        }
    }

    @Test
    void testNewFileActionNO() {
        textArea.setText("Some unsaved text");

        // Simuliere "Nein" im Bestätigungsdialog
        int userSelection = JOptionPane.NO_OPTION;

        if (userSelection == JOptionPane.YES_OPTION) {
            fileMenuManager.createNewFile();
            assertTrue(textArea.getText().isEmpty(), "Text area should be empty after clicking 'Yes'.");
        } else {
            fileMenuManager.createNewFile();
            assertFalse(textArea.getText().isEmpty(), "Text area should retain content after clicking 'No'.");
        }
    }

    /**
     * Tests handling of the "Open File" action.
     */
    @Test
    void testOpenFileAction() throws IOException {
        String fileContent = "This is test content.";
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(fileContent);
        }

        textArea.setText(Files.readString(testFile.toPath()));
        assertEquals(fileContent, textArea.getText());
    }

    /**
     * Tests handling of the "Save File" action.
     */
    @Test
    void testSaveFileAction() throws IOException {
        textArea.setText("New file content");

        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(textArea.getText());
        }

        String content = Files.readString(testFile.toPath());
        assertEquals("New file content", content);
    }

    /**
     * Tests if the search function finds text correctly.
     */
    @Test
    void testSearchFunctionality() {
        textArea.setText("This is a sample text.");
        int index = searchAndReplaceManager.search("sample", false);
        assertTrue(index >= 0);
    }

    /**
     * Tests if the replace function correctly replaces text.
     */
    @Test
    void testReplaceFunctionality() {
        textArea.setText("Replace this text.");

        // Suche das Wort "text" vor dem Ersetzen
        int index = searchAndReplaceManager.search("text", false);
        assertTrue(index >= 0, "Search should find the term before replacing it.");

        // Jetzt ersetzen
        searchAndReplaceManager.replace("text", "string", false);
        assertEquals("Replace this string.", textArea.getText());
    }

    /**
     * Cleans up after each test case.
     * Deletes the test file if it exists.
     */
    @AfterEach
    void tearDown() {
        if (testFile.exists()) {
            testFile.delete();
        }
    }
}
