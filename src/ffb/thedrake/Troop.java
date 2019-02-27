package ffb.thedrake;

public class Troop {
    private final String name;
    private final Offset2D aversPivot;
    private final Offset2D reversPivot;

    public Troop(String name, Offset2D aversPivot, Offset2D reversPivot) {
        this.name = name;
        this.aversPivot = aversPivot;
        this.reversPivot = reversPivot;
    }

    public Troop(String name, Offset2D pivot) {
        this(name, pivot, pivot);
    }

    public Troop(String name) {
        this(name, new Offset2D(1, 1), new Offset2D(1, 1));
    }

    public String name() {
        return this.name;
    }

    public Offset2D pivot(TroopFace face) {
        if (face == TroopFace.AVERS) {
            return this.aversPivot;
        }

        return this.reversPivot;
    }
}
