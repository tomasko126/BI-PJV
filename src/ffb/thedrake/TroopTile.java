package ffb.thedrake;

public class TroopTile implements Tile {
    private Troop troop;
    private PlayingSide playingSide;
    private TroopFace troopFace;

    // Konstruktor
    public TroopTile(Troop troop, PlayingSide side, TroopFace face){
        this.troop = troop;
        this.playingSide = side;
        this.troopFace = face;
    }

    // Vrací barvu, za kterou hraje jednotka na této dlaždici
    public PlayingSide side(){
        return playingSide;
    }

    // Vrací stranu, na kterou je jednotka otočena
    public TroopFace face(){
        return troopFace;
    }

    // Jednotka, která stojí na této dlaždici
    public Troop troop(){
        return troop;
    }

    // Vrací False, protože na dlaždici s jednotkou se nedá vstoupit
    public boolean canStepOn(){
        return false;
    }

    // Vrací True
    public boolean hasTroop(){
        return true;
    }

    // Vytvoří novou dlaždici, s jednotkou otočenou na opačnou stranu
    // (z rubu na líc nebo z líce na rub)
    public TroopTile flipped(){
        if(troopFace==TroopFace.AVERS)
            return new TroopTile(this.troop, this.playingSide, TroopFace.REVERS);
        return new TroopTile(this.troop, this.playingSide, TroopFace.AVERS);
    }


}