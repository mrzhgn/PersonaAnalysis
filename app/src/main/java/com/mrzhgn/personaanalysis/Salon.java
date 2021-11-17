package com.mrzhgn.personaanalysis;

import java.util.ArrayList;
import java.util.List;

public class Salon {

    private int id;
    private String adres;
    private String title;
    private int chairsNum;
    private int workersNum;

    private static List<Salon> salons;

    private Salon() {
        if (salons == null) salons = new ArrayList<>();
    }

    public static Salon addInstance(int id) {
        Salon salon = new Salon();
        salon.setId(id);
        salons.add(salon);
        return salon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getChairsNum() {
        return chairsNum;
    }

    public void setChairsNum(int chairsNum) {
        this.chairsNum = chairsNum;
    }

    public int getWorkersNum() {
        return workersNum;
    }

    public void setWorkersNum(int workersNum) {
        this.workersNum = workersNum;
    }

    public static Salon getSalonById(int id) {

        for (Salon salon : salons) {
            if (id == salon.getId()) return salon;
        }

        return null;
    }
}
