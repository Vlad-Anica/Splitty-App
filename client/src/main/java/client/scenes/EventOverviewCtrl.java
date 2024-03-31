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
    private Label inviteCode;
    @FXML
    private TextField emailField;
    @FXML
    private Button inviteButton;
    @FXML
    private Button goHomeButton;
    @FXML
    private ComboBox<String> showAllParticipantsInEvent;
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
    @FXML
    private Button goToExpenseEdit;
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
        overviewLabel.setText("<<EVENT>>");
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

    /**
     * Method that initialises the page and other useful fields.
     *
     * @param eventID event ID that represents the Event being parsed here.
     */
    public void setup(Long eventID) {

        try {
            event = server.getEvent(eventID);
            this.expenses = event.getExpenses();
        } catch (Exception e) {
            System.out.println("Cannot find associated Event within the repository!");
            return;
        }
        if (event == null) {
            System.out.println("Cannot find associated Event within the repository!");
            return;
        }
        this.overviewLabel.setText(event.getName());
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

    /**
     * Method that selects the Selected Person and also returns it.
     * @return currently selected Person.
     */
    public Person getSelectedPerson() {
        Person person = null;
        String fullName = null;
        if (showAllParticipantsInEvent.getValue() != null) {
            fullName = showAllParticipantsInEvent.getValue();
        } else {
            this.selectedPerson = person;
        return person;
        }
        for (Person p : this.event.getParticipants()) {
            if (new String(p.getFirstName() + " " + p.getLastName()).equals(fullName)) {
                this.selectedPerson = person;
                return selectedPerson;
            }
        }
        return person;
    }



    public void setLanguageIndicator() {
        ImageView flagImage = new ImageView(mainCtrl.getPathToFlagImage());
        Text language = new Text(mainCtrl.getLanguageWithoutImagePath());
        languageIndicator.getChildren().addAll(language, flagImage);
    }

    /**
     * Method that determines whether a Person has actually been selected in order to facilitate filtering.
     *
     * @return Boolean, true if a Person has been selected.
     */
    public boolean validFiltering() {
        return this.selectedPerson != null;
    }

    /**
     * Method that shows all Expenses in selected Event, if applicable.
     *
     * @param event Event in the page currently viewed
     */
    public void showAllExpensesInEvent(ActionEvent event) {
        resetFilteringPane();
        showAllExpensesFiltered(this.expenses);
    }

    /**
     * Method that shows all Expenses made by the selected Person iof applicable.
     *
     * @param event Event in the page currently viewed
     */
    public void showAllExpensesFromPerson(ActionEvent event) {
        resetFilteringPane();
        if (validFiltering()) {
            System.out.println("Cannot filter properly, no Person is selected");
            return;
        }
        List<Expense> selectedExpenses = new ArrayList<>();
        if (this.expenses == null) {
            selectedExpenses = null;
        } else {
            for (Expense expense : this.expenses) {
                if (expense.getReceiver().equals(selectedPerson)) {
                    selectedExpenses.add(expense);
                }
            }
        }
        showAllExpensesFiltered(selectedExpenses);
    }

    /**
     * Method that shows all Expenses associated with the selected Person if applicable.
     *
     * @param event Event in the page currently viewed
     */
    public void showAllExpensesWithPerson(ActionEvent event) {
        resetFilteringPane();
        if (validFiltering()) {
            System.out.println("Cannot filter properly, no Person is selected");
            return;
        }
        List<Expense> selectedExpenses = new ArrayList<>();
        if (this.expenses == null) {
            selectedExpenses = null;
        } else {
            for (Expense expense : this.expenses) {
                if (expense.getInvolved().contains(selectedPerson)) {
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

    /**
     * Removes an Expense from its association to Event
     *
     * @param expense Expense to remove
     * @return true, if successful
     */
    public boolean removeExpense(Expense expense) {
        if (this.event == null) {
            System.out.println("Event is null!");
            return false;
        }
        if (expense == null) {
            System.out.println("Expense is null!");
            return false;
        }
        if (!this.event.containsExpense(expense)) {
            System.out.println("Event doesn't contain the Expense!");
            return false;
        }
        this.event.removeExpense(expense);
        server.updateEvent(this.event.getId(), this.event);
        return true;
    }

    /**
     * Edits an Expense to a new one. Assumes Debts are automatically updated.
     *
     * @param replacedExpense replaced Expense
     * @param newExpense      new Expense
     * @return boolean, true if the operation was successful
     */
    public boolean editExpense(Expense replacedExpense, Expense newExpense) {
        if (replacedExpense == null) {
            System.out.println("Replaced Expense is null!");
            return false;
        }
        if (!this.removeExpense(replacedExpense)) {
            System.out.println("Could not delete the Expense!");
            return false;
        }
        this.event.addExpense(newExpense);
        server.updateEvent(this.event.getId(), this.event);
        return true;
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

    public String getEventName() {
        return overviewLabel.getText();
    }

    /**
     * Method that sends an email containing the inviteCode of the Event to the email address.
     *
     * @param event
     * @return boolean, true if the action was successfully executed.
     */
    public boolean sendInvite(ActionEvent event) {
        if (this.event == null) {
            System.out.println("Event is null! Cannot send out an invite!");
            return false;
        }
        if (this.event.getInviteCode() == null) {
            System.out.println("Invite Code could not be properly parsed!");
            return false;
        }
        String email = this.emailField.getText();
        if (email == null) {
            System.out.println("No email has been detected!");
            return false;
        }
        // <INSERT METHOD> someCreativeName(email, inviteCode)
        return true;
    }

}
