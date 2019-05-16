package ffb.thedrake.ui;

import ffb.thedrake.GameState;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class GameView extends BorderPane {
    private BoardView boardView;

    public GameView(GameState gameState) {
        boardView = new BoardView(gameState);

        this.setLeft(boardView);

        VBox stackBox = new VBox();
        stackBox.getChildren().add(boardView.getStackView());

        this.setRight(stackBox);
    }
}
