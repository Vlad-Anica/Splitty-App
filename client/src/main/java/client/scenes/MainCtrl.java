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
import commons.Expense;
import commons.Person;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
    private Scene selectServerScene;
    private SelectServerCtrl selectServerCtrl;


    private ServerUtils server;
    private int languageIndex;
    private List<String> languages;
    //id of the user using this app
    private long userId;
    private String language;
    private boolean restart = false;
    private String currentIPAddress;
    private File userConfig;

    private final Image logo = new Image("/logos/splittyLogo256.png");

    public void initialize(Stage primaryStage, Pair<SettingsCtrl, Parent> settings,
                           Pair<AddParticipantCtrl, Parent> addParticipant, Pair<HomeCtrl, Parent> home,
                           Pair<OpenDebtsCtrl, Parent> openDebts, Pair<AddExpenseCtrl, Parent> addExpense,
                           Pair<EventOverviewCtrl, Parent> eventOverview, Pair<StatisticsCtrl, Parent> statistics,
                           Pair<ManagementOverviewCtrl, Parent> managementOverview, Pair<AddLanguageCtrl, Parent> addLanguage,
                           Pair<StartPageCtrl, Parent> startPage, Pair<SeeEventsAsAdminCtrl, Parent> seeEventsAsAdmin,
                           Pair<CreateEventCtrl, Parent> createEvent, Pair<SelectServerCtrl, Parent> selectServer,
                           ServerUtils server) {
        File languageFile = new File("./src/main/resources/languages/languages.txt");
        System.out.println(languageFile.getAbsolutePath());
        if (languageFile.exists()) {
            Pair<Integer, List<String>> pair = readFromFile(languageFile.getAbsolutePath());
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
        System.out.println("LAnguages: " + languages);

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

        this.selectServerCtrl = selectServer.getKey();
        this.selectServerScene = new Scene(selectServer.getValue());

        this.server = server;

        showSelectServer();

        primaryStage.show();
        primaryStage.getIcons().add(logo);
        Font RowdiesTest = Font.loadFont(Main.class.getResourceAsStream("/client/fonts/Rowdies-Regular.ttf"), 16);
        System.out.println(RowdiesTest.getFamily());
        System.out.println(RowdiesTest.getName());

    }

    public ResourceBundle getLanguageResource() {
        int languageIndex = getLanguageIndex();
        if (languageIndex < 0)
            languageIndex = 0;
        return ResourceBundle.getBundle("languages.language_" +
                getLanguageWithoutImagePath());
    }

    public String getLanguageWithoutImagePath() {
        return languages.get(languageIndex).split(";")[0];
    }

    public String getPathToFlagImage() {
        return languages.get(languageIndex).split(";")[1];
    }


    public void getLastKnownInfo() {
        if (!userConfig.exists()) {
            languageIndex = 0;
            userId = -1;
        } else {
            Scanner fileScanner = null;
            try {
                fileScanner = new Scanner(userConfig);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                ;
            }
            languageIndex = fileScanner.nextInt();
            userId = fileScanner.nextLong();
        }
    }

    public int getLanguageIndex() {
        return languageIndex;
    }

    public String getLanguage() {
        return language;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setRestart(boolean restart) {
        this.restart = restart;
    }

    public boolean getRestart() {
        return restart;
    }

    public void setLanguageIndex(int languageIndex) {
        this.languageIndex = languageIndex;


        PrintWriter pw = null;
        try {
            pw = new PrintWriter(userConfig);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        pw.println(this.languageIndex);
        pw.println(this.userId);
        pw.close();
    }

    /**
     * returns or creates a new file given a file path
     *
     * @param path the file path provided
     * @return the file which is returned or created
     */
    public File getOrCreateFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * get from the IPAddresses file the ip addresses used before
     *
     * @return list of ip addresses used
     */
    public List<String> getUsedIPAddresses() {
        File file = getOrCreateFile("./src/main/resources/userInfo/IPAddresses.txt");
        List<String> IPAddresses = new ArrayList<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (scanner.hasNextLine()) {
            IPAddresses.add(scanner.nextLine());
        }
        return IPAddresses;
    }

    /**
     * check if a provided IP address has been used before
     *
     * @param IPAddress the provided IP address
     * @return true if the user has been connected to it before
     */
    public boolean hasBeenConnected(String IPAddress) {
        return getUsedIPAddresses().contains(IPAddress);
    }

    /**
     * adds a new IP address to the list of IP addresses used before
     *
     * @param IPAddress the IP address provided
     */
    public void addNewIPAddress(String IPAddress) {
        List<String> IPAddresses = getUsedIPAddresses();
        IPAddresses.add(IPAddress);
        File file = getOrCreateFile("./src/main/resources/userInfo/IPAddresses.txt");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (String address : IPAddresses) {
            assert writer != null;
            writer.println(address);
        }
        writer.close();
        setIPAddress(IPAddress);
    }

    /**
     * returns the position of the IP address in the
     *
     * @param IPAddress
     * @return
     */
    public int getIPAddressPosition(String IPAddress) {
        return getUsedIPAddresses().indexOf(IPAddress);
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void save(Pair<Integer, List<String>> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./src/main/resources/languages/languages.txt"))) {
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Pair<Integer, List<String>> readFromFile(String filename) {
        System.out.println(filename);
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Pair<Integer, List<String>> list = (Pair<Integer, List<String>>) ois.readObject();
            return list;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void showSelectServer() {
        selectServerCtrl.setup();
        primaryStage.setTitle("Select Server");
        primaryStage.setScene(selectServerScene);
        selectServerScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                System.out.println("Trying to join!");
                selectServerCtrl.connect();
            }
        });

    }

    public void showSeeEventsAsAdmin() {
        seeEventsAsAdminCtrl.setup();
        primaryStage.setTitle("See Events As admin");
        seeEventsAsAdminScene.getStylesheets().add("client/css/SeeEventsAsAdmin.css");
        primaryStage.setScene(seeEventsAsAdminScene);
    }

    public void showEventOverview(Long eventId) {
        eventOverviewCtrl.setup(eventId);
        primaryStage.setTitle("Event Overview");
        eventOverviewScene.getStylesheets().add("client/css/EventOverview.css");
        primaryStage.setScene(eventOverviewScene);
    }

    public void showCreateEvent() {
        primaryStage.setTitle("Create new Event");
        primaryStage.setScene(createEventScene);
        createEventCtrl.setup();
        createEventScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                System.out.println("Trying to create a new event!");
                createEventCtrl.createEvent(new ActionEvent());
            }
        });
        createEventScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.BACK_SPACE) {
                System.out.println("GOING HOME");
                try {
                    createEventCtrl.goHome(new ActionEvent());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void showAddParticipant(Long eventId, boolean isInEditMode, Person participantToEdit) {
        primaryStage.setTitle("Show Participants");
        primaryStage.setScene(addParticipantScene);
        addParticipantCtrl.setup(eventId, isInEditMode, participantToEdit);
    }

    public void showHome() {
        primaryStage.setTitle("Home");
        primaryStage.setScene(homeScene);
        homeCtrl.setUserName(userId);
        homeCtrl.setup();
        homeCtrl.refresh();
    }

    public void showOpenDebts() {
        primaryStage.setScene(openDebtsScene);
        openDebtsCtrl.setup();
        openDebtsScene.getStylesheets().add("/client/css/opendebts.css");

        openDebtsScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.BACK_SPACE) {
                System.out.println("Going Back!");
                try {
                    openDebtsCtrl.goBack(new ActionEvent());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void showSettings() {
        primaryStage.setTitle("Settings");
        primaryStage.setScene(settingsScene);
        settingsCtrl.setup();
        settingsScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                System.out.println("Submitting!");
                settingsCtrl.clickBtnSubmitAll();
            }
        });
        settingsScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.BACK_SPACE) {
                System.out.println("GOING HOME");
                settingsCtrl.clickBtnHome();
            }
        });
    }

    public void showStartPage() {
        primaryStage.setTitle("Start Page");
        primaryStage.setScene(startPageScene);
        startPageCtrl.setup();
        startPageScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                try {
                    System.out.println("Trying to create a new user!");
                    startPageCtrl.createUser(new ActionEvent());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void showAddExpense(long eventID, boolean  isInEditMode, Expense expenseToEdit) {
        primaryStage.setTitle("Add Expense");
        primaryStage.setScene(addExpenseScene);
        addExpenseCtrl.initializePage(eventID, isInEditMode, expenseToEdit);

        addExpenseScene.getStylesheets().add("/client/css/addExpense.css");
        addExpenseScene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> addExpenseCtrl.createExpense();
                case BACK_SPACE -> addExpenseCtrl.cancel();
            }
        });
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

    public void showStatsTest(Long eventId) {
        statisticsCtrl.setup(eventId);
        primaryStage.setTitle("Statistics");
        primaryStage.setScene(statisticsScene);

    }

    public void showManagementOverview() {
        primaryStage.setTitle("Management Overview");
        primaryStage.setScene(managementOverviewScene);
        managementOverviewScene.getStylesheets().add("client/css/ManagementOverview.css");
        managementOverviewCtrl.setUp();
    }

    public void showAddLanguage() {
        primaryStage.setTitle("Add Language");
        primaryStage.setScene(addLanguageScene);
        addLanguageCtrl.setUp();
        addLanguageScene.getStylesheets().add("/client/css/opendebts.css");
        addLanguageScene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case BACK_SPACE -> {
                    try {
                        addLanguageCtrl.goHome(new ActionEvent());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                case ENTER -> addLanguageCtrl.addLanguage(new ActionEvent());
            }
        });
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public List<Event> getEvents(Long userId) {
        return server.getEvents(userId);
    }

    public Long getUserId() {
        return userId;
    }

    public File getUserConfig() {
        return userConfig;
    }

    public void setIPAddress(String IPAddress) {
        this.currentIPAddress = IPAddress;
        this.userConfig = new File("userConfig" + getIPAddressPosition(IPAddress));
    }
}
