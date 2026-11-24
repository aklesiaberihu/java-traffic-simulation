package trafficsim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.IOException;

public class App implements ActionListener, Runnable {

    private static final int ROAD_X = 0;
    private static final int ROAD_Y = 0;
    private static final int LANE_HEIGHT = 120;
    private static final int ROAD_WIDTH = 1100;
    private static final int ROAD_PREF_HEIGHT = 410;

    private static final int TICK_MS = 10;

    private static final int START_X = 10;
    private static final int LANE_PADDING = 10;
    private static final int LTV_Y_OFFSET = 10;
    private static final int BIKE_Y_OFFSET = 20;

    private final JFrame frame;
    private final Road road;

    private final VehicleUtility vehicleUtility = new VehicleUtility();
    private final SoundManager soundManager = new SoundManager();
    private final VehicleManager<Vehicles> vehicleManager = new VehicleManager<>();


    private JButton start, stop, addHTV, addLTV, addBike, l1, l2, l3, red, green, remove;
    private JLabel outputLabel, densityLabel;
    private JLabel lane1CountLabel, lane2CountLabel, lane3CountLabel;
    private JLabel totalCountLabel, averageSpeedLabel, frequentTypeLabel;


    private int running = 0;
    private int lane = 1;
    private int density = 0;
    private int vehicleCount = 0;
    private long startTime = 0;


    public App() {

        frame = new JFrame("TrafficSim");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(true);

        road = new Road(ROAD_X, ROAD_Y, LANE_HEIGHT, ROAD_WIDTH);
        road.setPreferredSize(new Dimension(ROAD_WIDTH, ROAD_PREF_HEIGHT));
        frame.add(road, BorderLayout.CENTER);

        setupControlPanel();
        setupDashboardPanel();

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

        updateDashboard();
    }


    private void setupControlPanel() {

        JPanel controlPanel = new JPanel(new BorderLayout(10, 10));
        controlPanel.setBackground(new Color(240, 240, 240));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Font btnFont = new Font("Arial", Font.PLAIN, 15);

        start = new JButton("Start");
        stop = new JButton("Stop");
        addHTV = new JButton("Add HTV");
        addLTV = new JButton("Add LTV");
        addBike = new JButton("Add Bike");
        l1 = new JButton("Lane 1");
        l2 = new JButton("Lane 2");
        l3 = new JButton("Lane 3");
        red = new JButton("Red");
        green = new JButton("Green");
        remove = new JButton("Remove Vehicle");

        JButton[] allButtons = {
                start, stop, addHTV, addLTV,
                addBike, l1, l2, l3,
                red, green, remove
        };

        for (JButton btn : allButtons) {
            btn.setFont(btnFont);
            btn.addActionListener(this);
        }

        JPanel grid = new JPanel(new GridLayout(3, 4, 10, 10));
        grid.setOpaque(false);

        for (JButton btn : allButtons) grid.add(btn);
        grid.add(new JLabel(""));

        outputLabel = new JLabel("Output: 0.0");
        densityLabel = new JLabel("Density: 0");
        outputLabel.setFont(new Font("Arial", Font.BOLD, 14));
        densityLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.add(outputLabel);
        info.add(Box.createVerticalStrut(8));
        info.add(densityLabel);

        controlPanel.add(grid, BorderLayout.CENTER);
        controlPanel.add(info, BorderLayout.EAST);

        frame.add(controlPanel, BorderLayout.SOUTH);
    }


    private void setupDashboardPanel() {

        JPanel dashboardPanel = new JPanel(null);
        dashboardPanel.setPreferredSize(new Dimension(200, 700));
        dashboardPanel.setBackground(new Color(240, 240, 240));

        Font labelFont = new Font("Arial", Font.PLAIN, 14);

        JLabel[] labels = {
                lane1CountLabel = new JLabel("Lane 1: 0"),
                lane2CountLabel = new JLabel("Lane 2: 0"),
                lane3CountLabel = new JLabel("Lane 3: 0"),
                totalCountLabel = new JLabel("Total: 0"),
                averageSpeedLabel = new JLabel("Avg Speed: 0"),
                frequentTypeLabel = new JLabel("Frequent: -")
        };

        int yPos = 40;
        for (JLabel lbl : labels) {
            lbl.setFont(labelFont);
            lbl.setBounds(20, yPos, 160, 30);
            dashboardPanel.add(lbl);
            yPos += 40;
        }

        frame.add(dashboardPanel, BorderLayout.EAST);
    }


