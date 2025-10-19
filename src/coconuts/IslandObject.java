package coconuts;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// an object in the game, either something coming from the island or falling on it
// Each island object has a location and can determine if it hits another island object
// This is a domain class; do not introduce JavaFX or other GUI components here
public abstract class IslandObject {
    protected final int width;
    protected final OhCoconutsGameManager containingGame;
    protected int x, y;
    ImageView imageView = null;

    public IslandObject(OhCoconutsGameManager game, int x, int y, int width, Image image) {
        containingGame = game;
        if (image != null) {
            imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(width);
        }
        this.x = x;
        this.y = y;
        this.width = width;
        display();
        //System.out.println(this + " left " + left() + " right " + right());
    }

    protected ImageView getImageView() {
        return imageView;
    }

    public void display() {
        if (imageView != null) {
            imageView.setLayoutX(x);
            imageView.setLayoutY(y);
        }
    }

    public boolean isHittable() {
        return false;
    }

    protected int hittable_height() {
        return 0;
    }

    public boolean isGroundObject() {
        return false;
    }

    public boolean isFalling() {
        return false;
    }

    public boolean canHit(IslandObject other) {
        return false;
    }

    public boolean isTouching(IslandObject other) {
        return false;
    }

    // Type identification methods to replace instanceof
    public boolean isCoconut() {
        return false;
    }

    public boolean isCrab() {
        return false;
    }

    public boolean isBeach() {
        return false;
    }

    public boolean isLaserBeam() {
        return false;
    }

    public abstract void step();
}