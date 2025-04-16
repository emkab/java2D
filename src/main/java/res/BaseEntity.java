package res;

public class BaseEntity {
    private Vector2 pos;

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
}
