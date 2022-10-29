import javax.swing.*;
import java.awt.*;

class PublicWindow extends JFrame {
    private static final int MIN_WIDTH = 1024;
    private static final int MIN_HEIGHT = 576;

    private final JPanel pnlMain = new JPanel(new GridBagLayout());

    private final Application application;

    PublicWindow(Application application) {
        super("JHandball Table");

        this.application = application;

        setupLayout();

        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setVisible(true);
    }

    private void setupLayout() {


        add(pnlMain);
        pack();
    }
}
