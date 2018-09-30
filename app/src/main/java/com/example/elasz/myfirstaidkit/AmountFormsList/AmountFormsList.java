package com.example.elasz.myfirstaidkit.AmountFormsList;

/**
 * Created by elasz on 30.09.2018.
 */

public class AmountFormsList {


    int Id;
    String name;

    public AmountFormsList(int id, String name) {
        Id = id;
        this.name = name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
