package client.scenes;

import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class SettingsCtrl {
    @FXML
    private ComboBox selectedCurrency;
    @FXML
    private TextField publicUsername;
    @FXML
    private Button btnSubmitAll;
    List<String> btnSubmitAllText = new ArrayList<>(List.of("Submit", "Submit (in Dutch)"));
    @FXML
    private Text settingsTitle;
    List<String> settingsTitleText = new ArrayList<>(List.of("Settings", "Settings (in Dutch)"));
    @FXML
    private Button btnHome;
    List<String> btnHomeText = new ArrayList<>(List.of("Home", "Home (in Dutch)"));
    private MainCtrl mainCtrl;

    private String currentUsername = "Sapunaru";
    private String currency = "EUR";
    @Inject
    public SettingsCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void setup() {
        selectedCurrency.setItems(FXCollections.observableList(List.of("EUR", "USD", "CHF", "GBP")));
        selectedCurrency.getSelectionModel().select(currency);
        publicUsername.setText(currentUsername);
        settingsTitle.setFont(Font.font("Verdana", 30));
        setLanguageText();
    }

    public void setLanguageText() {
        int languageIndex = mainCtrl.getLanguageIndex();
        btnSubmitAll.setText(btnSubmitAllText.get(languageIndex));
        settingsTitle.setText(settingsTitleText.get(languageIndex));
        btnHome.setText(btnHomeText.get(languageIndex));

    }
    public void clickBtnSubmitAll() {
        currentUsername = publicUsername.getText();
        currency = (String)selectedCurrency.getValue();
    }

    public void clickBtnHome() {
        mainCtrl.showHome();
    }
}
