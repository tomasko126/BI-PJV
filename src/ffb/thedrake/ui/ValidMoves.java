package ffb.thedrake.ui;

import ffb.thedrake.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidMoves {
    private final GameState state;

    public ValidMoves(GameState state) {
        this.state = state;
    }

    public List<Move> boardMoves(BoardPos position) {
        if (state.armyOnTurn().boardTroops().isPlacingGuards())
            return Collections.emptyList();

        Tile tile = state.tileAt(position);
        if (tile.hasTroop()) {
            if (((TroopTile) tile).side() != state.sideOnTurn()) {
                return Collections.emptyList();
            }

            return ((TroopTile) tile).movesFrom(position, state);
        }

        return Collections.emptyList();
    }

    public List<Move> movesFromStack() {
        List<Move> moves = new ArrayList<Move>();
        PositionFactory pf = state.board().positionFactory();
        Army armyOnTurn = state.armyOnTurn();
        if (!armyOnTurn.boardTroops().isLeaderPlaced()) {
            int j = 0;
            if (state.sideOnTurn() == PlayingSide.ORANGE)
                j = state.board().dimension() - 1;

            for (int i = 0; i < state.board().dimension(); i++) {
                moves.add(new PlaceFromStack(pf.pos(i, j)));
            }
        } else if (armyOnTurn.boardTroops().isPlacingGuards()) {
            TilePos leader = armyOnTurn.boardTroops().leaderPosition();
            TilePos target = leader.step(0, 1);
            if (state.canPlaceFromStack(target)) {
                moves.add(new PlaceFromStack((BoardPos) target));
            }

            target = leader.step(0, -1);
            if (state.canPlaceFromStack(target)) {
                moves.add(new PlaceFromStack((BoardPos) target));
            }

            target = leader.step(1, 0);
            if (state.canPlaceFromStack(target)) {
                moves.add(new PlaceFromStack((BoardPos) target));
            }

            target = leader.step(-1, 0);
            if (state.canPlaceFromStack(target)) {
                moves.add(new PlaceFromStack((BoardPos) target));
            }
        } else {
            for (BoardPos pos : armyOnTurn.boardTroops().troopPositions()) {
                List<BoardPos> neighbours = pos.neighbours();
                for (BoardPos target : neighbours) {
                    if (state.canPlaceFromStack(target)) {
                        moves.add(new PlaceFromStack(target));
                    }
                }
            }
        }

        return moves;
    }

    public List<Move> allMoves() {
        List<Move> moves = new ArrayList<>();
        for (BoardPos pos : state.armyOnTurn().boardTroops().troopPositions()) {
            moves.addAll(boardMoves(pos));
        }

        moves.addAll(movesFromStack());
        return moves;
    }
}
