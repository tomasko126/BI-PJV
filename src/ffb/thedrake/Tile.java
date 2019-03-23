package ffb.thedrake;

import java.util.List;

public interface Tile {
    public boolean canStepOn();
    public boolean hasTroop();
    public List<Move> movesFrom(BoardPos pos, GameState state);
}
