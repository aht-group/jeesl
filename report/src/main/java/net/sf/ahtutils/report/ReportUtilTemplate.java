package net.sf.ahtutils.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import net.sf.ahtutils.xml.report.Element;
import net.sf.ahtutils.xml.report.Field;
import net.sf.ahtutils.xml.report.Jr;
import net.sf.ahtutils.xml.report.Media;
import net.sf.ahtutils.xml.report.Report;
import net.sf.ahtutils.xml.report.Reports;
import net.sf.ahtutils.xml.report.Template;
import net.sf.exlp.util.io.resourceloader.MultiResourceLoader;
import net.sf.exlp.util.xml.JaxbUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlDigesterFactory;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class ReportUtilTemplate
{
	final static Logger logger = LoggerFactory.getLogger(ReportUtilXls.class);
	
	private String reportRoot;
	private MultiResourceLoader mrl = new MultiResourceLoader();
	
	public ReportUtilTemplate(String reportRoot) throws FileNotFoundException{
		this.reportRoot = reportRoot;
		Reports reports = null;
		try {
			 reports = JaxbUtil.loadJAXB(mrl.searchIs(reportRoot +"/reports.xml"), Reports.class);
		}
		catch (Exception e)
		{
			reports = JaxbUtil.loadJAXB(mrl.searchIs("src/main/resources/" +reportRoot +"/reports.xml"), Reports.class);
		}
	}
	
	public void applyTemplates(Report report)
	{
		for (Media media : report.getMedia())
		{
			for (Jr jr : media.getJr())
			{
				
			}
		}
	}
	
	public JasperDesign create()
	{
		//JasperDesign  the in-memory JRXML report design
		JasperDesign design = new JasperDesign();
		design.setName("demoReport");
		JRDesignQuery query = new JRDesignQuery();
		query.setText("//*");
		query.setLanguage("XPath");
		design.setQuery(query);
		design.setBottomMargin(72);
		design.setTopMargin(72);
		design.setLeftMargin(72);
		design.setRightMargin(72);
		
		//A JRDesignBand can represent different report parts like the DetailBand, the PageHeader/Footer, ...
		JRDesignBand header = new JRDesignBand();
		header.setHeight(50);
		
		//Example of a dynamic text field
		JRDesignTextField textField=new JRDesignTextField();
		textField.setStretchWithOverflow(true);
		textField.setBlankWhenNull(true);
		textField.setX(0);
		textField.setY(4);
		textField.setWidth(100);
		textField.setHeight(13);
		textField.setKey("header");
	
		//TextField expression
		JRDesignExpression designExpression=new JRDesignExpression();
		designExpression.setValueClass(String.class);
		designExpression.setText("\"Welcome to the report.\"");

		//Setting the expression
		textField.setExpression(designExpression);
		
		//Add element to header band
		header.getChildren().add(textField);
		
		//A band is then set as a part of the report design
		design.setPageHeader(header);
		return design;
	}
	
	public JasperDesign create(Template template) throws JRException, ParserConfigurationException, SAXException, FileNotFoundException, ClassNotFoundException
	{
		//JasperDesign  the in-memory JRXML report design
		JasperDesign design = new JasperDesign();
		
		//Create basic elements
		design.setName(template.getId());
		JRDesignQuery query = new JRDesignQuery();
		query.setText("//*");
		query.setLanguage("XPath");
		design.setQuery(query);
		design.setBottomMargin(72);
		design.setTopMargin(72);
		design.setLeftMargin(72);
		design.setRightMargin(72);
		design.setLanguage("groovy");
		design.setColumnWidth(450);
		
		//Load the elements from template
		for (Element element : template.getElement())
		{
			InputStream is             = mrl.searchIs(reportRoot +"/templates/" +element.getFile() +".jrxml");
			JRXmlLoader jlo            = new JRXmlLoader(JRXmlDigesterFactory.createDigester());
			JasperDesign elementDesign = jlo.loadXML(is); 
			if (element.getType().equals("header"))
			{
				logger.debug("entering header section");
				design.setPageHeader(elementDesign.getTitle());
			}
			if (element.getType().equals("footer"))
			{
				logger.debug("entering footer section");
				design.setPageFooter(elementDesign.getPageFooter());
			}
		}
		
		//Now, the standard fields need to be declared (holding title, subtitle, logo, footer and record date)
		for (Field field : template.getField())
		{
			if (field.getType().equals("field"))
			{
				JRDesignField reportField = new JRDesignField();
				reportField.setName(field.getName());
				reportField.setDescription(field.getExpression());
				reportField.setValueClassName(field.getClassName());
				design.addField(reportField);
			}
			else if (field.getType().equals("parameter"))
			{
				JRDesignParameter parameter = new JRDesignParameter();
				parameter.setName(field.getName());
				parameter.setValueClassName(field.getClassName());
				design.addParameter(parameter);
			}
		}
		
		//JRXmlWriter cares about writing the in-memory design to an OutputStream
		JRXmlWriter.writeReport(design, System.out, "UTF-8");
		return design;
	}
	
	public void applyTemplate(JasperDesign design, Template template)
	{
		//Replace the elements in the design with the information from the template
	}
	
	public static void main(String[] args) throws JRException, FileNotFoundException, ParserConfigurationException, SAXException, ClassNotFoundException
	{
		ReportUtilTemplate templater = new ReportUtilTemplate(null);
		String rootDir = "../xml/src/test/resources/data/xml/report";
		
		File fXml = new File(rootDir,"template.xml");
		Template template = (Template)JaxbUtil.loadJAXB(fXml.getAbsolutePath(), Template.class);
		
		File fXml2 = new File(rootDir,"field.xml");
		Field field = (Field)JaxbUtil.loadJAXB(fXml2.getAbsolutePath(), Field.class);
		
		template.getField().add(field);
		templater.create(template);
	}

}
