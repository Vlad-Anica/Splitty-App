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
    private ComboBox pickedLanguage;
    @FXML
    private ComboBox selectedCurrency;
    @FXML
    private TextField publicUsername;
    @FXML
    private Button btnUpdPublicUsername;
    @FXML
    private Text settingsTitle;
    @FXML
    private Button btnHome;
    private MainCtrl mainCtrl;
    @Inject
    public SettingsCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void setup() {
        pickedLanguage.setItems(FXCollections.observableList(List.of("English", "Dutch")));
        selectedCurrency.setItems(FXCollections.observableList(List.of("EUR", "USD", "CHF", "GBP")));
        publicUsername.setText("Cristian Sapunaru #01");
        btnUpdPublicUsername.setText("Submit");
        settingsTitle.setText("Settings");
        settingsTitle.setFont(Font.font("Verdana", 30));
        btnHome.setText("Home");
    }

    public void clickBtnUpdPublicUsername() {
        //TODO: update the new public username in the databse

    }

    public void clickBtnHome() {
        //TODO: add a home button functionality
    }
}
