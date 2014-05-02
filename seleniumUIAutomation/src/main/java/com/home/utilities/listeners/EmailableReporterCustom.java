package com.home.utilities.listeners;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

import com.home.utilities.ReportAndMail;

public class EmailableReporterCustom implements IReporter {
	
	private static Logger log = Logger.getLogger(EmailableReporterCustom.class);
	private PrintWriter m_out;
	
 @Override
 public void generateReport(List<XmlSuite> xml, List<ISuite> suites, String outdir) {
	 try {
	      m_out = createWriter(outdir);
	    }
	    catch (IOException e) {
	    	log.error("output file", e);
	      return;
	    }
	    m_out.print(ReportAndMail.emalableHtmleReport);
	    m_out.flush();
	    m_out.close();
}
 
 private PrintWriter createWriter(String outdir) throws IOException {
	    new File(outdir).mkdirs();
	    return new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir,
	        "custom-emailable-report.html"))));
	  }
 
}
