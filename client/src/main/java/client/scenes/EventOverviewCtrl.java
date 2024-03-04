package client.scenes;

import commons.Event;
import commons.Person;
import commons.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EventOverviewCtrl {

    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Label overviewLabel;
    @FXML
    private Label eventNameLabel;
    @FXML
    private Button inviteButton;
    @FXML
    private Button goHomeButton;
    @FXML
    private Label participantNameLabel;
    @FXML
    private Button showAllParticipantsButton;
    @FXML
    private ScrollPane participantsList;
    @FXML
    private Label expensesLabel;
    @FXML
    private Button showAllExpensesButton;
    @FXML
    private Button showExpensesFromPersonButton;
    @FXML
    private Button showExpensesWithPersonButton;
}
