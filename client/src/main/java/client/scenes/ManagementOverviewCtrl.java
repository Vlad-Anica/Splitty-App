package client.scenes;

import client.utils.ServerUtils;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManagementOverviewCtrl {
    @FXML
    private Button goHomeButton;
    private List<String> goHomeButtonText = new ArrayList<>(List.of("Home", "Thuis"));
    private List<String> welcomeLabelText = new ArrayList<>(List.of("Welcome to the management overview for admins",
            "Welkom bij het beheerders overzicht voor administratoren"));
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
        int languageIndex = mainCtrl.getLanguageIndex();
        welcomeLabel.setText(welcomeLabelText.get(languageIndex));
        goHomeButton.setText(goHomeButtonText.get(languageIndex));

    }
    public void goHome(ActionEvent event) throws IOException {
        mainCtrl.showHome();
    }
}
