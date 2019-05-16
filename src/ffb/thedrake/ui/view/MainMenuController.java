package ffb.thedrake.ui.view;

import ffb.thedrake.GameResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {
    @FXML private Button closeButton;

    public void initialize(URL location, ResourceBundle resources) {

    }

    public void PlayButton(ActionEvent actionEvent) {
        GameResult.changeStateTo(GameResult.IN_PLAY);
    }

    public void CloseButton(ActionEvent actionEvent)
    {
        ((Stage) closeButton.getScene().getWindow()).close();
    }
}