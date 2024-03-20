package client.scenes;

import client.utils.ServerUtils;
import commons.Debt;
import commons.Expense;
import commons.Person;
import jakarta.inject.Inject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/***
 * Unused imports:
 * import javafx.scene.control.Label;
 * import javafx.scene.control.TextField;
 * import javafx.scene.paint.Color;
 */


public class OpenDebtsCtrl {
    List<String> titleText = new ArrayList<>(List.of("Open Debts", "Open Schulden"));
    @FXML
    private Button goHomeButton;
    List<String> goHomeButtonText = new ArrayList<>(List.of("Home", "Thuis"));
    @FXML
    private Button goBackButton;
    List<String> goBackButtonText = new ArrayList<>(List.of("Back", "terug"));
    @FXML
    private Text openDebtsTitle;
    List<String> openDebtsTitleText = new ArrayList<>(List.of("Open Debts", "Open Schulden"));
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;
    @FXML
    private AnchorPane debtsPane;
    @FXML
    private TableColumn<Debt, String> columnReceiver;
    List<String> columnReceiverText = new ArrayList<>(List.of("Receiver", "Ontvanger"));

    @FXML
    private TableColumn<Debt, Integer> columnAmount;
    List<String> columnAmountText = new ArrayList<>(List.of("Amount", "Aantal"));

    @FXML
    private TableColumn<Debt, String> columnGiver;
    List<String> columnGiverText = new ArrayList<>(List.of("Giver", "gever"));

    @FXML
    private TableColumn<Debt, String> columnExpense;
    List<String> columnExpenseText = new ArrayList<>(List.of("Expense", "Uitgave"));

    @FXML
    private TableColumn<Debt, Long> columnId;
    List<String> columnIdText = new ArrayList<>(List.of("id", "id"));

    @FXML
    private TableColumn<Debt, String> columnRemind;
    List<String> columnRemindText = new ArrayList<>(List.of("Remind", "Herinner"));

    @FXML
    private TableColumn<Debt, String> columnSettle;
    List<String> columnSettleText = new ArrayList<>(List.of("Settle", "Betaal"));

    @FXML
    private TableView<Debt> table;

    private MainCtrl mainCtrl;
    private ServerUtils server;

    @Inject
    public OpenDebtsCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void setup() {
        setTextLanguage();
        List<Debt> debts = server.getDebts(); //gets debt from server but for now uses debts from list created below for testing
        Expense e = new Expense();
        e.setDescription("We do that stuff");
        List<Debt> testDebts = List.of(new Debt(new Person("Frank", "Verkoren"), new Person("Duco", "Lam"), e, 2.50), new Debt(new Person("Duco", "Lam"), new Person("Frank", "Verkoren"), new Expense(), 404));
        ObservableList<Debt> debtsList = FXCollections.observableList(testDebts.stream().filter(x -> !x.getSettled()).toList());
        columnReceiver.setCellValueFactory(cellData -> {
            SimpleStringProperty property = new SimpleStringProperty();
            if (cellData.getValue() != null && cellData.getValue().getReceiver() != null) {
                property.set(cellData.getValue().getReceiver().getFirstName() + " " + cellData.getValue().getReceiver().getLastName());
            }
            return property;
        });
        columnGiver.setCellValueFactory(cellData -> {
            SimpleStringProperty property = new SimpleStringProperty();
            if (cellData.getValue() != null && cellData.getValue().getGiver() != null) {
                property.set(cellData.getValue().getGiver().getFirstName() + " " + cellData.getValue().getGiver().getLastName());
            }
            return property;
        });
        columnExpense.setCellValueFactory(cellData -> {
            SimpleStringProperty property = new SimpleStringProperty();
            if (cellData.getValue() != null && cellData.getValue().getExpense() != null) {
                property.set(cellData.getValue().getExpense().getDescription());
            }
            return property;
        });
        columnAmount.setCellValueFactory(new PropertyValueFactory<Debt, Integer>("amount"));
        columnId.setCellValueFactory(new PropertyValueFactory<Debt, Long>("id"));
        Callback<TableColumn<Debt, String>, TableCell<Debt, String>> cellFactoryRemind
                = //
                new Callback<>() {
                    public TableCell call(final TableColumn<Debt, String> param) {
                        final TableCell<Debt, String> cell = new TableCell<>() {
                            Button btn = new Button(columnRemindText.get(mainCtrl.getLanguageIndex()));

                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setStyle("-fx-font: 10 system;");
                                    btn.setOnAction(event -> {
                                        long giverId = getTableView().getItems().get(getIndex()).getGiver().getId();
                                        long receiverId = getTableView().getItems().get(getIndex()).getReceiver().getId();
                                        System.out.println("giver id: " + giverId + " receiver id: " + receiverId);
                                        System.out.println("An email needs to be sent");
                                    });
                                    setGraphic(btn);
                                }
                            }
                        };
                        return cell;
                    }
                };
        columnRemind.setCellFactory(cellFactoryRemind);
        Callback<TableColumn<Debt, String>, TableCell<Debt, String>> cellFactorySettle
                = //
                new Callback<>() {
                    public TableCell call(final TableColumn<Debt, String> param) {
                        final TableCell<Debt, String> cell = new TableCell<>() {
                            Button btn = new Button(columnSettleText.get(mainCtrl.getLanguageIndex()));

                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setStyle("-fx-font: 10 system;");
                                    btn.setOnAction(event -> {
                                        long id = getTableView().getItems().get(getIndex()).getId();
                                        System.out.println("debt with the id '" + id + "' needs to be settled");
                                    });
                                    setGraphic(btn);
                                }
                            }
                        };
                        return cell;
                    }
                };
        columnSettle.setCellFactory(cellFactorySettle);
        table.setItems(debtsList);
    }

    public void setTextLanguage() {
        int languageIndex = mainCtrl.getLanguageIndex();
        goHomeButton.setText(goHomeButtonText.get(languageIndex));
        goBackButton.setText(goBackButtonText.get(languageIndex));
        openDebtsTitle.setText(openDebtsTitleText.get(languageIndex));
        mainCtrl.getPrimaryStage().setTitle(titleText.get(languageIndex));
        columnReceiver.setText(columnReceiverText.get(languageIndex));
        columnAmount.setText(columnAmountText.get(languageIndex));
        columnGiver.setText(columnGiverText.get(languageIndex));
        columnExpense.setText(columnExpenseText.get(languageIndex));
        columnId.setText(columnIdText.get(languageIndex));
        columnRemind.setText(columnRemindText.get(languageIndex));
        columnSettle.setText(columnSettleText.get(languageIndex));
    }

    //need a way to show open debts from the database


    //For Now, the back button is the same as the home button, this needs changing in the future!
    @FXML
    public void goBack(ActionEvent event) throws IOException {
        mainCtrl.showHome();
    }

    public void goHome(ActionEvent event) throws IOException {
        mainCtrl.showHome();
    }

}
