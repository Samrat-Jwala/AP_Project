package org.example.demo1.Model;

import javafx.beans.property.SimpleStringProperty;

public class TMSCLASS {
    private SimpleStringProperty name;
    private  SimpleStringProperty age;
    private  SimpleStringProperty add;
    private  SimpleStringProperty contact;

    public TMSCLASS(String name, String age, String add, String contact) {
        this.name = new SimpleStringProperty(name);
        this.age = new SimpleStringProperty(age);
        this.add = new SimpleStringProperty(add);
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

    public String getAdd() {
        return add.get();
    }

    public SimpleStringProperty addProperty() {
        return add;
    }

    public void setAdd(String add) {
        this.add.set(add);
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
