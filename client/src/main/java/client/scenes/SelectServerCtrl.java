package client.scenes;

import client.utils.ServerUtils;
import jakarta.inject.Inject;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ResourceBundle;

public class SelectServerCtrl {
    MainCtrl mainCtrl;
    ServerUtils server;
    @FXML
    Text instructionMessage;
    @FXML
    TextField IPAddressField;
    @FXML
    TextField portField;
    @FXML
    Button btnConnect;
    @FXML
    StackPane pane;

    private String warningTitle;
    private String warningText;
    @Inject
    public SelectServerCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }



    public void setup() {
        setTextLanguage();
    }

    /**
     * when pressed the "Connect" button
     */
    public void connect() {
        String IPAddress = IPAddressField.getText();
        Integer port = 0;
        try {
            port = Integer.parseInt(portField.getText());
        } catch(NumberFormatException e) {
            showErrorMessage("InvalidPortNumberMessage");
            return;
        }
        if (IPAddress.contains(" ")) {
            showErrorMessage("NoSpaceCharsInIPAddress");
            return;
        }

        if (server.isOnline(IPAddress, port)) {
            server.setSERVER("http://" + IPAddress + ":" + port + "/");
            server.setWEBSOCKETSERVER("ws://" + IPAddress + ":" + port + "/websocket");
            System.out.println("starting ws connection");
            server.startConnection();

        } else {
            showErrorMessage("ServerNotFound");
            return;
        }
        if (mainCtrl.hasBeenConnected(IPAddress, port)) {
            mainCtrl.setServerInfo(IPAddress, port);
            mainCtrl.getLastKnownInfo();
            mainCtrl.showHome();
        } else {
            System.out.println("SIUUUU");
            mainCtrl.addNewServerInfo(IPAddress, port);
            mainCtrl.showStartPage();
        }

    }


    /**
     * displays and error message at the bottom of the page
     * @param resourceKey the key to the line we want in the language resource bundle
     */
    public void showErrorMessage(String resourceKey) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(warningTitle);
        alert.setContentText(warningText);
        alert.showAndWait();
        pane.getChildren().clear();
        ResourceBundle resourceBundle = mainCtrl.getLanguageResource();
        Text error = new Text(resourceBundle.getString(resourceKey));
        error.setStyle("-fx-text-fill: red;");
        setTextDown(error);

        PauseTransition delay = new PauseTransition(Duration.seconds(10));
        delay.setOnFinished(event -> {
            pane.getChildren().remove(error);
        });
        delay.play();
    }


    public void setTextDown(Text text) {
        pane.getChildren().add(text);
        StackPane.setAlignment(text, Pos.CENTER);
    }

    public void goHome() {
        mainCtrl.showHome();
    }

    public void setTextLanguage() {
        ResourceBundle resourceBundle = mainCtrl.getLanguageResource();
        instructionMessage.setText(resourceBundle.getString("ServerPageInstruction"));
        IPAddressField.setPromptText("IPAddress");
        portField.setPromptText("PortNumber");
        btnConnect.setText("Connect");
        warningTitle = resourceBundle.getString("PortWarning");
        warningText = resourceBundle.getString("Pleasechooseasuitableserver");
    }

}
