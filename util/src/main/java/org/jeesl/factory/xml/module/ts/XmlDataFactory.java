package org.jeesl.factory.xml.module.ts;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.jeesl.model.xml.module.ts.Data;
import org.jeesl.model.xml.module.ts.Points;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.DateUtil;

public class XmlDataFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlDataFactory.class);

	public static Data build(long id){return build(id,null,null);}

	public static Data build(Date record, Double value){return build(null,record,value);}
	public static Data buildWithGmt(Date record, Double value)
	{
		Data xml = build(null,record,value);
		if(record!=null)
		{
			LocalDateTime ldt = DateUtil.toLocalDateTime(record);
			ZonedDateTime zdt = ZonedDateTime.of(ldt,ZoneId.of("UTC"));
			xml.setRecord(DateUtil.toXmlGc(zdt));
		}
		return xml;
	}
	public static Data build(Long id, Date record, Double value)
	{
		Data xml = new Data();
		if(id!=null){xml.setId(id);}
		if(record!=null){xml.setRecord(DateUtil.toXmlGc(record));}
		if(value!=null){xml.setValue(value);}
		return xml;
	}

	public static Data build(Date record, Double x, Double y)
	{
		Data xml = new Data();
		if(record!=null){xml.setRecord(DateUtil.toXmlGc(record));}
		if(x!=null){xml.setX(x);}
		if(y!=null){xml.setY(y);}
		return xml;
	}

	public static Data build(Date record, Points points)
	{
		Data xml = new Data();
		if(record!=null){xml.setRecord(DateUtil.toXmlGc(record));}
		xml.setPoints(points);
		return xml;
	}
	
	public static Data build(LocalDateTime ldt, Points points)
	{
		Data xml = new Data();
		if(ldt!=null)
		{
			try
			{
				ZonedDateTime zoneDateTime = ZonedDateTime.of(ldt, ZoneId.systemDefault());
				GregorianCalendar gregorianCalendar = GregorianCalendar.from(zoneDateTime);
				xml.setRecord(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
			}
			catch (DatatypeConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		xml.setPoints(points);
		return xml;
	}
}