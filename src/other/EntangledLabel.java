package other;

import javax.swing.*;

public class EntangledLabel extends JLabel {
    private JLabel another;

    public EntangledLabel(String text, JLabel another) {
        super(text);

        this.another = another;
    }

    @Override
    public void setText(String text) {
        super.setText(text);

        if (another != null) {
            another.setText(text);
        }
    }
    public void setAnother(JLabel another) {
        this.another = another;
    }
}
