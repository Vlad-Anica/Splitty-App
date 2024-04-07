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
    private MainCtrl mainCtrl;
    private String alertTitle;
    private String alertText;

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
        btnSubmitAll.setText(resourceBundle.getString("Submit"));
        settingsTitle.setText(resourceBundle.getString("Settings"));
        btnHome.setText(resourceBundle.getString("Home"));
        alertTitle = resourceBundle.getString("Submit Alert");
        alertText = resourceBundle.getString("Are you sure you want to submit?");

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

    public void clickBtnHome() {
        mainCtrl.showHome();
    }
}
