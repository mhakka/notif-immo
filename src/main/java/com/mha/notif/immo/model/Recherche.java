/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mha.notif.immo.model;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mehdi
 */
@XmlRootElement
public class Recherche {
    
    private int nbTrouvees;
    
    private List<Annonce> annonces;

    public int getNbTrouvees() {
        return nbTrouvees;
    }
    
    @XmlElement
    public void setNbTrouvees(int nbTrouvees) {
        this.nbTrouvees = nbTrouvees;
    }

    public List<Annonce> getAnnonces() {
        return annonces;
    }
    
    @XmlElementWrapper(name="annonces")
    @XmlElement(name="annonce")
    public void setAnnonces(List<Annonce> annonces) {
        this.annonces = annonces;
    }
    
    
}
