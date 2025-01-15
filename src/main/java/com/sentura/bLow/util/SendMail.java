package com.sentura.bLow.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Component
public class SendMail {

    @Value("${mail.from}")
    private String mailFrom;

    @Value("${mail.user}")
    private String mailUser;

    @Value("${mail.password}")
    private String mailPassword;

    @Value("${mail.port}")
    private String mailPort;

    @Value("${mail.host}")
    private String mailHost;

    private Logger logger = LoggerFactory.getLogger(SendMail.class);

    public void sendEmail(String to, String cc, String subject, String body, boolean isHtmlBody, Map<String,String> attachmentData) {

        Session session = getSessionInstance();

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailFrom));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            if (cc != null) {
                InternetAddress[] addresses = null;
                if (cc.contains(",")) {
                    String ccArray[] = cc.split(",");
                    addresses = new InternetAddress[ccArray.length];
                    for(int i=0;i<ccArray.length;i++){
                        addresses[i] = new InternetAddress(ccArray[i]);
                    }
                } else if(!cc.equals("")) {
                    addresses = new InternetAddress[1];
                    addresses[0] = new InternetAddress(cc);
                }
                message.addRecipients(Message.RecipientType.CC, addresses);
            }

            Multipart getMessage = configureMessageBody(body,isHtmlBody,attachmentData);
            if (getMessage != null) {
                message.setSubject(subject);
                message.setContent(getMessage);
            } else {
                throw new Exception("Unexpected Error Occurred When Sending Mail!");
            }

            Transport.send(message);
            System.out.println("Sent Email Successfully....");
            logger.info("Sent Email Successfully...");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    //set the message body text and add the attachments to message
    private Multipart configureMessageBody(String body,boolean isHtmlBody,Map<String,String> attachmentData) {

        try {
            BodyPart messageInMailBody = new MimeBodyPart();
            List<BodyPart> attachmentsInMailBody = null;

            if (isHtmlBody) {
                messageInMailBody.setContent(body, "text/html");
            } else {
                messageInMailBody.setText(body);
            }
            if (attachmentData != null && !attachmentData.isEmpty()) {
                attachmentsInMailBody = new ArrayList<>();

                int index = 0;
                for(String path: attachmentData.values()) {
                    BodyPart attachment = new MimeBodyPart();
                    DataSource source = new FileDataSource(path);
                    attachment.setDataHandler(new DataHandler(source));
                    attachment.setFileName(attachmentData.keySet().toArray()[index].toString());
                    attachmentsInMailBody.add(attachment);
                    index++;
                }
            }

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageInMailBody);
            if (attachmentsInMailBody != null && !attachmentsInMailBody.isEmpty()) {
                for(BodyPart bodyPart: attachmentsInMailBody) {
                    multipart.addBodyPart(bodyPart);
                }
            }

            return multipart;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //use to get the session instance from properties data
    private Session getSessionInstance() {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", mailHost);
        properties.setProperty("mail.smtp.port", mailPort);
        properties.setProperty("mail.smtp.user", mailUser);
        properties.setProperty("mail.smtp.password", mailPassword);
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailUser,
                        mailPassword);
            }
        });
    }
}
