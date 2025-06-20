package org.jeesl.factory.xml.system.io.report;

import java.util.Objects;

import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.model.xml.io.report.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlFontFactory <STYLE extends JeeslReportStyle<?,?,?>>
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
		
		if(Objects.nonNull(q.isBold())) {xml.setBold(style.isFontBold());}
		if(Objects.nonNull(q.isItalic())) {xml.setItalic(style.isFontItalic());}
		
		return xml;
	}
	
	public static Font build()
	{
		Font xml = new Font();						
		return xml;
	}
}