package com.example.elasz.myfirstaidkit.FormsList;

/**
 * Created by elasz on 30.09.2018.
 */

public class FormsList {

    int Id;
    String name;

    public FormsList(int id, String name) {
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
