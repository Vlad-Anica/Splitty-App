/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.List;

public class MainCtrl {

    private Stage primaryStage;

    private QuoteOverviewCtrl overviewCtrl;
    private Scene overview;

    private AddQuoteCtrl addCtrl;
    private Scene add;
    //scene and controller for settings
    private SettingsCtrl settingsCtrl;
    private Scene settingsScene;

    //scene and controller for AddParticipant
    private AddParticipantCtrl addParticipantCtrl;
    private Scene addParticipantScene;

    //scene and controller for Home
    private HomeCtrl homeCtrl;
    private Scene homeScene;

    //open debts scene and controller
    private OpenDebtsCtrl openDebtsCtrl;
    private Scene openDebtsScene;

    //scene and controller for addExpense
    private AddExpenseCtrl addExpenseCtrl;
    private Scene addExpenseScene;

    //Scene and controller for EventOverview
    private EventOverviewCtrl eventOverviewCtrl;
    private Scene eventOverviewScene;

    private StatisticsCtrl statisticsCtrl;
    private Scene statisticsScene;

    //Scene and controller for ManagementOverview
    private ManagementOverviewCtrl managementOverviewCtrl;
    private Scene managementOverview;

    private ServerUtils server;
    private int languageIndex;


    public void initialize(Stage primaryStage, Pair<SettingsCtrl, Parent> settings,
                           Pair<AddParticipantCtrl, Parent> addParticipant, Pair<HomeCtrl, Parent> home,
                           Pair<OpenDebtsCtrl, Parent> openDebts, Pair<AddExpenseCtrl, Parent> addExpense,
                           Pair<EventOverviewCtrl, Parent> eventOverview, Pair<StatisticsCtrl, Parent> statistics,
                           Pair<ManagementOverviewCtrl, Parent> managementOverview,
                           ServerUtils server) {
        this.languageIndex = 0;
        this.primaryStage = primaryStage;

        this.settingsCtrl = settings.getKey();
        this.settingsScene = new Scene(settings.getValue());

        this.addParticipantCtrl = addParticipant.getKey();
        this.addParticipantScene = new Scene(addParticipant.getValue());

        this.homeCtrl = home.getKey();
        this.homeScene = new Scene(home.getValue());

        this.openDebtsCtrl = openDebts.getKey();
        this.openDebtsScene = new Scene(openDebts.getValue());

        this.addExpenseCtrl = addExpense.getKey();
        this.addExpenseScene = new Scene(addExpense.getValue());

        this.eventOverviewCtrl = eventOverview.getKey();
        this.eventOverviewScene = new Scene(eventOverview.getValue());

        this.statisticsCtrl = statistics.getKey();
        this.statisticsScene = new Scene(statistics.getValue());

        this.managementOverviewCtrl = managementOverview.getKey();
        this.managementOverview = new Scene(managementOverview.getValue());

        this.server = server;

        showHome();
        primaryStage.show();
    }

    public int getLanguageIndex() {
        return languageIndex;
    }

    public void setLanguageIndex(int languageIndex) {
        this.languageIndex = languageIndex;
    }

    public void showAddParticipant() {
        primaryStage.setTitle("Show Participants");
        primaryStage.setScene(addParticipantScene);
    }

    public void showHome() {
        primaryStage.setTitle("Home");
        primaryStage.setScene(homeScene);
        homeCtrl.setup();
    }

    public void showOpenDebts() {
        primaryStage.setScene(openDebtsScene);
        openDebtsCtrl.setup();
    }

    public void showSettings() {
        primaryStage.setTitle("Settings");
        primaryStage.setScene(settingsScene);
        settingsCtrl.setup();
    }

    public void showAddExpense() {
        primaryStage.setTitle("Add Expense");
        primaryStage.setScene(addExpenseScene);
        addExpenseCtrl.initializePage();
    }

    public void showEventOverview() {
        primaryStage.setTitle("Event Overview");
        primaryStage.setScene(eventOverviewScene);
    }

    public void showOverview() {
        primaryStage.setTitle("Quotes: Overview");
        primaryStage.setScene(overview);
        overviewCtrl.refresh();
    }

    public void showAdd() {
        primaryStage.setTitle("Quotes: Adding Quote");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    public void showStatsTest() {
        String name = eventOverviewCtrl.getEventName();
        primaryStage.setTitle("Statistics");
        primaryStage.setScene(statisticsScene);
        statisticsCtrl.PieChartExpenses.setTitle(name);
    }
    public void showManagementOverview(){
        primaryStage.setTitle("Management Overview");
        primaryStage.setScene(managementOverview);
        managementOverviewCtrl.setUp();
    }

    public Stage getPrimaryStage(){
        return primaryStage;
    }
    public List<Event> getEvents(Long userId) {
        return server.getEvents(userId);
    }
}