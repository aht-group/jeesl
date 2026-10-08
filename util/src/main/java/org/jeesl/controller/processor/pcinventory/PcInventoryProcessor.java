
package org.jeesl.controller.processor.pcinventory;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.jeesl.model.xml.module.inventory.pc.Computer;
import org.jeesl.model.xml.module.inventory.pc.Hardware;
import org.jeesl.model.xml.module.inventory.pc.Software;
import org.jeesl.model.xml.module.inventory.pc.Update;
import org.jeesl.model.xml.module.inventory.pc.Updates;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.xml.JDomUtil;

public class PcInventoryProcessor
{
	final static Logger logger = LoggerFactory.getLogger(PcInventoryProcessor.class);
	
	private static Document doc;
	private static Namespace ns;
	List<Element> sW, sys, upds, uItem;
		
	private String getItem(List<Element> type, String expression)
	{
		String rtn = null;
		XPathFactory xFactory = XPathFactory.instance();
		XPathExpression<Element> exprCN = xFactory.compile(expression, Filters.element(), null, ns);
		List<Element> listCN = exprCN.evaluate(type);
		for (Element eCN : listCN)
		{
			List<Element> lCN = eCN.getParentElement().getChildren();
			rtn = lCN.get(6).getValue();
		}
		return rtn;
	}
	
	private List<String> getItems(List<Element> type, String expression)
	{
		List<String> rtn = new ArrayList<String>();
		XPathFactory xFactory = XPathFactory.instance();
		XPathExpression<Element> exprCN = xFactory.compile(expression, Filters.element(), null, ns);
		List<Element> listCN = exprCN.evaluate(type);
		for (Element eCN : listCN)
		{
			List<Element> lCN = eCN.getParentElement().getChildren();
			rtn.add(lCN.get(6).getValue().toString());
		}
		return rtn;
	}
	
	private List<Element> getCategory(String expression)
	{
		List<Element> rtn = null;
		XPathFactory xFactory = XPathFactory.instance();
		XPathExpression<Element> exprCN = xFactory.compile(expression, Filters.element(), null, ns);
		List<Element> listCN = exprCN.evaluate(doc);
		for (Element eCN : listCN)
		{
			List<Element> lCN = eCN.getParentElement().getChildren();
			rtn = lCN;
		}
		return rtn;
	}

	public Computer transform(String xmlFile) throws DatatypeConfigurationException
	{
		logger.error("Hallo");
		ByteArrayInputStream stream = new ByteArrayInputStream(xmlFile.getBytes());
		doc = JDomUtil.load(stream);
		
		ns = Namespace.getNamespace("power", "http://schemas.microsoft.com/powershell/2004/04");
		sys = getCategory("//power:Obj/power:MS/power:S[@N='CategoryID' and text()='300']");
		sW = getCategory("//power:Obj/power:MS/power:S[@N='CategoryID' and text()='500']");
		upds = getCategory("//power:Obj/power:MS/power:S[@N='CategoryID' and text()='600']");
		
		Hardware hardware = new Hardware();
		// hardware.setId();
		hardware.setManufacturer(getItem(sys, "//power:Obj/power:MS/power:S[@N='ItemID' and text()='307']"));
		hardware.setModel(getItem(sys, "//power:Obj/power:MS/power:S[@N='ItemID' and text()='308']"));
		hardware.setSerial(getItem(sys, "//power:Obj/power:MS/power:S[@N='ItemID' and text()='309']"));
		
		Updates updates = new Updates();
		
		List<String> uNameList = getItems(upds, "//power:Obj/power:MS/power:S[@N='ItemID' and text()='601']");
		List<String> uDescritionList = getItems(upds, "//power:Obj/power:MS/power:S[@N='ItemID' and text()='603']");
		List<String> uRecordlist = getItems(upds, "//power:Obj/power:MS/power:S[@N='ItemID' and text()='602']");
		
		for (int i = 0; i < uNameList.size(); i++)
		{
			Update update = new Update();
			update.setId(i + 1);
			update.setCode(uNameList.get(i).replace("{", "").replace("}", ""));
			update.setDescription(uDescritionList.get(i));
			
			try
			{
				if (uRecordlist.get(i).length() == 0)
				{
					XMLGregorianCalendar x = DatatypeFactory.newInstance().newXMLGregorianCalendar(
							LocalDateTime.parse(uRecordlist.get(i), DateTimeFormat.forPattern("")).toString());
					update.setRecord((x));
				}
				
				if (uRecordlist.get(i).length() == 8)
				{
					if (uRecordlist.get(i).contains("/"))
					{
						XMLGregorianCalendar x = DatatypeFactory.newInstance().newXMLGregorianCalendar(
								LocalDateTime.parse(uRecordlist.get(i), DateTimeFormat.forPattern("M/d/yyyy")).toString());
						update.setRecord((x));
					}
					else
					{
					XMLGregorianCalendar x = DatatypeFactory.newInstance().newXMLGregorianCalendar(
							LocalDateTime.parse(uRecordlist.get(i), DateTimeFormat.forPattern("yyyyMMdd")).toString());
					update.setRecord((x));
					}
				}
				if (uRecordlist.get(i).length() == 9)
				{
					if (uRecordlist.get(i).contains("/"))
					{
						XMLGregorianCalendar x = DatatypeFactory.newInstance().newXMLGregorianCalendar(
								LocalDateTime.parse(uRecordlist.get(i), DateTimeFormat.forPattern("M/dd/yyyy")).toString());
						update.setRecord((x));
					}
				}
				if (uRecordlist.get(i).length() == 10)
				{
					if (uRecordlist.get(i).contains("/"))
					{
						XMLGregorianCalendar x = DatatypeFactory.newInstance().newXMLGregorianCalendar(LocalDateTime
								.parse(uRecordlist.get(i), DateTimeFormat.forPattern("MM/dd/yyyy")).toString());
						update.setRecord((x));
					}
					if (uRecordlist.get(i).contains("-"))
					{
						XMLGregorianCalendar x = DatatypeFactory.newInstance().newXMLGregorianCalendar(LocalDateTime
								.parse(uRecordlist.get(i), DateTimeFormat.forPattern("yyyy-MM-dd")).toString());
						update.setRecord((x));
					}
				}
			} catch (Exception e)
			{
				logger.error("wrong dateformat: "+ e.getStackTrace());
			}
			updates.getUpdate().add(i, update);
		}
		
		Software software = new Software();
		software.setUpdates(updates);
		Computer computer = new Computer();
		
		try
		{
			computer.setId(computer.getId() + 1);
		} catch (Exception e)
		{
			computer.setId(0);
		}
		computer.setName(getItem(sys, "//power:Obj/power:MS/power:S[@N='ItemName' and text()='Computer Name']"));
		computer.setHardware(hardware);
		computer.setSoftware(software);
		computer.setCode(getItem(sys, "//power:Obj/power:MS/power:S[@N='ItemName' and text()='Universal Unique ID']").replace("{", "").replace("}", ""));
		
		return computer;
	}
}