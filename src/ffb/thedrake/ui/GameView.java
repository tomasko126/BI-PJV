package ffb.thedrake.ui;

import ffb.thedrake.GameState;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GameView extends BorderPane {
    private BoardView boardView;

    private VBox infoBox;

    private Label playerOnTurn;
    private Label noOfCapturedTroops;

    public GameView(GameState gameState) {
        this.boardView = new BoardView(gameState);
        this.setLeft(boardView);

        VBox stackBox = new VBox();
        stackBox.getChildren().add(new Label("Stack:"));
        stackBox.getChildren().add(boardView.getStackView());

        this.infoBox = new VBox();
        this.playerOnTurn = new Label();
        this.noOfCapturedTroops = new Label();

        infoBox.getChildren().add(this.playerOnTurn);
        infoBox.getChildren().add(this.noOfCapturedTroops);

        infoBox.setAlignment(Pos.CENTER);

        this.setTop(infoBox);
        this.setRight(stackBox);
    }

    public Label getPlayerOnTurn() {
        return playerOnTurn;
    }

    public Label getNoOfCapturedTroops() {
        return noOfCapturedTroops;
    }

    public BoardView getBoardView() {
        return boardView;
    }

}
