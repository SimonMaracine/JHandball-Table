import other.Logging;
import windows.Application;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Logging.initialize();

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        System.out.println(Arrays.toString(fonts));

        // TODO choose fallback fonts
        Font defaultFont = new Font("Monospaced", Font.PLAIN, 26);
        Font listFont = new Font("Monospaced", Font.PLAIN, 22);
        Font menuFont = new Font("Monospaced", Font.PLAIN, 17);

        UIManager.put("Button.font", defaultFont);
        UIManager.put("Label.font", defaultFont);
        UIManager.put("List.font", listFont);
        UIManager.put("TextField.font", defaultFont);
        UIManager.put("MenuBar.font", menuFont);
        UIManager.put("Menu.font", menuFont);
        UIManager.put("MenuItem.font", menuFont);

        Logging.info("Starting GUI...");

        SwingUtilities.invokeLater(Application::new);
    }
}
