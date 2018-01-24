/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mha.notif.immo.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mehdi
 */
@XmlRootElement
public class Contact {
    
    private String nom;
    private String rcsSiren;

    public String getNom() {
        return nom;
    }
    
    @XmlElement
    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getRcsSiren() {
        return rcsSiren;
    }
    
    @XmlElement
    public void setRcsSiren(String rcsSiren) {
        this.rcsSiren = rcsSiren;
    }
    
    
}
