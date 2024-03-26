package client.scenes;

import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

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

    }
    public void clickBtnSubmitAll() {
        currentUsername = publicUsername.getText();
        currency = (String)selectedCurrency.getValue();
    }

    public void clickBtnHome() {
        mainCtrl.showHome();
    }
}
