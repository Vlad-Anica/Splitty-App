package client.scenes;

import client.utils.ServerUtils;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class AddLanguageCtrl{

    private MainCtrl mainCtrl;
    private ServerUtils server;

    @FXML
    private TextField name;
    @FXML
    private Label homepage;
    @FXML
    private Label languageName;
    @FXML
    private Button goHomeButton;

    @Inject
    public AddLanguageCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @FXML
    void addLanguage(ActionEvent event) {
        System.out.println("Adding language...");
    }
    public void setUp(){
        setLanguageText();
    }
    public void setLanguageText() {
        String language = mainCtrl.getLanguage();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.language_" + language);
        homepage.setText(resourceBundle.getString("Homepage"));
        languageName.setText(resourceBundle.getString("LanguageName"));
        goHomeButton.setText(resourceBundle.getString("Home"));
    }
    public void goHome(ActionEvent event) throws IOException {
        mainCtrl.showHome();
    }
}
