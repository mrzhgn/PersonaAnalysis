package com.mrzhgn.personaanalysis;

import java.util.ArrayList;
import java.util.List;

public class DataLists {

    private static List<DataItem> data;
    private static List<DataItem> data2;
    private static List<DataItem> data3;
    private static List<DataItem> data4;

    static {
        data = new ArrayList<>();
        data2 = new ArrayList<>();
        data3 = new ArrayList<>();
        data4 = new ArrayList<>();

        setInitialData();
    }

    public static void setInitialData() {
        data.add(new DataItem("Выручка салона", "1 098 640 руб"));
        data.add(new DataItem("Реализация потенциала салона", "39.81%"));
        data.add(new DataItem("Загрузка кресла", "74.6"));
        data.add(new DataItem("Доходность кресла", "219 728 руб"));
        data.add(new DataItem("Доверие клиента", "1.3"));
        data.add(new DataItem("Проходимость кресла", "57.2"));

        data2.add(new DataItem("Востребованность специалиста", "8.32 %"));
        data2.add(new DataItem("Татьяна Ломакина", "15.8 %"));
        data2.add(new DataItem("Дмитрий Николаев", "15.5 %"));
        data2.add(new DataItem("Глеб Гацюк", "14.7 %"));
        data2.add(new DataItem("Екатерина Калинина", "9.9 %"));
        data2.add(new DataItem("Кирилл Трухманов", "9.4 %"));
        data2.add(new DataItem("Виктория Жаринова", "8.3 %"));
        data2.add(new DataItem("Флоатинг", "5.6 %"));
        data2.add(new DataItem("Оксана Колюшкова", "5.4 %"));
        data2.add(new DataItem("Кирилл Тимофеев", "4.8 %"));
        data2.add(new DataItem("Кристина Иванникова", "4.8 %"));
        data2.add(new DataItem("Марина Козлова", "3.2 %"));
        data2.add(new DataItem("Ольга Бизина", "2.4 %"));

        data3.add(new DataItem("Парикмахерские Услуги ,%", "83.91%"));
        data3.add(new DataItem("Флоатинг ,%", "5.63%"));
        data3.add(new DataItem("Визаж ,%", "4.02%"));

        data4.add(new DataItem("% Постоянных клиентов", "?"));
        data4.add(new DataItem("% Клиентов салона", "?"));
        data4.add(new DataItem("% Новых клиентов", "?"));
        data4.add(new DataItem("Кол-во клиентов на 1 мастера", "?"));
    }

    public static List<DataItem> getData() {
        return data;
    }

    public static void setData(List<DataItem> data) {
        DataLists.data = data;
    }

    public static void addData(DataItem dataItem) {
        data.add(dataItem);
    }

    public static List<DataItem> getData2() {
        return data2;
    }

    public static void setData2(List<DataItem> data2) {
        DataLists.data2 = data2;
    }

    public static void addData2(DataItem dataItem) {
        data2.add(dataItem);
    }

    public static List<DataItem> getData3() {
        return data3;
    }

    public static void setData3(List<DataItem> data3) {
        DataLists.data3 = data3;
    }

    public static void addData3(DataItem dataItem) {
        data3.add(dataItem);
    }

    public static List<DataItem> getData4() {
        return data4;
    }

    public static void setData4(List<DataItem> data4) {
        DataLists.data4 = data4;
    }

    public static void addData4(DataItem dataItem) {
        data4.add(dataItem);
    }
}
