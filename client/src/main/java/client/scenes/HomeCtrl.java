package client.scenes;


import client.utils.ServerUtils;
import commons.Event;
import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeCtrl  {
    private List<String> titleText = new ArrayList<>(List.of("Home",
            "Thuis"));

    @FXML
    private Label mainPageTestLabel;
    private List<String> mainPageTestLabelText = new ArrayList<>(List.of("Welcom To Splitty",
            "Welkom bij Splitty!"));
    @FXML
    private Button goDebtsButton;
    private List<String> goDebtsButtonText = new ArrayList<>(List.of("Open Debts",
            "Open schulden"));

    @FXML
    private Button goHomeButton;
    private List<String> goHomeButtonText = new ArrayList<>(List.of("Home", "Thuis"));

    @FXML
    private Button goSettingsButton;
    private List<String> goSettingsButtonText = new ArrayList<>(List.of("Settings",
            "Instellingen"));
    @FXML
    private Button goEventButton;
    private List<String> goEventButtonText = new ArrayList<>(List.of("Submit, ",
            "Verstuur"));
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
    private List<String> eventListText = new ArrayList<>(List.of("Select Event",
            "Kies Evenement"));

    List<String> languages;
    List<String> eventNames;
    List<Long> eventIds;
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
        languages = new ArrayList<>(List.of("English", "Nederlands"));
        languageList.setItems(FXCollections.observableList(languages.stream().toList()));
        languageList.getSelectionModel().select(mainCtrl.getLanguageIndex());
        languageList.setOnAction(event -> {
            mainCtrl.setLanguageIndex(languageList.getSelectionModel().getSelectedIndex());
            setTextLanguage();
        });
        List<Event> events = server.getEvents(1L);
        eventNames = new ArrayList<>();
        eventIds = new ArrayList<>();
        for (Event event: events) {
            eventNames.add(event.getName());
            eventIds.add(event.getId());
        }

        eventList.setItems(FXCollections.observableList(eventNames.stream().toList()));
    }

    public void setTextLanguage() {
        int languageIndex = mainCtrl.getLanguageIndex();
        if(languageIndex < 0)
            languageIndex = 0;
        mainPageTestLabel.setText(mainPageTestLabelText.get(languageIndex));
        goDebtsButton.setText(goDebtsButtonText.get(languageIndex));
        goHomeButton.setText(goHomeButtonText.get(languageIndex));
        goSettingsButton.setText(goSettingsButtonText.get(languageIndex));
        goEventButton.setText(goEventButtonText.get(languageIndex));
        eventList.setPromptText(eventListText.get(languageIndex));
        mainCtrl.getPrimaryStage().setTitle(titleText.get(languageIndex));
    }

    public void goToEvent() {
        int index = eventList.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            return;
        }
        Long eventId = eventIds.get(index);
        //TODO: go to the event based on its id
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
