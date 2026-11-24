# Traffic Simulation System

An interactive 3-lane urban traffic simulator built in Java Swing for the Object-Oriented Programming course at the University of Messina (Prof. Salvatore Distefano).

Built by Aklesia Berihu Mekonnen, Hirut Getachew Regassa, and Meron Zemichael Kahsay.

For the full design breakdown and testing notes, see [`OOP_Project_Report.pdf`](./OOP_Project_Report.pdf).

## Features

- **Multi-lane simulation:** 3-lane road with three vehicle types (`Bike`, `LTV`, `HTV`), each with its own dimensions, speed, and sprite.
- **Interactive controls:** Start/stop the simulation, spawn vehicles into a selected lane, remove the last vehicle, and toggle the traffic light between red and green.
- **Traffic rules:** Enforces safe following distances, stops vehicles at the stop line on red, wraps vehicles around the road edge, and runs collision detection with a non-blocking alert.
- **Live dashboard:** Tracks throughput (vehicles/second), density, per-lane counts, average speed, and the most frequent vehicle type in real time.
- **Audio & fallbacks:** Plays a sound cue when spawning vehicles (`SoundManager`) and falls back to colored rectangles if sprite images fail to load.

## OOP Design (`trafficsim` package)

- **Abstraction & Interfaces:** `Drawable` defines the rendering contract (`createVehicle(Graphics g)`), implemented by the abstract `Vehicles` superclass.
- **Inheritance & Subtype Polymorphism:** `Bike`, `LTV`, and `HTV` extend `Vehicles` and override rendering; `Road` iterates a single `ArrayList<Vehicles>` and dispatches dynamically.
- **Encapsulation & Information Hiding:** Vehicle state (`x`, `y`, `width`, `height`, `speed`, `vehlane`) is private with getters/setters; `SoundManager` hides Java Sound API internals behind `playSound(...)`.
- **Generics, Overloading & Coercion:** `VehicleManager<T extends Vehicles>` handles type-safe storage; `SoundManager` and `VehicleUtility` demonstrate method overloading and numeric widening (`int` to `double`).
- **Composition & Exception Handling:** `App` composes `Road`, `SoundManager`, and `VehicleManager`; checked audio exceptions, `InterruptedException`, and image I/O errors are caught cleanly so the GUI stays responsive.

## Project Structure

- `App.java` - main `JFrame`, control panel, dashboard, and simulation thread
- `Road.java` - custom `JPanel` handling rendering, movement step loop, stop-line logic, and collision detection
- `Drawable.java` / `Vehicles.java` - rendering interface and abstract vehicle base class
- `Bike.java`, `LTV.java`, `HTV.java` - concrete vehicle subclasses
- `VehicleManager.java` - generic collection manager
- `SoundManager.java` - WAV audio playback utility
- `VehicleUtility.java` - overloaded logging utility
- Assets: `bike1.png`, `car1.png`, `truck1.png`, `beep.wav`

## Tech Stack

- Java SE 23
- Java Swing / AWT / Java2D
- Java Sound API (`javax.sound.sampled`)
- Built with NetBeans

## Running Locally

Open the project in NetBeans (or any Java IDE) and run `trafficsim.App`, or compile and run from the `src` directory:

```bash
javac trafficsim/*.java
java trafficsim.App
```
