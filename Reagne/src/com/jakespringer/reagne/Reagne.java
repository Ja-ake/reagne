package com.jakespringer.reagne;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.jakespringer.reagne.fundamental.Notifier;
import com.jakespringer.reagne.fundamental.Observable;
import com.jakespringer.reagne.fundamental.Subscribable;
import com.jakespringer.reagne.game.World;
import com.jakespringer.reagne.util.Command;

public class Reagne {
    private static Queue<Command> mainThread = new ConcurrentLinkedQueue<>();
    private static boolean running = true;
    private static World theWorld;

    ///
    /// Public member variables
    ///

    /**
     * A stream that fires every tick. Will stream the delta time between each
     * tick.
     */
    public static final Observable<Double> continuous = new Observable<>(0.0);

    /**
     * A stream that fires once at the start of when {@link Reagne#run()} is
     * called.
     */
    public static final Notifier initialize = new Subscribable();

    /**
     * Queues a command for executing during the next update loop.
     * 
     * @param command
     *            the command to execute
     */
    public static void queueCommand(Command command) {
        if (command == null) {
            throw new NullPointerException();
        }
        
        command.act();
    }
    
    public static void onMainThread(Command command) {
        if (command == null) {
            throw new NullPointerException();
        }
        
        mainThread.offer(command);
    }

    /**
     * Run the game loop.
     */
    public static void run() {
        // initialize the game
        ((Subscribable) initialize).alert();
        dispatchCommands();

        // run the game loop
        long currentTime = System.nanoTime();
        long previousTime = currentTime;
        while (running) {
            currentTime = System.nanoTime();
            double deltaTime = ((double) currentTime - (double) previousTime) * 0.000001;
            continuous.set(Double.valueOf(deltaTime));
            dispatchCommands();
            previousTime = currentTime;
        }
    }

    /**
     * Run the game loop and supply a world.
     */
    public static void run(World wld) {
        theWorld = wld;
        run();
    }

    /**
     * Requests the game to stop.
     */
    public static void stop() {
        running = false;
    }

    /**
     * Gets the instance of World associated with the engine.
     */
    public static World world() {
        return theWorld;
    }
    
    /**
     * Shorthand for Reagne.continuous.get().
     */
    public static double dt() {
        return Reagne.continuous.get();
    }

    /**
     * Returns the default resource folder.
     * 
     * @return the resource folder
     */
    public static String getResourceFolder() {
        return "./res";
    }

    private Reagne() {} // disable construction of Reagens

    private static void dispatchCommands() {
        int i=0; // don't allow anything to lock up main thread, spread it out
        while (!mainThread.isEmpty() && (++i) < 16) {
            mainThread.remove().act();
        }
    }
}
