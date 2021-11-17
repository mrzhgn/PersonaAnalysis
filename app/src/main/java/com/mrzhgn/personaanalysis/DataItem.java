package com.mrzhgn.personaanalysis;

public class DataItem {

    private String title;
    private String value;

    public DataItem(String title, String value){

        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
