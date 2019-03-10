
package com.mha.notif.immo.service;

import com.mha.notif.immo.model.Annonce;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MailService {

    private final Logger logger = LoggerFactory.getLogger(SeLogerClientService.class);

    private static final String SERVICE_STARTED_SUBJECT = "Démarrage du service de récupèration des annonces SeLoger";
    private static final String SERVICE_STOPED_SUBJECT = "Arrêt du service de récupèration des annonces SeLoger";
    private static final String NEW_ANNOUNCEMENT_SUBJECT = " Nouvelle(s) annonce(s) SeLoger";

    public void notifyServiceStart() {
        send(SERVICE_STARTED_SUBJECT, "Service démarré !");
    }

    public void notifyServiceStop() {
        send(SERVICE_STOPED_SUBJECT, "");
    }

    public void notifyNewAnnouncement(List<Annonce> notifications) {
        String body = "";
        for (Annonce annonce : notifications) {
            body += "<a href= '" + annonce.getPermaLien() + "'>" + annonce.getPermaLien() + "</a><br>";
            body += "Agence: " + annonce.getContact().getNom() + "<br>";
            body += "Titre: " + annonce.getTitre() + "<br>";
            body += "Descriptif: <p>" + annonce.getDescriptif() + "</p><br>";
            body += "Surface: " + annonce.getSurface() + " " + annonce.getSurfaceUnite() + "<br>";
            body += "Ville: " + annonce.getVille() + "<br>";
            body += "Code Postal: " + annonce.getCp() + "<br>";
            body += "Prix: " + annonce.getPrix() + " " + annonce.getPrixUnite() + "<br>";
            if (!StringUtils.isEmpty(annonce.getLatitude()) && !StringUtils.isEmpty(annonce.getLongitude())) {
                body += "Position: <a href= 'https://www.google.com/maps/?q="+ annonce.getLatitude()+","+ annonce.getLongitude()+"'>Lien Google Map</a><br>";
            }
            body += "<br><hr>";
        }
        send(notifications.size() + NEW_ANNOUNCEMENT_SUBJECT, body);
    }

    private void send(String subject, String body) {
        final Email from = new Email("app83424546@heroku.com", "Notif immo");
        final Email to = new Email("m.hakka@outlook.com");
        Content content = new Content("text/html", body);
        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            logger.info("Mail status response: {}", response.getStatusCode());
            logger.info("Mail response body: {}", response.getBody());
            logger.info("Mail response headers", response.getHeaders());
        } catch (IOException ex) {
            logger.error("Error sending mail with SendGrid", ex);
        }
    }
}