    private Vehicles createVehicleByType(Object source) {

        int yOffset = (lane - 1) * LANE_HEIGHT + LANE_PADDING;

        if (source == addHTV)
            return new HTV(START_X, yOffset, lane);
        else if (source == addLTV)
            return new LTV(START_X, yOffset + LTV_Y_OFFSET, lane);
        else if (source == addBike)
            return new Bike(START_X, yOffset + BIKE_Y_OFFSET, lane);
        else
            throw new IllegalArgumentException("Unknown vehicle type");
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        Object source = e.getSource();

        if (source == start && running == 0) {

            running = 1;
            road.resetCount();
            startTime = System.currentTimeMillis();
            new Thread(this).start();

        } else if (source == stop) {

            running = 0;
            road.resetCount();

        } else if (source == l1) lane = 1;
        else if (source == l2) lane = 2;
        else if (source == l3) lane = 3;

        else if (source == red) road.setTrafficLightState("red");
        else if (source == green) road.setTrafficLightState("green");

        else if (source == remove) {

            road.removeLastVehicle();
            vehicleManager.removeLast();
            if (density > 0) density--;

        } else if (source == addHTV || source == addLTV || source == addBike) {

            Vehicles v = createVehicleByType(source);
            vehicleUtility.logVehicle(v);

            road.addVehicle(v);
            vehicleManager.add(v);
            density++;

            try {
                soundManager.playSound("/trafficsim/beep.wav");
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                System.err.println("⚠️ Failed to play sound: " + ex.getMessage());
            }
        }

        updateDashboard();
        frame.repaint();
    }


    @Override
    public void run() {

        while (running == 1) {

            road.step();
            vehicleCount = road.getCount();

            long elapsedMillis = System.currentTimeMillis() - startTime;

            double throughput =
                    (elapsedMillis > 0)
                            ? vehicleCount / (elapsedMillis / 1000.0)
                            : 0;

            outputLabel.setText("Output: " + String.format("%.2f", throughput));
            densityLabel.setText("Density: " + density);

            updateDashboard();

            try {
                Thread.sleep(TICK_MS);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }


    private void updateDashboard() {

        int lane1 = 0, lane2 = 0, lane3 = 0;
        int totalSpeed = 0;
        int bikeCount = 0, ltvCount = 0, htvCount = 0;

        for (Vehicles v : road.getVehicles()) {

            switch (v.getLane()) {
                case 1 -> lane1++;
                case 2 -> lane2++;
                case 3 -> lane3++;
            }

            totalSpeed += v.getSpeed();

            if (v instanceof Bike) bikeCount++;
            else if (v instanceof LTV) ltvCount++;
            else if (v instanceof HTV) htvCount++;
        }

        int total = lane1 + lane2 + lane3;
        int avgSpeed = (total > 0) ? totalSpeed / total : 0;

        String frequent;
        if (total == 0) frequent = "-";
        else if (bikeCount >= ltvCount && bikeCount >= htvCount) frequent = "Bike";
        else if (ltvCount >= bikeCount && ltvCount >= htvCount) frequent = "LTV";
        else frequent = "HTV";

        lane1CountLabel.setText("Lane 1: " + lane1);
        lane2CountLabel.setText("Lane 2: " + lane2);
        lane3CountLabel.setText("Lane 3: " + lane3);
        totalCountLabel.setText("Total: " + total);
        averageSpeedLabel.setText("Avg Speed: " + avgSpeed);
        frequentTypeLabel.setText("Frequent: " + frequent);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::new);
    }
}