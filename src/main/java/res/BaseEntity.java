package res;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BaseEntity {
    private Vector2 pos;
    private Map<String, Object> args;

    public BaseEntity(Vector2 pos) {
        this.pos = pos;
        this.args = new HashMap<>();
    }

    public <T> void setEntityArg(String key, T value) {
        args.put(key, value);
    }

    public <T> T getEntityArg(String key, Class<T> clazz) {
        Object value = args.get(key);
        if (clazz.isInstance(value)) {
            return clazz.cast(value);
        }
        return null; // or throw an exception if you prefer
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

    public void draw(Screen screen, Graphics2D g2d) {
    }
}
