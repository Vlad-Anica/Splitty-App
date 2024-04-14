package client.scenes;

import client.utils.ServerUtils;
import commons.Debt;
import commons.Event;
import commons.Person;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

/***
 * Unused imports:
 * import javafx.scene.control.Label;
 * import javafx.scene.control.TextField;
 * import javafx.scene.paint.Color;
 */


public class OpenDebtsCtrl {
    @FXML
    private Button goHomeButton;
    @FXML
    private Button goBackButton;
    @FXML
    private Text openDebtsTitle;
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;

    @FXML
    private Accordion debtList;

    private MainCtrl mainCtrl;
    private ServerUtils server;
    private String owesString;
    private String bankInfo;
    private String emailConfigConfirmation;
    private String badNumberInput;
    String emailTitle, emailMessage;
    Event event;

    @Inject
    public OpenDebtsCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void setup(Event event) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.language_" + mainCtrl.getLanguageWithoutImagePath());

        setTextLanguage();
        this.event = event;

        for (int i = 0; i < event.getDebts().size(); i++) {
            VBox vbox = new VBox();
            Debt debt = event.getDebts().get(i);
            TitledPane debtTitle = new TitledPane(getDebtTitle(debt), vbox);

            if (hasBankInfo(debt.getReceiver())) {
                bankInfo = "BIC: " + debt.getReceiver().getBIC() + "\n" + "IBAN: " +
                        debt.getReceiver().getIBAN() + "\n";
            } else {
                bankInfo = resourceBundle.getString("NoBankInfo");
            }
            Button btnReminder = new Button("Reminder");
            if (hasMail(debt.getReceiver()) &&  mainCtrl.checkEmailConfig()) {
                emailConfigConfirmation = resourceBundle.getString("EmailIsConfigured");
                btnReminder.setOnAction(event1 -> {
                    emailTitle = resourceBundle.getString("DebtTo") + " " + debt.getReceiver().getFirstName() +
                            " " + debt.getReceiver().getFirstName();
                    emailMessage = resourceBundle.getString("PayTo") + " " + debt.getReceiver().getFirstName() +
                            " " + debt.getReceiver().getFirstName() + " " + debt.getAmount() + debt.getCurrency();
                    mainCtrl.sendEmailFromCurrentUser(debt.getReceiver().getEmail(), emailMessage, emailTitle);
                });
            } else {
                btnReminder.setDisable(true);
                emailConfigConfirmation = resourceBundle.getString("EmailNotConfigured");
            }

            Button btnSettle = new Button(resourceBundle.getString("Settle"));
            btnSettle.setOnAction(event1 -> {
                server.settleDebt(debt.getId());
                setup(server.getEvent(event.getId()));
            });

            TextField partialPay = new TextField();
            partialPay.setPromptText("Partial amount");

            Button btnPayPartially = new Button(resourceBundle.getString("PayPartially"));
            btnPayPartially.setOnAction(event1 -> {
                try {
                    final double amt = Double.parseDouble(partialPay.getText());
                    server.payDebtPartially(debt.getId(), amt);
                    setup(server.getEvent(event.getId()));
                } catch (NumberFormatException e) {
                    warnNumber();
                } catch (NullPointerException e) {
                    warnNumber();
                }

            });

            vbox.getChildren().add(new Label(bankInfo));
            vbox.getChildren().add(new Label(emailConfigConfirmation));
            vbox.getChildren().add(partialPay);
            vbox.getChildren().add(btnPayPartially);
            vbox.getChildren().add(btnReminder);
            vbox.getChildren().add(btnSettle);
            debtList.getPanes().add(debtTitle);
        }

    }

    public String getDebtTitle(Debt debt) {
        return debt.getGiver().getFirstName() + " " + debt.getGiver().getLastName()
                + " " + owesString + " " + debt.getReceiver().getFirstName() + " " +
                debt.getReceiver().getLastName() + " " + debt.getAmount() + debt.getCurrency();
    }

    public void warnNumber() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.language_" + mainCtrl.getLanguageWithoutImagePath());

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(resourceBundle.getString("BadNumberInput"));
        alert.setContentText(resourceBundle.getString("BadNumberInput"));
        alert.showAndWait();
    }

    public boolean hasBankInfo(Person p) {
        return (p.getIBAN() != null && !p.getIBAN().isEmpty()
                && p.getBIC() != null && !p.getBIC().isEmpty());
    }
    public boolean hasMail(Person p) {
        return (p.getEmail() != null && !p.getEmail().isEmpty());
    }

    public void setTextLanguage() {
        String language = mainCtrl.getLanguage();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.language_" + mainCtrl.getLanguageWithoutImagePath());
        int languageIndex = mainCtrl.getLanguageIndex();
        goHomeButton.setText(resourceBundle.getString("Home"));
        goBackButton.setText(resourceBundle.getString("Back"));
        openDebtsTitle.setText(resourceBundle.getString("OpenDebts"));
        mainCtrl.getPrimaryStage().setTitle(resourceBundle.getString("OpenDebts"));
        owesString = resourceBundle.getString("Owes");
    }

    //need a way to show open debts from the database


    //For Now, the back button is the same as the home button, this needs changing in the future!
    @FXML
    public void goBack(ActionEvent event) throws IOException {
        mainCtrl.showEventOverview(this.event.getId());
    }

    public void goHome(ActionEvent event) throws IOException {
        mainCtrl.showHome();
    }

}
