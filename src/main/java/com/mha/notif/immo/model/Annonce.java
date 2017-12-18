/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mha.notif.immo.model;

import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mehdi
 */
@XmlRootElement
public class Annonce {
    
    private String idAnnonce;
    private String permaLien;
    private Date dtFraicheur;
    private Date dtCreation;

    public String getIdAnnonce() {
        return idAnnonce;
    }
    
    @XmlElement   
    public void setIdAnnonce(String idAnnonce) {
        this.idAnnonce = idAnnonce;
    }

    public String getPermaLien() {
        return permaLien;
    }
    
    @XmlElement
    public void setPermaLien(String permaLien) {
        this.permaLien = permaLien;
    }

    public Date getDtFraicheur() {
        return dtFraicheur;
    }
    
    @XmlElement
    public void setDtFraicheur(Date dtFraicheur) {
        this.dtFraicheur = dtFraicheur;
    }

    public Date getDtCreation() {
        return dtCreation;
    }
    
    @XmlElement
    public void setDtCreation(Date dtCreation) {
        this.dtCreation = dtCreation;
    }
    
    
    
}
