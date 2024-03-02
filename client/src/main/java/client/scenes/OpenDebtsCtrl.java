package client.scenes;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
/***
 * Unused imports:
 * import javafx.scene.control.Label;
 * import javafx.scene.control.TextField;
 * import javafx.scene.paint.Color;
 */


public class OpenDebtsCtrl {
    @FXML
    private Button backButton;

    //need a way to show open debts from the database

    @FXML
    public void goBack(ActionEvent event) {

        System.out.println("go back to prevoius scene");
    }
}
