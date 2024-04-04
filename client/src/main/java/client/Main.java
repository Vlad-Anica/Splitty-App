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
package client;

import client.scenes.*;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ResourceBundle;

import static com.google.inject.Guice.createInjector;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);


    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("languages.language_English(US)");

        var home = FXML.load(HomeCtrl.class, bundle, "client", "scenes", "Home.fxml");
        var addExpense = FXML.load(AddExpenseCtrl.class, bundle, "client", "scenes", "AddExpense.fxml");
        var addParticipant = FXML.load(AddParticipantCtrl.class, bundle, "client", "scenes", "AddParticipant.fxml");
        var openDebts = FXML.load(OpenDebtsCtrl.class, bundle, "client", "scenes", "OpenDebts.fxml");
        var settings = FXML.load(SettingsCtrl.class, bundle, "client", "scenes", "Settings.fxml");
        var eventOverview = FXML.load(EventOverviewCtrl.class, bundle, "client", "scenes", "EventOverview.fxml");
        var statistics = FXML.load(StatisticsCtrl.class, bundle, "client", "scenes", "Statistics.fxml");
        var managementOverview = FXML.load(ManagementOverviewCtrl.class, bundle, "client", "scenes", "ManagementOverview.fxml");
        var addLanguage = FXML.load(AddLanguageCtrl.class, bundle, "client", "scenes", "AddLanguage.fxml");
        var startPage = FXML.load(StartPageCtrl.class, bundle, "client", "scenes", "StartPage.fxml");
        var seeEventsAsAdmin = FXML.load(SeeEventsAsAdminCtrl.class, bundle, "client", "scenes", "SeeEventsAsAdmin.fxml");
        var createEvent =  FXML.load(CreateEventCtrl.class, bundle, "client", "scenes", "CreateEvent.fxml");
        var selectServer = FXML.load(SelectServerCtrl.class, bundle, "client", "scenes", "SelectServer.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        ServerUtils server = INJECTOR.getInstance(ServerUtils.class);
        mainCtrl.initialize(primaryStage, settings, addParticipant, home, openDebts, addExpense, eventOverview, statistics, managementOverview, addLanguage, startPage, seeEventsAsAdmin, createEvent, selectServer, server);
        primaryStage.setOnCloseRequest(e -> {
            seeEventsAsAdmin.getKey().stop();
        });
    }

}