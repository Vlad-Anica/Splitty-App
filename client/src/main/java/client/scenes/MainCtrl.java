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

import client.Main;
import client.utils.ServerUtils;
import commons.Event;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    private StartPageCtrl startPageCtrl;
    private Scene startPageScene;

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
    private Scene managementOverviewScene;

    //Scene and controller for AddLanguage
    private AddLanguageCtrl addLanguageCtrl;
    private Scene addLanguageScene;

    private SeeEventsAsAdminCtrl seeEventsAsAdminCtrl;

    private CreateEventCtrl createEventCtrl;
    private Scene createEventScene;
    private Scene seeEventsAsAdminScene;


    private ServerUtils server;
    private int languageIndex;
    private List<String> languages;
    //id of the user using this app
    private long userId;
    private String language;
    private boolean restart = false;

    private final Image logo = new Image("/logos/splittyLogo256.png");

    public void initialize(Stage primaryStage, Pair<SettingsCtrl, Parent> settings,
                           Pair<AddParticipantCtrl, Parent> addParticipant, Pair<HomeCtrl, Parent> home,
                           Pair<OpenDebtsCtrl, Parent> openDebts, Pair<AddExpenseCtrl, Parent> addExpense,
                           Pair<EventOverviewCtrl, Parent> eventOverview, Pair<StatisticsCtrl, Parent> statistics,
                           Pair<ManagementOverviewCtrl, Parent> managementOverview, Pair<AddLanguageCtrl, Parent> addLanguage,
                           Pair<StartPageCtrl, Parent> startPage, Pair<SeeEventsAsAdminCtrl, Parent> seeEventsAsAdmin,
                           Pair<CreateEventCtrl, Parent> createEvent, ServerUtils server) {
        getLastKnownInfo();
        File languageFile = new File("client/src/main/resources/languages/languages.txt");
        if (languageFile.exists()) {
            Pair<Integer, List<String>> pair = readFromFile("client/src/main/resources/languages/languages.txt");
            if (pair == null) {
                System.out.println("NULLLLLL");
            }
            assert pair != null;

            this.languages = pair.getValue();
        } else {
            languages = new ArrayList<>();
            languages.add("English(US);client/images/EnglishFlag.jpg");
            languages.add("Nederlands;client/images/DutchFlag.png");
            Pair<Integer, List<String>> languageData = new Pair<>(0, languages);
            try {
                languageFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            save(languageData);
        }
        System.out.println("LAnguages: "+languages);

//        this.languages = languages;
//        this.languageIndex = 0;

        this.language = languages.get(languageIndex);
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
        this.managementOverviewScene = new Scene(managementOverview.getValue());

        this.addLanguageCtrl = addLanguage.getKey();
        this.addLanguageScene = new Scene(addLanguage.getValue());

        this.startPageCtrl = startPage.getKey();
        this.startPageScene = new Scene(startPage.getValue());

        this.seeEventsAsAdminCtrl = seeEventsAsAdmin.getKey();
        this.seeEventsAsAdminScene = new Scene(seeEventsAsAdmin.getValue());

        this.createEventCtrl = createEvent.getKey();
        this.createEventScene = new Scene(createEvent.getValue());

        this.server = server;

        File file = new File("userConfig.txt");
        if(!file.exists()){
            showStartPage();
        } else {
            getLastKnownInfo();
            showHome();
        }
        primaryStage.show();
        primaryStage.getIcons().add(logo);
        Font RowdiesTest = Font.loadFont(Main.class.getResourceAsStream("/client/fonts/Rowdies-Regular.ttf"), 16);
        System.out.println(RowdiesTest.getFamily());
        System.out.println(RowdiesTest.getName());

    }

    public String getLanguageWithoutImagePath() {
        return languages.get(languageIndex).split(";")[0];
    }
    public String getPathToFlagImage() {
        return languages.get(languageIndex).split(";")[1];
    }

    public void getLastKnownInfo() {
        File file = new File("userConfig.txt");
        if (!file.exists()) {
            languageIndex = 0;
            userId = -1;
        } else {
            Scanner fileScanner = null;
            try {
                fileScanner = new Scanner(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();;
            }
            languageIndex = fileScanner.nextInt();
            userId = fileScanner.nextLong();
        }
    }

    public int getLanguageIndex() {
        return languageIndex;
    }
    public String getLanguage(){
        return language;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setRestart(boolean restart) {
        this.restart = restart;
    }

    public boolean getRestart(){
        return restart;
    }
    public void setLanguageIndex(int languageIndex) {
        this.languageIndex = languageIndex;
        File file = new File("userConfig.txt");

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        pw.println(this.languageIndex);
        pw.println(this.userId);
        pw.close();
    }
    public void setLanguage(String language){
        this.language = language;
    }
    public void save(Pair<Integer,List<String>> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("client/src/main/resources/languages/languages.txt"))) {
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Pair<Integer,List<String>> readFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Pair<Integer,List<String>> list = (Pair<Integer,List<String>>) ois.readObject();
            return list;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void showSeeEventsAsAdmin() {
        seeEventsAsAdminCtrl.setup();
        primaryStage.setTitle("See Events As admin");
        primaryStage.setScene(seeEventsAsAdminScene);
    }

    public void showEventOverview(Long eventId) {
        eventOverviewCtrl.setup(eventId);
        primaryStage.setTitle("Event Overview");
        primaryStage.setScene(eventOverviewScene);
    }

    public void showCreateEvent() {
        primaryStage.setTitle("Create new Event");
        primaryStage.setScene(createEventScene);
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

    public void showStartPage() {
        primaryStage.setTitle("Start Page");
        primaryStage.setScene(startPageScene);
        startPageCtrl.initializePage();
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
        primaryStage.setScene(managementOverviewScene);
        managementOverviewCtrl.setUp();
    }
    public void showAddLanguage(){
        primaryStage.setTitle("Add Language");
        primaryStage.setScene(addLanguageScene);
        addLanguageCtrl.setUp();
    }

    public Stage getPrimaryStage(){
        return primaryStage;
    }

    public List<Event> getEvents(Long userId) {
        return server.getEvents(userId);
    }

    public Long getUserId() {
        return userId;
    }
}
