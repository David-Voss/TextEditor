import controller.TextEditorMainController;
import gui.TextEditorMainGUI;

public class TextEditorMain {
    public static void main(String[] args) {
        new RunTextEditor();
    }

    public static class RunTextEditor {

        public RunTextEditor() {
            TextEditorMainGUI gui = new TextEditorMainGUI("Texteditor | Unbenannt");
            new TextEditorMainController(gui);
        }
    }
}