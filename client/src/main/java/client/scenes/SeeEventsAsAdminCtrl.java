package client.scenes;

import client.sceneSupportClasses.EventInfo;
import client.utils.ServerUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Event;
import commons.Expense;
import commons.Person;
import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class SeeEventsAsAdminCtrl {
    private MainCtrl mainCtrl;
    ServerUtils server;
    @FXML
    Button btnDeleteEvent;
    @FXML
    Button btnDownload;
    @FXML
    Button btnImport;
    @FXML
    Pane importPane;
    @FXML
    TextArea jsonDumpField;
    @FXML
    Button btnImportOK;
    @FXML
    Button btnImportCancel;
    @FXML
    Label jsonImportLabel;
    @FXML
    TextField eventToDeleteId;
    @FXML
    Label statusLabel;
    @FXML
    Button btnManagementOverview;
    @FXML
    TableView<EventInfo> eventTable;
    @FXML
    ComboBox<String> selectOrdering;
    @FXML
    ComboBox<String> selectAscDesc;
    List<String> types = new ArrayList<>(List.of("name", "creationDate", "lastUpdate"));
    List<String> ascDesc = new ArrayList<>(List.of("asc", "desc"));
    List<Event> events;
    ObservableList<Event> data;
    Boolean ascending;
    String statusNonExistentEvent;
    String downloadEventSucces;
    String deleteEventSucces;
    @Inject
    public SeeEventsAsAdminCtrl(MainCtrl mainCtrl, ServerUtils sever) {
        this.mainCtrl = mainCtrl;
        this.server = sever;
    }

    public void refresh() {
        events = server.getAllEvents();
        data = FXCollections.observableList(events);
        eventTable.setItems(FXCollections.observableList(data.stream()
                .map(e -> new EventInfo(e.getId(), e.getName(),
                        e.getCreatedAt(), e.getUpdatedAt(), e.getParticipants().size()))
                .toList()));
    }
    public void setup() {

        createTable();
        setTextLanguage();
        hideImportPane();
        ascending = false;
        selectOrdering.setItems(FXCollections.observableList(types.stream().toList()));
        selectAscDesc.setItems(FXCollections.observableList(ascDesc.stream().toList()));
        selectAscDesc.setOnAction(event -> {
            if (selectOrdering.getSelectionModel().getSelectedIndex() >= 0) {
                int selection = selectAscDesc.getSelectionModel().getSelectedIndex();
                if (selection >= 0) {
                    if ((selection == 0 && !ascending) || (selection == 1 && ascending)) {
                        events = events.reversed();
                        ascending = !ascending;
                    }
                    printToTable();
                }
            }
        });
        btnDownload.setOnAction(event -> {

            int languageIndex = mainCtrl.getLanguageIndex();
            if (languageIndex < 0)
                languageIndex = 0;
            ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.language_" + mainCtrl.getLanguageWithoutImagePath());
            try {
                Long eventId = Long.parseLong(eventToDeleteId.getText());
                if (server.getEvent(eventId) == null) {
                    statusLabel.setText(statusNonExistentEvent);
                    statusLabel.setTextFill(Color.RED);
                    return;
                }
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Download alert");
                alert.setContentText(resourceBundle.getString("DownloadThisEvent"));
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK)
                  downloadJSONDump();
            } catch (URISyntaxException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText(resourceBundle.getString("InputProperEventId"));
            }
        });
        btnImport.setOnAction(event -> showImportPane());
        btnImportOK.setOnAction(event -> {

            int languageIndex = mainCtrl.getLanguageIndex();
            if (languageIndex < 0)
                languageIndex = 0;
            ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.language_" + mainCtrl.getLanguageWithoutImagePath());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Import Alert");
            alert.setContentText(resourceBundle.getString("ImportThisEvent"));
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
                importJSONDump();

        });
        btnImportCancel.setOnAction(event -> {
            hideImportPane();
        });
        selectOrdering.setOnAction(event -> {
            printSortedEvents();
        });
        server.registerForUpdates(event -> {
            printSortedEvents();
        });
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                printSortedEvents();
            }
        }, 1000L, 5000L);
    }


    public void computeUpdatedAt() {
        ArrayList<Event> eList = (ArrayList<Event>) server.getAllEvents();
        for(Event event : eList) {
            for(Person person : event.getParticipants()) {
                if(person.getUpdatedAt().after(event.getUpdatedAt())) {
                    event.setUpdatedAt(person.getUpdatedAt());
                }
            }
            for(Expense expense : event.getExpenses()) {
                if(expense.getUpdatedAt().after(event.getUpdatedAt())) {
                    event.setUpdatedAt(expense.getUpdatedAt());
                }
            }
            server.updateEvent(event.getId(), event);
        }

    }
    public void printSortedEvents() {
        int selection = selectOrdering.getSelectionModel().getSelectedIndex();
        if (selection >= 0) {
                computeUpdatedAt();
            if (selection == 0) {
                events = server.getEventsOrderedByName();
            } else if (selection == 1) {
                events = server.getEventsOrderedByCreationDate();
            } else if (selection == 2) {
                events = server.getEventsOrderedByLastModificationDate();
            }
            if (ascending) {
                events = events.reversed();
            }
            printToTable();
        }
    }

    /**
     * Write the list of events to the table
     */
    public void printToTable() {
        List<EventInfo> toShow = new ArrayList<>();
        if (events.size() > 0) {
            for (Event x: events) {
                toShow.add(new EventInfo(x.getId(), x.getName(), x.getCreatedAt(), x.getUpdatedAt(), x.getParticipants().size()));
            }
        }
        eventTable.setItems(FXCollections.observableList(toShow.stream().toList()));
    }
    /**
     * Create the table's columns and set what each of them will hold
     */
    public void createTable() {
        TableColumn<EventInfo, String> idColumn = new TableColumn<>();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<EventInfo, String> nameColumn = new TableColumn<>();
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<EventInfo, String> createdAtColumn = new TableColumn<>();
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        TableColumn<EventInfo, String> updatedAtColumn = new TableColumn<>();
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));

        TableColumn<EventInfo, String> nrParticipantsColumn = new TableColumn<>();
        nrParticipantsColumn.setCellValueFactory(new PropertyValueFactory<>("nrParticipants"));

        eventTable.getColumns().setAll(idColumn, nameColumn, createdAtColumn, updatedAtColumn, nrParticipantsColumn);

    }

    /**
     * download a json dump of the event with the specified id
     * @throws URISyntaxException
     * @throws IOException
     */
    public void downloadJSONDump() throws URISyntaxException, IOException {
        Long eventId = Long.parseLong(eventToDeleteId.getText());
        statusLabel.setText(downloadEventSucces);
        statusLabel.setTextFill(Color.BLACK);
        server.downloadJSONDump(eventId);
    }

    /**
     * Import a json dump of an event and map it to an event object
     *
     */
    public void importJSONDump()  {

        try {
            if (jsonDumpField == null || jsonDumpField.getText().isEmpty())
                return;
            ObjectMapper mapper = new ObjectMapper();
            Event event = mapper.readValue(jsonDumpField.getText().getBytes(), Event.class);
            server.createEvent(event);
            hideImportPane();
        } catch (IOException e) {
            System.out.println("Invalid JSON Format");
        }

    }

    public void showImportPane() {

        importPane.setVisible(true);
        importPane.setDisable(false);
    }

    public void hideImportPane() {
        jsonDumpField.clear();
        importPane.setDisable(true);
        importPane.setVisible(false);
    }

    public void stop() {
        server.stop();
    }
    
    public void deleteEventById() {
        Long eventId = Long.parseLong(eventToDeleteId.getText());
        server.deleteEventById(eventId);
        int selection = selectOrdering.getSelectionModel().getSelectedIndex();
        if (selection >= 0) {
            if (selection == 0) {
                events = server.getEventsOrderedByName();
            } else if (selection == 1) {
                events = server.getEventsOrderedByCreationDate();
            } else if (selection == 2) {
                events = server.getEventsOrderedByLastModificationDate();
            }
            if (ascending) {
                events = events.reversed();
            }
            printToTable();
        }
        statusLabel.setTextFill(Color.BLACK);
        statusLabel.setText(deleteEventSucces);
    }

    public void setTextLanguage() {
        int languageIndex = mainCtrl.getLanguageIndex();
        if (languageIndex < 0)
            languageIndex = 0;
        ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.language_" + mainCtrl.getLanguageWithoutImagePath());
        btnDeleteEvent.setText(resourceBundle.getString("DeleteEvent"));
        btnDownload.setText(resourceBundle.getString("Download"));
        btnImport.setText(resourceBundle.getString("Import"));
        eventToDeleteId.setPromptText(resourceBundle.getString("EventId"));
        btnManagementOverview.setText(resourceBundle.getString("ManagementOverview"));
        selectAscDesc.setPromptText(resourceBundle.getString("SelectAscDesc"));
        ascDesc.set(0, resourceBundle.getString("Ascending"));
        ascDesc.set(1, resourceBundle.getString("Descending"));
        selectOrdering.setPromptText(resourceBundle.getString("SelectHowToSeeEvents"));
        statusNonExistentEvent = resourceBundle.getString("NonExistentInputEvent");
        deleteEventSucces = resourceBundle.getString("DeleteEventSuccess");
        downloadEventSucces = resourceBundle.getString("DownloadEventSuccess");
        types.set(0, resourceBundle.getString("ByName"));
        types.set(1, resourceBundle.getString("ByCreationDate"));
        types.set(2, resourceBundle.getString("ByLastUpdate"));
        eventTable.getColumns().get(0).setText(resourceBundle.getString("Id"));
        eventTable.getColumns().get(1).setText(resourceBundle.getString("Name"));
        eventTable.getColumns().get(2).setText(resourceBundle.getString("CreatedAt"));
        eventTable.getColumns().get(3).setText(resourceBundle.getString("UpdatedAt"));
        eventTable.getColumns().get(4).setText(resourceBundle.getString("NrParticipants"));
        btnImportCancel.setText(resourceBundle.getString("Cancel"));
        jsonImportLabel.setText(resourceBundle.getString("JSONPasteLabel"));

    }

        public void goToManagementOverview() {
        mainCtrl.showManagementOverview();
    }

}
