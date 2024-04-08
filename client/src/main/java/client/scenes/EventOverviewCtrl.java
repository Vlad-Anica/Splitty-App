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
import javafx.scene.layout.Pane;
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
    private Label eventDateLabel;
    @FXML
    private Button goToStatsButton;
    @FXML
    private Label inviteCodeLabel;
    @FXML
    private Button refreshInviteCodeButton;
    @FXML
    private TextField emailField;
    @FXML
    private Button inviteButton;


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
    private Button showAllTagsInEvent;
    @FXML
    private List<CheckBox> tagsCheckBoxes;
    @FXML
    private AnchorPane chooseTagsPane;
    @FXML
    private Button goToEditTagButton;
    @FXML
    private Button removeTagButton;
    @FXML
    private Pane EditTitlePane;
    @FXML
    private Button editTitleButton;
    @FXML
    private TextField editTitleTextField;
    @FXML
    private Button SubmitEditButton;


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
        eventDateLabel.setText("<<DATE>>");
        inviteCodeLabel.setText("<<CODE>>");
    }

    public void refreshInviteCode(ActionEvent event) {
        this.event.refreshInviteCode();
        this.inviteCodeLabel.setText(this.event.getInviteCode());
        server.updateEvent(this.event.getId(), this.event);
    }

    public Event getEvent() {
        return event;
    }

    /**
     * Method that selects the Selected Person and also returns it.
     */
    public void computeSelectedPerson() {
        String fullName = null;
        Person person = null;
        if (showAllParticipantsInEventComboBox.getValue() != null) {
            fullName = showAllParticipantsInEventComboBox.getValue();
        } else {
            this.selectedPerson = person;
            this.renameFilters();
            return;
        }
        for (Person p : this.event.getParticipants()) {
            System.out.println("Finding the selected Participant...");
            if ((p.getFirstName() + " " + p.getLastName()).equals(fullName)) {
                this.selectedPerson = p;
                this.renameFilters();
                return;
            }
        }
        this.renameFilters();
        return;

    }

    public void computeSelectedExpenses() {
        ArrayList<Expense> expenses = new ArrayList<>();
        String text = null;
        for (CheckBox box : this.expenseCheckBoxes) {
            if (box.isSelected()) {
                text = box.getText();
            } else {
                continue;
            }
            for (Expense e : this.event.getExpenses()) {
                if ((e.getTag() + ", paid by " + e.getReceiver().getFirstName() + " " + e.getReceiver().getLastName()).equals(text)) {
                    this.selectedExpenses.add(e);
                    return;
                }
            }
            return;
        }
        this.selectedExpenses = expenses;
    }

    public void toggleInSelectedExpenses(Expense expense) {
        if (this.selectedExpenses == null) {
            this.selectedExpenses = new ArrayList<>();
        }
        if (this.selectedExpenses.contains(expense)) {
            this.selectedExpenses.remove(expense);
        } else {
            this.selectedExpenses.add(expense);
        }
    }

    public void toggleInSelectedTags(Tag tag) {
        if (this.selectedTags == null) {
            this.selectedTags = new ArrayList<>();
        }
        if (this.selectedTags.contains(tag)) {
            this.selectedTags.remove(tag);
        } else {
            this.selectedTags.add(tag);
        }
    }

    /**
     * Method that initialises the page and other useful fields.
     *
     * @param eventID event ID that represents the Event being parsed here.
     */
    public void setup(Long eventID) {
        EditTitlePane.setVisible(false);
        try {
            this.showAllParticipantsInEventComboBox.setItems(FXCollections.observableArrayList());
            eventId = eventID;
            this.event = server.getEvent(eventID);

            /*
            if(this.event.getInviteCode().equals("OW99THEL")) {

                this.event.addParticipant(new Person("Obama", "bARACK", "34", "444", "343", Currency.EUR, 0, this.event, new User()));
            }
            */
            eventDateLabel.setText(event.getDate().toString());
            inviteCodeLabel.setText(event.getInviteCode());
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
            showAllParticipantsInEventComboBox.setItems(FXCollections.observableArrayList(
                    participants.stream().map(p -> p.getFirstName() + " " + p.getLastName()).toList()
            ));
            showAllParticipantsInEventComboBox.setOnAction(this::showAllParticipantsInEvent);

            this.choosePersonsPane.setVisible(false);
            this.goToEditPersonButton.setVisible(false);
            this.removePersonButton.setVisible(false);
            if (validPersonSelection()) {
                this.goToEditPersonButton.setVisible(true);
                this.removePersonButton.setVisible(true);
            }

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
        if (this.selectedPerson == null) {
            resetPersonsPane();
            this.choosePersonsPane.setVisible(false);
            this.goToEditPersonButton.setVisible(false);
            this.removePersonButton.setVisible(false);
        } else {
            this.choosePersonsPane.setVisible(true);
            this.goToEditPersonButton.setVisible(true);
            this.removePersonButton.setVisible(true);
        }
    }

    public void showAllParticipantsInEvent(ActionEvent event) {
        computeSelectedPerson();
        this.choosePersonsVisibilityCheck();
    }

    public void goToEditPerson(ActionEvent event) throws IOException {
        if (!validPersonSelection()) {
            System.out.println("Cannot edit Person as none was selected.");
        } else {
            //goToEditPerson() tbi etc
        }
    }


    public void removePerson(ActionEvent event) throws IOException {
        if (!validPersonSelection()) {
            System.out.println("Cannot remove Person as none was selected.");
        } else {
            this.severPersonConnection(selectedPerson);
        }
    }

    public boolean severPersonConnection(Person person) {
        if (this.event == null) {
            System.out.println("Event is null!");
            return false;
        }
        if (!validPersonSelection()) {
            System.out.println("Person is null!");
            return false;
        }
        if (!this.event.isAttending(person)) {
            System.out.println("Event doesn't contain the Person!");
            return false;
        }
        this.event.severPersonConnection(person);
        server.updateEvent(this.event.getId(), this.event);
        this.setup(eventId);
        return true;
    }

    /**
     * Method that sets or resets visibility to buttons related to filtering and selecting Expenses.
     */
    public void expenseFilteringVisibilityCheck() {
        if (this.selectedPerson == null) {
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

    public void expenseFilteringVisibilityCheckNoSelection() {
        this.filteringExpensesPane.setVisible(true);
        this.goToEditExpenseButton.setVisible(true);
        this.removeExpensesButton.setVisible(true);
    }

    /**
     * Method that determines whether a Person has actually been selected in order to facilitate filtering.
     *
     * @return Boolean, true if a Person has been selected.
     */
    public boolean validPersonSelection() {
        return this.selectedPerson != null && this.event.isAttending(selectedPerson);
    }

    /**
     * Method that resets the parameters of the expense filtering pane as needed.
     */
    private void resetExpenseFilteringPane() {

        CheckBox box = new CheckBox();
        Label label = new Label();
        for (Object element : this.filteringExpensesPane.getChildren()) {
            if (element.getClass() == box.getClass()) {
                ((CheckBox) element).setVisible(false);
            }
            if (element.getClass() == label.getClass()) {
                ((Label) element).setVisible(false);
            }
        }
        this.filteringExpensesPane.getChildren().removeAll();
        this.filteringExpensesPane.setLayoutX(163);
        this.filteringExpensesPane.setLayoutY(160);
        this.filteringExpensesPane.setPrefHeight(174);
        this.filteringExpensesPane.setPrefWidth(80);
    }

    private void resetPersonsPane() {
        this.choosePersonsPane.getChildren().removeAll();
        this.choosePersonsPane.setLayoutX(14);
        this.choosePersonsPane.setLayoutY(120);
        this.choosePersonsPane.setPrefHeight(216);
        this.choosePersonsPane.setPrefWidth(149);
    }

    public void resetTagsPane() {
        CheckBox box = new CheckBox();
        for (Object element : this.chooseTagsPane.getChildren()) {
            if (element.getClass() == box.getClass()) {
                ((CheckBox) element).setVisible(false);
            }
        }
        this.chooseTagsPane.getChildren().removeAll();
        this.chooseTagsPane.setLayoutX(454);
        this.chooseTagsPane.setLayoutY(133);
        this.chooseTagsPane.setPrefHeight(200);
        this.chooseTagsPane.setPrefWidth(143);
    }

    /**
     * Upon selecting a Person, this method should be called in order to rename the filters.
     */
    public void renameFilters() {
        if (!validPersonSelection()) {
            System.out.println("Selected Person is null!!! Filter refresh aborted and reset to normal.");
            this.showExpensesWithPersonButton.setText("Select a Person!");
            this.showExpensesFromPersonButton.setText("Select a Person!");
            return;
        }
        try {
            showExpensesFromPersonButton.setText("From " + selectedPerson.getFirstName() + " " + selectedPerson.getLastName());
            showExpensesWithPersonButton.setText("Including " + selectedPerson.getFirstName() + " " + selectedPerson.getLastName());

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
        expenseFilteringVisibilityCheckNoSelection();
        resetExpenseFilteringPane();
        showAllExpensesFiltered(this.event.getExpenses());
    }

    /**
     * Method that shows all Expenses made by the selected Person iof applicable.
     *
     * @param event Event in the page currently viewed
     */
    public void showAllExpensesFromPerson(ActionEvent event) {
        expenseFilteringVisibilityCheck();
        resetExpenseFilteringPane();
        if (!validPersonSelection()) {
            System.out.println("Cannot filter properly, no Person is selected");
            return;
        }
        List<Expense> selectedExpenses = new ArrayList<>();
        if (this.event.getExpenses() == null) {
            selectedExpenses = null;
        } else {
            for (Expense expense : this.event.getExpenses()) {
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
        if (!validPersonSelection()) {
            System.out.println("Cannot filter properly, no Person is selected");
            return;
        }
        List<Expense> selectedExpenses = new ArrayList<>();
        if (this.event.getExpenses() == null) {
            selectedExpenses = null;
        } else {
            for (Expense expense : this.event.getExpenses()) {
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
                System.out.println("The Event has no such Expenses associated with it.");
                Label label = new Label("There's nothing to display, silly!");
                filteringExpensesPane.getChildren().add(label);
                label.setLayoutY(5);
                return true;
            }
            int y = 5;
            for (Expense e : selectedExpenses) {
                CheckBox newBox = new CheckBox(
                        e.getTag() + ", paid by " + e.getReceiver().getFirstName() + " " + e.getReceiver().getLastName());
                filteringExpensesPane.getChildren().add(newBox);
                newBox.setOnAction(event -> toggleInSelectedExpenses(e));
                newBox.setLayoutY(y);
                y += 25;
            }
            expenseCheckBoxes = filteringExpensesPane.getChildren().stream().map(t -> (CheckBox) t).toList();
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
        if (!this.severExpenseConnection(replacedExpense)) {
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
                this.severExpenseConnection(expense);
                this.selectedExpenses.remove(expense);
            }
            this.setup(eventId);
        }
    }

    /**
     * Removes an Expense from its association to Event
     *
     * @param expense Expense to remove
     * @return true, if successful
     */
    public boolean severExpenseConnection(Expense expense) {
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
        this.setup(eventId);
        return true;
    }


    public void tagsVisibilityCheck() {
        if (this.goToEditTagButton.isVisible()) {
            resetTagsPane();
            this.chooseTagsPane.setVisible(false);
            this.goToEditTagButton.setVisible(false);
            this.removeTagButton.setVisible(false);
        } else {
            this.chooseTagsPane.setVisible(true);
            this.goToEditTagButton.setVisible(true);
            this.removeTagButton.setVisible(true);
        }
    }

    public void showAllTagsInEvent(ActionEvent event) {
        tagsVisibilityCheck();
        resetTagsPane();
        showAllTags(this.event.getTags());
    }

    public void showAllTags(List<Tag> tags) {
        int y = 5;
        for (Tag t : tags) {
            CheckBox newBox = new CheckBox(t.getType());
            newBox.setOnAction(event -> toggleInSelectedTags(t));
            chooseTagsPane.getChildren().add(newBox);
            newBox.setLayoutY(y);
            y += 25;
        }
        tagsCheckBoxes = chooseTagsPane.getChildren().stream().map(t -> (CheckBox) t).toList();
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
            for (Tag tag : this.event.getTags()) {
                for (Expense expense : this.event.getExpenses()) {
                    if (expense.getTag().equals(tag)) {
                        System.out.println("Cannot proceed with operation as Tag is currently in use.");
                        return;
                    }
                    this.severTagConnection(tag);
                }
            }
        }
    }

    public boolean severTagConnection(Tag tag) {
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
        this.event.deprecateTag(tag);
        server.updateEvent(this.event.getId(), this.event);
        this.setup(eventId);
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

    public void showEditPage(ActionEvent event){
        EditTitlePane.setVisible(true);
    }

    public void UpdateTitle(ActionEvent event){
        String newTitle = editTitleTextField.getText();
        Event currentEvent = null;
        try {
            currentEvent = server.getEvent(eventId);
        } catch (Exception e){
            System.out.println("An error occured while fetching the current event!");
        }
        currentEvent.setName(newTitle);
        try {
            server.updateEvent(currentEvent);
        } catch (Exception e){
            System.out.println("An error occured whilst trying to persist the event!");
        }
        mainCtrl.showEventOverview(eventId);
    }

}
