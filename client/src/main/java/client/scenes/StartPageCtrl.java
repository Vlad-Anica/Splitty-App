package client.scenes;

import client.utils.ServerUtils;
import commons.Currency;
import commons.User;
import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.io.PrintWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class StartPageCtrl {

    private MainCtrl mainCtrl;
    @FXML
    private TextField firstname;

    @FXML
    private Label title;

    @FXML
    private TextField lastname;

    @FXML
    private TextField email;

    @FXML
    private ComboBox<Currency> currencyComboBox;

    @FXML
    private ComboBox<String> languageComboBox;

    private String warningTitle;
    private String warningText;
    private String alertTitle;
    private String alertText;

    private ServerUtils server;
    List<String> languages;

    @Inject
    public StartPageCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    public void createUser(ActionEvent event) throws IOException {
        try {
            if (!isValidInput()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(warningTitle);
                alert.setContentText(warningText);
                alert.showAndWait();
                return;
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(alertTitle);
            alert.setContentText(alertText);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                String emailHolder = "-1";
                if (!(email.getText().isEmpty()))
                    emailHolder = email.getText();
                User u = new User(firstname.getText(), lastname.getText(), emailHolder, getCurrencyData());

                u = server.addUser(firstname.getText(), lastname.getText(), emailHolder, getCurrencyData());
                File file = mainCtrl.getUserConfig();
                if (!file.exists()) {
                    file.createNewFile();
                }
                PrintWriter pw = new PrintWriter(file);
                pw.println(mainCtrl.getLanguageIndex());
                pw.println(u.getId());
                System.out.println(u.getId());
                pw.close();
                System.out.println("User Created Successfully !!!");
                mainCtrl.getLastKnownInfo();
                mainCtrl.showHome();
            } else {
                firstname.setText(null);
                lastname.setText(null);
                email.setText(null);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public boolean isValidInput() {
        if (firstname.getText().isEmpty())
            return false;
        if (lastname.getText().isEmpty())
            return false;
        if (getCurrencyData() == null)
            return false;
        if (getLanguageData() == null)
            return false;
        return true;
    }

    public Currency getCurrencyData() {
        return currencyComboBox.getValue();
    }

    public String getLanguageData() {
        return languageComboBox.getValue();
    }

    public void setup() {
        currencyComboBox.setItems(FXCollections.observableList(
                List.of(Currency.EUR, Currency.USD,
                        Currency.CHF, Currency.GBP)));
        currencyComboBox.getSelectionModel().selectFirst();
        languages = new ArrayList<>(List.of("English", "Nederlands"));
        languageComboBox.setItems(FXCollections.observableList(languages.stream().toList()));
        languageComboBox.getSelectionModel().select(mainCtrl.getLanguageIndex());
        languageComboBox.setOnAction(event -> {
            mainCtrl.setLanguageIndex(languageComboBox.getSelectionModel().getSelectedIndex());
            setTextLanguage();
        });

    }

    public void setTextLanguage() {
        ResourceBundle resourceBundle = mainCtrl.getLanguageResource();
        title.setText(resourceBundle.getString("Sign up"));
        lastname.setPromptText(resourceBundle.getString("Last name"));
        firstname.setPromptText(resourceBundle.getString("First name"));
        email.setPromptText(resourceBundle.getString("Email (optional)"));
        currencyComboBox.setPromptText(resourceBundle.getString("Currency"));
        languageComboBox.setPromptText(resourceBundle.getString("Language"));
        alertTitle = resourceBundle.getString("User Creation Confirmation Alert");
        alertText = resourceBundle.getString("Do you want to create this user?");
        warningTitle = resourceBundle.getString("User Creation Warning");
        warningText = resourceBundle.getString("Please fill all necessary fields");

    }
}