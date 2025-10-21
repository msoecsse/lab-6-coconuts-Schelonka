package coconuts;

// https://stackoverflow.com/questions/42443148/how-to-correctly-separate-view-from-model-in-javafx

import javafx.scene.layout.Pane;

import java.util.Collection;
import java.util.LinkedList;

// This class manages the game, including tracking all island objects and detecting when they hit
public class OhCoconutsGameManager implements Subject {
    private final Collection<HitObserver> observers = new LinkedList<>();

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
        attach(new CollisionObserver(this));


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
                    String eventType = "";

                    if (thisObj.isLaserBeam() && hittableObject.isCoconut())
                        eventType = "COCONUT_DESTROYED";
                    else if (thisObj.isCoconut() && hittableObject.isCrab())
                        eventType = "CRAB_HIT";
                    else if (thisObj.isCoconut() && hittableObject.isBeach())
                        eventType = "COCONUT_HIT_BEACH";

                    if (!eventType.isEmpty()) {
                        HitEvent event = new HitEvent(thisObj, hittableObject, eventType);
                        notifyAll(event);
                    }
//                    hittableIslandSubjects.remove(hittableObject);
//
//                    // Check if this is a laser hitting a coconut
//                    if (gameController != null) {
//                            gameController.incrementCoconutsDestroyed();
//                    }
////                    }
//                    // Check if this is a coconut hitting the crab, end the game
//                    if (thisObj.isCoconut() && hittableObject.isCrab()) {
//                        killCrab(); // This will end the game
//                        return; // Exit immediately to stop game processing
//                    }
//                    // Check if this is a coconut hitting the beach, increment beach hit counter
//                    else if (thisObj.isCoconut() && hittableObject.isBeach()) {
//                        if (gameController != null) {
//                            gameController.incrementCoconutsHitBeach();
//                        }
//                        // Remove the coconut, not the beach
//                        scheduledForRemoval.add(thisObj);
//                        gamePane.getChildren().remove(thisObj.getImageView());
//                        continue; // Skip the general removal logic below
//                    }
//                    scheduledForRemoval.add(hittableObject);
//                    gamePane.getChildren().remove(hittableObject.getImageView());
                }
            }
        }
        // actually remove the objects as needed
//        for (IslandObject thisObj : scheduledForRemoval) {
//            removeObject(thisObj);
//        }
//        scheduledForRemoval.clear();
    }



    public void makeLaser() {
        LaserBeam laser = new LaserBeam(this, theCrab.y, theCrab.x + 25);
        registerObject(laser);
        gamePane.getChildren().add(laser.getImageView());
    }


    public boolean done() {
        // Game ends if crab is killed or if all coconuts are gone and max time reached
        return theCrab == null || (coconutsInFlight == 0 && gameTick >= MAX_TIME);
    }

    public void removeObject(IslandObject obj) {
        allObjects.remove(obj);
        if (obj.isHittable()) hittableIslandSubjects.remove(obj);
        gamePane.getChildren().remove(obj.getImageView());
    }


    @Override
    public void attach(HitObserver observer) {
        observers.add(observer);
    }

    @Override
    public void detach(HitObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyAll(HitEvent event) {
        for (HitObserver server: observers) {
            server.update(event);
        }
    }

}