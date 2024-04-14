package client.scenes;
import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Person;
import commons.Tag;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.*;

public class ExpenseOverviewCtrl implements Initializable {

    @FXML
    Label ExpenseNameLabel;
    @FXML
    Label EventNameLabel;
    @FXML
    Label TimeLabel;
    @FXML
    Label NameFromLabel;
    @FXML
    Label AmountLabel;
    @FXML
    Label TagNameLabel;
    @FXML
    ListView<String> ToListView;
    @FXML
    Button goBackButtonExpenses;
    @FXML
    Rectangle upperLine;
    @FXML
    Rectangle lowerLine;




    private MainCtrl mainCtrl;
    private ServerUtils server;
    private EventOverviewCtrl eventOverview;
    private long eventId;
    private long expenseId;
    private TreeSet<String> recieverNames = new TreeSet<>();

    @Inject
    public ExpenseOverviewCtrl(MainCtrl mainCtrl, ServerUtils server, EventOverviewCtrl eventOverview) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.eventOverview = eventOverview;
    }

    public void setup(Long eventId, Long expenseId){
        this.eventId = eventId;
        this.expenseId = expenseId;
        try {
            Event currentEvent = server.getEvent(eventId);
            String eventName = currentEvent.getName();
            Expense currentExpense = server.getExpenseById(expenseId);
            String expenseName = currentExpense.getDescription();
            double amount = currentExpense.getAmount();
            Date date = currentExpense.getDate();
            String giver = currentExpense.getReceiver().getFirstName();
            List<Person> recievers = currentExpense.getGivers();
            for (Person p : recievers) {
                recieverNames.add(p.getFirstName());
            }

            try {
                ExpenseNameLabel.setText(expenseName);
                ExpenseNameLabel.setCenterShape(true);
                ExpenseNameLabel.setTextAlignment(TextAlignment.CENTER);
                ExpenseNameLabel.setAlignment(Pos.CENTER);
                EventNameLabel.setText(eventName);
                AmountLabel.setText(String.valueOf(amount));
                TimeLabel.setText(date.toString());
                NameFromLabel.setText(giver);
                ToListView.getItems();
                for (String name : recieverNames){
                    if (!ToListView.getItems().contains(name)) ToListView.getItems().add(name);
                }

            } catch (Exception e){
                System.out.println("There was an issue setting the data into the scene!");
            }
            try {
                Tag expenseTag = currentExpense.getTag();
                String color = expenseTag.getColor();
                String tagName = expenseTag.getType();
                TagNameLabel.setText(tagName);
                upperLine.setFill(Color.web(color));
                lowerLine.setFill(Color.web(color));
                System.out.println(color);
                System.out.println(tagName);
            } catch (Exception e){
                System.out.println("An error has occured whilst setting up tags!");
            }

        } catch (Exception e){
            System.out.println("There was an error whilst fetching expense Data");
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void goBackToEvent(){
        mainCtrl.showEventOverview(eventId);
    }

    public void setTagColor(){

    }

}
