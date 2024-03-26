package client.scenes;

import client.sceneSupportClasses.EventInfo;
import commons.Event;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

public class SeeEventsAsAdminCtrl {
    @Inject
    private MainCtrl mainCtrl;
    @FXML
    Button btnManagementOverview;
    @FXML
    TableView<EventInfo> eventTable;

    public SeeEventsAsAdminCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }
}
