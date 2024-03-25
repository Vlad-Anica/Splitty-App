package client.scenes;

import client.utils.ServerUtils;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.ResourceBundle;

public class ManagementOverviewCtrl {
    @FXML
    private Button goHomeButton;
    @FXML
    private Label welcomeLabel;
    private MainCtrl mainCtrl;
    private ServerUtils server;

    @Inject
    public ManagementOverviewCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }
    public void setUp(){
        setLanguageText();
    }
    public void setLanguageText() {
        String language = mainCtrl.getLanguage();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.language_" + language);
        welcomeLabel.setText(resourceBundle.getString("WelcomeMO"));
        goHomeButton.setText(resourceBundle.getString("Home"));
    }
    public void goHome(ActionEvent event) throws IOException {
        mainCtrl.showHome();
    }
}
