package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.Person;
import commons.Currency;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;

public class AddParticipantCtrl {

    @FXML
    private Button goHomeButton;
    @FXML
    private Button goBackButton;
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;
    @FXML
    private Button addParticipantButton;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;
    @FXML
    private TextField IBAN;
    @FXML
    private TextField BIC;
    @FXML
    Label participantAdded;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    private String participantEditedSuccess;
    private String participantAddedSuccess;
    private String participantInvalidWarning;
    private String participantAlreadyAdded;
    List<Person> participants = new ArrayList<>();

    private MainCtrl mainCtrl;
    private ServerUtils server;
    private String warningTitle;
    private String warningText;
    private String warningText1;
    private String warningTitle2;
    private String warningText2;
    private String alertTitle;
    private String alertText;
    private Person participantToEdit;
    private Event currentEvent;
    private boolean isInEditMode;
    @Inject
    public AddParticipantCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void setup(Long eventId, boolean isInEditMode, Person participantToEdit) {
        this.currentEvent = server.getEvent(eventId);
        this.isInEditMode = isInEditMode;
        this.participantToEdit = participantToEdit;

        if (isInEditMode) {
            firstName.setText(participantToEdit.getFirstName());
            lastName.setText(participantToEdit.getLastName());
            IBAN.setText(participantToEdit.getIBAN());
            BIC.setText(participantToEdit.getBIC());
            if (participantToEdit.getEmail() != null && !participantToEdit.getEmail().equals("-1"))
             email.setText(participantToEdit.getEmail());
        }
        else {
            clearFields();
        }

        setTextLanguage();
    }
    public void setTextLanguage() {
        String language = mainCtrl.getLanguage();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.language_" + mainCtrl.getLanguageWithoutImagePath());
        firstName.setPromptText(resourceBundle.getString("FirstName"));
        firstNameLabel.setText(resourceBundle.getString("FirstName"));
        lastName.setPromptText(resourceBundle.getString("LastName"));
        lastNameLabel.setText(resourceBundle.getString("LastName"));
        goHomeButton.setText(resourceBundle.getString("Home"));
        goBackButton.setText(resourceBundle.getString("Back"));
        addParticipantButton.setText(resourceBundle.getString("AddParticipant"));
        warningTitle =resourceBundle.getString("ParticipantAddedWarning");
        warningText =resourceBundle.getString("Theparticipant");
        warningText1 =resourceBundle.getString("wasalreadyadded");
        warningTitle2 =resourceBundle.getString("AddingParticipantWarning");
        warningText2 =resourceBundle.getString("Pleasefillallfieldscorrectly");
        alertTitle =resourceBundle.getString("AddingParticipantAlert");
        alertText =resourceBundle.getString("Doyouwanttoaddthisparticipant");

        participantAddedSuccess = resourceBundle.getString("ParticipantAdded");
        participantEditedSuccess = resourceBundle.getString("ParticipantEdited");
        participantInvalidWarning = resourceBundle.getString("InvalidInputParticipant");
        participantAlreadyAdded = resourceBundle.getString("ParticipantAlreadyAdded");
    }

    @FXML
    public void addParticipant(ActionEvent event) {

        if (!isValidInput()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(warningTitle2);
            alert.setContentText(warningText2);
            alert.showAndWait();
            participantAdded.setText(participantInvalidWarning);
            participantAdded.setTextFill(Color.RED);
            participantAdded.setStyle("-fx-font-weight: bold");
            return;
        }

        try {
            Person p = getParticipant();
            if (participants.contains(p))
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(warningTitle);
                alert.setContentText(warningText + p.getFirstName() +" "+ warningText1);
                alert.showAndWait();
                participantAdded.setText(p.getFirstName() + " " + participantAlreadyAdded);
                participantAdded.setTextFill(Color.RED);
                participantAdded.setStyle("-fx-font-weight: bold");
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(alertTitle);
            alert.setContentText(alertText);
            Optional<ButtonType> result = alert.showAndWait();
             if(result.get() == ButtonType.OK) {

                if (isInEditMode) {
                    if (currentEvent.getParticipants().contains(participantToEdit))
                      currentEvent.getParticipants().remove(participantToEdit);

                    participantToEdit.setFirstName(p.getFirstName());
                    participantToEdit.setLastName(p.getLastName());
                    participantToEdit.setEmail(p.getEmail());
                    participantToEdit.setIBAN(p.getIBAN());
                    participantToEdit.setBIC(p.getBIC());
                    server.updatePerson(participantToEdit.getId(), participantToEdit);
                    if (!currentEvent.getParticipants().contains(participantToEdit))
                     currentEvent.getParticipants().add(participantToEdit);
                    participantAdded.setText(participantEditedSuccess);
                }
                else {
                    currentEvent.addParticipant(p);
                    participantAdded.setText(participantAddedSuccess);
                }
                 server.send("/app/events", currentEvent);
                 participantAdded.setTextFill(Color.BLACK);
                System.out.println(p);
             }
             else {
                clearFields();
             }
            } catch (WebApplicationException e) {

                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            }






    }

    public Person getParticipant() {
        Person p;
        if (email == null || email.getText().isEmpty())
        {
            p = new Person(firstName.getText(), lastName.getText(), null,
                    IBAN.getText(), BIC.getText(), Currency.EUR, 0.0, currentEvent, null);
        }
        else
        {
            p = new Person(firstName.getText(), lastName.getText(), email.getText(),
                    IBAN.getText(), BIC.getText(), Currency.EUR, 0.0, currentEvent, null);
        }

        return p;
    }

    public void clearFields() {
        firstName.setText(null);
        lastName.setText(null);
        email.setText(null);
        IBAN.setText(null);
        BIC.setText(null);
    }
    public boolean isValidInput() {

        if (firstName == null || firstName.getText().isEmpty())
            return false;

        if (lastName == null || lastName.getText().isEmpty())
            return false;

        if (email != null && !email.getText().isEmpty() &&
        !email.getText().contains("@") && !email.getText().contains(".com"))
            return false;

        return true;
    }

    public void goHome(ActionEvent event) throws IOException {
        mainCtrl.showHome();
    }

    public void goToEventOverview() {
        mainCtrl.showEventOverview(this.currentEvent.getId());
    }

}
