package coconuts;

import java.util.ArrayList;
import java.util.List;

// An abstraction of all objects that can be hit by another object
// This captures the Subject side of the Observer pattern; observers of the hit event will take action
//   to process that event
// This is a domain class; do not introduce JavaFX or other GUI components here
public class HitEvent {
    private IslandObject target;
    private IslandObject hitter;
    private String type;

    public HitEvent(IslandObject tar, IslandObject hit, String type) {
        target = tar;
        hitter = hit;
        this.type = type;
    }

    public IslandObject getHitter() { return hitter; }
    public IslandObject getTarget() { return target; }
    public String getType() { return type;}
}