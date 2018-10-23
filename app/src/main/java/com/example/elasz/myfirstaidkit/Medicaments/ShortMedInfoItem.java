package com.example.elasz.myfirstaidkit.Medicaments;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.sql.Blob;

/**
 * Created by elasz on 19.09.2018.
 */

public class ShortMedInfoItem implements Serializable {

    int Id;
    String name;
    String expdate;
    String form;
    String purpose;
    String amount;
    String amoutform;
    String power;
    Bitmap image;
    String code;

    public ShortMedInfoItem(int id, String name, String expdate, String form, String purpose, String amount, String amoutform, String power, Bitmap image, String code) {
        Id = id;
        this.name = name;
        this.expdate = expdate;
        this.form = form;
        this.purpose = purpose;
        this.amount = amount;
        this.amoutform = amoutform;
        this.power = power;
        this.image = image;
        this.code = code;
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

    public String getExpdate() {
        return expdate;
    }

    public void setExpdate(String expdate) {
        this.expdate = expdate;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmoutform() {
        return amoutform;
    }

    public void setAmoutform(String amoutform) {
        this.amoutform = amoutform;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }


    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
