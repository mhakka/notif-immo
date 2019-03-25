/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mha.notif.immo.model;

import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
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
    private String titre;
    private String libelle;
    private String descriptif;
    private String prix;
    private String prixUnite;
    private String surface;
    private String surfaceUnite;
    private String cp;
    private String codeInsee;
    private String ville;
    private String latitude;
    private String longitude;
    private Contact contact;
    private List<Photo> photos;

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

    public String getTitre() {
        return titre;
    }
    
    @XmlElement
    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getLibelle() {
        return libelle;
    }
    
    @XmlElement
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescriptif() {
        return descriptif;
    }
    
    @XmlElement
    public void setDescriptif(String descriptif) {
        this.descriptif = descriptif;
    }

    public String getPrix() {
        return prix;
    }

    @XmlElement
    public void setPrix(String prix) {
        this.prix = prix;
    }

    public String getPrixUnite() {
        return prixUnite;
    }
    
    @XmlElement
    public void setPrixUnite(String prixUnite) {
        this.prixUnite = prixUnite;
    }

    public String getSurface() {
        return surface;
    }
    
    @XmlElement
    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getSurfaceUnite() {
        return surfaceUnite;
    }
    
    @XmlElement
    public void setSurfaceUnite(String surfaceUnite) {
        this.surfaceUnite = surfaceUnite;
    }

    public String getCp() {
        return cp;
    }
    
    @XmlElement
    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getCodeInsee() {
        return codeInsee;
    }
    
    @XmlElement
    public void setCodeInsee(String codeInsee) {
        this.codeInsee = codeInsee;
    }

    public String getVille() {
        return ville;
    }
    
    @XmlElement
    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getLatitude() {
        return latitude;
    }
    
    @XmlElement
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }
    
    @XmlElement
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Contact getContact() {
        return contact;
    }
    
    @XmlElement
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public List<Photo> getPhotos() {
        return photos;
    }
    
    @XmlElementWrapper(name="photos")
    @XmlElement(name="photo")
    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
    
}
