package client.scenes;

import client.utils.ServerUtils;
import commons.Tag;
import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreateEventCtrl {

    @FXML
    private Button clearBtn;

    @FXML
    private Button createBtn;

    @FXML
    private DatePicker dateField;

    @FXML
    private Label dateLabel;

    @FXML
    private TextArea descField;

    @FXML
    private Label descLabel;

    @FXML
    private Button homeBtn;

    @FXML
    private TextArea inviteField;

    @FXML
    private Label inviteLabel;

    @FXML
    private TextField nameField;

    @FXML
    private Label nameLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private ComboBox<String> tagComboBox;

    @FXML
    private Button addTagBtn;

    private MainCtrl mainCtrl;
    private ServerUtils server;

    private List<Tag> tags;

    @Inject
    public CreateEventCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @FXML
    public void clearFields(ActionEvent event) {
        nameField.clear();
        dateField.getEditor().clear();
        inviteField.clear();
        descField.clear();
    }

    @FXML
    public void createEvent(ActionEvent event) {

        if (!isValidInput()) {
            statusLabel.setStyle("-fx-font-weight: bold");
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Fill out every field correctly!");
            return;
        }

        String inviteCode = generateRandomInviteCode();
        statusLabel.setTextFill(Color.BLACK);
        statusLabel.setText("Invite code: " + inviteCode);
    }

    @FXML
    public void goHome(ActionEvent event) throws IOException {
       mainCtrl.showHome();
    }

    void addTag() {

    }

    public boolean isValidInput() {

        if (nameField == null || nameField.getText().isEmpty())
            return false;

        if (descField == null || descField.getText().isEmpty())
            return false;

        if (dateField == null || dateField.getEditor().getText().isEmpty())
            return false;

        return true;
    }

    public String generateRandomInviteCode() {
        Random random = new Random();
        return random.ints(48, 123)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(7)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }

    public void setup() {

        tags = new ArrayList<>();
        tagComboBox.setItems(FXCollections.observableArrayList("Party", "Dinner",
                "Trip"));

    }

}
