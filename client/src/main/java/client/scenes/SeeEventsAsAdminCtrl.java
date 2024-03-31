package client.scenes;

import client.sceneSupportClasses.EventInfo;
import client.utils.ServerUtils;
import commons.Event;
import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SeeEventsAsAdminCtrl {
    private MainCtrl mainCtrl;
    ServerUtils server;
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
    Boolean ascending;

    @Inject
    public SeeEventsAsAdminCtrl(MainCtrl mainCtrl, ServerUtils sever) {
        this.mainCtrl = mainCtrl;
        this.server = sever;
    }

    public void setup() {
        createTable();
        setTextLanguage();
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
        selectOrdering.setOnAction(event -> {
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
        });
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

    public void setTextLanguage() {
        int languageIndex = mainCtrl.getLanguageIndex();
        if (languageIndex < 0)
            languageIndex = 0;
        ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.language_" + mainCtrl.getLanguageWithoutImagePath());
        btnManagementOverview.setText(resourceBundle.getString("ManagementOverview"));
        selectAscDesc.setPromptText(resourceBundle.getString("SelectAscDesc"));
        ascDesc.set(0, resourceBundle.getString("Ascending"));
        ascDesc.set(1, resourceBundle.getString("Descending"));
        selectOrdering.setPromptText(resourceBundle.getString("SelectHowToSeeEvents"));
        types.set(0, resourceBundle.getString("ByName"));
        types.set(1, resourceBundle.getString("ByCreationDate"));
        types.set(2, resourceBundle.getString("ByLastUpdate"));
        eventTable.getColumns().get(0).setText(resourceBundle.getString("Id"));
        eventTable.getColumns().get(1).setText(resourceBundle.getString("Name"));
        eventTable.getColumns().get(2).setText(resourceBundle.getString("CreatedAt"));
        eventTable.getColumns().get(3).setText(resourceBundle.getString("UpdatedAt"));
        eventTable.getColumns().get(4).setText(resourceBundle.getString("NrParticipants"));

    }

        public void goToManagementOverview() {
        mainCtrl.showManagementOverview();
    }

}
