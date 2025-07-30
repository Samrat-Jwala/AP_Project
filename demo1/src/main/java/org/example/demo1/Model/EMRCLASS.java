package org.example.demo1.Model;

import javafx.beans.property.SimpleStringProperty;

public class EMRCLASS {
    private SimpleStringProperty name;
    private SimpleStringProperty location;
    private SimpleStringProperty contact;
    private SimpleStringProperty details;

    public EMRCLASS(String name, String location, String contact, String details) {
        this.name = new SimpleStringProperty(name);
        this.location = new SimpleStringProperty(location);
        this.contact = new SimpleStringProperty(contact);
        this.details = new SimpleStringProperty(details);
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

    public String getLocation() {
        return location.get();
    }

    public SimpleStringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public String getContact() {
        return contact.get();
    }

    public SimpleStringProperty contactProperty() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact.set(contact);
    }

    public String getDetails() {
        return details.get();
    }

    public SimpleStringProperty detailsProperty() {
        return details;
    }

    public void setDetails(String details) {
        this.details.set(details);
    }
}