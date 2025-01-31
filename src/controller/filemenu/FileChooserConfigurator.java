package controller.filemenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class FileChooserConfigurator {

    public static void configureFileChooser(JFileChooser fileChooser) {
        fileChooser.setFocusable(true);
        fileChooser.requestFocusInWindow();

        // ENTER-Taste: Datei auswählen ODER Button klicken
        fileChooser.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke("ENTER"), "confirmSelection");

        fileChooser.getActionMap().put("confirmSelection", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();

                // Prüfen, ob ein Button (Öffnen/Speichern) fokussiert ist
                if (focusOwner instanceof JButton) {
                    ((JButton) focusOwner).doClick();
                }
                // Prüfen, ob die Dateiliste den Fokus hat
                else if (focusOwner instanceof JList) {
                    fileChooser.approveSelection(); // Simuliert Klick auf "Öffnen"
                }
            }
        });
    }
}
