package trafficsim;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.io.IOException;

public class LTV extends Vehicles {

    private final Image myimage;

    public LTV(int nx, int ny, int nlane) {
        super(nx, ny, nlane);

        setWidth(150);
        setHeight(65);
        setSpeed(3);

        Image loadedImage = null;

        try {
            var is = getClass().getResourceAsStream("/trafficsim/car1.png");

            if (is == null) {
                throw new IllegalArgumentException("Resource not found: car1.png");
            }

            loadedImage = ImageIO.read(is);

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.myimage = loadedImage;
    }

    @Override
    public void createVehicle(Graphics g) {
        if (myimage != null) {
            g.drawImage(myimage, getX(), getY(), getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(getX(), getY(), getWidth(), getHeight());
        }
    }
}