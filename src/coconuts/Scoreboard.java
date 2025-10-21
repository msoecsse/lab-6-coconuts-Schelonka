/*
 ** Course: CSC 1110A
 ** Fall 2024
 ** Assignment:
 ** Name : Joshua Schelonka
 ** Date Created:
 */

package coconuts;

public class Scoreboard {

    private int coconutsDestroyedByLaser = 0;
    private int coconutsHitBeach = 0;

    private long startTime = 0;
    private long pausedAt = 0;
    private long totalPausedTime = 0;
    private boolean paused = false;

    public void start() {
        startTime = System.currentTimeMillis();
        pausedAt = 0;
        totalPausedTime = 0;
        paused = false;
        coconutsDestroyedByLaser = 0;
        coconutsHitBeach = 0;
    }

    public void coconutDestroyed() {
        coconutsDestroyedByLaser++;
    }

    public void incrementCoconutsHitBeach() {
        coconutsHitBeach++;
    }

    /** Returns elapsed time in seconds */
    public long getTime() {
        if (paused) {
            return (pausedAt - startTime - totalPausedTime) / 1000; // ms → s
        } else {
            return (System.currentTimeMillis() - startTime - totalPausedTime) / 1000;
        }
    }

    public int getCoconutsDestroyedByLaser() {
        return coconutsDestroyedByLaser;
    }

    public int getCoconutsHitBeach() {
        return coconutsHitBeach;
    }

    /** Pause timer */
    public void pause() {
        if (!paused) {
            pausedAt = System.currentTimeMillis();
            paused = true;
        }
    }

    /** Resume timer */
    public void resume() {
        if (paused) {
            totalPausedTime += System.currentTimeMillis() - pausedAt;
            paused = false;
        }
    }

    public boolean isPaused() {
        return paused;
    }
}
