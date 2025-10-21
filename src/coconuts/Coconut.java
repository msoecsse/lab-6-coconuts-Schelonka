package coconuts;

import javafx.scene.image.Image;

// Represents the falling object that can kill crabs. If hit by a laser, the coconut disappears
// This is a domain class; other than Image, do not introduce JavaFX or other GUI components here
public class Coconut extends HittableIslandObject {
    private static final int WIDTH = 50;
    private static final Image coconutImage = new Image("file:images/coco-1.png");

    public Coconut(OhCoconutsGameManager game, int x) {
        super(game, x, 0, WIDTH, coconutImage);
    }

    @Override
    public void step() {
        y += 5;
    }

    @Override
    public boolean canHit(IslandObject other) {
        // Coconuts can hit crabs and beaches
        return other.isCrab() || other.isBeach() || other.isLaserBeam();
    }

    @Override
    public boolean isCoconut() {
        return true;
    }

    @Override
    public boolean isTouching(IslandObject other) {
        if (other.isLaserBeam()) {
            return true;
        }
        if (other.isCrab()) {
            // Check if the bottom of the coconut is touching the top of the crab
            // and if the coconut's center is between the crab's left and right edges
            int coconutBottom = y + WIDTH;
            int crabTop = other.y;
            int coconutCenter = x + WIDTH / 2;
            int crabLeft = other.x;
            int crabRight = other.x + other.width;

            return coconutBottom >= crabTop &&
                   coconutCenter >= crabLeft &&
                   coconutCenter <= crabRight;
        } else if (other.isBeach()) {
            // Check if the bottom of the coconut is touching the beach
            // The beach is at the bottom of the game area
            int coconutBottom = y + WIDTH;
            int gameHeight = containingGame.getHeight();

            return coconutBottom >= gameHeight + 50;
        }
        return false;
    }

    @Override
    public boolean isFalling() {
        return true;
    }
}