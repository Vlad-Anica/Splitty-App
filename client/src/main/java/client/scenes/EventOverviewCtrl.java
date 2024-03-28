package client.scenes;

import client.utils.ServerUtils;
import commons.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private ComboBox showAllParticipantsInEvent;
    @FXML
    private AnchorPane choosePersonsPane;
    @FXML
    private Button goToAddExpense;
    @FXML
    private Label expensesLabel;
    @FXML
    private Button showAllExpensesButton;
    @FXML
    private Button showExpensesFromPersonButton;
    @FXML
    private Button showExpensesWithPersonButton;
    @FXML
    private AnchorPane filteringExpensesPane;
    @FXML
    private static AnchorPane filteringExpensesPaneOriginal;
    @FXML
    private Button goToStatsButton;
    @FXML
    private List<CheckBox> checkBoxes;
    @FXML
    TextFlow languageIndicator;
    private MainCtrl mainCtrl;
    private ServerUtils server;

    private Event event;
    private List<Person> participants;
    private Person selectedPerson;
    private List<Expense> expenses;


    @Inject
    public EventOverviewCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Resets text fields to original values for testing purposes.
     */
    private void resetTextFields() {
        showExpensesFromPersonButton.setText("From <<PERSON>>");
        showExpensesWithPersonButton.setText("Including <<PERSON>>");
    }

    public Event getEvent() {
        return event;
    }

    /**
     * Method that resets the parameters of the filtering pane as needed.
     */
    private void resetFilteringPane() {
        this.filteringExpensesPane.getChildren().removeAll();
        this.filteringExpensesPane.setLayoutX(294);
        this.filteringExpensesPane.setLayoutY(133);
        this.filteringExpensesPane.setPrefHeight(200);
        this.filteringExpensesPane.setPrefWidth(200);
    }

    public void setup(Long eventID) {


        try {
            event = server.getEvent(eventID);
            this.expenses = event.getExpenses();
            overviewLabel.setText(event.getName());
        } catch (Exception e) {
            System.out.println("Cannot find associated Event within the repository!");
            return;
        }
        if (event == null) {
            System.out.println("Cannot find associated Event within the repository!");
            return;
        }
        try {
            participants = new ArrayList<>();
            participants.addAll(event.getParticipants());
            int y = 5;
            for (Person p : participants) {
                CheckBox newBox = new CheckBox(
                        p.getFirstName() + " " + p.getLastName());
                choosePersonsPane.getChildren().add(newBox);
                newBox.setLayoutY(y);
                y += 25;
            }
            showAllParticipantsInEvent.setItems(FXCollections.observableArrayList(
                    participants.stream().map(p -> p.getFirstName() + " " + p.getLastName()).toList()
            ));

            checkBoxes = choosePersonsPane.getChildren().stream().map(t -> (CheckBox) t).toList();
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
    }

    public void setLanguageIndicator() {
        ImageView flagImage = new ImageView(mainCtrl.getPathToFlagImage());
        Text language = new Text(mainCtrl.getLanguageWithoutImagePath());
        languageIndicator.getChildren().addAll(language, flagImage);
    }

    public boolean validFiltering() {
        return this.selectedPerson != null;
    }

    /**
     * Method that shows all Expenses in selected Event, if applicable.
     * @param event Event in the page currently viewed
     */
    public void showAllExpensesInEvent(ActionEvent event) {
        resetFilteringPane();
        if (validFiltering()) {
            System.out.println("Cannot filter properly, no Person is selected");
            return;
        }
        showAllExpensesFiltered(this.expenses);
    }

    /**
     * Method that shows all Expenses made by the selected Person iof applicable.
     * @param event Event in the page currently viewed
     */
    public void showAllExpensesFromPerson(ActionEvent event) {
        resetFilteringPane();
        if (validFiltering()) {
            System.out.println("Cannot filter properly, no Person is selected");
            return;
        }
        List<Expense> selectedExpenses = new ArrayList<>();
        if(this.expenses == null) {
            selectedExpenses = null;
        } else {
            for(Expense expense: this.expenses) {
                if(expense.getReceiver().equals(selectedPerson)) {
                    selectedExpenses.add(expense);
                }
            }
        }
        showAllExpensesFiltered(selectedExpenses);
    }

    /**
     * Method that shows all Expenses associated with the selected Person if applicable.
     * @param event Event in the page currently viewed
     */
    public void showAllExpensesWithPerson(ActionEvent event) {
        resetFilteringPane();
        if (validFiltering()) {
            System.out.println("Cannot filter properly, no Person is selected");
            return;
        }
        List<Expense> selectedExpenses = new ArrayList<>();
        if(this.expenses == null) {
            selectedExpenses = null;
        } else {
            for(Expense expense: this.expenses) {
                if(expense.getInvolved().contains(selectedPerson)) {
                    selectedExpenses.add(expense);
                }
            }
        }
        showAllExpensesFiltered(selectedExpenses);
    }

    public void showAllExpensesFiltered(List<Expense> selectedExpenses) {
        try {
            if (selectedExpenses == null) {
                System.out.println("Cannot filter properly, Expenses are null.");
                return;
            }
            if (selectedExpenses.isEmpty()) {
                System.out.println("The Event has no Expenses associated with it.");
                CheckBox newBox = new CheckBox("There's nothing to display, silly!");
                filteringExpensesPane.getChildren().add(newBox);
                newBox.setLayoutY(5);
                return;
            }
            int y = 5;
            for (Expense e : selectedExpenses) {
                CheckBox newBox = new CheckBox(
                        e.getTag() + ", paid by " + e.getReceiver().getFirstName() + " " + e.getReceiver().getLastName());
                filteringExpensesPane.getChildren().add(newBox);
                newBox.setLayoutY(y);
                y += 25;
            }

            checkBoxes = filteringExpensesPane.getChildren().stream().map(t -> (CheckBox) t).toList();
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
    }

    public void renameFilters() {
        if (selectedPerson == null) {
            System.out.println("Selected Person is null!!! Filter refresh aborted.");
        }
        try {
            showExpensesFromPersonButton.setText("From" + selectedPerson.getFirstName() + " " + selectedPerson.getLastName());
            showExpensesWithPersonButton.setText("Including" + selectedPerson.getFirstName() + " " + selectedPerson.getLastName());
            System.out.println("Successful filter refresh!");
        } catch (Exception e) {
            System.out.println("Error encountered while receiving selected Person info.");
        }
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
    // so i can pass it to the stats page. Idk of this is the correct label tho. Yeah, it is :)
    public String getEventName() {
        return overviewLabel.getText();
    }

}
