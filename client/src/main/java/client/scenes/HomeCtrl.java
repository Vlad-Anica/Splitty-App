package client.scenes;


import jakarta.inject.Inject;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
    private Button goDepthButton;
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

    public void clickBtnSettings() {
        mainCtrl.showSettings();
    }


    public void goToAddParticipant(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("AddParticipant.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goToAddExpense(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("AddExpense.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goToDepths(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("OpenDebts.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goHome(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



}
