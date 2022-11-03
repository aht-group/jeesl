package org.jeesl.util.report; 

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.fill.JRFillParameter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/*
 * Contains methods to process data needed when creating reports using JasperReports
 * e.g. to create lists of options used for answering a question
 */
public class SurveyHelperScriptlet extends JRDefaultScriptlet { 
    long pageInitTime = 0; 
    final static Logger logger = LoggerFactory.getLogger(SurveyHelperScriptlet.class);

	/*
	 * Demo method for testing the availability of the SurveyHelperScriptlet in reports
	 * @return a demo text
	 */
	public static String helloWorld()
	{
		return "SCRIPTLET Welcomes you";
	}
    
	/*
	 * This method helps to convert an ISO time String to a Java Date
	 * @param isoTimeString The ISO time String representation of the date
	 * @return the Java Date representation of the date
	 * @throws Exception
	 */
	public static Date parseISO(String isoTimeString) throws Exception
    {
		Calendar cal = javax.xml.bind.DatatypeConverter.parseDateTime(isoTimeString);
		return cal.getTime();
    }
	
	/*
	 * For a survey question, create a list of all options without tickable field
	 * @return The list of options for the given question as comma separated list
	 * @throws Exception
	 */
	public String getOptionsAsList() throws Exception 
	{
		return getOptionsAsList(false);
	}
	
	/*
	 * For a survey question, create a list of all options
	 * @param addTickField also renders a [ ] to be ticked on paper
	 * @return The list of options for the given question as comma separated list
	 * @throws Exception
	 */
	public String getOptionsAsList(boolean addTickField) throws Exception 
	{
		// Use the XML data document passed as the reports data source for requesting information
    	JRXmlDataSource ds = (JRXmlDataSource)this.getParameterValue("REPORT_DATA_SOURCE");
		
		// Get some insights of the additional data available when method is used
		if (logger.isTraceEnabled()) 
		{
			for (String key : this.parametersMap.keySet())
			{
				JRFillParameter param = this.parametersMap.get(key);
				logger.trace("Key: " +key +", value: " +param.getValueClassName());
			}
		}
        
		// Request the current questions id
		String questionId = (String)this.getFieldValue("questionId");
		
		// Prepare the list to be returned later as comma separated version
		ArrayList<String> options = new ArrayList<String>();
		
		// Prepare the XPath requesting the options associated with the given question by its id
		// IMPORTANT NOTE: Avoid general expressions beginning with // because they may cause strong performance issues!!
		// In case of this subreport, the report XPath is pointing at question and so the expression must be written as follows
		XPath xpath = XPathFactory.newInstance().newXPath();
		String xPathString = "question/options/option[parent::options/parent::question/@id='"+ questionId +"']";
		if (logger.isTraceEnabled()) {logger.trace("Requesting: " +xPathString);}
		
		// Now read the options and add them to the list of options
		try {
    		if (logger.isTraceEnabled()) {logger.trace("Document of Type " +ds.subDocument().getClass().toString());}
			
			NodeList optionNodes = (NodeList) xpath.evaluate(xPathString, ds.subDocument(), XPathConstants.NODESET);
			for (int i=0;i<optionNodes.getLength();i++)
			{
				Element option = (Element)optionNodes.item(i);
				
				// Since the report is configured to render HTML as markup, make the options code bold printed for better readablility
				StringBuilder optionStringBuilder = new StringBuilder();
				if (addTickField) {optionStringBuilder.append("[&nbsp;]");}
				optionStringBuilder.append("&nbsp;<b>" +option.getAttribute("code").toString().replace(" ", "&nbsp;") +"</b>&nbsp;" +option.getAttribute("label").toString().replace(" ", "&nbsp;"));
				options.add(optionStringBuilder.toString());
				if (logger.isTraceEnabled()) {logger.debug("ADDED " +optionStringBuilder.toString());}
			}
		} catch (Exception e) {
			logger.error("An error occured while trying to prepare the list of options for a given question as comma separated list: " +e.getMessage());
		}
		
		// Return the comma separated list using Apache Commons StrinUtils join method
		if (logger.isTraceEnabled()) {logger.trace("Adding Options: " +options.toArray().toString());}
		String returnValue = StringUtils.join(options, "     ");
		if (logger.isTraceEnabled()) {logger.trace("Returning " +returnValue);}
		return returnValue;
    }
}