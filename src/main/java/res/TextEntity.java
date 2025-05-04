package res;

import java.awt.*;

public class TextEntity extends BaseEntity {
    public TextEntity(Vector2 pos, String text, int fontSize, Color color) {
        super(pos);
        setText(text);
        setFontSize(fontSize);
        setColor(color);
    }

    public TextEntity() {
        super();
        setText("");
    }

    public String getText() {
        return getEntityArg("text", String.class);
    }

    public void setText(String text) {
        setEntityArg("text", text);
    }

    public Font getFont() {
        return getEntityArg("font", Font.class);
    }

    public void setFont(Font font) {
        setEntityArg("font", font);
    }

    public Integer getFontSize() {
        return getEntityArg("fontSize", Integer.class);
    }

    public void setFontSize(Integer fontSize) {
        setEntityArg("fontSize", fontSize);
    }

    public Color getColor() {
        return getEntityArg("color", Color.class);
    }

    public void setColor(Color color) {
        setEntityArg("color", color);
    }

    @Override
    public void draw(Screen screen, Graphics2D g2d) {
        g2d.setColor(getColor());
        g2d.setFont(g2d.getFont().deriveFont((float) getFontSize()));
        g2d.drawString(getText(), getPos().getX(), getPos().getY());
    }
}
