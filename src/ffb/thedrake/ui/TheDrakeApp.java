package ffb.thedrake.ui;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ffb.thedrake.*;

public class TheDrakeApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        AnchorPane mainMenu = FXMLLoader.load(getClass().getResource("view/drake.fxml"));

        Scene sceneMainMenu = new Scene(mainMenu);

        stage.setScene(sceneMainMenu);
        stage.setTitle("The Drake");
        stage.show();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (GameResult.getStateChanged()) {
                    GameResult.changeStateChangedTo(false);

                    if (GameResult.getState() == GameResult.IN_PLAY) {
                        GameState gameState = createSampleGameState();
                        BoardView boardView = new BoardView(gameState);
                        Scene gameScene = new Scene(boardView);


                        stage.setScene(gameScene);
                        stage.show();
                    }
                }
            }
        }.start();
    }

    private static GameState createSampleGameState() {
        Board board = new Board(4);
        PositionFactory positionFactory = board.positionFactory();
        board = board.withTiles(new Board.TileAt(positionFactory.pos(1, 1), BoardTile.MOUNTAIN));
        return new StandardDrakeSetup().startState(board);
    }

}