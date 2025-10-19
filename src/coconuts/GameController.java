package coconuts;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

// JavaFX Controller class for the game - generally, JavaFX elements (other than Image) should be here
public class GameController {

    /**
     * Time between calls to step() (ms)
     */
    private static final double MILLISECONDS_PER_STEP = 1000.0 / 30;
    private Timeline coconutTimeline;
    private Timeline scoreboardTimeline;
    private boolean started = false;
    private long gameStartTime;
    private int coconutsDestroyedByLaser = 0;
    private int coconutsHitBeach = 0;

    @FXML
    private Pane gamePane;
    @FXML
    private Pane theBeach;
    @FXML
    private HBox scoreboard;
    @FXML
    private Label timeLabel;
    @FXML
    private Label coconutsDestroyedLabel;
    @FXML
    private Label coconutsHitBeachLabel;
    private OhCoconutsGameManager theGame;

    @FXML
    public void initialize() {
        theGame = new OhCoconutsGameManager((int) (gamePane.getPrefHeight() - theBeach.getPrefHeight()),
                (int) (gamePane.getPrefWidth()), gamePane);
        theGame.setGameController(this);

        gamePane.setFocusTraversable(true);

        coconutTimeline = new Timeline(new KeyFrame(Duration.millis(MILLISECONDS_PER_STEP), (e) -> {
            theGame.tryDropCoconut();
            theGame.advanceOneTick();
            if (theGame.done()) {
                coconutTimeline.pause();
                scoreboardTimeline.pause(); // Also pause the scoreboard timeline when game ends
            }
        }));
        coconutTimeline.setCycleCount(Timeline.INDEFINITE);

        // Scoreboard timeline to update time display
        scoreboardTimeline = new Timeline(new KeyFrame(Duration.millis(100), (e) -> {
            updateScoreboard();
        }));
        scoreboardTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    @FXML
    public void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.RIGHT && !theGame.done()) {
            theGame.getCrab().crawl(10);
        } else if (keyEvent.getCode() == KeyCode.LEFT && !theGame.done()) {
            theGame.getCrab().crawl(-10);
        } else if (keyEvent.getCode() == KeyCode.SPACE) {
            if (!started) {
                gameStartTime = System.currentTimeMillis();
                coconutTimeline.play();
                scoreboardTimeline.play();
                started = true;
            } else {
                coconutTimeline.pause();
                scoreboardTimeline.pause();
                started = false;
            }
        }
    }

    /**
     * Updates the scoreboard display with current time and coconut destruction count
     */
    private void updateScoreboard() {
        if (started) {
            long currentTime = System.currentTimeMillis();
            double elapsedSeconds = (currentTime - gameStartTime) / 1000.0;
            timeLabel.setText(String.format("Time: %.1fs", elapsedSeconds));
        }
        coconutsDestroyedLabel.setText("Coconuts Destroyed: " + coconutsDestroyedByLaser);
        coconutsHitBeachLabel.setText("Coconuts Hit Beach: " + coconutsHitBeach);
    }

    /**
     * Increments the count of coconuts destroyed by laser
     */
    public void incrementCoconutsDestroyed() {
        coconutsDestroyedByLaser++;
    }

    /**
     * Increments the count of coconuts that hit the beach
     */
    public void incrementCoconutsHitBeach() {
        coconutsHitBeach++;
    }
}