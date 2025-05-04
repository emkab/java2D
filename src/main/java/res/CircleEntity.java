package res;

import java.awt.*;

public class CircleEntity extends BaseEntity {
    public CircleEntity(Vector2 pos, Integer radius, Color color) {
        super(pos);
        setRadius(radius);
        setColor(color);
    }

    public CircleEntity() {
        super();
        setRadius(10);
        setColor(Color.white);
    }

    public Integer getRadius() {
        return getEntityArg("radius", Integer.class);
    }

    public void setRadius(Integer radius) {
        setEntityArg("radius", radius);
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
        g2d.drawOval((int) getPos().getX(), (int) getPos().getY(), getRadius(), getRadius());
        g2d.setColor(getColor());
        g2d.fillOval((int) getPos().getX(), (int) getPos().getY(), getRadius(), getRadius());
    }
}
