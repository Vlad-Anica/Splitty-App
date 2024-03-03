package client.scenes;

import jakarta.inject.Inject;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/***
 * Unused imports:
 * import javafx.scene.control.Label;
 * import javafx.scene.control.TextField;
 * import javafx.scene.paint.Color;
 */




public class OpenDebtsCtrl {
    @FXML
    private Button goHomeButton;
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;
    @FXML
    private Button backButton;

    private MainCtrl mainCtrl;

    @Inject
    public OpenDebtsCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    //need a way to show open debts from the database


    //For Now, the back button is the same as the home button, this needs changing in the future!
    @FXML
    public void goBack(ActionEvent event) throws IOException {
        mainCtrl.showHome();
    }

    public void goHome(ActionEvent event) throws IOException {
        mainCtrl.showHome();
    }

}
