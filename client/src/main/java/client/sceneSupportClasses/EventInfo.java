package client.sceneSupportClasses;

import javafx.beans.property.SimpleStringProperty;

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

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getCreatedAt() {
        return createdAt.get();
    }

    public SimpleStringProperty createdAtProperty() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt.set(createdAt);
    }

    public String getUpdatedAt() {
        return updatedAt.get();
    }

    public SimpleStringProperty updatedAtProperty() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt.set(updatedAt);
    }

    public String getNrParticipants() {
        return nrParticipants.get();
    }

    public SimpleStringProperty nrParticipantsProperty() {
        return nrParticipants;
    }

    public void setNrParticipants(String nrParticipants) {
        this.nrParticipants.set(nrParticipants);
    }
}
