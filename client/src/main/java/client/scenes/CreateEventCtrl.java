package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.Tag;
import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
    private final Clipboard clipboard = Clipboard.getSystemClipboard();


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

        //convert LocalDate to date
        LocalDate localDate = dateField.getValue();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Event newEvent = new Event(nameField.getText(), descField.getText(),
                new ArrayList<>(), date, new ArrayList<>(), new ArrayList<>());

       // server.createEvent(newEvent);
        server.send("/app/events", newEvent);

        statusLabel.setTextFill(Color.BLACK);
        ClipboardContent inviteCodeClipboard = new ClipboardContent();
        inviteCodeClipboard.putString(newEvent.getInviteCode());
        clipboard.setContent(inviteCodeClipboard);
        statusLabel.setText("Invite code: " + newEvent.getInviteCode() + " (Copied to clipboard!)");


    }

    @FXML
    public void goHome(ActionEvent event) throws IOException {
       mainCtrl.showHome();
    }

    public void addTag() {

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
