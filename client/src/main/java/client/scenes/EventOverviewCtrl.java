package client.scenes;

import client.utils.ServerUtils;
import commons.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class EventOverviewCtrl implements Initializable {

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
    @FXML
    private Button hideEditPage;


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

    private ObservableList<Expense> expenseData;
    @FXML
    private ListView<Expense> expenseListView;
    @Inject
    public EventOverviewCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    public void refresh() {
        event = server.getEvent(eventId);
        var expenses = event.getExpenses();
        expenseData = FXCollections.observableList(expenses);
        expenseListView.setItems(expenseData);
        showAllExpensesInEvent(new ActionEvent());

    }
    public void setLanguageIndicator() {
        ImageView flagImage = new ImageView(mainCtrl.getPathToFlagImage());
        Text language = new Text(mainCtrl.getLanguageWithoutImagePath());
        languageIndicator.getChildren().addAll(language, flagImage);
    }

    /**
     * Action that opens up the Stats Page for the associated Event.
     * @param event event that triggers the method
     * @throws IOException Possible IO Exception due to bad arguments
     */
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

    /**
     * Method that refreshes an Event's invite code and reflects it on the Server.
     * @param event event that triggers the method
     */
    public void refreshInviteCode(ActionEvent event) {
        this.event.refreshInviteCode();
        this.inviteCodeLabel.setText(this.event.getInviteCode());
        server.updateEvent(this.event.getId(), this.event);
    }

    /**
     * Getter for the current Event shown on the page.
     * @return Event object being shown.
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Method that computes the currently SelectedPerson. Additionally, sets UI elements to reflect this change.
     */
    public void computeSelectedPerson() {
        String fullName;
        if (showAllParticipantsInEventComboBox.getValue() != null) {
            fullName = showAllParticipantsInEventComboBox.getValue();
        } else {
            this.selectedPerson = null;
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
    }

    /**
     * Method that whenever is called adds/removes the respective Expense argument from
     * the list of currently selected Expenses.
     * @param expense Expense to add
     */
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

    /**
     * Method that whenever is called adds/removes the respective Tag argument from the list of currently selected Tags
     * @param tag Tag to add/remove
     */
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

        server.registerForAddition("/topic/expenses", Expense.class, e -> {
            Platform.runLater(() -> {
                expenseData.add(e);
                refresh();
            });

        });
        EditTitlePane.setVisible(false);
        try {
            Image refreshImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/client/images/Refresh.png")));
            ImageView refreshImageView = new ImageView(refreshImage);
            refreshImageView.setTranslateX(-4);
            refreshImageView.setFitHeight(20);
            refreshImageView.setFitWidth(20);
            refreshInviteCodeButton.setGraphic(refreshImageView);
            this.showAllParticipantsInEventComboBox.setItems(FXCollections.observableArrayList(new ArrayList<String>(List.of("Participants"))));
            eventId = eventID;
            this.event = server.getEvent(eventID);
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
            selectedExpenses = new ArrayList<>();
            participants = new ArrayList<>();
            participants.addAll(event.getParticipants());
            showAllParticipantsInEventComboBox.setItems(FXCollections.observableArrayList(
                    participants.stream().map(p -> p.getFirstName() + " " + p.getLastName()).toList()
            ));
            showAllParticipantsInEventComboBox.setOnAction(e -> {
                computeSelectedPerson();
                showAllParticipantsInEvent(e);
            });
            //showAllParticipantsInEventComboBox.setOnAction(this::showAllParticipantsInEvent);

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
            refreshLastVisited();
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void refreshLastVisited() {
        for (Person person: participants) {
            if (person.getUser().getId() == mainCtrl.getUserId()) {
                person.updateLastVisited();
                System.out.println("REFRESHED!!! " + person.getLastVisited());
            }
        }

        server.updateEvent(event.getId(), event);
    }

    public void choosePersonsVisibilityCheck() {
        if (this.selectedPerson == null) {
            this.goToEditPersonButton.setVisible(false);
            this.removePersonButton.setVisible(false);
        } else {
            this.goToEditPersonButton.setVisible(true);
            this.removePersonButton.setVisible(true);
        }
    }

    /**
     * Method that handles all logic for selecting a Person and all buttons related to it.
     * @param event event that triggers the method
     */
    public void showAllParticipantsInEvent(ActionEvent event) {
        computeSelectedPerson();
        this.choosePersonsVisibilityCheck();
    }

    /**
     * Method that redirects the User to another Page where they can edit the Details of the selected Person.
     * @param event event that triggers the method
     * @throws IOException Possible IOException due to bad arguments
     */
    public void goToEditPerson(ActionEvent event) throws IOException {
        if (!validPersonSelection()) {
            System.out.println("Cannot edit Person as none was selected.");
        } else {
            mainCtrl.showAddParticipant(eventId, true, selectedPerson);
        }
    }

    /**
     * Method that removes the Selected Person, if possible, from the associated Event and persists the change
     * to the Database.
     * @param event event that triggers the method
     * @throws IOException Possible IOException due to bad arguments
     */
    public void removePerson(ActionEvent event) throws IOException {
        if (!validPersonSelection()) {
            System.out.println("Cannot remove Person as none was selected.");
        } else {
            this.severPersonConnection(selectedPerson);
        }
    }

    /**
     * Method that removes any association a Person has with the current Event and persists the change
     * to the database.
     * @param person Person to remove from the Event.
     */
    public void severPersonConnection(Person person) {
        if (this.event == null) {
            System.out.println("Event is null!");
            return;
        }
        if (!validPersonSelection()) {
            System.out.println("Person is null!");
            return;
        }
        if (!this.event.isAttending(person)) {
            System.out.println("Event doesn't contain the Person!");
            return;
        }
        this.event.severPersonConnection(person);
        server.deletePerson(person.getId());
        server.updateEvent(this.event.getId(), this.event);
        this.setup(eventId);
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
        } else {
            this.filteringExpensesPane.setVisible(true);
            this.goToEditExpenseButton.setVisible(true);
            this.removeExpensesButton.setVisible(true);
        }
    }

    /**
     * Method that allows for All Expenses to be shown without having a Selected Person exist.
     */
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
        this.filteringExpensesPane.setLayoutX(175);
        this.filteringExpensesPane.setLayoutY(170);
        this.filteringExpensesPane.setPrefWidth(85);
        this.filteringExpensesPane.setPrefHeight(280);
    }

    /**
     * Method that resets the Tags Pane to allow for changes to be better reflected.
     */
    public void resetTagsPane() {
        CheckBox box = new CheckBox();
        for (Object element : this.chooseTagsPane.getChildren()) {
            if (element.getClass() == box.getClass()) {
                ((CheckBox) element).setVisible(false);
            }
        }
        this.chooseTagsPane.getChildren().removeAll();
        this.chooseTagsPane.setLayoutX(625);
        this.chooseTagsPane.setLayoutY(142);
        this.chooseTagsPane.setPrefWidth(100);
        this.chooseTagsPane.setPrefHeight(280);
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
     */
    public void showAllExpensesFiltered(List<Expense> selectedExpenses) {
        try {
            if (selectedExpenses == null) {
                System.out.println("Cannot filter properly, Expenses are null.");
                return;
            }
            if (selectedExpenses.isEmpty()) {
                System.out.println("The Event has no such Expenses associated with it.");
                Label label = new Label("There's nothing to display, silly!");
                filteringExpensesPane.getChildren().add(label);
                label.setLayoutY(5);
                return;
            }
            expenseListView.setItems(FXCollections.observableList(selectedExpenses));
            expenseCheckBoxes = filteringExpensesPane.getChildren().stream().map(t -> (CheckBox) t).toList();
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
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
        mainCtrl.showAddExpense(event.getId(), false, null);
    }

    /**
     * Method that redirects the User to an Edit Expense page if only one was selected.
     *
     * @param e event that triggers the method
     */

    public void goToEditExpense(ActionEvent e) throws IOException {
        Expense selectedExpense = expenseListView.getSelectionModel().getSelectedItem();
        if (selectedExpense == null) {
            System.out.println("Cannot edit expense as none was selected!");
            return;
        }

        System.out.println(selectedExpense);
        mainCtrl.showAddExpense(event.getId(), true, selectedExpense);
    }
    /**
     * Method that removes the Selected Expense from the Event.
     * @param event event that triggers the method
     */
    public void removeExpenses(ActionEvent event) throws IOException {
        Expense selectedExpense = expenseListView.getSelectionModel().getSelectedItem();
        if (selectedExpense == null) {
            return;
        }

        severExpenseConnection(selectedExpense);
        refresh();

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

    /**
     * Method that toggles between visible and invisible modes for several UI elements related to Tag selection
     * based on whether they should logically be shown.
     */
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

    /**4
     * Method that shows all the Tags in the Event as CheckBoxes.
     * @param event event that triggers the method
     */
    public void showAllTagsInEvent(ActionEvent event) {
        tagsVisibilityCheck();
        resetTagsPane();
        showAllTags(this.event.getTags());
    }

    /**
     * Method that populates and displays CheckBoxes filled with Tags on the Page to allow for selection.
     * @param tags tags to populate the Pane with.
     */
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

    /**
     * Method that redirects the User to a page to modify their selected Tag.
     * @param event event that triggers the method
     */
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
    }

    /**
     * Method that removes the Selected Tags from the Event. Is only carried out if no Expenses are associated with said
     * Tags. If they are, the method will remove elements in the order they were selected until a Tag in use is
     * discovered.
     * @param event event that triggers the method
     */
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
                    System.out.println("Successfully severed the connection with a Tag of the type: " + tag.getType());
                }
            }
        }
    }

    /**
     * Method that removes a specified Tag from an Event. Does not check if it is in use, only if it is associated
     * with the Event's List of Tags
     * @param tag Tag to remove.
     */
    public void severTagConnection(Tag tag) {
        if (this.event == null) {
            System.out.println("Event is null!");
            return;
        }
        if (tag == null) {
            System.out.println("Tag is null!");
            return;
        }
        if (!this.event.containsTag(tag)) {
            System.out.println("Event doesn't contain the Tag!");
            return;
        }
        this.event.deprecateTag(tag);
        server.updateEvent(this.event.getId(), this.event);
        this.setup(eventId);
    }

    public String getExpenseShownData(Expense e) {

        Double realAmount = BigDecimal.valueOf(e.getAmount() / 1.168958841856)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
       return e.getReceiver().getFirstName() + " paid " + realAmount +
                        e.getCurrency() + " for " + e.getDescription();

    }
    /**
     * Method that provides the currently selected Event's name
     * @return String, representing the Event's name
     */

    public String getEventName() {
        return overviewLabel.getText();
    }

    /**
     * Method that sends an email containing the inviteCode of the Event to the email address.
     *
     * @param event event that triggers the method
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
        if (email == null || email.isEmpty()) {
            System.out.println("No email has been detected!");
            return false;
        }
        this.sendMailToParticipants(emailField.getText(), this.event.getInviteCode(), this.getEventName());
        System.out.println("Successfully sent out an Email!");
        return true;
    }

    /**
     * Sends out an email to the specified Address in order to give an invite to the Event.
     * @param mail mail field that's filled in by the User. Represents the email address to send an invite to
     * @param inviteCode invite code to the current event to provide in the email
     * @param eventName name of the Event to use within the invite email
     */
    public void sendMailToParticipants(String mail, String inviteCode, String eventName){
        CreateEventCtrl.sendInviteMailToParticipants(mail, inviteCode, eventName, server);
    }

    /**
     * Method that shows the popup for editing an Event's name.
     * @param event event that triggers the method
     */
    public void showEditPage(ActionEvent event){
        if (EditTitlePane.isVisible()){
            EditTitlePane.setVisible(false);
        }
        else {
            EditTitlePane.setVisible(true);
        }
    }

    /**
     * Method that updates the Title of the Event within the popup.
     * @param event event that triggers the method
     */
    public void UpdateTitle(ActionEvent event) {
        String newTitle = editTitleTextField.getText();
        if (newTitle.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR");
            alert.setContentText("Please provide a valid title!");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure?");
        alert.setContentText("Are you sure you want to update the title of this event?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Event currentEvent = null;
            try {
                currentEvent = server.getEvent(eventId);
            } catch (Exception e) {
                System.out.println("An error occurred while fetching the current event!");
            }
            currentEvent.setName(newTitle);
            try {
                server.updateEvent(currentEvent);
            } catch (Exception e) {
                System.out.println("An error occurred whilst trying to persist the event!");
            }
            mainCtrl.showEventOverview(eventId);
        }
    }

}
