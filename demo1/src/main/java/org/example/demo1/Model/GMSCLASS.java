package org.example.demo1.Model;

import javafx.beans.property.SimpleStringProperty;

public class GMSCLASS {
    private SimpleStringProperty name;
    private  SimpleStringProperty age;
    private  SimpleStringProperty email;
    private  SimpleStringProperty contact;

    public GMSCLASS(String name, String age, String email, String contact) {
        this.name = new SimpleStringProperty(name);
        this.age = new SimpleStringProperty(age);
        this.email = new SimpleStringProperty(email);
        this.contact = new SimpleStringProperty(contact);
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

    public String getAge() {
        return age.get();
    }

    public SimpleStringProperty ageProperty() {
        return age;
    }

    public void setAge(String age) {
        this.age.set(age);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
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
}
