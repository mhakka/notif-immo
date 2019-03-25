package com.mha.notif.immo.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class Photo {
    
    private String titre;
    private String ordre;
    private String stdUrl;

    public String getTitre() {
        return titre;
    }
    
    @XmlElement
    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getOrdre() {
        return ordre;
    }
    
    @XmlElement
    public void setOrdre(String ordre) {
        this.ordre = ordre;
    }

    public String getStdUrl() {
        return stdUrl;
    }

    @XmlElement
    public void setStdUrl(String stdUrl) {
        this.stdUrl = stdUrl;
    }
    
}
