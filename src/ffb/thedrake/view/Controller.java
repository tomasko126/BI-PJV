package ffb.thedrake.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML private Button closeButton;

    public void initialize(URL location, ResourceBundle resources) {

    }
    public void CloseButton(ActionEvent actionEvent)
    {
        ((Stage) closeButton.getScene().getWindow()).close();
    }

}