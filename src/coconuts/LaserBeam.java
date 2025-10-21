package coconuts;

import javafx.scene.image.Image;

// Represents the beam of light moving from the crab to a coconut; can hit only falling objects
// This is a domain class; do not introduce JavaFX or other GUI components here
public class LaserBeam extends IslandObject {
    private static final int WIDTH = 5; // must be updated with image
    private static final Image laserImage = new Image("file:images/laser-1.png");

    public LaserBeam(OhCoconutsGameManager game, int eyeHeight, int crabCenterX) {
        super(game, crabCenterX, eyeHeight, WIDTH, laserImage);
    }

    public int hittable_height() {
        return y + WIDTH;
    }

    @Override
    public void step() {
        y -= 3;
    }

    @Override
    public boolean isLaserBeam() {
        return true;
    }

    @Override
    public boolean canHit(IslandObject other){
        return true;
    }
    @Override
    public boolean isTouching(IslandObject other) {
        if (!other.isCoconut()) return false;

        // Simple bounding box collision
        int laserLeft = x;
        int laserRight = x + width;
        int laserTop = y;
        int laserBottom = y + width; // you could define a height if different

        int coconutLeft = other.x;
        int coconutRight = other.x + other.width;
        int coconutTop = other.y;
        int coconutBottom = other.y + other.width; // assume square

        // Check for overlap
        return !(laserRight < coconutLeft || laserLeft > coconutRight ||
                laserBottom < coconutTop || laserTop > coconutBottom);
    }
}