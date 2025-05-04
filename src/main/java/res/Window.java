package res;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Window implements Runnable, ActionListener, MouseListener, MouseMotionListener, KeyListener, ComponentListener, MouseWheelListener {
    protected boolean running;
    protected long lastUpdate;
    private int fps;
    private float deltaTime;
    private Duration deltaTimeDuration;
    protected TreeSet<String> keysDown;
    protected TreeSet<String> processedKeys;
    protected TreeSet<String> unprocessedKeys;
    protected HashMap<String, ArrayList<ArrayList>> keyBindings;
    protected HashMap<Window, ArrayList<ArrayList>> mouseBindings;
    protected final Object keyLock = new Object();
    protected JFrame window;
    protected JLabel draw;
    protected ImageIcon icon;
    protected BufferedImage onscreenImage;
    protected Graphics2D onscreen;
    protected int width, height, rWidth, rHeight;
    private double scale;
    private double graphicsScale = 0.4;
    private float strokeThickness = 4.0f;
    private Screen screen;
    private BasicStroke stroke;
    private Font font;

    public void run() {
        if (Thread.currentThread().getName().equals("render")) {
            renderLoop();
        } else if (Thread.currentThread().getName().equals("listen")) eventLoop();
    }

    protected void eventLoop() {
        //System.out.println("EVENT LOOP STARTED");

        long time = 0;
        while (running) {
            time = System.nanoTime();
            processKeys();
            waitUntil(time + 1000000000 / fps);
        }
    }

    protected void renderLoop() {
        //System.out.println("RENDER LOOP STARTED");
        long time = 0;
        long startTime = System.nanoTime();
        while (running) {
            Instant beginTime = Instant.now();
            time = System.nanoTime();
            drawBackground(onscreen);
            update();
            window.repaint();
            if (!waitUntil(time + 1000000000 / fps)) fps--;
            else if (fps < 30) fps++;
            deltaTimeDuration = Duration.between(beginTime, Instant.now());
            deltaTime = (float) deltaTimeDuration.getNano() / 1000000000;
        }
    }

    public float getCurrentFPS() {
        if (deltaTimeDuration != null)
            return 1f / deltaTime;
        else return 0f;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getrWidth() {
        return rWidth;
    }

    public int getrHeight() {
        return rHeight;
    }

    public double getScale() {
        return scale;
    }

    public Screen getScreen() {
        return screen;
    }

    protected boolean waitUntil(Long time) {
        long now = System.nanoTime();
        if (now < time) {
            try {
                Thread.sleep((time - now) / 1000000);
            } catch (Exception ignored) {
            }
            return true;
        } else return false;
    }

    public Window(int _width, int _height, double _graphicsScale, int _fps) {
        graphicsScale = _graphicsScale;
        width = (int) (_width * graphicsScale);
        height = (int) (_height * graphicsScale);

        fps = _fps;
    }

    private void windowSetup() {
        window = new JFrame();
        window.setSize(width, height);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);


        icon = new ImageIcon();
        draw = new JLabel(icon);
        window.setContentPane(draw);
        setupBuffering();
    }

    public void init(String title) {
        scale = 1;
        running = true;

        windowSetup();
        window.setTitle(title);
        screen = new Screen(width, height);

        keyBindings = new HashMap<String, ArrayList<ArrayList>>();
        mouseBindings = new HashMap<Window, ArrayList<ArrayList>>();
        keysDown = new TreeSet<String>();
        processedKeys = new TreeSet<String>();
        unprocessedKeys = new TreeSet<String>();


        try {
            font = Font.createFont(Font.TRUETYPE_FONT, ClassLoader.getSystemResource("rubik.ttf").openStream());
            GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
            genv.registerFont(font);
            font = font.deriveFont(Font.PLAIN, 12f);
            onscreen.setFont(font);
            System.out.println(onscreen.getFont().toString());
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }

        startThreads();
    }

    protected void startThreads() {
        window.addComponentListener(this);
        draw.addComponentListener(this);
        draw.addMouseListener(this);
        draw.addMouseMotionListener(this);
        draw.addMouseWheelListener(this);
        window.addKeyListener(this);
        draw.addKeyListener(this);
        draw.requestFocus();

        (new Thread(this, "listen")).start();
        (new Thread(this, "render")).start();
//        rWidth = width - (window.getInsets().left + window.getInsets().right);
        int barHeight = (window.getInsets().top + window.getInsets().bottom);
        if (barHeight < 28) barHeight = 28;
        rHeight = height - barHeight;
        window.setVisible(true);
    }

    public void drawCircle(int x, int y, int r, Color color, Color fillColor, Graphics2D g2d) {
        g2d.setColor(color);
        g2d.drawOval(x, y, r, r);
        g2d.setColor(fillColor);
        g2d.fillOval(x, y, r, r);
    }

    public Color backgroundColor = Color.black;

    protected void drawBackground(Graphics2D g2d) {
        g2d.setColor(backgroundColor);
        g2d.drawRect(0, 0, width, height);
        g2d.fillRect(0, 0, width, height);

        stroke = new BasicStroke(strokeThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g2d.setStroke(stroke);
    }

    protected void setupBuffering() {
        lastUpdate = 0;
        onscreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        onscreen = onscreenImage.createGraphics();
        onscreen.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        icon.setImage(onscreenImage);
    }

    protected BaseEntity[] entities = {};

    public int addEntity(BaseEntity e)
    {
        BaseEntity[] _entities = new BaseEntity[entities.length + 1];
        _entities[_entities.length - 1] = e;
        entities = _entities;
        return entities.length;
    }

    public int removeEntity(int index) {
        if (entities.length == 0 || index >= entities.length) return 0;
        BaseEntity[] _entities = new BaseEntity[entities.length - 1];

        boolean found = false;
        for (int i = 0; i < entities.length - 1; i++) {
            if (i == index) {
                found = true;
                continue;
            }

            _entities[found ? i - 1 : i] = entities[i];
        }
        
        entities = _entities;
        return entities.length;
    }

    protected void update() {
        for (BaseEntity e : entities) {
            e.draw(screen, onscreen);
        }
    }

    protected void processKeys() {
        //System.out.println(keysDown);
        TreeSet<String> keysDownCopy = new TreeSet<String>();
        synchronized (keyLock) {
            keysDownCopy = (TreeSet<String>) keysDown.clone();
        }
        keysDownCopy.addAll(unprocessedKeys);
        for (String keyText : keysDownCopy) {
            if (keyBindings.containsKey(keyText)) {
                for (ArrayList binding : keyBindings.get(keyText)) {
                    Window m = (Window) binding.get(0);
                    String className = (String) binding.get(1);
                    String methodName = (String) binding.get(2);
                    Boolean repeat = (Boolean) binding.get(3);
                    if (!repeat && processedKeys.contains(keyText)) break;
                    unprocessedKeys.remove(keyText);
                    processedKeys.add(keyText);
                    try {
                        Class cls = Class.forName(className);
                        Object clsInstance = (Object) cls.newInstance();
                        Method m0 = clsInstance.getClass().getMethod(methodName, m.getClass());
                        m0.invoke(clsInstance, m);
                    } catch (Exception e1) {
                        try {
                            Class cls = Class.forName(className);
                            Object clsInstance = (Object) cls.newInstance();
                            Method m0 = clsInstance.getClass().getMethod(methodName, m.getClass(), keyText.getClass());
                            m0.invoke(clsInstance, m, keyText);
                        } catch (Exception e2) {
                            try {
                                Class cls = Class.forName(className);
                                Object clsInstance = (Object) cls.newInstance();
                                Method m0 = clsInstance.getClass().getMethod(methodName);
                                m0.invoke(clsInstance);
                            } catch (Exception e3) {
                                System.out.println("KeyBinding for " + keyText + " has failed.");
                                e1.printStackTrace();
                                e2.printStackTrace();
                                e3.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void resizeComponent() {
        width = (int) draw.getBounds().getWidth();
        height = (int) draw.getBounds().getHeight();
        setupBuffering();
//        rWidth = width - (window.getInsets().left + window.getInsets().right);
        int barHeight = (window.getInsets().top + window.getInsets().bottom);
        if (barHeight < 28) barHeight = 28;
        rHeight = height - barHeight;
        screen = new Screen(width, height);

        drawBackground(onscreen);
        update();
        window.repaint();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        resizeComponent();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {


    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }

    public void drawCircle(int x, int y, int r, Color color, Color fillColor, float strokeSize, Graphics2D g2d) {
        Vector2 newPos = screen.normalToScreen(x, y);
        g2d.setStroke(new BasicStroke(strokeSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        drawCircle((int) newPos.getX() - r / 2, (int) newPos.getY() - r / 2, r, color, fillColor, g2d);
    }

    public void drawCircle(Vector2 pos, int r, Color color, Color fillColor, float strokeSize, Graphics2D g2d) {
        Vector2 newPos = screen.normalToScreen(pos.getX(), pos.getY());
        g2d.setStroke(new BasicStroke(strokeSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        drawCircle((int) newPos.getX() - r / 2, (int) newPos.getY() - r / 2, r, color, fillColor, g2d);
    }

    public void write(String string, Vector2 pos, Color color, float fontSize, Graphics2D g2d) {
        Vector2 newPos = screen.normalToScreen(pos.getX(), pos.getY());
        g2d.setColor(color);
        g2d.setFont(g2d.getFont().deriveFont(fontSize));
        g2d.drawString(string, newPos.getX(), newPos.getY());
    }

    public void write(String string, int x, int y, Color color, float fontSize, Graphics2D g2d) {
        Vector2 newPos = screen.normalToScreen(x, y);
        g2d.setColor(color);
        g2d.setFont(g2d.getFont().deriveFont(fontSize));
        g2d.drawString(string, newPos.getX(), newPos.getY());
    }

    public void drawLine(Vector2 pos1, Vector2 pos2, Color color, float strokeSize, Graphics2D g2d) {
        pos1 = screen.normalToScreen(pos1);
        pos2 = screen.normalToScreen(pos2);
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(strokeSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine((int) pos1.getX(), (int) pos1.getY(), (int) pos2.getX(), (int) pos2.getY());
    }
}