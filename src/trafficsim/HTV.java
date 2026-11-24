package trafficsim;

import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.IOException;

public class HTV extends Vehicles {

    private final Image myimage;

    public HTV(int nx, int ny, int nlane) {
        super(nx, ny, nlane);

        setWidth(240);
        setHeight(95);
        setSpeed(1);

        Image loadedImage = null;

        try {
            var is = getClass().getResourceAsStream("/trafficsim/truck1.png");
            if (is == null) {
                throw new IllegalArgumentException("Resource not found: truck1.png");
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
            g.fillRect(getX(), getY(), getWidth(), getHeight());
        }
    }
}