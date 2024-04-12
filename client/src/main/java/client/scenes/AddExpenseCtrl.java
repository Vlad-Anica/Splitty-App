package client.scenes;

import client.utils.ServerUtils;
import commons.*;
import commons.Currency;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AddExpenseCtrl {

    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private AnchorPane tagPane;
    @FXML
    private Parent root;
    @FXML
    private ComboBox<String> payerComboBox;
    @FXML
    private TextField descriptionField;
    @FXML
    private Label descText;
    @FXML
    private Label title;
    @FXML
    private Label payerText;
    @FXML
    private Label amountText;
    @FXML
    private Label typeText;
    @FXML
    private TextField amountField;
    @FXML
    private TextField tagNameField;
    @FXML
    private TextField tagClrField;
    @FXML
    private DatePicker dateField;
    @FXML
    private Label chooseText;
    @FXML
    private Label dateLabel;
    @FXML
    private ComboBox<Currency> currencyComboBox;
    @FXML
    private RadioButton splitEvenButton;
    @FXML
    private RadioButton splitButton;
    @FXML
    private AnchorPane splitPersonsPane;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private Button cancelButton;
    @FXML
    private Button addButton;
    @FXML
    private Button goHomeButton;
    @FXML
    private Button backButton;
    @FXML
    private Button addTagButton;
    @FXML
    private Label statusLabel;
    @FXML
    private List<CheckBox> checkBoxes;
    private List<Person> participants;
    private List<Expense> expenses;
    private List<Tag> tags;
    private MainCtrl mainCtrl;
    private EventOverviewCtrl eventOverviewCtrl;
    private ServerUtils server;
    private Event event;
    private Expense expenseToEdit;
    private String warningTitle;
    private String warningText;
    private String alertTitle;
    private String alertText;
    private String whoPaidString;
    private String whatTypeString;
    private boolean inEditMode;

    @Inject
    public AddExpenseCtrl(MainCtrl mainCtrl, EventOverviewCtrl eventOverviewCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.eventOverviewCtrl = eventOverviewCtrl;
        this.server = server;
    }

    public Expense getExpenseToEdit() {
        return expenseToEdit;
    }

    public void setExpenseToEdit(Expense expenseToEdit) {
        this.expenseToEdit = expenseToEdit;
    }

    public void initializePage(long eventID, boolean isInEditMode, Expense expense)  {

        try {

            inEditMode = isInEditMode;
            event = server.getEvent(eventID);
            participants = new ArrayList<>();
            participants.addAll(server.getPersons());
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

            List<Tag> defaultTags = List.of(
                    new Tag("green", "Food"),
                    new Tag("blue", "Entrance Fees"),
                    new Tag("red", "Travel")
            );
            tags = new ArrayList<>();
            tags.addAll(defaultTags);
            typeComboBox.setItems(FXCollections.observableArrayList(
                    defaultTags.stream().map(Tag::getType).toList()));

            currencyComboBox.setItems(FXCollections.observableList(
                    List.of(Currency.EUR, Currency.USD,
                            Currency.CHF, Currency.GBP)));
            currencyComboBox.getSelectionModel().selectFirst();

            checkPersonBoxes(new ActionEvent());
            expenses = new ArrayList<>();
            setTextLanguage();

            if (isInEditMode) {
                setExpenseToEdit(expense);
                payerComboBox.setValue(expenseToEdit.getReceiver().getFirstName() + " " +
                        expenseToEdit.getReceiver().getLastName());
                checkPersonBoxes(new ActionEvent());
                LocalDate shownDate = expenseToEdit.getDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                String parsedDate = shownDate.format(DateTimeFormatter.ofPattern("d/MM/yyyy"));
                dateField.getEditor().setText(parsedDate);
                typeComboBox.setValue(expense.getTag().getType());
                descriptionField.setText(expenseToEdit.getDescription());
                amountField.setText(BigDecimal.valueOf(expense.getAmount() + 0.0)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue()+"");
            }
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
    }

    public void setTextLanguage() {
        String language = mainCtrl.getLanguage();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.language_" + mainCtrl.getLanguageWithoutImagePath());
        payerComboBox.setPromptText(resourceBundle.getString("ChooseAPayer"));
        title.setText(resourceBundle.getString("Editexpense"));
        backButton.setText(resourceBundle.getString("Back"));
        goHomeButton.setText(resourceBundle.getString("Home"));
        whoPaidString = resourceBundle.getString("WhoPaid");
        payerComboBox.setPromptText(whoPaidString);
        descText.setText(resourceBundle.getString("WhatFor"));
        currencyComboBox.setPromptText(resourceBundle.getString("Currency"));
        amountText.setText(resourceBundle.getString("HowMuch"));
        dateLabel.setText(resourceBundle.getString("When"));
        chooseText.setText(resourceBundle.getString("HowToSplit"));
        splitEvenButton.setText(resourceBundle.getString("SplitEvenly"));
        splitButton.setText(resourceBundle.getString("SomePeople"));
        typeText.setText(resourceBundle.getString("ExpenseType"));
        whatTypeString = resourceBundle.getString("ChooseAType");
        typeComboBox.setPromptText(whatTypeString);
        addTagButton.setText(resourceBundle.getString("AddTag"));
        cancelButton.setText(resourceBundle.getString("Cancel"));
        addButton.setText(resourceBundle.getString("Add"));
        warningTitle = resourceBundle.getString("CreateExpenseWarning");
        warningText = resourceBundle.getString("Pleasefillallfieldscorrectly");
        alertTitle = resourceBundle.getString("AddingExpenseConfirmationAlert");
        alertText = resourceBundle.getString("Doyouwanttoaddthisexpense");
    }

    public void createExpense() throws RuntimeException  {

        try {

            if (!isValidInput())
            {
                statusLabel.setStyle("-fx-font-weight: bold");
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("Fill out every field correctly!");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(warningTitle);
                alert.setContentText(warningText);
                alert.showAndWait();
                System.out.println("Every field needs to filled properly");
                return;
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(alertTitle);
            alert.setContentText(alertText);
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK) {
                String description;
                if (descriptionField == null || descriptionField.getText().isEmpty())
                    description = null;
                else
                    description = descriptionField.getText();

                Double amount = Double.valueOf(amountField.getText());

            double amountPerPerson = splitEvenButton.isSelected() ?
                    amount / getAllGivers().size() : amount / selectedBoxesNumber();

                //convert LocalDate to date
                LocalDate localDate = dateField.getValue();
                Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                System.out.println(getPayerData() + " " + getCurrencyData() + " " + getTypeData());
                Expense e = new Expense(description, 0.0, date, getPayerData(), getAllGivers(),
                    getCurrencyData(), getTypeData());
                e.setAmount(0.0 + amount);

               if (inEditMode) {
                   if (event.containsExpense(expenseToEdit))
                     event.removeExpense(expenseToEdit);

                   expenseToEdit.setAmount(e.getAmount());
                   expenseToEdit.setDescription(e.getDescription());
                   expenseToEdit.setReceiver(e.getReceiver());
                   expenseToEdit.setDate(e.getDate());
                   expenseToEdit.setCurrency(e.getCurrency());
                   expenseToEdit.setTag(e.getTag());
                   expenseToEdit.setGivers(e.getGivers());

                   server.updateExpense(expenseToEdit.getId(), expenseToEdit);

                   if (!event.containsExpense(expenseToEdit))
                     event.addExpense(expenseToEdit);

                   server.updateEvent(event.getId(), event);
               }
               else {
                  // server.send("/app/expenses", e);
                   event.addExpense(e);
                   server.send("/app/events", event);
               }


               System.out.println("Created expense");
               statusLabel.setText("Expense created!");
               clearFields();
            }
            else{
                payerComboBox.cancelEdit();
                descriptionField.setText(null);
                amountField.setText(null);
                typeComboBox.cancelEdit();
            }
            

        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }
    public void checkPersonBoxes(ActionEvent event) {

        if (splitEvenButton.isSelected())
            for (CheckBox box : checkBoxes) {
                box.setSelected(false);
                box.setDisable(true);
            }
        else if (splitButton.isSelected()) {
            if (getPayerData() == null) {
                for (CheckBox box : checkBoxes) {
                    box.setSelected(false);
                    box.setDisable(true);
                }

            }
            else {
                String name = getPayerData().getFirstName() + " " +
                              getPayerData().getLastName();
                System.out.println(name);
                for (CheckBox box : checkBoxes) {
                    box.setDisable(true);
                    if (box.getText().equals(name)) box.setSelected(false);
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
        if (dateField == null || dateField.getEditor().getText().isEmpty())
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
            String tagName = typeComboBox.getValue();
            for (Tag tag : tags)
                if (tag.getType().equals(tagName))
                    t = tag;
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


    public void clearFields() {

        payerComboBox.setPromptText(whoPaidString);
        amountField.clear();
        descriptionField.clear();
        dateField.getEditor().clear();
        typeComboBox.setPromptText(whatTypeString);

    }

    public void cancel() {
        clearFields();
        goToEventOverview();
    }

    public void showAddTag(ActionEvent event) throws IOException {
        tagPane.setDisable(false);
        tagPane.setVisible(true);
    }

    public void hideAddTag(ActionEvent event) {
        tagClrField.clear();
        tagNameField.clear();
        tagPane.setDisable(true);
        tagPane.setVisible(false);
    }

    public void saveTag(ActionEvent event) {

        if (tagNameField == null || tagNameField.getText().isEmpty())
            return;
        if (tagClrField == null || tagClrField.getText().isEmpty())
            return;
        Tag tag = (new Tag(tagClrField.getText(), tagNameField.getText()));
        tags.add(tag);
        typeComboBox.getItems().add(tag.getType());
        server.addTag(tag);
        tagClrField.clear();
        tagNameField.clear();
        tagPane.setDisable(true);
        tagPane.setVisible(false);
    }
    public void goHome(ActionEvent event) throws IOException {
        mainCtrl.showHome();
    }

    public void goToEventOverview() {
        mainCtrl.showEventOverview(event.getId());
    }


}
