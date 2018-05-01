package com.troy.keeper.core.base.service;

import com.troy.keeper.core.base.entity.BaseAuditingEntity;
import io.github.jhipster.config.JHipsterProperties;
import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.util.Locale;

/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String CONTEXT = "context";
    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    public MailService(JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender,
            MessageSource messageSource, SpringTemplateEngine templateEngine) {

        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("Email could not be sent to user '{}'", to, e);
            } else {
                log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
            }
        }
    }

//    @Async
//    public void sendEmailFromTemplate(BaseAuditingEntity user, String templateName, String titleKey) {
//        sendEmailFromTemplate(user, user.getEmail(),templateName,titleKey);
//    }

    @Async
    public void sendEmailFromTemplate(BaseAuditingEntity user, String to, String templateName, String titleKey) {
        sendEmailFromTemplate((Object)user, to, templateName, titleKey);
    }

    @Async
    public void sendEmailFromTemplate(Object obj, String to, String templateName, String subject) {
        Locale locale = Locale.getDefault();
        Context context = new Context(locale);
        if (obj instanceof BaseAuditingEntity) {
            context.setVariable(USER, obj);
        } else {
            context.setVariable(CONTEXT, obj);
        }
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        sendEmail(to, subject, content, false, true);

    }

//    @Async
//    public void sendActivationEmail(BaseAuditingEntity user) {
//        log.debug("Sending activation email to '{}'", user.getEmail());
//        sendEmailFromTemplate(user, "activationEmail", "email activation title");
//    }
//
//    @Async
//    public void sendCreationEmail(BaseAuditingEntity user) {
//        log.debug("Sending creation email to '{}'", user.getEmail());
//        sendEmailFromTemplate(user, "creationEmail", "email activation title");
//    }
//
//    @Async
//    public void sendPasswordResetMail(BaseAuditingEntity user) {
//        log.debug("Sending password reset email to '{}'", user.getEmail());
//        sendEmailFromTemplate(user, "passwordResetEmail", "email reset title");
//    }
}
