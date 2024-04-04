package client.scenes;


import client.sceneSupportClasses.LanguageListListCell;
import client.utils.ServerUtils;
import commons.Event;
import commons.Person;
import commons.User;
import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomeCtrl {

    @FXML
    private Label mainPageTestLabel;
    @FXML
    private Button goDebtsButton;
    @FXML
    private Button goHomeButton;
    @FXML
    private Button goSettingsButton;
    @FXML
    private Button goEventButton;
    @FXML
    private Button addParticipantButton;
    @FXML
    private Button addExpenseButton;
    @FXML
    private Button addLanguageButton;
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;
    @FXML
    private ComboBox<String> languageList;
    @FXML
    private ComboBox<String> eventList;
    @FXML
    private PasswordField adminPasswordField;
    @FXML
    private Label adminPasswordMessage;
    @FXML
    private Label adminLogInLabel;
    @FXML
    private Button adminLogInButton;
    @FXML
    private Button createEventBtn;
    @FXML
    TextField inviteCodeText;
    @FXML
    Button btnSearchEvent;
    List<String> languages;
    List<String> eventNames;
    List<Long> eventIds;
    ObservableList<Event> eventData;
    private MainCtrl mainCtrl;

    private ServerUtils server;

    @Inject
    public HomeCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * set up the home page
     */
    public void setup() {

        server.registerForAddition("/topic/events", Event.class, e -> {
            eventData.add(e);
            System.out.println("refreshed");
            refresh();
        });

        System.out.println("!!!!! " + server.getAllEvents().size());
        setTextLanguage();
        languages = mainCtrl.getLanguages();
        System.out.println("Combobox: " + languages);
        languageList.setButtonCell(new LanguageListListCell());
        if(mainCtrl.getRestart()){
            languageList.setItems(FXCollections.observableList(List.of("Restart")));
            languageList.getSelectionModel().select(0);
        }else{

            languageList.setItems(FXCollections.observableList(languages));
            languageList.getSelectionModel().select(mainCtrl.getLanguageIndex());
            languageList.setOnAction(event -> {
                int selection = languageList.getSelectionModel().getSelectedIndex();
                System.out.println("selection: " + selection);
                if (selection >= 0) {
                    mainCtrl.setLanguageIndex(selection);
                    mainCtrl.setLanguage(languages.get(selection));
                    mainCtrl.save(new Pair<>(mainCtrl.getLanguageIndex(), mainCtrl.getLanguages()));
                    setTextLanguage();
                }

        });}
        languageList.setCellFactory(c -> new LanguageListListCell());
        List<Event> events = server.getEvents(mainCtrl.getUserId());
        eventNames = new ArrayList<>();
        eventIds = new ArrayList<>();
        for (Event event : events) {
            eventNames.add(event.getName());
            eventIds.add(event.getId());
        }

        eventList.setItems(FXCollections.observableList(eventNames.stream().toList()));
    }

    public void refresh() {
        var events = server.getEvents(mainCtrl.getUserId());
        eventData = FXCollections.observableList(events);
        eventList.setItems(FXCollections.observableList(eventData.stream().map(Event::getName).toList()));

    }
    public void searchAndGoToEvent() {
        Event event = server.getEventByInviteCode(inviteCodeText.getText());
        User currentUser = server.getUserById(mainCtrl.getUserId());
        Person person = new Person(currentUser.getFirstName(), currentUser.getLastName(), currentUser.getEmail(), currentUser.getIBAN(),
                currentUser.getBIC(), currentUser.getPreferredCurrency(), 0.0, event, currentUser);
        server.addPerson(person);
        mainCtrl.showEventOverview(event.getId());
    }

    public void setTextLanguage() {
        int languageIndex = mainCtrl.getLanguageIndex();
        if (languageIndex < 0)
            languageIndex = 0;
        String language = mainCtrl.getLanguage();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.language_" + mainCtrl.getLanguageWithoutImagePath());
        btnSearchEvent.setText(resourceBundle.getString("Search"));
        inviteCodeText.setPromptText(resourceBundle.getString("InviteCode"));
        goDebtsButton.setText(resourceBundle.getString("OpenDebts"));
        goHomeButton.setText(resourceBundle.getString("Home"));
        goSettingsButton.setText(resourceBundle.getString("Settings"));
        goEventButton.setText(resourceBundle.getString("Submit"));
        eventList.setPromptText(resourceBundle.getString("SelectEvent"));
        addParticipantButton.setText(resourceBundle.getString("AddParticipant"));
        addExpenseButton.setText(resourceBundle.getString("AddExpense"));
        addLanguageButton.setText(resourceBundle.getString("AddLanguage"));
        mainCtrl.getPrimaryStage().setTitle(resourceBundle.getString("Home"));
        adminPasswordField.setPromptText(resourceBundle.getString("AdminPassword"));
        adminLogInButton.setText(resourceBundle.getString("LogIn"));
        if (!adminPasswordMessage.getText().isEmpty()) {
            adminPasswordMessage.setText(resourceBundle.getString("IncorrectPassword"));
        }
    }

    public void goToEvent() {
        int index = eventList.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            return;
        }
        Long eventId = eventIds.get(index);
        mainCtrl.showEventOverview(eventId);
    }

    public void goToSettings() {
        mainCtrl.showSettings();
    }


    public void goToAddParticipant(ActionEvent event) throws IOException {
        mainCtrl.showAddParticipant();
    }

    public void goToAddExpense(ActionEvent event) throws IOException {

    }

    public void goToDebts(ActionEvent event) throws IOException {
        mainCtrl.showOpenDebts();
    }

    public void goHome(ActionEvent event) throws IOException {
        refresh();
    }

    public void goToEventOverview(ActionEvent event) throws IOException {
        mainCtrl.showEventOverview();
    }

    public void goToAddLanguage(ActionEvent event) throws IOException {
        mainCtrl.showAddLanguage();
    }

    public void goToCreateEvent(ActionEvent event) throws IOException {
        mainCtrl.showCreateEvent();
    }
    /**
     * checks with the server whether the password is correct and displays if it is correct
     *
     * @param event event
     */
    public void adminLogIn(ActionEvent event) {
        if (adminPasswordField.getText().isEmpty() || !server.checkAdminPassword(adminPasswordField.getText())) {
            adminPasswordMessage.setText(ResourceBundle.getBundle("languages.language_" + mainCtrl.getLanguageWithoutImagePath()).getString("IncorrectPassword"));
            adminPasswordMessage.setTextFill(Color.rgb(210, 39, 30));
        } else {
            mainCtrl.showManagementOverview();
        }
        adminPasswordField.clear();
    }


}
