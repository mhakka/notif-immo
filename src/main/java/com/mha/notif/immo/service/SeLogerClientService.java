/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mha.notif.immo.service;

import com.mha.notif.immo.model.Annonce;
import com.mha.notif.immo.model.Recherche;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mehdi
 */
public class SeLogerClientService {

    private final Logger logger = LoggerFactory.getLogger(SeLogerClientService.class);
    private static final String BASE_URL = "http://ws.seloger.com";
    private static final String SEARCH_URL = BASE_URL + "/search.xml?ci=750109,750110,750111,750119,750120"
            + "&idtt=1&idtypebien=1,2&nb_pieces=2&pxmax=1060&surfacemin=30&tri=d_dt_crea";

    private final MailService mailService = new MailService();

    private Date lastAnnonce;

    public void start() {
        mailService.notifyServiceStart();

        this.lastAnnonce = new Date();
        Client client = Client.create();
        WebResource webResource = client.resource(SEARCH_URL);
        ClientResponse response;
        Recherche output;

        while (true) {
            
            //Pause
            try {
                Thread.sleep(60000);
            } catch (Exception e) {
                logger.error("thread sleep error", e);
            }
            
            // SeLoger request
            try {
                response = webResource.accept("application/xml")
                    .get(ClientResponse.class);
            } catch (Exception ex) {
                logger.error("Failed to execute request to {}", SEARCH_URL, ex);
                continue;
            }
                    
            logger.info("Status: {}", response.getStatus());

            if (response.getStatus() != 200) {
                logger.error("Failed : HTTP error code : {}", response.getStatus());
                continue;
            }

            output = response.getEntity(Recherche.class);
            logger.info("Number of announcements found: {}", output.getNbTrouvees());
            logger.info("Number of announcements treated: {}", output.getAnnonces().size());

            List<Annonce> notification = output.getAnnonces().stream()
                    .filter(annonce -> annonce.getDtCreation().after(lastAnnonce))
                    .collect(Collectors.toList());
            //set last annonce date
            lastAnnonce = output.getAnnonces().stream().map(a -> a.getDtCreation()).max(Date::compareTo).get();
            logger.info("Date of last annonce: {}", lastAnnonce);

            if (!notification.isEmpty()) {
                //Send new Annonces by email            
                logger.info(">>>>>>>>>>>>>>>>>> {} notification(s) to send", notification.size());
                mailService.notifyNewAnnouncement(notification);
            }
          
        }

    }

}
