package ffb.thedrake.ui;

import ffb.thedrake.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.util.List;

public class BoardView extends GridPane implements TileViewContext {

    private GameState gameState;

    private ValidMoves validMoves;

    private TileView selectedTileView;

    private StackView selectedStackView;

    public BoardView(GameState gameState) {
        this.gameState = gameState;


        PositionFactory positionFactory = gameState.board().positionFactory();

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                int i = x;
                int j = 3 - y;
                BoardPos boardPos = positionFactory.pos(i, j);
                add(new TileView(boardPos, gameState.tileAt(boardPos), this), x, y);
            }
        }

        this.validMoves = new ValidMoves(gameState);

        setHgap(5);
        setVgap(5);
        setPadding(new Insets(15));
        setAlignment(Pos.CENTER);

        this.selectedStackView = new StackView(this);
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
        if (selectedTileView != null && selectedTileView != tileView) {
            selectedTileView.unselect();
            selectedTileView = null;
            selectedStackView.unselect();
        }

        selectedTileView = tileView;
        clearMoves();
        showMoves(validMoves.boardMoves(tileView.position()));
    }

    @Override
    public void stackViewSelected(StackView stackView) {
        if (selectedTileView != null) {
            selectedTileView.unselect();
        }

        selectedStackView = stackView;
        clearMoves();

        List<Move> validStackMoves = validMoves.movesFromStack();
        showMoves(validStackMoves);
    }

    @Override
    public void executeMove(Move move) {
        if (selectedTileView != null) {
            selectedTileView.unselect();
            selectedTileView = null;
        }

        clearMoves();
        gameState = move.execute(gameState);
        validMoves = new ValidMoves(gameState);
        updateTiles();

        GameResult.changeStateTo(gameState.result());
    }

    private void updateTiles() {
        for (Node node : getChildren()) {
            TileView tileView = (TileView) node;
            tileView.setTile(gameState.tileAt(tileView.position()));
            tileView.update();
            selectedStackView.update();
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public StackView getStackView() {
        return selectedStackView;
    }
}
