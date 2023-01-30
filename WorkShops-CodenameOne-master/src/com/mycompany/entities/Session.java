/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.entities;

import com.codename1.io.Preferences;

/**
 *
 * @author tchet
 */
public class Session {
    
      public  Preferences pref ; // a small memory to store data
    

    private  int id ; 
    private  String email ; 
    private  String firstName; 
    private  String passowrd ;
    private  String image;

    public  Preferences getPref() {
        return pref;
    }

    public Session(Preferences pref, int id, String email, String firstName, String passowrd, String image) {
        this.pref = pref;
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.passowrd = passowrd;
        this.image = image;
    }

    public Session() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassowrd() {
        return passowrd;
    }

    public void setPassowrd(String passowrd) {
        this.passowrd = passowrd;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Session{" + "pref=" + pref + ", id=" + id + ", email=" + email + ", firstName=" + firstName + ", passowrd=" + passowrd + ", image=" + image + '}';
    }

   
   

    
   
    
    
}
