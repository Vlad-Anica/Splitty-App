package client.scenes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.*;

import java.net.URL;
import java.util.ResourceBundle;


public class StatisticsCtrl implements Initializable {

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

@Override
public void initialize(URL location, ResourceBundle resources) {
    PieChartExpenses.setData(pieChartExpensesData);
}


}
