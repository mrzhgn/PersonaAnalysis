package com.mrzhgn.personaanalysis;

public class SalonItem {

    private String title;
    private String adres;

    public SalonItem(Salon salon){

        this.title = salon.getTitle();
        this.adres = salon.getAdres();
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdres() {
        return this.adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

}
