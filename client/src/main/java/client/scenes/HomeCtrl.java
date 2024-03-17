package client.scenes;


import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeCtrl  {


    @FXML
    private Label MainPageTestLabel;
    @FXML
    private Button addParticipantButton;
    @FXML
    private Button addExpenseButton;
    @FXML
    private Button goDebtsButton;

    @FXML
    private Button goHomeButton;
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;

    private MainCtrl mainCtrl;

    @Inject
    public HomeCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }


    public void goToSettings() {
        mainCtrl.showSettings();
    }


    public void goToAddParticipant(ActionEvent event) throws IOException {
        mainCtrl.showAddParticipant();
    }

    public void goToAddExpense(ActionEvent event) throws IOException {
        mainCtrl.showAddExpense();
    }

    public void goToDebts(ActionEvent event) throws IOException {
        mainCtrl.showOpenDebts();
    }

    public void goHome(ActionEvent event) throws IOException {
        mainCtrl.showHome();
    }

    public void goToEventOverview(ActionEvent event) throws IOException {
        mainCtrl.showEventOverview();
    }




}
