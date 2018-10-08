package com.example.elasz.myfirstaidkit.PersonList;

/**
 * Created by elasz on 04.10.2018.
 */

public class PersonList {

    int Id;
    String name;

    public PersonList(int id, String name) {
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
