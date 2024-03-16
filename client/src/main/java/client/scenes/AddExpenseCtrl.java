package client.scenes;

import client.utils.ServerUtils;
import commons.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddExpenseCtrl {

    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;
    @FXML
    private ComboBox<String> payerComboBox;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField amountField;
    @FXML
    private ComboBox<Currency> currencyComboBox;
    @FXML
    private MenuButton currencyMenu;
    @FXML
    private MenuItem currencyOption1;
    @FXML
    private RadioButton splitEvenButton;
    @FXML
    private RadioButton splitButton;
    @FXML
    private AnchorPane splitPersonsPane;
    @FXML
    private CheckBox splitPersonBox1;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private MenuButton typeMenu;
    @FXML
    private MenuItem typeOption1;
    @FXML
    private Button cancelButton;
    @FXML
    private Button addButton;
    @FXML
    private Button goHomeButton;
    @FXML
    private List<CheckBox> checkBoxes;
    private List<Person> participants;
    private List<Expense> expenses;

    private MainCtrl mainCtrl;
    private ServerUtils server;
    @Inject
    public AddExpenseCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void createExpense(ActionEvent event) throws RuntimeException  {

        try {
            if (!isValidInput())
            {
                System.out.println("Every field needs to filled properly");
                return;
            }
            String description;
            if (descriptionField == null || descriptionField.getText().isEmpty())
                description = null;
            else
                description = descriptionField.getText();
            double amount = Double.parseDouble(amountField.getText());
            double amountPerPerson = splitEvenButton.isSelected() ?
                    amount / getAllGivers().size() : amount / selectedBoxesNumber();
            List<Debt> debts = new ArrayList<>();

            for (Person p : getAllGivers()) {
                Debt debt = new Debt(getPayerData(), p, null, amountPerPerson);
                debts.add(debt);
            }
            Expense e = new Expense(description, amount, new Date(), getPayerData(), debts,
                    getCurrencyData(), getTypeData());

            for (Debt debt : debts)
                debt.setExpense(e);

            expenses.add(e);
            System.out.println("Created expense");
            System.out.println(expenses.size());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }


    }
    public void checkPersonBoxes(ActionEvent event) {

        if (splitEvenButton.isSelected())
            for (CheckBox box : checkBoxes)
                box.setDisable(true);
        else if (splitButton.isSelected()) {
            if (getPayerData() == null) {
                for (CheckBox box : checkBoxes)
                    box.setDisable(true);
            }
            else {
                String name = getPayerData().getFirstName() + " " +
                              getPayerData().getLastName();
                System.out.println(name);
                for (CheckBox box : checkBoxes) {
                    box.setDisable(true);
                    if (!box.getText().equals(name)) box.setDisable(false);
                }
            }
        }
    }
    public boolean isValidInput() {

        if (getPayerData() == null)
            return false;

        if (getCurrencyData() == null)
            return false;

        if (getTypeData() == null)
            return false;

        if (amountField == null || amountField.getText().isEmpty())
            return false;

        if (splitButton.isSelected() && selectedBoxesNumber() == 0)
            return false;

        return true;
    }

    public List<Person> getAllGivers() {
        List<Person> allGivers = new ArrayList<>();
        if (splitEvenButton.isSelected()) {
            for (Person p : participants) {
                if (!p.equals(getPayerData()))
                    allGivers.add(p);
            }
        }
        else {
            for (CheckBox box : checkBoxes)
                for (Person p : participants) {
                    String name = p.getFirstName() + " " + p.getLastName();
                    if (name.equals(box.getText()) && box.isSelected())
                        allGivers.add(p);
                }
        }

        return allGivers;
    }
    public Person getPayerData() {
        Person p = null;
        if (payerComboBox.getValue() != null) {
            String fullName = payerComboBox.getValue();
            for (Person participant : participants)
            {
                String participantName = participant.getFirstName() +" " +
                        participant.getLastName();
                if (participantName.equals(fullName))
                    p = participant;
            }
        }
        return p;
    }

    public Tag getTypeData() {

        Tag t = null;
        if (typeComboBox.getValue() != null) {
            switch (typeComboBox.getValue()) {
                case "type1" -> t =  new Tag("Red", "Type 1");
                case "type2" -> t = new Tag("Green", "Type 2");
                case "type3" -> t = new Tag("Purple", "Type 3");
                default -> t = null;
            };
        }
        return t;
    }

    public Currency getCurrencyData() {

        return currencyComboBox.getValue();
    }

    public int selectedBoxesNumber() {
        int k = 0;

        for (CheckBox box : checkBoxes)
            if (box.isSelected()) k++;

        return k;
    }
    public void initializePage() {

        try {
            participants = new ArrayList<>();
            participants.addAll(server.getPersons());
            participants.addAll(List.of(
                    new Person("John", "Wick", "john@email.com",
                            "NL32323232322232322342", "MIGBL233",
                            Currency.EUR, 0.0, new Event(), new User()),
                    new Person("Bruce", "Wayne", "batman@email.com",
                            "NL32323232322232322344", "MIGBL233",
                            Currency.EUR, 0.0, new Event(), new User()),
                    new Person("Donnie", "Brasco", "brasco@email.com",
                            "NL32323232322232322343", "MIGBL233",
                            Currency.EUR, 0.0, new Event(), new User())
            ));
            int y = 5;
            for (Person p : participants) {
                CheckBox newBox = new CheckBox(
                        p.getFirstName() + " " + p.getLastName());
                splitPersonsPane.getChildren().add(newBox);
                newBox.setLayoutY(y);
                y += 25;
            }
            payerComboBox.setItems(FXCollections.observableArrayList(
                    participants.stream().map(p -> p.getFirstName() + " " + p.getLastName()).toList()
            ));

            checkBoxes = splitPersonsPane.getChildren().stream().map(t -> (CheckBox) t).toList();


            typeComboBox.setItems(FXCollections.observableArrayList(
                    "type1", "type2", "type3"));
            currencyComboBox.setItems(FXCollections.observableList(
                    List.of(Currency.EUR, Currency.USD,
                            Currency.CHF, Currency.GBP)));
            currencyComboBox.getSelectionModel().selectFirst();
            checkPersonBoxes(new ActionEvent());
            expenses = new ArrayList<>();
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }


    }
    public void goHome(ActionEvent event) throws IOException {
        mainCtrl.showHome();
    }
}
