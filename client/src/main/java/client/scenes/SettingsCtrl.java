package client.scenes;

import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;

public class SettingsCtrl {
    @FXML
    private ComboBox<String> selectedLanguage;
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
    private String language = "English";
    @Inject
    public SettingsCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void setup() {
        selectedLanguage.setItems(FXCollections.observableList(List.of("English", "Dutch")));
        selectedCurrency.setItems(FXCollections.observableList(List.of("EUR", "USD", "CHF", "GBP")));
        publicUsername.setText("Cristian Sapunaru #01");
        btnSubmitAll.setText("Submit");
        settingsTitle.setText("Settings");
        settingsTitle.setFont(Font.font("Verdana", 30));
        btnHome.setText("Home");
    }

    public void clickBtnSubmitAll() {
        currentUsername = publicUsername.getText();
        language = (String)selectedLanguage.getValue();
        currency = (String)selectedCurrency.getValue();
    }

    public void clickBtnHome() {
        mainCtrl.showHome();
    }
}
