import controller.TextEditorController;
import gui.TextEditorGUI;

public class TextEditorMain {
    public static void main(String[] args) {
        new RunTextEditor();
    }

    public static class RunTextEditor {

        public RunTextEditor() {
            TextEditorGUI gui = new TextEditorGUI("Texteditor | Unbenannt");
            new TextEditorController(gui);
        }
    }
}