import controller.TextEditorMainController;
import gui.TextEditorMainGUI;


/**
 * The main entry point for the Text Editor application.
 * This class initialises the graphical user interface (GUI)
 * and its corresponding controller.
 */
public class TextEditorMain {
    public static void main(String[] args) {
        new RunTextEditor();
    }

    /**
     * Inner class responsible for initialising the GUI and controller.
     */
    public static class RunTextEditor {

        public RunTextEditor() {
            // Initialise the graphical user interface with a default title
            TextEditorMainGUI gui = new TextEditorMainGUI("Texteditor | Unbenannt");
            // Create and bind the controller to handle logic and events
            new TextEditorMainController(gui);
        }
    }
}