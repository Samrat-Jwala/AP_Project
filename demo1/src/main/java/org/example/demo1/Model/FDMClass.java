package org.example.demo1.Model;

import javafx.beans.property.SimpleStringProperty;

public class FDMClass {
    private SimpleStringProperty festival;
    private SimpleStringProperty start_date;
    private SimpleStringProperty end_date;
    private SimpleStringProperty discount;

    public FDMClass(String festival, String start_date , String end_date, String discount) {
        this.festival = new SimpleStringProperty(festival);
        this.start_date = new SimpleStringProperty(start_date);
        this.end_date = new SimpleStringProperty(end_date);
        this.discount = new SimpleStringProperty(discount);
    }

    public String getFestival() {
        return festival.get();
    }

    public SimpleStringProperty festivalProperty() {
        return festival;
    }

    public void setFestival(String festival) {
        this.festival.set(festival);
    }

    public String getStart_date() {
        return start_date.get();
    }

    public SimpleStringProperty start_dateProperty() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date.set(start_date);
    }

    public String getEnd_date() {
        return end_date.get();
    }

    public SimpleStringProperty end_dateProperty() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date.set(end_date);
    }

    public String getDiscount() {
        return discount.get();
    }

    public SimpleStringProperty discountProperty() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount.set(String.valueOf(discount));
    }
}
