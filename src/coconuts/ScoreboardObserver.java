package coconuts;


import javafx.application.Platform;
import javafx.scene.layout.VBox;

public class ScoreboardObserver extends VBox implements HitObserver {
    private Scoreboard scoreboard;

    public ScoreboardObserver(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }



    @Override
    public void update(HitEvent event) {
        Platform.runLater(() -> {
            switch (event.getType()) {
                case "COCONUT_DESTROYED" -> {
                    scoreboard.coconutDestroyed();
                }
                case "TIME" -> {
                    scoreboard.getTime();
                }
                case "COCONUT_HIT_BEACH" -> {
                    scoreboard.incrementCoconutsHitBeach();

                }
            }

        });
    }


}