package coconuts;

// https://stackoverflow.com/questions/42443148/how-to-correctly-separate-view-from-model-in-javafx

import javafx.scene.layout.Pane;

import java.util.Collection;
import java.util.LinkedList;

// This class manages the game, including tracking all island objects and detecting when they hit
public class OhCoconutsGameManager {
    private final Collection<IslandObject> allObjects = new LinkedList<>();
    private final Collection<HittableIslandObject> hittableIslandSubjects = new LinkedList<>();
    private final Collection<IslandObject> scheduledForRemoval = new LinkedList<>();
    private final int height, width;
    private final int DROP_INTERVAL = 10;
    private final int MAX_TIME = 100;
    private Pane gamePane;
    private Crab theCrab;
    private Beach theBeach;
    private GameController gameController;
    private int coconutsInFlight = 0;
    private int gameTick = 0;

    public OhCoconutsGameManager(int height, int width, Pane gamePane) {
        this.height = height;
        this.width = width;
        this.gamePane = gamePane;

        this.theCrab = new Crab(this, height, width);
        registerObject(theCrab);
        gamePane.getChildren().add(theCrab.getImageView());

        this.theBeach = new Beach(this, height, width);
        registerObject(theBeach);
        if (theBeach.getImageView() != null)
            System.out.println("Unexpected image view for beach");
    }

    private void registerObject(IslandObject object) {
        allObjects.add(object);
        if (object.isHittable()) {
            HittableIslandObject asHittable = (HittableIslandObject) object;
            hittableIslandSubjects.add(asHittable);
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void coconutDestroyed() {
        coconutsInFlight -= 1;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void tryDropCoconut() {
        // Only drop coconuts if crab is still alive
        if (gameTick % DROP_INTERVAL == 0 && theCrab != null) {
            coconutsInFlight += 1;
            Coconut c = new Coconut(this, (int) (Math.random() * width));
            registerObject(c);
            gamePane.getChildren().add(c.getImageView());
        }
        gameTick++;
    }

    public Crab getCrab() {
        return theCrab;
    }

    public void killCrab() {
        theCrab = null;
    }

    public void advanceOneTick() {
        for (IslandObject o : allObjects) {
            o.step();
            o.display();
        }
        scheduledForRemoval.clear();
        for (IslandObject thisObj : allObjects) {
            for (HittableIslandObject hittableObject : hittableIslandSubjects) {
                if (thisObj.canHit(hittableObject) && thisObj.isTouching(hittableObject)) {
                    // Check if this is a laser hitting a coconut
                    if (thisObj.isLaserBeam() && hittableObject.isCoconut()) {
                        if (gameController != null) {
                            gameController.incrementCoconutsDestroyed();
                        }
                    }
                    // Check if this is a coconut hitting the crab, end the game
                    else if (thisObj.isCoconut() && hittableObject.isCrab()) {
                        killCrab(); // This will end the game
                        return; // Exit immediately to stop game processing
                    }
                    // Check if this is a coconut hitting the beach, increment beach hit counter
                    else if (thisObj.isCoconut() && hittableObject.isBeach()) {
                        if (gameController != null) {
                            gameController.incrementCoconutsHitBeach();
                        }
                        // Remove the coconut, not the beach
                        scheduledForRemoval.add(thisObj);
                        gamePane.getChildren().remove(thisObj.getImageView());
                        continue; // Skip the general removal logic below
                    }
                    scheduledForRemoval.add(hittableObject);
                    gamePane.getChildren().remove(hittableObject.getImageView());
                }
            }
        }
        // actually remove the objects as needed
        for (IslandObject thisObj : scheduledForRemoval) {
            allObjects.remove(thisObj);
            if (thisObj.isHittable()) {
                hittableIslandSubjects.remove((HittableIslandObject) thisObj);
            }
        }
        scheduledForRemoval.clear();
    }


    public boolean done() {
        // Game ends if crab is killed or if all coconuts are gone and max time reached
        return theCrab == null || (coconutsInFlight == 0 && gameTick >= MAX_TIME);
    }
}