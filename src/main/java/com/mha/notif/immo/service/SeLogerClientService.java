package com.mha.notif.immo.service;

import com.mha.notif.immo.model.Annonce;
import com.mha.notif.immo.model.Recherche;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SeLogerClientService implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(SeLogerClientService.class);
    private static final String BASE_URL = "http://ws.seloger.com";
    private static final String SEARCH_URL = BASE_URL + "/search.xml?ci=750110,750111,750103"
            + "&idtt=1&idtypebien=1,2&nb_pieces=2&pxmax=1200&surfacemin=30&tri=d_dt_crea";
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MailService mailService;

    private Date lastAnnonce;
    private List<String> lastAnnoncesIds;
    private Recherche searchResponse;

    public void start() {
        logger.info("Starting the SeLoger ads retrieval service");
        mailService.notifyServiceStart();
        
        this.lastAnnonce = new Date();
        this.lastAnnoncesIds = new ArrayList<>();

        while (true) {

            //Pause
            try {
                Thread.sleep(60000);
            } catch (Exception e) {
                logger.error("thread sleep error", e);
            }

            // SeLoger request
            ResponseEntity<Recherche> response = restTemplate
                    .exchange(SEARCH_URL, HttpMethod.GET, null, Recherche.class);

            logger.info("Status: {}", response.getStatusCodeValue());

            if (!response.getStatusCode().is2xxSuccessful()) {
                logger.error("Failed : HTTP error code : {}", response.getStatusCodeValue());
                continue;
            }

            searchResponse = response.getBody();
            logger.info("Number of announcements found: {}", searchResponse.getNbTrouvees());
            logger.info("Number of announcements treated: {}", searchResponse.getAnnonces().size());

            List<Annonce> notification = searchResponse.getAnnonces().stream()
                    .filter(annonce -> annonce.getDtCreation().after(lastAnnonce)
                            || (annonce.getDtCreation().equals(lastAnnonce)
                            && !lastAnnoncesIds.contains(annonce.getIdAnnonce())))
                    .collect(Collectors.toList());
            //set last annonce date
            lastAnnonce = searchResponse.getAnnonces().stream()
                    .map(a -> a.getDtCreation())
                    .max(Date::compareTo).get();
            logger.info("Date of last annonce: {}", lastAnnonce);
            //set last Ids
            this.lastAnnoncesIds = searchResponse.getAnnonces().stream()
                    .map(a -> a.getIdAnnonce())
                    .collect(Collectors.toList());

            if (!notification.isEmpty()) {
                //Send new Annonces by email            
                logger.info(">>>>>>>>>>>>>>>>>> {} notification(s) to send", notification.size());
                mailService.notifyNewAnnouncement(notification);
            }

        }

    }

    @Override
    public void run(String... args) throws Exception {
        this.start();
    }

}
