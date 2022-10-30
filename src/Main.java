import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Logging.initialize();

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        System.out.println(Arrays.toString(fonts));

        // TODO choose fallback fonts
        Font defaultFont = new Font("Monospaced", Font.PLAIN, 14);

        UIManager.put("Button.font", defaultFont);
        UIManager.put("Label.font", defaultFont);
        UIManager.put("List.font", defaultFont);
        UIManager.put("TextField.font", defaultFont);
        UIManager.put("MenuBar.font", defaultFont);
        UIManager.put("Menu.font", defaultFont);
        UIManager.put("MenuItem.font", defaultFont);

        Logging.info("Starting GUI...");

        SwingUtilities.invokeLater(Application::new);
    }
}
