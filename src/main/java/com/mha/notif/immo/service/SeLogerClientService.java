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

    private Date lastAnnonceDatetime;
    private List<String> lastAnnoncesIds;
    private Recherche searchResponse;

    public void start() {
        //logger.info("Starting the SeLoger ads retrieval service");
        mailService.notifyServiceStart();

        this.lastAnnonceDatetime = new Date();
        this.lastAnnoncesIds = new ArrayList<>();

        while (true) {

            //Pause
            try {
                Thread.sleep(60000);
            } catch (Exception e) {
                logger.error("thread sleep error", e);
            }
            
            logger.info(">>>>>>>>>>>> Call to SeLoger Web Service... <<<<<<<<<<<<");
            boolean moreResult = true;
            List<Annonce> allAnnouncements = new ArrayList<>();
            String request = SEARCH_URL;
            int responseNbr = 0;
            while (moreResult) {
                // SeLoger request
                ResponseEntity<Recherche> response = restTemplate
                        .exchange(request, HttpMethod.GET, null, Recherche.class);

                logger.info("Response Status: {}", response.getStatusCodeValue());
                if (!response.getStatusCode().is2xxSuccessful()) {
                    logger.error("Failed : HTTP error code : {}", response.getStatusCodeValue());
                    break;
                }
                
                logger.info("Response n° {}:", ++responseNbr);
                searchResponse = response.getBody();
                logger.info("{} announcements retrieved from total of {}", 
                        searchResponse.getAnnonces().size(), 
                        searchResponse.getNbTrouvees());

                allAnnouncements.addAll(searchResponse.getAnnonces());
                
                if (searchResponse.getPageSuivante() == null) 
                    moreResult = false;
                
                request = searchResponse.getPageSuivante();
            }
            
            if (allAnnouncements.isEmpty()) 
                continue;

            //filter announcements by date of last announcement
            List<Annonce> notification = filterAdsByDatetime(allAnnouncements, lastAnnonceDatetime);

            //set last annonce date
            lastAnnonceDatetime = searchResponse.getAnnonces().stream()
                    .map(a -> a.getDtCreation())
                    .max(Date::compareTo).get();
            logger.info("Date of last annonce: {}", lastAnnonceDatetime);
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

    private List<Annonce> filterAdsByDatetime(List<Annonce> ads, Date date) {
        return ads.stream()
                .filter(annonce -> annonce.getDtCreation().after(lastAnnonceDatetime)
                        || (annonce.getDtCreation().equals(lastAnnonceDatetime)
                        && !lastAnnoncesIds.contains(annonce.getIdAnnonce())))
                .collect(Collectors.toList());
    }

    @Override
    public void run(String... args) throws Exception {
        this.start();
    }

}
