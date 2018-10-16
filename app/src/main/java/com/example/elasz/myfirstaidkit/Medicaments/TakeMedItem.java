package com.example.elasz.myfirstaidkit.Medicaments;

import android.graphics.Bitmap;

/**
 * Created by elasz on 11.10.2018.
 */

public class TakeMedItem {

    int Id;
    String name;
    String power;
    String amoutform;
    String amount;
    Bitmap image;

    String editAmount;

    public TakeMedItem(int id, String name, String power, String amoutform, String amount, Bitmap image) {
        Id = id;
        this.name = name;
        this.power = power;
        this.amoutform = amoutform;
        this.amount = amount;
        this.image = image;
        //this.editAmount = editAmount;
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

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getAmoutform() {
        return amoutform;
    }

    public void setAmoutform(String amoutform) {
        this.amoutform = amoutform;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getEditAmount() {
        return editAmount;
    }

    public void setEditAmount(String editAmount) {
        this.editAmount = editAmount;
    }

}
