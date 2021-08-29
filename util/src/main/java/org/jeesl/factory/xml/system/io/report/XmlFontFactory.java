package org.jeesl.factory.xml.system.io.report;

import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReportCategory;
import org.jeesl.interfaces.model.io.report.data.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportWorkbook;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.report.Font;

public class XmlFontFactory <STYLE extends JeeslReportStyle<?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlFontFactory.class);
	
	private Font q;
	
	public XmlFontFactory(Font q)
	{
		this.q=q;
	}
	
	public Font build(STYLE style)
	{
		Font xml = build();
		
		if(q.isSetBold()){xml.setBold(style.isFontBold());}
		if(q.isSetItalic()){xml.setItalic(style.isFontItalic());}
		
		return xml;
	}
	
	public static Font build()
	{
		Font xml = new Font();						
		return xml;
	}
}