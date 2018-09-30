package com.example.elasz.myfirstaidkit.PurposeFormsList;

/**
 * Created by elasz on 30.09.2018.
 */

public class PurposeList {

    int Id;
    String name;

    public PurposeList(int id, String name) {
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
