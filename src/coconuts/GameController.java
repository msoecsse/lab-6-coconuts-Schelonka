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
    private Scoreboard scoreboard;
    private ScoreboardObserver scoreboardObserver;
    private boolean running = false;

    @FXML
    private Pane gamePane;
    @FXML
    private Pane theBeach;
//    @FXML
//    private HBox scoreboard;
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
        scoreboard = new Scoreboard();
        scoreboardObserver = new ScoreboardObserver(scoreboard);
        theGame.attach(scoreboardObserver);
    }

    @FXML
    public void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.RIGHT && !theGame.done()) {
            if (running) {
                theGame.getCrab().crawl(10);
            }
        } else if (keyEvent.getCode() == KeyCode.LEFT && !theGame.done()) {
            if (running) {
                theGame.getCrab().crawl(-10);
            }
        } else if (keyEvent.getCode() == KeyCode.UP) {
            theGame.makeLaser();
        } else if (keyEvent.getCode() == KeyCode.SPACE) {
            toggleStart();
        }
    }

    private void toggleStart() {
        if (!started) {
            scoreboard.start();
            started = true;
        }
        if (!running) {

            scoreboard.resume();
            coconutTimeline.play();
            scoreboardTimeline.play();
            running = true;
        } else {

            scoreboard.pause();
            coconutTimeline.pause();
            scoreboardTimeline.pause();
            running = false;
        }
    }

    /**
     * Updates the scoreboard display with current time and coconut destruction count
     */
    private void updateScoreboard() {
        if (running) {
            timeLabel.setText(String.format("Time: %ds", scoreboard.getTime()));
        }
        coconutsDestroyedLabel.setText("Coconuts Destroyed: " + scoreboard.getCoconutsDestroyedByLaser());
        coconutsHitBeachLabel.setText("Coconuts Hit Beach: " + scoreboard.getCoconutsHitBeach());
    }

    /**
     * Increments the count of coconuts destroyed by laser
     */


    /**
     * Increments the count of coconuts that hit the beach
     */


}