package org.jeesl.processor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.jeesl.JeeslMavenBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class JeeslXhtmlParser
{
	final static Logger logger = LoggerFactory.getLogger(JeeslXhtmlParser.class);
	
	private File src;
	
	private XMLReader xmlReader;
	private SAXBuilder jsb;
	private final XPathFactory xpf;
	private final Namespace nsPrimefaces = Namespace.getNamespace("p", "http://primefaces.org/ui");
	
	List<String> parseStats; public List<String> getStats() {return parseStats;}
	private int count; public int getCount() {return count;}
	
	public JeeslXhtmlParser(String pathToXhtmlFolder) throws ParserConfigurationException, SAXException
	{
		parseStats = new ArrayList<>();
		
		jsb = new SAXBuilder();
		xpf = XPathFactory.instance();
		
		SAXParserFactory spf = SAXParserFactory.newInstance();
	    spf.setNamespaceAware(true);
	    SAXParser saxParser = spf.newSAXParser();
		xmlReader = saxParser.getXMLReader();
	    xmlReader.setContentHandler(new DefaultHandler());
	    src = new File(pathToXhtmlFolder);
	}

	public void parse()
	{
		count=0;
		if(xmlReader!=null && src!=null)
		{
			parseFiles(src.listFiles(),xmlReader);
		}
	}

	 private void parseFiles(File[] files, XMLReader xmlReader)
	 {
        for(File file : files)
        {
            if(file.isDirectory())
            {
            	parseFiles(file.listFiles(),xmlReader); // Calls recursive method.
            }
            else
            {
            	if(file.getAbsolutePath().endsWith(".xhtml"))
            	{
	            	count++;
	            	try
					{
						xmlReader.parse(new InputSource(file.getAbsolutePath()));
							
				        Document jdom = jsb.build(file);

				        XPathExpression<Element> xpeDatatable = xpf.compile("//p:dataTable", Filters.element(), null, nsPrimefaces);
				        for(Element e : xpeDatatable.evaluate(jdom))
				        {
				        	if(Objects.isNull(e.getAttribute("styleClass"))) {logger.warn("Missing p:dataTable(styleClass): "+file.getAbsolutePath());}
				        	else
				        	{
				        		String v = e.getAttributeValue("styleClass");
				        		if(!v.contains("jeesl-datatable")) {logger.warn("Wrong p:dataTable(styleClass): "+file.getAbsolutePath());}
				        		if(v.contains("jeeslDatatable")) {logger.warn("Wrong p:dataTable(styleClass): "+file.getAbsolutePath());}
				        	}
				        }
				        
				        XPathExpression<Element> xpePanel = xpf.compile("//p:panel", Filters.element(), null, nsPrimefaces);
				        for(Element e : xpePanel.evaluate(jdom))
				        {
				        	if(Objects.isNull(e.getAttribute("styleClass"))) {logger.warn("Missing p:dataTable(styleClass): "+file.getAbsolutePath());}
				        	else
				        	{
				        		String v = e.getAttributeValue("styleClass");
				        		if(!(v.contains("jeesl-panel") || v.contains("jeesl-tree"))) {logger.warn("Wrong p:panel(styleClass): "+file.getAbsolutePath());}
				        		if(v.contains("jeeslPanel")) {logger.warn("Wrong p:panel(styleClass): "+file.getAbsolutePath());}
				        	}
				        }
					}
	            	catch (IOException | SAXException | JDOMException e)
					{
	            		parseStats.add(e.toString());
					}
            	}
            }
        }
    }

	public static void main(String[] args) throws Exception
    {
		JeeslMavenBootstrap.init();
		
		String dir = "/Users/thorsten/workspace/4.27/meis/web7";
		JeeslXhtmlParser cli = new JeeslXhtmlParser(dir);
		cli.parse();
		
		logger.info("Parsed "+cli.getCount()+" files");
    }
	 
}