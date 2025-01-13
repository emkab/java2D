import res.Window;

import java.awt.*;

public class SampleWindow extends Window {

    public SampleWindow(int _width, int _height, double _graphicsScale, int _fps) {
        super(_width, _height, _graphicsScale, _fps);
    }

    public void init(String title) {
        super.init(title);
    }

    public void update() {
        write("Hello, world!", -width / 2 + 5, -rHeight / 2 + 5, Color.WHITE, 28, onscreen);
    }
}
