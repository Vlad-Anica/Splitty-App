package client.scenes;
import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Tag;
import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StatisticsCtrl implements Initializable {


private final EventOverviewCtrl eventController;
private Event event;
private String eventTitle;
private ServerUtils server;
private ArrayList<Tag> tags;
private ArrayList<Expense> allExpenses;

@FXML
Button temporaryButton;

@FXML
PieChart PieChartExpenses;
@FXML
ObservableList<PieChart.Data> pieChartExpensesData =
        FXCollections.observableArrayList(
                new PieChart.Data("Grapefruit", 13),
                new PieChart.Data("Oranges", 25),
                new PieChart.Data("Plums", 10),
                new PieChart.Data("Pears", 22),
                new PieChart.Data("Apples", 30));

    @Inject
    public StatisticsCtrl(EventOverviewCtrl eventController, ServerUtils server){
        this.eventController = eventController;
        this.server = server;
    }

    public void setup(Long eventId) {
        try {
            event = server.getEvent(eventId);
            eventTitle = event.getName();
        } catch (Exception e) {
            System.out.println("Cannot find associated Event within the repository!");
            return;
        }
        if (event == null) {
            System.out.println("Cannot find associated Event within the repository!");
            return;
        }
        try{
            allExpenses = (ArrayList<Expense>) event.getExpenses();
        } catch (Exception e){
            System.out.println("An error has occured when fetching the expenses!");
        }
        try{
            tags = new ArrayList<>();
            for (Expense e : allExpenses){
                tags.add(e.getTag());
            }
        } catch (Exception e){
            System.out.println("Whoopsie, an error has occured!");
            return;
        }
        try{
            pieChartExpensesData.clear();
            ArrayList<String> uniqueTags = new ArrayList<>();
            ArrayList<String> allTagNames = new ArrayList<>();
            for (Tag tag : tags){
                String tagName = tag.getType();
                if(!uniqueTags.contains(tagName)){
                    uniqueTags.add(tagName);
                    allTagNames.add(tagName);
                }
                else {
                    allTagNames.add(tagName);
                }
            }
            for (String tagName : uniqueTags){
                int amount = 0;
                for (String allTags : allTagNames){
                    if (allTags.equals(tagName)) amount++;
                }
                pieChartExpensesData.add(new PieChart.Data(tagName, amount));
            }
        } catch (Exception e){
            System.out.println("An error occured when processing the tags!");
            pieChartExpensesData.add(new PieChart.Data("Whoops No Data Found", 1));
            return;
        }
    }


    @Override
public void initialize(URL location, ResourceBundle resources) {
    PieChartExpenses.setData(pieChartExpensesData);
    PieChartExpenses.setTitle(eventTitle);
}


}
