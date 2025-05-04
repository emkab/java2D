package res;

import java.awt.*;

public class Particle extends CircleEntity {
    Particle(Vector2 pos, Integer radius, Color color) {
        super(pos, radius, color);
    }

    Particle() {
        super();
    }

    public float getVelocity() {
        return getEntityArg("velocity", float.class);
    }

    public void setVelocity(float velocity) {
        setEntityArg("velocity", velocity);
    }

    public float getAcceleration() {
        return getEntityArg("acceleration", float.class);
    }

    public void setAcceleration(float acceleration) {
        setEntityArg("acceleration", acceleration);
    }

    public void update() {
    }
}
