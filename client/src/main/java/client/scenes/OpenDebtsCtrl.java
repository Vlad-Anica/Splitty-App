package client.scenes;

import client.utils.ServerUtils;
import commons.Debt;
import commons.Event;
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
import java.util.List;
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
    private AnchorPane debtsPane;
    @FXML
    private TableColumn<Debt, String> columnReceiver;

    @FXML
    private TableColumn<Debt, Integer> columnAmount;

    @FXML
    private TableColumn<Debt, String> columnGiver;

    @FXML
    private TableColumn<Debt, Long> columnId;

    @FXML
    private TableColumn<Debt, String> columnRemind;

    @FXML
    private TableColumn<Debt, String> columnSettle;

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
//        List<Debt> debts = server.getDebts(); //gets debt from server but for now uses debts from list created below for testing
        List<Person> persons = server.getPersonsByUserId(mainCtrl.getUserId());
        for (Person p: persons
             ) {
            System.out.println(p);
        }
        List<Debt> debtTest = server.getDebtsByUserId(mainCtrl.getUserId());
        System.out.println(debtTest.size());
        for (Debt d:debtTest) {
            System.out.println(d);
        }
        Expense e = new Expense();
        e.setDescription("We do that stuff");
        List<Debt> testDebts = List.of(new Debt(new Person("Frank", "Verkoren"), new Person("Duco", "Lam"), new Event(), 2.50), new Debt(new Person("Duco", "Lam"), new Person("Frank", "Verkoren"), new Event(), 404));
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
        columnAmount.setCellValueFactory(new PropertyValueFactory<Debt, Integer>("amount"));
        columnId.setCellValueFactory(new PropertyValueFactory<Debt, Long>("id"));
        Callback<TableColumn<Debt, String>, TableCell<Debt, String>> cellFactoryRemind
                = //
                new Callback<>() {
                    public TableCell call(final TableColumn<Debt, String> param) {
                        final TableCell<Debt, String> cell = new TableCell<>() {
                            Button btn = new Button(ResourceBundle.getBundle("languages.language_" + mainCtrl.getLanguageWithoutImagePath()).getString("Remind"));

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
                            Button btn = new Button(ResourceBundle.getBundle("languages.language_" + mainCtrl.getLanguageWithoutImagePath()).getString("Settle"));

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
        String language = mainCtrl.getLanguage();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.language_" + mainCtrl.getLanguageWithoutImagePath());
        int languageIndex = mainCtrl.getLanguageIndex();
        goHomeButton.setText(resourceBundle.getString("Home"));
        goBackButton.setText(resourceBundle.getString("Back"));
        openDebtsTitle.setText(resourceBundle.getString("OpenDebts"));
        mainCtrl.getPrimaryStage().setTitle(resourceBundle.getString("OpenDebts"));
        columnReceiver.setText(resourceBundle.getString("Receiver"));
        columnAmount.setText(resourceBundle.getString("Amount"));
        columnGiver.setText(resourceBundle.getString("Giver"));
        columnId.setText(resourceBundle.getString("Id"));
        columnRemind.setText(resourceBundle.getString("Remind"));
        columnSettle.setText(resourceBundle.getString("Settle"));
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
