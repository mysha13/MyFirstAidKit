package com.example.elasz.myfirstaidkit.Medicaments;

import java.io.Serializable;

/**
 * Created by elasz on 17.09.2018.
 */

public class UserMed implements Serializable {

    int Id;
    String name;
    int id_medicament;
    String expdate;
    String opendate;
    String form;
    String purpose;
    String amount;
    String amoutform;
    int person;
    String note;

    public UserMed (int id, String name, int id_medicament, String expdate, String opendate, String form,
                    String purpose, String amount, String amoutform, int person, String note){
        Id=id;
        this.name=name;
        this.id_medicament=id_medicament;
        this.expdate=expdate;
        this.opendate=opendate;
        this.form=form;
        this.purpose=purpose;
        this.amount=amount;
        this.person=person;
        this.note=note;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getId_medicament() {
        return id_medicament;
    }

    public void setId_medicament(int id_medicament) {
        this.id_medicament = id_medicament;
    }

    public String getExpdate() {
        return expdate;
    }

    public void setExpdate(String expdate) {
        this.expdate = expdate;
    }

    public String getOpendate() {
        return opendate;
    }

    public void setOpendate(String opendate) {
        this.opendate = opendate;
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

    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


}
