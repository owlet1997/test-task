package com.haulmont.testtask.data.entities;

import java.io.Serializable;


public class Client implements Serializable {

    private Long id;

    private String name;

    private String surname;

    private String fatherName;

    private String phone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        if (fatherName == null) {
            this.fatherName = "";
        }
        else this.fatherName = fatherName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
