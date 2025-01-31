package controller.filemenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FileChooserConfigurator {

    public static void configureFileChooser(JFileChooser fileChooser) {
        fileChooser.setFocusable(true);
        fileChooser.requestFocusInWindow();

        fileChooser.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke("ENTER"), "pressButton");

        fileChooser.getActionMap().put("pressButton", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                if (focusOwner instanceof JButton) {
                    ((JButton) focusOwner).doClick();
                }
            }
        });
    }
}
