package client.scenes;

import client.utils.ServerUtils;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

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
            showInvalidPortNumberMessage();
        }

        if (server.isOnline(IPAddress, port)) {
            server.setSERVER("http://" + IPAddress + ":" + port + "/");
            mainCtrl
        } else {
            showServerNotFound();
        }

    }

    /**
     * show error text if you did not input a valid number in the port field
     */
    public void showInvalidPortNumberMessage() {
        ResourceBundle resourceBundle = mainCtrl.getLanguageResource();
        Text error = new Text(resourceBundle.getString("IvalidPortNumberMessage"));
        error.setStyle("-fx-text-fill: red;");
        setTextDown(error);
    }

    /**
     * show error message if the server you want to connect to cannot be found
     */
    public void showServerNotFound() {
        ResourceBundle resourceBundle = mainCtrl.getLanguageResource();
        Text error = new Text(resourceBundle.getString("ServerNotFound"));
        error.setStyle("-fx-text-fill: red;");
        setTextDown(error);
    }

    public void setTextDown(Text text) {
        BorderPane pane = new BorderPane();

// Create a StackPane, add the Text node to it, and set the alignment to center
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(text);
        StackPane.setAlignment(text, Pos.CENTER);

// Set the StackPane at the bottom of the BorderPane
        pane.setBottom(stackPane);
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
    }
    @Inject
    public SelectServerCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }


}
