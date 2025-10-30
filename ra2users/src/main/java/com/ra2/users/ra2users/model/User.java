package com.ra2.users.ra2users.model;

import java.sql.Timestamp;

public class User {
    private Long id;
    private String nom;
    private String desc;
    private String email;
    private String passwd;
    private Timestamp ultimAcces;
    private Timestamp dataCreated;
    private Timestamp dataUpdated; 

    public User(){

    }
    public User(Long id, String nom, String desc, String email, String passwd, Timestamp ultimAcces,
            Timestamp dataCreated, Timestamp dataUpdated) {
        this.id = id;
        this.nom = nom;
        this.desc = desc;
        this.email = email;
        this.passwd = passwd;
        this.ultimAcces = ultimAcces;
        this.dataCreated = dataCreated;
        this.dataUpdated = dataUpdated;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
    public void setUltimAcces(Timestamp ultimAcces) {
        this.ultimAcces = ultimAcces;
    }
    public void setDataCreated(Timestamp dataCreated) {
        this.dataCreated = dataCreated;
    }
    public void setDataUpdated(Timestamp dataUpdated) {
        this.dataUpdated = dataUpdated;
    }
    public Long getId() {
        return id;
    }
    public String getNom() {
        return nom;
    }
    public String getDesc() {
        return desc;
    }
    public String getEmail() {
        return email;
    }
    public String getPasswd() {
        return passwd;
    }
    public Timestamp getUltimAcces() {
        return ultimAcces;
    }
    public Timestamp getDataCreated() {
        return dataCreated;
    }
    public Timestamp getDataUpdated() {
        return dataUpdated;
    }
    @Override
    public String toString() {
        return "User [id=" + id + ", nom=" + nom + ", desc=" + desc + ", email=" + email + ", passwd=" + passwd + "]";
    }
}
