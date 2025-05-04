import res.RectangleEntity;
import res.TextEntity;
import res.Vector2;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SampleWindow window = new SampleWindow(1920, 1080, 1.0 / 2.0, 120);
        window.init("Bezier");

        TextEntity helloWorld = new TextEntity(new Vector2(5, 25), "Hello, world!", 18, new Color(255, 255, 255, 255));
        RectangleEntity helloRect = new RectangleEntity(window.getScreen().worldToScreen(0, 0), 40, 20, new Color(255, 255, 0, 255));

        window.addEntity(helloWorld);
        window.addEntity(helloRect);
    }
}
