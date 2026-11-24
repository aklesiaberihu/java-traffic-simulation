package trafficsim;

import java.awt.Rectangle;

public abstract class Vehicles implements Drawable {
    private int x;
    private final int y;
    private final int vehlane;
    private int width;
    private int height;
    private int speed;

    public Vehicles(int nx, int ny, int nlane) {
        this.x = nx;
        this.y = ny;
        this.vehlane = nlane;
    }

    public final Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public void setX(int nx) {
        this.x = nx;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int w) {
        if (w <= 0) throw new IllegalArgumentException("Width must be positive");
        this.width = w;
    }
    
    public void setHeight(int h) {
        if (h <= 0) throw new IllegalArgumentException("Height must be positive");
        this.height = h;
    }
    

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        if (speed < 0) throw new IllegalArgumentException("Speed can't be negative");
        this.speed = speed;
    }    

    public int getLane() {
        return vehlane;
    }

}