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

    public void start() {
        logger.info("Starting the SeLoger ads retrieval service");
        //mailService.notifyServiceStart();
        
        logger.info("#### First call to SeLoger Web Service... ####");
        List<String> lastAnnouncementsIds = fetchAnnouncements(SEARCH_URL).stream()
                .map(a -> a.getIdAnnonce())
                .collect(Collectors.toList());

        while (true) {
            //Pause
            try {
                Thread.sleep(60000);
            } catch (Exception e) {
                logger.error("thread sleep error", e);
            }
            
            logger.info("#### Call to SeLoger Web Service... ####");
            List<Annonce> announcements = fetchAnnouncements(SEARCH_URL);
            logger.info("Total retrieved announcements: {}", announcements.size());
            if (announcements.isEmpty()) {
                continue;
            }
            List<Annonce> notification = filterAnnouncementsExcludingByIds(announcements, lastAnnouncementsIds);
            //set last Ids
            lastAnnouncementsIds = announcements.stream()
                    .map(a -> a.getIdAnnonce())
                    .collect(Collectors.toList());

            if (!notification.isEmpty()) {
                //Send new Annonces by email            
                logger.info(">>>>>>>>>>>>>>>>>> {} notification(s) to send", notification.size());
                mailService.notifyNewAnnouncement(notification);
            }
        }
    }

    private List<Annonce> fetchAnnouncements(String request) {
        Recherche searchResponse;
        List<Annonce> announcements = new ArrayList<>();
        int responseNbr = 0;
        while (true) {
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

            announcements.addAll(searchResponse.getAnnonces());

            if (searchResponse.getPageSuivante() == null) {
                break;
            }
            request = searchResponse.getPageSuivante();
        }
        return announcements;
    }

    private List<Annonce> filterAnnouncementsByDatetime(List<Annonce> Announcements, Date datetime, List<String> lastAnnoncesIds) {
        return Announcements.stream()
                .filter(annonce -> annonce.getDtCreation().after(datetime)
                        || (annonce.getDtCreation().equals(datetime)
                        && !lastAnnoncesIds.contains(annonce.getIdAnnonce())))
                .collect(Collectors.toList());
    }

    private List<Annonce> filterAnnouncementsExcludingByIds(List<Annonce> Announcements, List<String> idsToExclude) {
        return Announcements.stream()
                .filter(a -> !idsToExclude.contains(a.getIdAnnonce()))
                .collect(Collectors.toList());
    }

    @Override
    public void run(String... args) throws Exception {
        this.start();
    }

}
