package client.scenes;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class AddParticipantCtrl {

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
    Label participantAdded;

    @FXML
    public void addParticipant(ActionEvent event) {

        if (isValidInput()) {
            participantAdded.setText("Added " + firstName.getText() + " " +  lastName.getText() + " to event!");
            participantAdded.setTextFill(Color.BLACK);
            participantAdded.setStyle("-fx-font-weight: light");
        }
        else {
            participantAdded.setText("Please fill out all fields correctly!");
            participantAdded.setTextFill(Color.RED);
            participantAdded.setStyle("-fx-font-weight: bold");
        }

    }

    public boolean isValidInput() {

        if (firstName == null || firstName.getText().isEmpty())
            return false;

        if (lastName == null || lastName.getText().isEmpty())
            return false;

        if (email == null || email.getText().isEmpty())
            return false;

        if (IBAN == null || IBAN.getText().length() < 34)
            return false;

        return true;
    }
}
