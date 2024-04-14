package client.scenes;

import client.utils.ServerUtils;
import commons.Currency;
import commons.User;
import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;

import java.util.*;

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
                if (!(email.getText().isEmpty())) {
                    emailHolder = email.getText();
                    //sendWelcomeMail(emailHolder, firstname.getText());
                }
                User u = new User(firstname.getText(), lastname.getText(), emailHolder, getCurrencyData());
                u = server.addUser(firstname.getText(), lastname.getText(), emailHolder, getCurrencyData());
                mainCtrl.setUserId(u.getId());
                mainCtrl.setEmailAddress(emailHolder);

                System.out.println("User Created Successfully !!!");

                System.out.println("Saved email in the mailConfig.txt!");

                String userMail = mainCtrl.getEmailAddress();
                mainCtrl.sendWelcomeMail(userMail, u.getFirstName());

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
        setTextLanguage();
        currencyComboBox.setItems(FXCollections.observableList(
                List.of(Currency.EUR, Currency.USD,
                        Currency.CHF, Currency.GBP)));
        currencyComboBox.getSelectionModel().selectFirst();
        languages = new ArrayList<>(List.of("English", "Nederlands", "Romana"));
        languageComboBox.setItems(FXCollections.observableList(languages.stream().toList()));
        languageComboBox.getSelectionModel().select(mainCtrl.getLanguageIndex());
        languageComboBox.setOnAction(event -> {
            mainCtrl.setLanguageIndex(languageComboBox.getSelectionModel().getSelectedIndex());
            setTextLanguage();
        });

    }

    public void setTextLanguage() {
        ResourceBundle resourceBundle = mainCtrl.getLanguageResource();
        title.setText(resourceBundle.getString("Signup"));
        lastname.setPromptText(resourceBundle.getString("LastName"));
        firstname.setPromptText(resourceBundle.getString("FirstName"));
        email.setPromptText(resourceBundle.getString("Email(optional)"));
        currencyComboBox.setPromptText(resourceBundle.getString("Currency"));
        languageComboBox.setPromptText(resourceBundle.getString("Language"));
        alertTitle = resourceBundle.getString("UserCreationConfirmationAlert");
        alertText = resourceBundle.getString("Doyouwanttocreatethisuser");
        warningTitle = resourceBundle.getString("UserCreationWarning");
        warningText = resourceBundle.getString("Pleasefillallnecessaryfields");

    }
}