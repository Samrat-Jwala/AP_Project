package org.example.demo1.Model;

import javafx.beans.property.SimpleStringProperty;

public class AMPCLASS {
    private  SimpleStringProperty name;
    private  SimpleStringProperty location;
    private  SimpleStringProperty description;
    private  SimpleStringProperty preference;

    public AMPCLASS(String name, String location, String description, String preference) {
        this.name = new SimpleStringProperty(name);
        this.location = new SimpleStringProperty(location);
        this.description = new SimpleStringProperty(description);
        this.preference = new SimpleStringProperty(preference);
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

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getPreference() {
        return preference.get();
    }

    public SimpleStringProperty preferenceProperty() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference.set(preference);
    }
}
