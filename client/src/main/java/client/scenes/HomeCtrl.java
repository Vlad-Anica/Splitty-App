package client.scenes;


import client.sceneSupportClasses.LanguageListListCell;
import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Person;
import commons.User;
import jakarta.inject.Inject;
import javafx.application.Platform;
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

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class HomeCtrl {

    @FXML
    private Label mainPageTestLabel;
    @FXML
    private Button goSettingsButton;
    @FXML
    private Button goEventButton;
    @FXML
    private Button addLanguageButton;
    @FXML
    Label homeWelcomeLabel;
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;
    @FXML
    private Button downloadBtn;
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
    @FXML
    ListView<Event> eventsListView;
    @FXML
    ListView<String> expenseListView;
    @FXML
    private Label eventsLabel;
    @FXML
    private Label expensesLabel;
    List<String> languages;
    List<String> eventNames;
    List<Long> eventIds;

    ObservableList<Event> eventData;
    ObservableList<Expense> expenseData;

    private MainCtrl mainCtrl;

    private ServerUtils server;
    private String warningTitle;
    private String warningText;
    private String alertTitle;
    private String alertText;
    private String welcomeText;
    private String firstName;
    private String downloadString;
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
            Platform.runLater(() -> {
                eventData.add(e);
                System.out.println("refreshed");
                refresh();
            });

        });
        downloadBtn.setOnAction(e -> {
            try {
                downloadTemplate();
            } catch (IOException | URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
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
            languageList.getItems().clear();
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

        //eventList.setItems(FXCollections.observableList(eventNames.stream().toList()));
    }

    public void refresh() {
        try {

            var events = server.getEvents(mainCtrl.getUserId());
            var expenses = server.getExpensesForUser(mainCtrl.getUserId());
            eventData = FXCollections.observableList(events);
            eventIds = events.stream().map(e -> e.getId()).toList();
            eventsListView.setItems(eventData);
            expenseData = FXCollections.observableList(expenses);
            expenseListView.setItems(FXCollections.observableList(
                    expenseData.stream().map(this::showExpenseDate).toList())
            );
        } catch (NullPointerException e)
        {
            System.out.println("No expenses associated with the user");
        }


    }
    public void setUserName(long userId){
        ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.language_" + mainCtrl.getLanguageWithoutImagePath());
        welcomeText = resourceBundle.getString("WelcomeText");
        User user = server.getUserById(userId);
        firstName = user.getFirstName();
        homeWelcomeLabel.setText(welcomeText + " " + firstName + "!");
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
        welcomeText = resourceBundle.getString("WelcomeText");
        homeWelcomeLabel.setText(welcomeText + " " + firstName + "!");
        inviteCodeText.setPromptText(resourceBundle.getString("InviteCode"));
        createEventBtn.setText(resourceBundle.getString("CreateEvent"));
        goSettingsButton.setText(resourceBundle.getString("Settings"));
        goEventButton.setText(resourceBundle.getString("Submit"));
        eventsLabel.setText(resourceBundle.getString("Events"));
        expensesLabel.setText(resourceBundle.getString("YourExpenses"));
        addLanguageButton.setText(resourceBundle.getString("AddLanguage"));
        mainCtrl.getPrimaryStage().setTitle(resourceBundle.getString("Home"));
        adminPasswordField.setPromptText(resourceBundle.getString("AdminPassword"));
        adminLogInButton.setText(resourceBundle.getString("LogIn"));
        warningTitle = resourceBundle.getString("WrongPasswordWarning");
        warningText = resourceBundle.getString("Pleaseinputthecorrectpassword");
        alertTitle = resourceBundle.getString("AdminLoginAlert");
        alertText = resourceBundle.getString("Doyouwanttologinasadmin");
        downloadString = resourceBundle.getString("DownloadTemplate");
        downloadBtn.setText(downloadString);
        if (!adminPasswordMessage.getText().isEmpty()) {
            adminPasswordMessage.setText(resourceBundle.getString("IncorrectPassword"));
        }
    }

    public void goToEvent() {
        Event event = eventsListView.getSelectionModel().getSelectedItem();
        if (event == null) {
            return;
        }
        mainCtrl.showEventOverview(event.getId());
    }

    public void goToSettings() {
        mainCtrl.showSettings();
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
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(warningTitle);
            alert.setContentText(warningText);
            alert.showAndWait();
            adminPasswordMessage.setText(ResourceBundle.getBundle("languages.language_" + mainCtrl.getLanguageWithoutImagePath()).getString("IncorrectPassword"));
            adminPasswordMessage.setTextFill(Color.rgb(210, 39, 30));
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(alertTitle);
            alert.setContentText(alertText);
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK)
                mainCtrl.showManagementOverview();
            else
                adminPasswordField.setText(null);
        }
        adminPasswordField.clear();
    }

    public void downloadTemplate() throws IOException, URISyntaxException {

        server.downloadLanguageTemplate();
    }

    public String showExpenseDate(Expense expense) {
        LocalDate shownDate = expense.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        String truncatedAmount = BigDecimal.valueOf(expense.getAmount())
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue()+"";
        String parsedDate = shownDate.format(DateTimeFormatter.ofPattern("d/MM/yyyy"));
        return expense.getDescription() + " " + truncatedAmount + expense.getCurrency()
                + ", " + parsedDate + "; " + expense.getReceiver().getEvent().getName();
    }
}
