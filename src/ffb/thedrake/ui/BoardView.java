package ffb.thedrake.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import ffb.thedrake.BoardPos;
import ffb.thedrake.GameState;
import ffb.thedrake.Move;
import ffb.thedrake.PositionFactory;

import java.util.List;

public class BoardView extends GridPane implements TileViewContext {

    private GameState gameState;

    private ValidMoves validMoves;

    private TileView selected;

    public BoardView(GameState gameState) {
        this.gameState = gameState;
        this.validMoves = new ValidMoves(gameState);

        PositionFactory positionFactory = gameState.board().positionFactory();

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                int i = x;
                int j = 3 - y;
                BoardPos boardPos = positionFactory.pos(i, j);
                add(new TileView(boardPos, gameState.tileAt(boardPos), this), x, y);
            }
        }

        setHgap(5);
        setVgap(5);
        setPadding(new Insets(15));
        setAlignment(Pos.CENTER);
    }

    private void clearMoves() {
        for (Node node : getChildren()) {
            TileView tileView = (TileView) node;
            tileView.clearMove();
        }
    }

    private void showMoves(List<Move> moveList) {
        for (Move move : moveList) {
            tileViewAt(move.target()).setMove(move);
        }
    }

    private TileView tileViewAt(BoardPos target) {
        int index = (3 - target.j()) * 4 + target.i();
        return (TileView) getChildren().get(index);

    }

    @Override
    public void tileViewSelected(TileView tileView) {
        if (selected != null && selected != tileView) {
            selected.unselect();
        }

        selected = tileView;
        clearMoves();
        showMoves(validMoves.boardMoves(tileView.position()));
    }

    @Override
    public void executeMove(Move move) {
        selected.unselect();
        selected = null;
        clearMoves();
        gameState = move.execute(gameState);
        validMoves = new ValidMoves(gameState);
        updateTiles();
    }

    private void updateTiles() {
        for (Node node : getChildren()) {
            TileView tileView = (TileView) node;
            tileView.setTile(gameState.tileAt(tileView.position()));
            tileView.update();
        }
    }


}
