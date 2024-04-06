package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.Tag;
import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.paint.Color;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static com.sun.prism.impl.PrismSettings.debug;

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
        sendMail();
        if (!isValidInput()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Event Creation Warning");
            alert.setContentText("Please fill all fields correctly!");
            alert.showAndWait();
            statusLabel.setStyle("-fx-font-weight: bold");
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Fill out every field correctly!");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Event Creation Alert");
        alert.setContentText("Do you want to create this event?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {
            //convert LocalDate to date
            LocalDate localDate = dateField.getValue();
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            Event newEvent = new Event(nameField.getText(), descField.getText(),
                    new ArrayList<>(), date, new ArrayList<>(), new ArrayList<>());

            server.createEvent(newEvent);
            statusLabel.setTextFill(Color.BLACK);
            ClipboardContent inviteCodeClipboard = new ClipboardContent();
            inviteCodeClipboard.putString(newEvent.getInviteCode());
            clipboard.setContent(inviteCodeClipboard);
            statusLabel.setText("Invite code: " + newEvent.getInviteCode() + " (Copied to clipboard!)");
        }
        else{
            nameField.setText(null);
            inviteField.setText(null);
            descField.setText(null);
            dateField.getEditor().clear();
        }
    }

    @FXML
    public void goHome(ActionEvent event) throws IOException {
       mainCtrl.showHome();
    }

    public void addTag() {

    }

    public void sendMail(){
        final String username = "use.splitty";
        final String password = "sbfs akue pjrj oiqt";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });


        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("use.splitty@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("duco.floris@gmail.com")
            );
            message.setSubject("Testing Gmail SSL");
            message.setText("Dear Mail Crawler,"
                    + "\n\n Please do not spam my email!");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
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
