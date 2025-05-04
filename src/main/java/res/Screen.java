package res;

public class Screen {
    private int width, height;

    public Screen(int _width, int _height) {
        width = _width;
        height = _height;
    }

    public Vector2 screenToWorld(Vector2 pos) {
        return new Vector2(pos.getX() - (float) width / 2, pos.getY() - (float) height / 2);
    }

    public Vector2 screenToWorld(float x, float y) {
        return new Vector2(x - (float) width / 2, y - (float) height / 2);
    }

    public Vector2 worldToScreen(Vector2 pos) {
        return new Vector2(pos.getX() + (float) width / 2, pos.getY() + (float) height / 2);
    }

    public Vector2 worldToScreen(float x, float y) {
        return new Vector2(x + (float) width / 2, y + (float) height / 2);
    }
}
