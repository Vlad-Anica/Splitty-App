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
    private TextField publicUsername;
    @FXML
    private Button btnSubmitAll;
    @FXML
    private Text settingsTitle;
    @FXML
    private Button btnHome;
    @FXML
    private Text currencyText;
    @FXML
    private Text publicUsernameText;
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
        publicUsername.setText(currentUsername);
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
        publicUsernameText.setText(resourceBundle.getString("PublicUsername"));
        btnSelectServer.setText("ChangeServer");

    }
    public void clickBtnSubmitAll() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(alertTitle);
        alert.setContentText(alertText);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {
            currentUsername = publicUsername.getText();
            currency = (String) selectedCurrency.getValue();
        }
        else{
            publicUsername.setText(null);
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

    public void clickBtnHome() {
        mainCtrl.showHome();
    }
}
