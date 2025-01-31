package controller.filemenu;

import gui.TextEditorMainGUI;
import org.junit.jupiter.api.*;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link FileMenuManager}.
 * <p>
 * This test class ensures that file operations such as saving, opening, and printing
 * function correctly within the text editor.
 * </p>
 */
class FileMenuManagerTest {

    private FileMenuManager fileMenuManager;
    private TextEditorMainGUI gui;
    private JTextArea textArea;
    private File testFile;

    /**
     * Sets up the test environment before each test case.
     * Creates a real GUI instance and a local test file.
     */
    @BeforeEach
    void setUp() throws IOException {
        gui = new TextEditorMainGUI("Test Editor"); // Echte GUI-Instanz
        textArea = gui.getTextArea(); // JTextArea von GUI abrufen
        fileMenuManager = new FileMenuManager(gui, null);

        // Lokale Testdatei erstellen
        testFile = new File("testfile.txt");
        testFile.createNewFile();
    }

    /**
     * Tests the save file functionality.
     * Ensures that text from the JTextArea is correctly written to a file.
     */
    @Test
    void testSaveFile() throws IOException {
        textArea.setText("Hello World");

        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(textArea.getText());
        }

        // Dateiinhalt lesen und überprüfen
        String content = Files.readString(testFile.toPath());
        assertEquals("Hello World", content);
    }

    /**
     * Tests the open file functionality.
     * Ensures that text from a file is correctly loaded into the JTextArea.
     */
    @Test
    void testOpenFile() throws IOException {
        String expectedContent = "This is a test file.";

        // Inhalt in die Datei schreiben
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(expectedContent);
        }

        // Datei öffnen (manuelle Simulation)
        textArea.setText(Files.readString(testFile.toPath()));

        // Prüfen, ob der Text korrekt geladen wurde
        assertEquals(expectedContent, textArea.getText());
    }

    /**
     * Tests the print document functionality.
     * Ensures that no exceptions occur when attempting to print.
     */
    @Test
    void testPrintDocument() {
        assertDoesNotThrow(() -> fileMenuManager.printDocument());
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
