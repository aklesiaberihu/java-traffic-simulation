package trafficsim;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Road extends JPanel {

    private static final int SAFE_DISTANCE = 35;
    private static final int STOPLINE_OFFSET = 15;
    private static final int RED_STOP_WINDOW = 60;

    private final ArrayList<Vehicles> veh = new ArrayList<>();

    private final int x, y;
    private final int laneHeight;
    private final int roadWidth;

    private int carCount = 0;
    private String trafficLightState = "green";

    public Road(int x, int y, int laneHeight, int roadWidth) {
        this.x = x;
        this.y = y;
        this.laneHeight = laneHeight;
        this.roadWidth = roadWidth;
    }

    public void addVehicle(Vehicles v) {
        veh.add(v);
    }

    public void removeLastVehicle() {
        if (!veh.isEmpty()) {
            veh.remove(veh.size() - 1);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(x, y, getWidth(), laneHeight * 3);

        g.setColor(Color.YELLOW);
        g.fillRect(roadWidth - (roadWidth / 5), 0, 5, laneHeight * 3 + 20);

        g.setColor(Color.BLACK);
        g.fillRect((roadWidth - (roadWidth / 5)) - 25, laneHeight * 3 + 20, 55, 110);

        g.setColor("green".equals(trafficLightState) ? Color.GREEN : Color.RED);
        g.fillOval((roadWidth - (roadWidth / 5)) - 20,
                "green".equals(trafficLightState) ? laneHeight * 3 + 75 : laneHeight * 3 + 25,
                45, 45);

        g.setColor(Color.WHITE);
        for (int i = laneHeight; i < laneHeight * 3; i += laneHeight) {
            for (int j = 0; j < getWidth(); j += 40) {
                g.fillRect(j, i, 30, 5);
            }
        }

        for (Vehicles v : veh) {
            v.createVehicle(g);
        }
    }

    public void step() {
        int stopLine = roadWidth - (roadWidth / 5) - STOPLINE_OFFSET;

        for (int i = 0; i < veh.size(); i++) {
            Vehicles v = veh.get(i);

            boolean isAtRed = "red".equals(trafficLightState);
            int vehicleFront = v.getX() + v.getWidth();

            boolean shouldStopForRed =
                    isAtRed &&
                    vehicleFront >= stopLine &&
                    vehicleFront <= stopLine + RED_STOP_WINDOW;

            boolean clearAhead = true;

            for (int j = 0; j < veh.size(); j++) {
                if (i == j) continue;

                Vehicles other = veh.get(j);

                if (v.getLane() == other.getLane()) {
                    int gap = other.getX() - (v.getX() + v.getWidth());
                    boolean ahead = other.getX() > v.getX();

                    if (ahead && gap < SAFE_DISTANCE) {
                        clearAhead = false;
                        break;
                    }
                }
            }

            boolean canMove = !shouldStopForRed && clearAhead;

            if (canMove || v.getX() + v.getWidth() >= roadWidth) {
                v.setX(v.getX() + v.getSpeed());

                if (v.getX() >= roadWidth) {
                    v.setX(0 - v.getWidth());
                    carCount++;
                }
            }
        }

        detectCollisions();
        repaint();
    }

    private void detectCollisions() {
        for (int i = 0; i < veh.size(); i++) {
            for (int j = i + 1; j < veh.size(); j++) {
                Vehicles a = veh.get(i), b = veh.get(j);
                if (a.getLane() == b.getLane() && a.getBounds().intersects(b.getBounds())) {
                    showCollisionPopup();
                    veh.remove(j);
                    return;
                }
            }
        }
    }

    private void showCollisionPopup() {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                "⚠ Collision Detected!",
                "Vehicle Collision",
                JOptionPane.WARNING_MESSAGE));
    }

    public int getCount() {
        return carCount;
    }

    public void resetCount() {
        carCount = 0;
    }

    public void setTrafficLightState(String state) {
        this.trafficLightState = state;
    }

    public List<Vehicles> getVehicles() {
        return veh;
    }
}