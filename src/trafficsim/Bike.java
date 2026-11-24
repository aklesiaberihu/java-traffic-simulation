package trafficsim;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Bike extends Vehicles {

    private final Image myimage;

    public Bike(int nx, int ny, int nlane) {
        super(nx, ny, nlane);

        setWidth(100);
        setHeight(40);
        setSpeed(5);

        Image loadedImage = null;

        try {
            var is = getClass().getResourceAsStream("/trafficsim/bike1.png");

            if (is == null) {
                throw new IllegalArgumentException("Resource not found: bike1.png");
            }

            loadedImage = ImageIO.read(is);

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.myimage = loadedImage;
    }

    @Override
    public void createVehicle(Graphics g) {
        int drawY = getY() - 5;

        if (myimage != null) {
            g.drawImage(myimage, getX(), drawY, getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(getX(), drawY, getWidth(), getHeight());
        }
    }
}
