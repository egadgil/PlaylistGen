package com.example.sumon.androidvolley;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.util.Log;

/**
 * Public class that lays the foundation to send an email and sets the parameters in order to connect to Gmail's smtp port.
 *  Will also send the email once helper classes step in at the end
 * @author Zach
 */
public class GmailAuthenicationActivity {

    private final String emailPort = "587";// gmail's smtp port
    private final String smtpAuth = "true";
    private final String starttls = "true";
    private final String emailHost = "smtp.gmail.com";

    private String fromEmail;
    private String fromPassword;
    private List toEmailList;
    private String emailSubject;
    private String emailBody;

    private Properties emailProperties;
    private Session mailSession;
    private MimeMessage emailMessage;

    /**
     * Standard no-parameter constructor, shouldn't be used in this class
     */
    public GmailAuthenicationActivity() {

    }

    /**
     * Constructor for GmailAuthenicationActivity that should be the only one used. Determines all of the details of the email being sent
     * @param fromEmail The email address that the email is coming from, in our case it's always zmans1205@gmail.com
     * @param fromPassword The password for the email address that the email is coming from, in our case it's a application specific password
     *                     for my email. "xbsm xojh sbqo zsgp" is that value
     * @param toEmailList This param determines who the email goes to, in our application we won't send emails to multiple people so it
     *                    always goes to just one address at a time
     * @param emailSubject This param determines the emailSubject to be sent
     * @param emailBody This param determines the emailBody to be sent
     */
    public GmailAuthenicationActivity(String fromEmail, String fromPassword,
                 List toEmailList, String emailSubject, String emailBody) {
        this.fromEmail = fromEmail;
        this.fromPassword = fromPassword;
        this.toEmailList = toEmailList;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;

        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", smtpAuth);
        emailProperties.put("mail.smtp.starttls.enable", starttls);
        Log.i("GMail", "Mail server properties set.");
        Log.d("GMAIL INFO", fromEmail);
        Log.d("GMAIL INFO", fromPassword);
        Log.d("GMAIL INFO", emailSubject);
    }

    /**
     * Public method that creates the MimeMessage which is essentially the Email being constructed with all of the parameters
     * given in the constructor.
     * @return A MimeMessage with all of the email details that is ready to be sent.
     * @throws AddressException  This exception can be thrown if any of the Gmail addresses that are given are invalid
     * @throws MessagingException This exception can be thrown if the message attempting to be sent is invalid (null)
     * @throws UnsupportedEncodingException This exception can be thrown if the text/html that we are trying to use is unsupported
     * for any reason
     */
    public MimeMessage createEmailMessage() throws AddressException,
            MessagingException, UnsupportedEncodingException {

        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);

        emailMessage.setFrom(new InternetAddress(fromEmail, fromEmail));
        for (Object toEmail : toEmailList) {
            Log.i("GMail","toEmail: "+toEmail);
            emailMessage.addRecipient(Message.RecipientType.TO,
                    new InternetAddress((String) toEmail));
        }

        emailMessage.setSubject(emailSubject);
        emailMessage.setContent(emailBody, "text/html");// for a html email
        // emailMessage.setText(emailBody);// for a text email
        Log.i("GMail", "Email Message created.");
        return emailMessage;
    }

    /**
     * Public method that sends the email after it's parameters have been set, and it's been created by the previous two methods.
     * @throws AddressException This exception can get thrown if the Transport doesn't connect to Gmail's smtp port correctly
     * @throws MessagingException This exception can get thrown if the Transport isn't able to send the Message correctly
     */
    public void sendEmail() throws AddressException, MessagingException {

        Transport transport = mailSession.getTransport("smtp");
        transport.connect(emailHost, fromEmail, fromPassword);
        Log.i("GMail","allrecipients: "+emailMessage.getAllRecipients());
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        Log.i("GMail", "Email sent successfully.");
    }

}
