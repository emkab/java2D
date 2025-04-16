package res;

import java.awt.*;

public class BaseEntity {
    private Vector2 pos;
    private EntityArgs args = new EntityArgs();

    public BaseEntity(Vector2 _pos) {
        pos = _pos;
    }

    public BaseEntity() {
        pos = new Vector2();
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public EntityArgs getArgs() {
        return args;
    }

    public void setArgs(EntityArgs args) {
        this.args = args;
    }

    public void draw(Screen screen, Graphics2D g2d) {}
}
