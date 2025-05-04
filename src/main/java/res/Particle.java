package res;

import java.awt.*;

public class Particle extends CircleEntity {
    Particle(Vector2 pos, Integer radius, Color color) {
        super(pos, radius, color);
    }

    Particle() {
        super();
    }

    public Integer getVelocity() {
        return getEntityArg("velocity", Integer.class);
    }

    public void setVelocity(Integer velocity) {
        setEntityArg("velocity", velocity);
    }


}
