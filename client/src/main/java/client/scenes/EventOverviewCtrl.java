package client.scenes;

import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class EventOverviewCtrl {

    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;
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
    private CheckBox selectPersonBox1;
    @FXML
    private Label expensesLabel;
    @FXML
    private Button showAllExpensesButton;
    @FXML
    private Button showExpensesFromPersonButton;
    @FXML
    private Button showExpensesWithPersonButton;
    @FXML
    private Button goToStatsButton;

    private MainCtrl mainCtrl;

    @Inject
    public EventOverviewCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void goHome(ActionEvent event) throws IOException {
        mainCtrl.showHome();
    }

    public void goToAddExpense(ActionEvent event) throws IOException {
        mainCtrl.showAddExpense();
    }

    public void goToStats(ActionEvent event) throws IOException {
        mainCtrl.showStatsTest();
    }

    //Will use this method to get the name of the event,
    // so i can pass it to the stats page. Idk of this is the correct label tho.
    public String getEventName(){
        return overviewLabel.getText();
    }

}
