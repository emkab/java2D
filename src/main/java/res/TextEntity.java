package res;

import java.awt.*;

public class TextEntity extends BaseEntity{
    public String getText() {
        return getArgs().getText();
    }

    public void setText(String text) {
        getArgs().setText(text);
    }

    public TextEntity(Vector2 _pos, String text, int fontSize, Color color) {
        super(_pos);
        setText(text);
        getArgs().setFontSize(fontSize);
        getArgs().setColor(color);
    }

    public TextEntity() {
        super();
        setText("");
    }

    @Override
    public void draw(Screen screen, Graphics2D g2d) {
        g2d.setColor(getArgs().getColor());
        g2d.setFont(g2d.getFont().deriveFont((float) getArgs().getFontSize()));
        g2d.drawString(getText(), getPos().getX(), getPos().getY());
    }
}
