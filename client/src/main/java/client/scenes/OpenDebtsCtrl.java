package client.scenes;

import client.utils.ServerUtils;
import commons.Debt;
import commons.Expense;
import commons.Person;
import jakarta.inject.Inject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
    @FXML
    private Button goHomeButton;
    List<String> goHomeButtonText = new ArrayList<>(List.of("Home", "Home (in Dutch)"));
    @FXML
    private Button goBackButton;
    List<String> goBackButtonText = new ArrayList<>(List.of("Back", "Back (in Dutch)"));
    @FXML
    private Text openDebtsTitle;
    List<String> openDebtsTitleText = new ArrayList<>(List.of("Open Debts", "Open Debts (in Dutch)"));
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;
    @FXML
    private AnchorPane debtsPane;
    @FXML
    private TableColumn<Debt, String> columnC1;

    @FXML
    private TableColumn<Debt, Integer> columnC2;

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
        List<Debt> debts = server.getDebts();
        ObservableList<Debt> testDebts = FXCollections.observableArrayList(new Debt(new Person("Joe", "Biden"), new Person("Donald", "Trump"), new Expense(), 69), new Debt(new Person("Donald", "Trump"), new Person("Joe", "Biden"), new Expense(), 420));
        for (Debt debt:testDebts) {
            int height = 0;
//            debtsPane.getChildren().add();
            columnC1.setCellValueFactory(cellData -> {
                // Assuming receiver is of type Person
                SimpleStringProperty property = new SimpleStringProperty();
                if (cellData.getValue() != null && cellData.getValue().getReceiver() != null) {
                    property.set(cellData.getValue().getReceiver().getFirstName()+" "+cellData.getValue().getReceiver().getLastName());
                }
                return property;
            });
            columnC2.setCellValueFactory(new PropertyValueFactory<Debt, Integer>("amount"));
            table.setItems(testDebts);
        }
    }
    public void setTextLanguage() {
        int languageIndex = mainCtrl.getLanguageIndex();
        goHomeButton.setText(goHomeButtonText.get(languageIndex));
        goBackButton.setText(goBackButtonText.get(languageIndex));
        openDebtsTitle.setText(openDebtsTitleText.get(languageIndex));
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
