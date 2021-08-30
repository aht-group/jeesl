package org.jeesl.factory.xml.system.io.report;

import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
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