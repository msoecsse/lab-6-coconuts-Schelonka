package coconuts;

// the beach catches (hits) coconuts and increases the coconut score
// This is a domain class; do not introduce JavaFX or other GUI components here
public class Beach extends HittableIslandObject {

    public Beach(OhCoconutsGameManager game, int skyHeight, int islandWidth) {
        super(game, 0, skyHeight, islandWidth, null);
        // System.out.println("Beach at y = " + this.y);
    }

    @Override
    public void step() { /* do nothing */ }

    @Override
    public boolean isBeach() {
        return true;
    }
}