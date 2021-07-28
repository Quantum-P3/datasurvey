package org.datasurvey.service;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.datasurvey.domain.User;
import org.datasurvey.domain.UsuarioExtra;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import tech.jhipster.config.JHipsterProperties;

/**
 * Service for sending emails.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";

    private static final String CONTRASENNA = "contrasenna";

    private static final String BASE_URL = "baseUrl";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    public MailService(
        JHipsterProperties jHipsterProperties,
        JavaMailSender javaMailSender,
        MessageSource messageSource,
        SpringTemplateEngine templateEngine
    ) {
        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug(
            "Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart,
            isHtml,
            to,
            subject,
            content
        );

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        if (user.getEmail() == null) {
            log.debug("Email doesn't exist for user '{}'", user.getLogin());
            return;
        }
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendEmailFromTemplateEncuesta(User user, String templateName, String titleKey, String contrasenna) {
        if (user.getEmail() == null) {
            log.debug("Email doesn't exist for user '{}'", user.getLogin());
            return;
        }
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(CONTRASENNA, contrasenna);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
    }

    @Async
    public void sendCreationEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title");
    }

    @Async
    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");
    }

    @Async
    public void sendPasswordRestoredMail(User user) {
        log.debug("Sending password restored email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/passwordRestoredEmail", "email.restored.title");
    }

    @Async
    public void sendSuspendedAccountMail(UsuarioExtra user) {
        log.debug("Sending suspended account mail to '{}'", user.getUser().getEmail());
        sendEmailFromTemplate(user.getUser(), "mail/suspendedAccountEmail", "email.suspended.title");
    }

    @Async
    public void sendActivatedAccountMail(UsuarioExtra user) {
        log.debug("Sending reactivated  account mail to '{}'", user.getUser().getEmail());
        sendEmailFromTemplate(user.getUser(), "mail/reactivatedAccountEmail", "email.reactivation.title");
    }

    @Async
    public void sendPublishedPrivateMail(UsuarioExtra user, String contrasenna) {
        log.debug("Sending reactivated  account mail to '{}'", user.getUser().getEmail());
        sendEmailFromTemplateEncuesta(user.getUser(), "mail/encuestaPrivadaEmail", "email.private.title", contrasenna);
    }

    @Async
    public void sendPublishedPublicMail(UsuarioExtra user) {
        log.debug("Sending reactivated  account mail to '{}'", user.getUser().getEmail());
        sendEmailFromTemplate(user.getUser(), "mail/encuestaPublicaEmail", "email.public.title");
    }

    @Async
    public void sendEncuestaDeleted(UsuarioExtra user) {
        log.debug("Sending encuesta deletion notification mail to '{}'", user.getUser().getEmail());
        sendEmailFromTemplate(user.getUser(), "mail/encuestaDeletedEmail", "email.encuestaDeleted.title");
    }
}
