package client.scenes;

import client.utils.ServerUtils;
import commons.Currency;
import commons.User;
import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.File;
import java.io.PrintWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartPageCtrl {

    private MainCtrl mainCtrl;
    @FXML
    private TextField firstname;
    private List<String> fnText = new ArrayList<>(List.of("First name",
            "Voornaam"));
    @FXML
    private Label title;
    private List<String> titleText = new ArrayList<>(List.of("Sign Up",
            "Begin"));
    @FXML
    private TextField lastname;
    private List<String> lnText = new ArrayList<>(List.of("Last name",
            "Achternaam"));
    @FXML
    private TextField email;
    private List<String> emText = new ArrayList<>(List.of("Email (optional)",
            "Email (optioneel)"));
    @FXML
    private ComboBox<Currency> currencyComboBox;
    private List<String> cText = new ArrayList<>(List.of("Currency",
            "Munteenheid"));
    @FXML
    private ComboBox<String> languageComboBox;
    private List<String> lText = new ArrayList<>(List.of("Language",
            "Taal"));
    private ServerUtils server;
    List<String> languages;

    @Inject
    public StartPageCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }


    public void createUser(ActionEvent event) throws IOException {
        try {
            if (!isValidInput())
            {
                System.out.println("Every field needs to filled properly");
                return;
            }
            String emailHolder = "-1";
            if(!(email.getText().isEmpty()))
                emailHolder = email.getText();
            User u = new User(firstname.getText(), lastname.getText(), emailHolder, getCurrencyData() );

            u = server.addUser(firstname.getText(), lastname.getText(), emailHolder, getCurrencyData() );
            File file = mainCtrl.getUserConfig();
            if(!file.exists()){
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
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public boolean isValidInput() {
        if(firstname.getText().isEmpty())
            return false;
        if(lastname.getText().isEmpty())
            return false;
        if(getCurrencyData() == null)
            return false;
        if(getLanguageData() == null)
            return false;
        return true;
    }

    public Currency getCurrencyData() {
        return currencyComboBox.getValue();
    }
    public String getLanguageData() {
        return languageComboBox.getValue();
    }

    public void setup()  {
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
        int languageIndex = mainCtrl.getLanguageIndex();
        if(languageIndex < 0)
            languageIndex = 0;
        title.setText(titleText.get(languageIndex));
        lastname.setPromptText(lnText.get(languageIndex));
        firstname.setPromptText(fnText.get(languageIndex));
        email.setPromptText(emText.get(languageIndex));
        currencyComboBox.setPromptText(cText.get(languageIndex));
        languageComboBox.setPromptText(lText.get(languageIndex));
    }

}