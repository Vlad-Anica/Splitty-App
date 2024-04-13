package client.scenes;
import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Tag;
import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class StatisticsCtrl implements Initializable {


private final EventOverviewCtrl eventController;
private final MainCtrl mainCtrl;
private Event event;
private String eventTitle;
private ServerUtils server;
private ArrayList<Tag> tags;
private ArrayList<Expense> allExpenses;
private double totalAmount;
private boolean check = false;

private long eventId;

@FXML
Button temporaryButton;

@FXML
private Label totalExpensesLabel;

@FXML
PieChart PieChartExpenses;

@FXML
Button goBackButton;
@FXML
ObservableList<PieChart.Data> pieChartExpensesData =
        FXCollections.observableArrayList();

    @Inject
    public StatisticsCtrl(EventOverviewCtrl eventController, ServerUtils server, MainCtrl mainCtrl){
        this.eventController = eventController;
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void setup(Long eventId) {
        System.out.println("Setting up");
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
        PieChartExpenses.setTitle(eventTitle);
        this.eventId = eventId;
        loadExpenses(eventId);
    }

    public void loadExpenses(Long eventId){
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
        //pieChartExpensesData.clear();
        try{
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
                boolean checker = false;
                for (PieChart.Data piedata : pieChartExpensesData){
                    if (piedata.getName().equals(tagName)){
                        checker = true;
                        if (piedata.getPieValue() != amount) piedata.setPieValue(amount);
                    }
                }
                if (!checker) pieChartExpensesData.add(new PieChart.Data(tagName, amount));
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            System.out.println("An error occured when processing the tags!");
            pieChartExpensesData.add(new PieChart.Data("Whoops No Data Found", 1));
        }
        setTotalExpenses();
        String language = mainCtrl.getLanguage();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.language_" + mainCtrl.getLanguageWithoutImagePath());
        totalExpensesLabel.setText(resourceBundle.getString("Totalamounts") + " " + Math.round(totalAmount * 100.0) / 100.0);
        //HERE IS THE ERROR, AFTER THIS LINE
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PieChartExpenses.setData(pieChartExpensesData);
    }


    public void setTotalExpenses(){
        List<Expense> allExpenses = event.getExpenses();
        totalAmount = 0;
        for (Expense expense : allExpenses){
            totalAmount += expense.getAmount();
        }
    }

    public void goHome(ActionEvent event){
        mainCtrl.showEventOverview(eventId);
    }


}
