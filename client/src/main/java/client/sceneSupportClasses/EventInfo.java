package client.sceneSupportClasses;

import javafx.beans.property.SimpleStringProperty;
import javassist.Loader;

import java.util.Date;

public class EventInfo {
    private SimpleStringProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty createdAt;
    private SimpleStringProperty updatedAt;
    private SimpleStringProperty nrParticipants;

    public EventInfo(Long id, String name, Date createdAt, Date updatedAt, Integer nrParticipants) {
        this.id = new SimpleStringProperty(id.toString());
        this.name = new SimpleStringProperty(name);
        this.createdAt = new SimpleStringProperty(createdAt.toString());
        this.updatedAt = new SimpleStringProperty(updatedAt.toString());
        this.nrParticipants = new SimpleStringProperty(nrParticipants.toString());
    }
}
