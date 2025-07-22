package com.example.soba2clean.mail;

import com.example.soba2clean.enums.EmailTemplateName;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class EmailService {
    Logger logger = Logger.getLogger(EmailService.class.getName());
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine emailTemplateEngine;

    EmailService(JavaMailSender javaMailSender, SpringTemplateEngine emailTemplateEngine) {
        this.javaMailSender = javaMailSender;
        this.emailTemplateEngine = emailTemplateEngine;
    }

    public void sendHtmlEmail(String to, String subject, EmailTemplateName emailTemplateNameEnum, Map<String, Object> variables) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        String htmlBody = processThymeleafTemplate(emailTemplateNameEnum, variables);
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        try {
            javaMailSender.send(message);
            logger.info("HTML email sent successfully to: " + to);
        } catch (MailException e) {
            logger.severe("Error sending HTML email to " + to + ": " + e.getMessage());
            throw new MessagingException("Failed to send HTML email", e);
        }
    }

    /**
     * Overload: Sends an HTML email using a Thymeleaf template without any dynamic variables.
     * This method is useful for static email templates that don't require personalization.
     * @param to The recipient's email address.
     * @param subject The email subject.
     * @param templateNameEnum The enum representing the email template name.
     * @throws MessagingException If there's an error during template processing or email sending.
     */
    public void sendHtmlEmail(String to, String subject, EmailTemplateName templateNameEnum) throws MessagingException {
        this.sendHtmlEmail(to, subject, templateNameEnum, Collections.emptyMap());
    }

    /**
     * Processes a Thymeleaf template with the given variables and returns the rendered HTML string.
     * This method is now made private as it's an internal helper for sending HTML emails.
     * @param templateNameEnum Enum The name of the Thymeleaf template (e.g., "welcome_email").
     * Assumes its templates are in src/main/resources/templates/emails/ or configured prefix.
     * @param variables A map of variables to be passed to the template.
     * @return The rendered HTML string.
     */
    private String processThymeleafTemplate(EmailTemplateName templateNameEnum, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return emailTemplateEngine.process(templateNameEnum.getTemplateFileName(), context);
    }
}
