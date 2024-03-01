package client.scenes;

import commons.Person;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

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
    private TextField BIC;
    @FXML
    Label participantAdded;
    List<Person> participants = new ArrayList<>();
    @FXML
    public void addParticipant(ActionEvent event) {

        if (isValidInput()) {
            participantAdded.setText("Sent invite to " + firstName.getText() + "!");
            participantAdded.setTextFill(Color.BLACK);
            participantAdded.setStyle("-fx-font-weight: light");

            Person p = new Person(firstName.getText(), lastName.getText(),
                    email.getText(), IBAN.getText(), BIC.getText());
            participants.add(p);
            System.out.println(p);
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

        if (BIC == null || BIC.getText().isEmpty())
             return false;

        return true;
    }
}