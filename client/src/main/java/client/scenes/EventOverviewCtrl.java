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
    TextFlow languageIndicator;
    @FXML
    private Label overviewLabel;
    @FXML
    private Button goHomeButton;
    @FXML
    private Label eventNameLabel;
    @FXML
    private Button goToStatsButton;
    @FXML
    private Label inviteCodeLabel;
    @FXML
    private TextField emailField;
    @FXML
    private Button inviteButton;


    @FXML
    private List<CheckBox> personCheckBoxes;
    @FXML
    private ComboBox<String> showAllParticipantsInEventComboBox;
    @FXML
    private AnchorPane choosePersonsPane;
    @FXML
    private Button goToEditPersonButton;
    @FXML
    private Button removePersonButton;

    @FXML
    private Label expensesLabel;
    @FXML
    private List<CheckBox> expenseCheckBoxes;
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
    private Button goToAddExpenseButton;
    @FXML
    private Button goToEditExpenseButton;
    @FXML
    private Button removeExpensesButton;

    @FXML
    private Label tagsLabel;
    @FXML
    private List<CheckBox> tagsCheckBoxes;
    @FXML
    private Button goToEditTagButton;
    @FXML
    private Button removeTagButton;

    private MainCtrl mainCtrl;
    private ServerUtils server;

    private Event event;
    private long eventId;
    private List<Person> participants;
    private Person selectedPerson;
    private List<Expense> expenses;
    private List<Expense> selectedExpenses;
    private List<Tag> tags;
    private List<Tag> selectedTags;


    @Inject
    public EventOverviewCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void setLanguageIndicator() {
        ImageView flagImage = new ImageView(mainCtrl.getPathToFlagImage());
        Text language = new Text(mainCtrl.getLanguageWithoutImagePath());
        languageIndicator.getChildren().addAll(language, flagImage);
    }

    public void goToStats(ActionEvent event) throws IOException {
        mainCtrl.showStatsTest(eventId);
    }

    /**
     * Resets text fields to original values for testing purposes.
     */
    private void resetTextFields() {
        showExpensesFromPersonButton.setText("From <<PERSON>>");
        showExpensesWithPersonButton.setText("Including <<PERSON>>");
        eventNameLabel.setText("<<EVENT>>");
        inviteCodeLabel.setText("<<CODE>>");
    }

    public Event getEvent() {
        return event;
    }

    /**
     * Method that selects the Selected Person and also returns it.
     */
    public void computeSelectedPerson() {
        String fullName = null;
        for (CheckBox box : this.personCheckBoxes) {
            if (box.isSelected()) {
                fullName = box.getText();
            } else {
                continue;
            }
            for (Person p : this.event.getParticipants()) {
                if ((p.getFirstName() + " " + p.getLastName()).equals(fullName)) {
                    this.selectedPerson = p;
                    this.renameFilters();
                    return;
                }
            }
            this.renameFilters();
            return;
        }
        this.selectedPerson = null;
        /*
        if (showAllParticipantsInEventComboBox.getValue() != null) {
            fullName = showAllParticipantsInEventComboBox.getValue();
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
        this.renameFilters();
        return person;
        */

    }

    public void computeSelectedExpenses() {
        ArrayList<Expense> expenses;
        String text = null;
    }

    /**
     * Method that consistently makes sure only one checkBox for Selected Person can be active at once.
     *
     * @param event
     */
    public void checkPersonBoxes(ActionEvent event) {
        computeSelectedPerson();
        if (selectedPerson == null) {
            return;
        }
        String name = selectedPerson.getFirstName() + " " + selectedPerson.getLastName();
        for (CheckBox box : personCheckBoxes) {
            box.setDisable(false);
            if (!box.getText().equals(name)) {
                box.setSelected(false);
            }
        }
    }

    public void checkExpenseBoxes(ActionEvent event) {

    }

    /**
     * Method that initialises the page and other useful fields.
     *
     * @param eventID event ID that represents the Event being parsed here.
     */
    public void setup(Long eventID) {

        try {
            eventId = eventID;
            this.event = server.getEvent(eventID);
            this.expenses = event.getExpenses();
            this.tags = event.getTags();
            eventNameLabel.setText(event.getName());
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
                newBox.setOnAction(this::checkPersonBoxes);
                choosePersonsPane.getChildren().add(newBox);
                newBox.setLayoutY(y);
                y += 25;
            }
            showAllParticipantsInEventComboBox.setItems(FXCollections.observableArrayList(
                    participants.stream().map(p -> p.getFirstName() + " " + p.getLastName()).toList()
            ));

            personCheckBoxes = choosePersonsPane.getChildren().stream().map(t -> (CheckBox) t).toList();
            computeSelectedPerson();
            checkPersonBoxes(new ActionEvent());
            this.choosePersonsPane.setVisible(false);
            this.showAllParticipantsInEventComboBox.setVisible(false);
            this.goToEditPersonButton.setVisible(false);
            this.removePersonButton.setVisible(false);


            this.filteringExpensesPane.setVisible(false);
            this.goToEditExpenseButton.setVisible(false);
            this.removeExpensesButton.setVisible(false);

            this.goToEditTagButton.setVisible(false);
            this.removeTagButton.setVisible(false);
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
    }

    public void choosePersonsVisibilityCheck() {

    }

    public void showAllParticipantsInEvent(ActionEvent event) {

    }

    public void goToEditPerson(ActionEvent event) throws IOException {

    }


    public void removePersons(ActionEvent event) throws IOException {

    }

    /**
     * Method that sets or resets visibility to buttons related to filtering and selecting Expenses.
     */
    public void expenseFilteringVisibilityCheck() {
        if (this.goToEditExpenseButton.isVisible()) {
            resetExpenseFilteringPane();
            this.filteringExpensesPane.setVisible(false);
            this.goToEditExpenseButton.setVisible(false);
            this.removeExpensesButton.setVisible(false);
            return;
        } else {
            this.filteringExpensesPane.setVisible(true);
            this.goToEditExpenseButton.setVisible(true);
            this.removeExpensesButton.setVisible(true);
        }
    }

    /**
     * Method that determines whether a Person has actually been selected in order to facilitate filtering.
     *
     * @return Boolean, true if a Person has been selected.
     */
    public boolean validPersonSelection() {
        return this.selectedPerson != null;
    }

    /**
     * Method that resets the parameters of the expense filtering pane as needed.
     */
    private void resetExpenseFilteringPane() {
        this.filteringExpensesPane.getChildren().removeAll();
        this.filteringExpensesPane.setLayoutX(294);
        this.filteringExpensesPane.setLayoutY(133);
        this.filteringExpensesPane.setPrefHeight(200);
        this.filteringExpensesPane.setPrefWidth(200);
    }

    /**
     * Upon selecting a Person, this method should be called in order to rename the filters.
     */
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
     * Method that shows all Expenses in selected Event, if applicable.
     *
     * @param event Event in the page currently viewed
     */
    public void showAllExpensesInEvent(ActionEvent event) {
        expenseFilteringVisibilityCheck();
        resetExpenseFilteringPane();
        showAllExpensesFiltered(this.expenses);
    }

    /**
     * Method that shows all Expenses made by the selected Person iof applicable.
     *
     * @param event Event in the page currently viewed
     */
    public void showAllExpensesFromPerson(ActionEvent event) {
        expenseFilteringVisibilityCheck();
        resetExpenseFilteringPane();
        if (validPersonSelection()) {
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
        expenseFilteringVisibilityCheck();
        resetExpenseFilteringPane();
        if (validPersonSelection()) {
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

    /**
     * Method that takes a List of Expenses and parses it to the UI to display it as a list of Expenses.
     *
     * @param selectedExpenses List of Expenses to show in the Pane.
     * @return boolean, true if the List was parsed successfully
     */
    public boolean showAllExpensesFiltered(List<Expense> selectedExpenses) {
        try {
            if (selectedExpenses == null) {
                System.out.println("Cannot filter properly, Expenses are null.");
                return false;
            }
            if (selectedExpenses.isEmpty()) {
                System.out.println("The Event has no Expenses associated with it.");
                CheckBox newBox = new CheckBox("There's nothing to display, silly!");
                filteringExpensesPane.getChildren().add(newBox);
                newBox.setLayoutY(5);
                return true;
            }
            int y = 5;
            for (Expense e : selectedExpenses) {
                CheckBox newBox = new CheckBox(
                        e.getTag() + ", paid by " + e.getReceiver().getFirstName() + " " + e.getReceiver().getLastName());
                filteringExpensesPane.getChildren().add(newBox);
                newBox.setOnAction(this::checkExpenseBoxes);
                newBox.setLayoutY(y);
                y += 25;
            }
            personCheckBoxes = filteringExpensesPane.getChildren().stream().map(t -> (CheckBox) t).toList();
            return true;
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return false;
        }
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

    public void goToAddExpense(ActionEvent e) throws IOException {
        mainCtrl.showAddExpense(event.getId());
    }

    /**
     * Method that redirects the User to an Edit Expense page if only one was selected.
     *
     * @param event
     * @throws IOException IO Exception that could occur
     */
    public void goToEditExpense(ActionEvent event) throws IOException {
        if (selectedExpenses == null || selectedExpenses.isEmpty()) {
            System.out.println("Cannot edit expense as none was selected!");
            return;
        }
        if (selectedExpenses.size() >= 2) {
            System.out.println("Cannot edit expense as multiple expenses have been selected at once.");
            return;
        }
        //goToExpenseEdit stuff tbi
        return;
    }

    public void removeExpenses(ActionEvent event) throws IOException {
        if (this.selectedExpenses == null || this.selectedExpenses.isEmpty()) {
            System.out.println("Cannot remove Expense as none was selected.");
        } else {
            for (Expense expense : this.selectedExpenses) {
                this.removeExpense(expense);
            }
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

    public void goToEditTag(ActionEvent event) {
        if (selectedTags == null || selectedTags.isEmpty()) {
            System.out.println("Cannot edit tag as none was selected!");
            return;
        }
        if (selectedTags.size() >= 2) {
            System.out.println("Cannot edit tag as multiple tags have been selected at once.");
            return;
        }
        //goToEdiTag stuff tbi
        return;
    }

    public void removeTags(ActionEvent event) {
        if (this.selectedTags == null || this.selectedTags.isEmpty()) {
            System.out.println("Cannot remove Tag as none was selected.");
        } else {
            for (Tag tag : this.tags) {
                for(Expense expense : expenses) {
                    if(expense.getTag().equals(tag)) {
                        System.out.println("Cannot proceed with operation as Tag is currently in use.");
                        return;
                    }
                    this.removeTag(tag);
                }
            }
        }
    }

    public boolean removeTag(Tag tag) {
        if (this.event == null) {
            System.out.println("Event is null!");
            return false;
        }
        if (tag == null) {
            System.out.println("Tag is null!");
            return false;
        }
        if (!this.event.containsTag(tag)) {
            System.out.println("Event doesn't contain the Tag!");
            return false;
        }
        this.event.removeTag(tag);
        server.updateEvent(this.event.getId(), this.event);
        return true;
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
