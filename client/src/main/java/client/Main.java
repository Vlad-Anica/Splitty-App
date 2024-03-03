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
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URISyntaxException;

import static com.google.inject.Guice.createInjector;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }
    //Test made by Emanuel
    int oneFour = 10 + 4;
    @Override
    public void start(Stage primaryStage) throws IOException {

//        var overview = FXML.load(QuoteOverviewCtrl.class, "client", "scenes", "QuoteOverview.fxml");
//        var add = FXML.load(AddQuoteCtrl.class, "client", "scenes", "AddQuote.fxml");
//
//        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
//        mainCtrl.initialize(primaryStage);

//        var page = FXML.load(AddParticipantCtrl.class, "client", "scenes", "AddParticipant.fxml");
//        var ctrl = INJECTOR.getInstance(AddParticipantCtrl.class);

//        var HomePage = FXML.load((HomeCtrl.class), "client", "scenes", "Home.fxml");
//        var HomeCtrl = INJECTOR.getInstance(HomeCtrl.class);

        var home = FXML.load(HomeCtrl.class, "client", "scenes", "Home.fxml");
        var addExpense = FXML.load(AddExpenseCrtl.class, "client", "scenes", "AddExpense.fxml");
        var addParticipant = FXML.load(AddParticipantCtrl.class, "client", "scenes", "AddParticipant.fxml");
        var openDebts = FXML.load(OpenDebtsCtrl.class, "client", "scenes", "OpenDebts.fxml");
        var settings = FXML.load(SettingsCtrl.class, "client", "scenes", "Settings.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, settings, addParticipant, home, openDebts, addExpense);
    }

}