/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mehdi
 */
public class MailService {

    private final Logger logger = LoggerFactory.getLogger(SeLogerClientService.class);

    private static final String SERVICE_STARTED_SUBJECT = "Démarrage du service de récupèration des annonces SeLoger";
    private static final String SERVICE_STOPED_SUBJECT = "Arrêt du service de récupèration des annonces SeLoger";
    private static final String NEW_ANNOUNCEMENT_SUBJECT = "Nouvelle(s) annonce(s) SeLoger";

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
            if (!StringUtils.isBlank(annonce.getLatitude()) && !StringUtils.isBlank(annonce.getLongitude())) {
                body += "Position: <a href= 'https://www.google.com/maps/?q="+ annonce.getLatitude()+","+ annonce.getLongitude()+"'>Lien Google Map</a><br>";
            }
            body += "<br><hr>";
        }
        send(NEW_ANNOUNCEMENT_SUBJECT, body);
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

    private void sendJavaMail(String subject, String body) {

        final String fromEmail = "m.hakka@outlook.com";
        final String password = "";
        final String toEmail = "m.hakka@outlook.com";

        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp-mail.outlook.com");
        props.put("mail.smtp.auth", "true");

        //TLS
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");

        //SSL
        /*props.put("mail.smtp.socketFactory.port", "465"); //SSL Port
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
        props.put("mail.smtp.port", "465"); //SMTP Port*/
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };

        Session session = Session.getInstance(props, auth);

        try {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");
            msg.setFrom(new InternetAddress(fromEmail, "NoReply-mha"));
            msg.setSubject(subject, "UTF-8");
            msg.setText(body, "UTF-8", "html");
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            Transport.send(msg);

        } catch (MessagingException | UnsupportedEncodingException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
