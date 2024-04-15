package org.jeesl.factory.txt.module.finance;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import javax.xml.datatype.XMLGregorianCalendar;

import org.exlp.util.system.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtTimeFactory
{
	final static Logger logger = LoggerFactory.getLogger(TxtTimeFactory.class);
	
	private DateTimeFormatter dtf; public TxtTimeFactory dtf(DateTimeFormatter value) {this.dtf=value; return this;}
	
	private String fallback;
	
	public static TxtTimeFactory instance() {return new TxtTimeFactory();}
	private TxtTimeFactory()
	{
		dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		this.fallback = "";
	}
	
//	public <E extends Enum<E>, C extends Enum<C>> String build(Figures figures, E figureCode, C code)
//	{
//		Counter f = null;
//		try {f = FiguresXpath.getFiguresCounter(figures,figureCode,code);}
//		catch (ExlpXpathNotFoundException | ExlpXpathNotUniqueException e) {}
//		return this.build(f);
//	}
	
	public String build(XMLGregorianCalendar xml)
	{
		if(Objects.isNull(xml)) {return fallback;}
		return this.build(DateUtil.toLocalDate(xml));
	}
	
	public String build(LocalDate ld)
	{
		if(Objects.isNull(ld)) {return fallback;}
//		logger.info(ld.toString());
		return dtf.format(ld);
	}
}
