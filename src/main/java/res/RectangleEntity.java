package res;

import java.awt.*;

public class RectangleEntity extends BaseEntity{
    public Integer getWidth() {
        return getEntityArg("width", Integer.class);
    }

    public void setWidth(Integer width) {
        setEntityArg("width", width);
    }

    public Integer getHeight() {
        return getEntityArg("height", Integer.class);
    }

    public void setHeight(Integer height) {
        setEntityArg("height", height);
    }

    public Color getColor() {
        return getEntityArg("color", Color.class);
    }

    public void setColor(Color color) {
        setEntityArg("color", color);
    }

    public RectangleEntity() {
        super();
        setWidth(10);
        setHeight(10);
        setColor(new Color(255, 255, 255, 255));
    }

    public RectangleEntity(Integer width, Integer height, Color color) {
        super();
        setWidth(width);
        setHeight(height);
        setColor(color);
    }

    public RectangleEntity(Vector2 pos, Integer width, Integer height, Color color) {
        super(pos);
        setWidth(width);
        setHeight(height);
        setColor(color);
    }

    @Override
    public void draw(Screen screen, Graphics2D g2d) {
        g2d.setColor(getColor());
        g2d.drawRect((int) getPos().getX(), (int) getPos().getY(), getWidth(), getHeight());
    }
}
