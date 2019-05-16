package ffb.thedrake.ui;

import ffb.thedrake.*;
import ffb.thedrake.ui.view.EndGameController;
import ffb.thedrake.ui.view.MainMenuController;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class TheDrakeApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        AnchorPane mainMenu = FXMLLoader.load(getClass().getResource("view/MainMenu.fxml"));
        Scene mainMenuScene = new Scene(mainMenu);

        GameState gameState = createSampleGameState();
        GameView gameView = new GameView(gameState);
        Scene gameScene = new Scene(gameView);

        FXMLLoader loader = new FXMLLoader();
        AnchorPane endView = loader.load(getClass().getResource("view/EndGame.fxml").openStream());
        EndGameController controller = loader.getController();

        Scene endScene = new Scene(endView);
        endScene.getStylesheets().add("ffb/thedrake/ui/view/style.css");

        stage.setScene(mainMenuScene);
        stage.setTitle("The Drake");
        stage.show();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (GameResult.getStateChanged()) {
                    GameResult.changeStateChangedTo(false);

                    if (GameResult.getState() == GameResult.IN_PLAY) {
                        stage.setScene(gameScene);
                        stage.show();
                    } else if (GameResult.getState() == GameResult.VICTORY || GameResult.getState() == GameResult.DRAW) {
                        if (GameResult.getState() == GameResult.DRAW) {
                            controller.setWonText("Draw!");
                        } else {
                            PlayingSide sideNotOnTurn = gameView.getBoardView().getGameState().armyNotOnTurn().side();
                            controller.setWonText(sideNotOnTurn + " has won!");
                        }

                        stage.setScene(endScene);
                        stage.show();
                    } else if (GameResult.getState() == GameResult.MAIN_MENU) {
                        stage.setScene(mainMenuScene);
                    }
                }

                PlayingSide sideOnTurn = gameView.getBoardView().getGameState().sideOnTurn();

                gameView.getPlayerOnTurn().setText("Side on turn: " + sideOnTurn);
                gameView.getNoOfCapturedTroops().setText("Number of captured troops: " + gameView.getBoardView().getGameState().armyOnTurn().captured().size());
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
