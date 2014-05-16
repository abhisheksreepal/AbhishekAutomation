package com.home.utilities;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.home.application.pages.BaseWebPage;

public class ReportAndMail extends LoggerUtility
{

    public ReportAndMail(Logger log)
    {
        super(log);

    }

    public ReportAndMail()
    {
        super();

    }

    static String hostName = "";

    static
    {

        InetAddress addr;

        try
        {
            addr = InetAddress.getLocalHost();
            hostName = addr.getHostName();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();

        }
    }

    // TODO try to send mail with atachment -> index.html
    // If you are successful -> use this method in AfterSuite and DO NOT use
    // huson emaiol facility
    // BUT if you are able to have hudson email plugin do the job of attaching
    // email -> remove this method
    public void sendMail(long timeTaken, final String username,
            final String password, String reportmailServer,
            String reportRecipients, String reportAddressFrom,
            String defaultEnv, String application)
    {

        boolean debug = false;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", reportmailServer);
        props.put("mail.smtp.port", "587");

        String[] recipients = new BaseWebPage().getStringTokenized(
                reportRecipients, ";");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator()
                {
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(username, password);
                    }
                });
        session.setDebug(debug);

        Message message = new MimeMessage(session);
        try
        {
            message.setFrom(new InternetAddress(reportAddressFrom));
            InternetAddress[] addressTo = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++)
            {
                addressTo[i] = new InternetAddress(recipients[i]);
            }
            message.setRecipients(Message.RecipientType.TO, addressTo);

            // Setting the Subject and Content Type
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String dateNow = formatter.format(Calendar.getInstance().getTime());

            message.setSubject(defaultEnv.toUpperCase()
                    + " Test Automation result from " + hostName
                    + " for Application " + application.toUpperCase()
                    + " as on " + dateNow);

            //StringBuilder sb = updateHTML(timeTaken);

            MimeBodyPart attachment = new MimeBodyPart();
            FileDataSource file = new FileDataSource(new File("log/logs/"
                    + LoggerUtility.DATEFOLDER + "/"
                    + "testSummaryResults/testSummaryResult.csv"))
            {
                @Override
                public String getContentType()
                {
                    return "application/octet-stream";
                }
            };

            attachment.setDataHandler(new DataHandler(file));
            attachment.setFileName("Test Summary.csv");

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            //messageBodyPart.setContent(sb.toString(), "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachment);

            ((MimeMessage) message).setContent(multipart);

            Transport.send(message);
        }
        catch (AddressException e)
        {

            new LoggerUtility().log
                    .error("Mail not sent due to following reason - "
                            + e.getMessage());
        }
        catch (MessagingException e)
        {
            new LoggerUtility().log
                    .error("Mail not sent due to following reason - "
                            + e.getMessage());
        }
    }

}
