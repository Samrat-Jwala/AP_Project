package org.example.demo1.Model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class BMSCLASS {
    private SimpleStringProperty Tourist;
    private  SimpleStringProperty Guide;
    private  SimpleStringProperty Date;
    private SimpleStringProperty Discount;

    public BMSCLASS(String Tourist, String Guide, String Date, String Discount) {
        this.Tourist = new SimpleStringProperty(Tourist);
        this.Guide = new SimpleStringProperty(Guide);
        this.Date = new SimpleStringProperty(Date);
        this.Discount = new SimpleStringProperty(Discount);
    }

    public String getTourist() {
        return Tourist.get();
    }

    public SimpleStringProperty touristProperty() {
        return Tourist;
    }

    public void setTourist(String tourist) {
        this.Tourist.set(tourist);
    }

    public String getGuide() {
        return Guide.get();
    }

    public SimpleStringProperty guideProperty() {
        return Guide;
    }

    public void setGuide(String guide) {
        this.Guide.set(guide);
    }

    public String getDate() {
        return Date.get();
    }

    public SimpleStringProperty dateProperty() {
        return Date;
    }

    public void setDate(String date) {
        this.Date.set(date);
    }

    public String getDiscount() {
        return Discount.get();
    }

    public SimpleStringProperty discountProperty() {
        return Discount;
    }

    public void setDiscount(String discount) {
        this.Discount.set(String.valueOf(Double.parseDouble(discount)));
    }
}
