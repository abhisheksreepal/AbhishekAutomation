package com.home.utilities;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import com.home.pages.BaseWebPage;

public class ReportAndMail {
	
	 private static PropertiesConfiguration envProperties;
	 
	 public static StringBuilder emalableHtmleReport;
	 
	 static String hostName = "";
	
	private static Logger log = Logger.getLogger(ReportAndMail.class);
	 
	  static {
		 try
	        {
	            envProperties = new PropertiesConfiguration(
	                    "src/test/resources/config/environment.properties");

	        }
	        catch (ConfigurationException e)
	        {
	            e.printStackTrace();
	        }
		 

	        InetAddress addr;

	        try
	        {
	            addr = InetAddress.getLocalHost();
	            hostName = addr.getHostName();
	        }
	        catch (UnknownHostException e)
	        {
	            LoggerUtility
	                    .logTraceMessage(log, "Not able to capture IP address");
	        }
	}
	
	@SuppressWarnings("unused")
    public static StringBuilder updateHTML(long timeTaken)
    {

	    StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();

        StringBuilder sb2ForAppDB = new StringBuilder();

        double totalPassCount = 0;
        double totalFailCount = 0;
        double totalCount = 0;
        double totalTCICount = 0;
        DecimalFormat decimalformat = new DecimalFormat("#.##");

        Set<String> classNames = LoggerUtility.classDetails.keySet();

        String testFailed = "";
        for (Iterator<String> iterator = classNames.iterator(); iterator
                .hasNext();)
        {
            String className = (String) iterator.next();
            double modulePassCount = 0;
            double moduleFailCount = 0;
            double moduleTotalCount = 0;
            double moduleTCICount = 0;
            String failbgColor = "";
            String passbgColor = "";
            Set<String> testMethod = LoggerUtility.classDetails.get(className)
                    .keySet();
            for (Iterator<String> iterator2 = testMethod.iterator(); iterator2
                    .hasNext();)
            {
                String testName = (String) iterator2.next();
                HashMap<String, String> tcDetails = LoggerUtility.classDetails
                        .get(className).get(testName);

                String status = tcDetails.get("Status");
                String comment = "Please refer to screenshot "
                        + tcDetails.get("ErrorScreenshotName");
                if (status.equalsIgnoreCase("Pass"))
                {
                    ++modulePassCount;
                }
                else if (status.equalsIgnoreCase("Fail"))
                {
                    ++moduleFailCount;

                }
            }
            moduleTotalCount = modulePassCount + moduleFailCount;
            totalPassCount = totalPassCount + modulePassCount;
            totalFailCount = totalFailCount + moduleFailCount;
            moduleTCICount = (modulePassCount / moduleTotalCount) * 100;
            if ((moduleFailCount == 0))
            {
                failbgColor = "WHITE";
                testFailed = "WHITE";
            }
            else
            {
                failbgColor = "RED";
                testFailed = "RED";
            }
            if (modulePassCount == 0)
            {
                passbgColor = "WHITE";
            }
            else
            {
                passbgColor = "GREEN";
            }

            sb.append("<tr><td style='background:#D6E6FA'>" + className
                    + "</td><td bgcolor =" + passbgColor + ">"
                    + modulePassCount + "</td><td bgcolor = " + failbgColor
                    + ">" + moduleFailCount
                    + "</td><td style='background:#D6E6FA'>" + moduleTotalCount
                    + "</td><td style='background:#D6E6FA'>"
                    + decimalformat.format(moduleTCICount) + "</td>");
        }

        totalCount = totalPassCount + totalFailCount;
        totalTCICount = (totalPassCount / totalCount) * 100;
        sb
                .append("<tr><td style='background:#8CB3E5'><b>Total Count</b></td><td style='background:#8CB3E5'>"
                        + totalPassCount
                        + "</td><td style='background:#8CB3E5'>"
                        + totalFailCount
                        + "</td><td style='background:#8CB3E5'>"
                        + totalCount
                        + "</td><td style='background:#8CB3E5'>"
                        + decimalformat.format(totalTCICount) + "</td></tr>");

        /*
         * sb1 .append("<html><h1>Automation Test Execution Summary*</h1><table
         * border = 1><td><b>FUNCTIONALITY NAME</b></td><td><b>PASSCOUNT</b></td>
         * </td><td><b>FAILCOUNT</b></td><td><b>Total Count</b></td><td><b>TCI
         * Index</b></td><td><b>Test Link Ids</b></td>" + sb + "</table></html>");
         */
        long millis = timeTaken;
        String hms = String.format("%02dHr %02dMin %02dSec",
                TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS
                        .toMinutes(millis)
                        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                                .toHours(millis)), TimeUnit.MILLISECONDS
                        .toSeconds(millis)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                                .toMinutes(millis)));
        sb1
                .append("<html><h1>Automation Test Execution Summary*</h1><table border=1><tr><td style='background:#8CB3E5'><b>Total Execution Time:</b></td><td style='background:#8CB3E5'><b>"
                        + hms
                        + "</b></td></tr></table><table border = 1><td style='background:#8CB3E5'><b>Functionality Name</b></td><td style='background:#8CB3E5'><b>Pass Count</b></td><td style='background:#8CB3E5'><b>Fail Count</b></td><td style='background:#8CB3E5'><b>Total Count</b></td><td style='background:#8CB3E5'><b>TCI Index</b></td>"
                        + sb + "</table></html>");
        sb1
                .append("<p bgcolor = BLUE><b>Note: * -  Please refer attached Test Summary.csv for list of test cases executed.</b></p>");
        sb1.append("<br>Thanks & Regards,<br> Automation Team");
        
        //Assigning html report to emailableHtmlreport for later use
        emalableHtmleReport=sb1;
        
        return sb1;

    }

	//TODO try to send mail with atachment -> index.html 
	// If you are successful -> use this method in AfterSuite and DO NOT use huson emaiol facility
	//BUT if you are able to have hudson email plugin do the job of attaching email -> remove this method
    public static void sendMail(long timeTaken) 
    {

    	boolean debug = false;
    	final String username = envProperties.getString("emailUserName");
		final String password = envProperties.getString("emailPassword");
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", envProperties.getString("ReportmailServer"));
		props.put("mail.smtp.port", "587");
		
		String[] recipients = BaseWebPage.getStringTokenized(envProperties
                .getString("Reportrecipients"), ";");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
		session.setDebug(debug);
 
		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(envProperties.getString("ReportaddressFrom")));
			InternetAddress[] addressTo = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++)
            {
                addressTo[i] = new InternetAddress(recipients[i]);
            }
            message.setRecipients(Message.RecipientType.TO, addressTo);
			
			// Setting the Subject and Content Type
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			String dateNow = formatter.format(Calendar.getInstance().getTime());
			
			
			message.setSubject(envProperties.getString("default").toUpperCase()
                     + " Test Automation result from " + hostName
                     + " using "
                     + envProperties.getString("BROWSER_NAME").toUpperCase()
                     + " as on " + dateNow);
			
			StringBuilder sb = updateHTML(timeTaken);
			message.setContent(sb.toString(), "text/html");
			
			 MimeBodyPart attachment = new MimeBodyPart();
	            FileDataSource file = new FileDataSource(new File(
	                    "log/testSummaryResults/testSummaryResult.csv"))
	            {
	                @Override
	                public String getContentType()
	                {
	                    return "application/octet-stream";
	                }
	            };
			
			Transport.send(message);
		} catch (AddressException e) {
			log.error("Mail not sent due to following reason - "+e.getMessage());
		} catch (MessagingException e) {
			log.error("Mail not sent due to following reason - "+e.getMessage());
		}
	}
    
    
    public static void createSummaryResultLog() throws Exception
    {
    	String browser =  envProperties.getString("BROWSER_NAME");
        String filePath = "log/testSummaryResults/testSummaryResult.csv";
        File file2 = new File(filePath);
        BufferedWriter buffWriter2 = new BufferedWriter(new FileWriter(file2));
        buffWriter2.write("Module,Test Case Name,Test Case Id in Test Link,Execution result (P/F),Comment in case of ERROR failure,Comment in case of VERIFICATION failure,Comment in case of CONFIGURATION Failure");
        buffWriter2.newLine();
		
        
        String moduleTestSummaryResult = "log/testSummaryResults/results/moduleSummaryResult.csv";                
        File file3 = new File(moduleTestSummaryResult);
        BufferedWriter buffWriter3 = new BufferedWriter(new FileWriter(file3));
        buffWriter3.write("Test Case Name,PassCount,FailCount,Browser Used for Testing");
        buffWriter3.newLine();
        
        
		Set<String> classNames = LoggerUtility.classDetails.keySet();
		
		 String configurationFailureComment = null;
			if(LoggerUtility.configurationFailure!=null){
				configurationFailureComment = "Please find following Configuration Problems "+LoggerUtility.configurationFailure;
			}else{
				configurationFailureComment = "No Configuration errors";
			}
			
		if(classNames.iterator().hasNext()){
			for (Iterator<String> iterator = classNames.iterator(); iterator.hasNext();) {
				String className = (String) iterator.next();
				int passCount = 0;
				int failCount = 0;
				Set<String> testMethod  = LoggerUtility.classDetails.get(className).keySet();
				if(testMethod.iterator().hasNext()){
					for (Iterator<String> iterator2 = testMethod.iterator(); iterator2.hasNext();) {
						String testName = (String) iterator2.next();
						HashMap<String, String> tcDetails =  LoggerUtility.classDetails.get(className).get(testName);
						String tcId = tcDetails.get("TestCaseID");
						String status = tcDetails.get("Status");
						String comment = "Please refer to screenshot "+tcDetails.get("ErrorScreenshotName");
						String verifyComment = "Please refer to screenshot "+tcDetails.get("VerificationErrorScreenshotName");				
						if(status.equalsIgnoreCase("Pass")){
							++passCount;
						}else if(status.equalsIgnoreCase("Fail")){
							++failCount;
						}
						buffWriter2.write(className+","+testName + ","
				                + tcId + "," +status + ","
				                + comment+","+verifyComment+","+configurationFailureComment);
				        buffWriter2.newLine();				
						}
						buffWriter3.write(className+","+passCount+","+failCount+","+browser);
					}else{
						buffWriter2.write(""+","+"" + ","
			                + "" + "," +"" + ","
			                + ""+","+""+","+configurationFailureComment);
						}
			}
		}else{
			buffWriter2.write(""+","+"" + ","
	                + "" + "," +"" + ","
	                + ""+","+""+","+configurationFailureComment);
		}
		
		buffWriter2.close();
        buffWriter3.close();
		
    }

}
