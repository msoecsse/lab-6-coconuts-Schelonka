/*
 ** Course: CSC 1110A
 ** Fall 2024
 ** Assignment:
 ** Name : Joshua Schelonka
 ** Date Created:
 */

package coconuts;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class CollisionObserver implements HitObserver{

    OhCoconutsGameManager gameManager;

    public CollisionObserver(OhCoconutsGameManager manager) {
        gameManager = manager;
    }

    @Override
    public void update(HitEvent event) {
        switch (event.getType()) {
            case "COCONUT_DESTROYED" -> {
                Platform.runLater(() -> {
                    gameManager.removeObject(event.getHitter());
                    gameManager.removeObject(event.getTarget());
                });
            }
            case "CRAB_HIT" -> {
                Platform.runLater(() -> {
                    gameManager.killCrab();
                });
            }
            case "COCONUT_HIT_BEACH" -> {
                Platform.runLater(() -> {
                    gameManager.removeObject(event.getTarget());
                });
            }
        }
    }
}
