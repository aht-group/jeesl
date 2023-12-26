package net.sf.ahtutils.report;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import net.sf.ahtutils.report.exception.ReportException;
import net.sf.exlp.util.xml.JDomUtil;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.jeesl.model.xml.io.report.Jr;
import org.jeesl.model.xml.io.report.Media;
import org.jeesl.model.xml.io.report.Report;
import org.jeesl.model.xml.io.report.Reports;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportUtilFonts {
	
	private Reports reports;
	private String  reportRoot;
	
	final static Logger logger = LoggerFactory.getLogger(ReportUtilFonts.class);

	public ReportUtilFonts(Reports reports) throws ReportException {
		this.reports = reports;
		reportRoot = reports.getDir() +"/";
	}
	
	public void replaceAll(String oldFont, String newFont, Boolean productive) throws JDOMException, IOException
	{
		for(Report report : reports.getReport())
 		{
 			for (Media media : report.getMedia())
 			{
 				for (Jr jr : media.getJr())
 				{
					if (media.getType().equals("pdf"))
					{
						String jrxml  = reportRoot +"/" +"jrxml"  +"/" +report.getDir() +"/" + media.getType() + "/" + jr.getType() + jr.getName() +".jrxml";
						logger.debug("Inspecting " +jrxml +" to find Font attributes.");

						org.jdom2.Document jdomDoc =  JDomUtil.load(jrxml);

						XPathExpression<Element> xpath = XPathFactory.instance().compile("//*[local-name()='font']", Filters.element());
						List<Element> nodes = xpath.evaluate(jdomDoc);
						logger.warn("*************");
						logger.warn("This has been migrated to jdom2. Needs to be tested!");
						logger.warn("*************");
	// 					XPath xpath = XPath.newInstance("//*[local-name()='font']");
	// 					ArrayList<Element> nodes = new ArrayList<Element>();
	 //					nodes.addAll((ArrayList<Element>) xpath.selectNodes(jdomDoc));
						Boolean changed = false;
						for (Element node : nodes)
						{
							Element font = (Element) node;
							String fontName = font.getAttributeValue("fontName");
							if (fontName!=null && fontName.equals(oldFont))
							{
								logger.debug("Changing font from " +oldFont +" to " +newFont);
								font.setAttribute("fontName", newFont);
								changed = true;
							}
						}
						if (changed)
						{
							XMLOutputter xmlOutput = new XMLOutputter(org.jdom2.output.Format.getPrettyFormat());
							if (productive)
							{
								xmlOutput.output(jdomDoc, new FileWriter(jrxml));
							}
							else
							{
								logger.info("Changed font from " +oldFont +" to " +newFont +" in " +jr.getType() + jr.getName() +".jrxml");
							}

						}
					}
 				}
 			}
 		}			
	}
}
