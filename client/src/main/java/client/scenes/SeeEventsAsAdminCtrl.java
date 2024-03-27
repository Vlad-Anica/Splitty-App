package client.scenes;

import client.sceneSupportClasses.EventInfo;
import client.utils.ServerUtils;
import commons.Event;
import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;

public class SeeEventsAsAdminCtrl {
    private MainCtrl mainCtrl;
    ServerUtils server;
    @FXML
    Button btnManagementOverview;
    @FXML
    TableView<EventInfo> eventTable;
    @FXML
    ComboBox<String> showEventType;
    List<String> types = new ArrayList<>(List.of("By name", "By creation date", "By last update"));

    @Inject
    public SeeEventsAsAdminCtrl(MainCtrl mainCtrl, ServerUtils sever) {
        this.mainCtrl = mainCtrl;
        this.server = sever;
    }

    public void setup() {
        showEventType.setItems(FXCollections.observableList(types.stream().toList()));
        showEventType.setOnAction(event -> {
            int selection = showEventType.getSelectionModel().getSelectedIndex();
            if (selection >= 0) {
                List<EventInfo> toShow = new ArrayList<>();
                List<Event> events = null;
                if (selection == 0) {
                    events = server.getEventsOrderedByName();
                } else if (selection == 1) {
                    events = server.getEventsOrderedByCreationDate();
                } else if (selection == 2) {
                    events = server.getEventsOrderedByLastModificationDate();
                }
                if (events.size() > 0) {
                    for (Event x: events) {
                        toShow.add(new EventInfo(x.getId(), x.getName(), x.getCreatedAt(), x.getUpdatedAt(), x.getParticipants().size()));
                    }
                }

                eventTable.setItems(FXCollections.observableList(toShow.stream().toList()));

            }
        });
    }

    public void goToManagementOverview() {
        mainCtrl.showManagementOverview();
    }

}
