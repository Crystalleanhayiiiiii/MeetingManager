package com.notificationservice.notificationservice.services;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;

//config để gửi mail
@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMeetingNotification(String toEmail, String subject, String body, String attachmentPath)
            throws MessagingException {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom("danghang2411@gmail.com"); // Replace with your sending email
            messageHelper.setTo(toEmail);
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true); // HTML content body// true indicates HTML content
            // messageHelper.setContentType("text/html; charset=UTF-8");

            if (attachmentPath != null) {
                messageHelper.addAttachment("meeting-agenda.pdf", new java.io.File(attachmentPath));
            }
        };

        javaMailSender.send(messagePreparator);
    }

}