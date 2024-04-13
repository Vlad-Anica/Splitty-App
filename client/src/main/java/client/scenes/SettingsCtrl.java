package client.scenes;

import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class SettingsCtrl {
    @FXML
    private ComboBox selectedCurrency;
    @FXML
    private Button btnSubmitAll;
    @FXML
    private Text settingsTitle;
    @FXML
    private Button btnHome;
    @FXML
    private Text currencyText;
    @FXML
    private TextField firstNameField;
    @FXML
    private Text firstNameText;
    @FXML
    private Text lastNameText;
    @FXML
    private TextField lastNameField;
    @FXML
    private Text emailText;
    @FXML
    private TextField emailField;
    @FXML
    private Text passwordText;
    @FXML
    private TextField passwordField;
    @FXML
    private Text smtpText;
    @FXML
    private TextField smtpField;
    @FXML
    private Text portText;
    @FXML
    private TextField portField;
    @FXML
    private Text emailConfiguredText;
    @FXML
    private Button btnSubmitEmailConfig;
    @FXML
    private Button btnSelectServer;
    private MainCtrl mainCtrl;
    private String alertTitle;
    private String alertText;
    private String alertServerSwitchText;
    private String alertServerSwitchTitle;

    private String currentUsername = "Sapunaru";
    private String currency = "EUR";
    @Inject
    public SettingsCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void setup() {
        selectedCurrency.setItems(FXCollections.observableList(Stream.of("EUR", "USD", "CHF", "GBP").toList()));
        selectedCurrency.getSelectionModel().select(currency);

        firstNameField.setText(mainCtrl.getFirstName());
        lastNameField.setText(mainCtrl.getLastName());

        emailField.setText(mainCtrl.getEmailAddress());
        System.out.println("Settings email check: " + mainCtrl.getEmailPort());
        portField.setText(mainCtrl.getEmailPort());
        smtpField.setText(mainCtrl.getSmtp());
        passwordField.setText(mainCtrl.getEmailPassword());

        settingsTitle.setFont(Font.font("Verdana", 30));
        setLanguageText();
    }

    public void setLanguageText() {
        String language = mainCtrl.getLanguage();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.language_" + mainCtrl.getLanguageWithoutImagePath());
        mainCtrl.getPrimaryStage().setTitle(resourceBundle.getString("Settings"));
        btnSubmitAll.setText(resourceBundle.getString("Submit"));
        settingsTitle.setText(resourceBundle.getString("Settings"));
        btnHome.setText(resourceBundle.getString("Home"));
        alertTitle = resourceBundle.getString("SubmitAlert");
        alertText = resourceBundle.getString("Areyousureyouwanttosubmit");
        alertServerSwitchTitle = resourceBundle.getString("ServerSwitch");
        alertServerSwitchText = resourceBundle.getString("ConfirmServerSwitch");
        currencyText.setText(resourceBundle.getString("PreferredCurrency"));
        lastNameText.setText(resourceBundle.getString("LastName"));
        firstNameText.setText(resourceBundle.getString("FirstName"));
        emailText.setText(resourceBundle.getString("Email"));
        passwordText.setText(resourceBundle.getString("Password"));
        smtpText.setText(resourceBundle.getString("Smtp"));
        portText.setText(resourceBundle.getString("Port"));
        btnSubmitEmailConfig.setText(resourceBundle.getString("Save"));
        if (mainCtrl.checkEmailConfig()) {
            emailConfiguredText.setText(resourceBundle.getString("EmailIsConfigured"));
        } else {
            emailConfiguredText.setText(resourceBundle.getString("EmailNotConfigured"));
        }
        btnSelectServer.setText(resourceBundle.getString("ChangeServer"));

    }
    public void clickBtnSubmitAll() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(alertTitle);
        alert.setContentText(alertText);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {
            currency = (String) selectedCurrency.getValue();
            mainCtrl.setFirstName(firstNameField.getText());
            mainCtrl.setLastName(lastNameField.getText());
        }
        else{
            firstNameField.setText("");
            lastNameField.setText("");
        }
    }
    public void goToSelectServer() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(alertServerSwitchTitle);
        alert.setContentText(alertServerSwitchText);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
            mainCtrl.showSelectServer();
    }

    public void saveEmailConfig() {
        mainCtrl.setEmailAddress(emailField.getText());
        mainCtrl.setSmtp(smtpField.getText());
        mainCtrl.setEmailPassword(passwordField.getText());
        mainCtrl.setEmailPort(portField.getText());
        String language = mainCtrl.getLanguage();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.language_" + mainCtrl.getLanguageWithoutImagePath());
        if (mainCtrl.checkEmailConfig()) {
            emailConfiguredText.setText(resourceBundle.getString("EmailIsConfigured"));
        } else {
            emailConfiguredText.setText(resourceBundle.getString("EmailNotConfigured"));
        }
    }

    public void clickBtnHome() {
        mainCtrl.showHome();
    }
}
