package ffb.thedrake;

import java.util.List;

public class Troop {
    private final String name;
    private final Offset2D aversPivot;
    private final Offset2D reversPivot;
    private List<TroopAction> aversActions;
    private List<TroopAction> reversActions;

    public Troop(String name, Offset2D aversPivot, Offset2D reversPivot, List<TroopAction> aversActions, List<TroopAction> reversActions) {
        this.name = name;
        this.aversPivot = aversPivot;
        this.reversPivot = reversPivot;
        this.aversActions = aversActions;
        this.reversActions = reversActions;
    }

    public Troop(String name, Offset2D pivot, List<TroopAction> aversActions, List<TroopAction> reversActions) {
        this(name, pivot, pivot, aversActions, reversActions);
    }

    public Troop(String name, List<TroopAction> aversActions, List<TroopAction> reversActions) {
        this(name, new Offset2D(1, 1), new Offset2D(1, 1), aversActions, reversActions);
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
    public List<TroopAction> actions(TroopFace face){
        if(face.equals(TroopFace.AVERS))
            return aversActions;
        return reversActions;
    }
}
