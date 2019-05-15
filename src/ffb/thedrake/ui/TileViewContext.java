package ffb.thedrake.ui;

import ffb.thedrake.GameState;
import ffb.thedrake.Move;

public interface TileViewContext {

    void tileViewSelected(TileView tileView);

    void executeMove(Move move);

    GameState getGameState();

}
