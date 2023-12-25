package org.jeesl.factory.xml.module.survey;

import org.jeesl.interfaces.model.module.survey.correlation.JeeslSurveyCorrelation;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.xml.module.survey.Correlation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlCorrelationFactory<CORRELATION extends JeeslSurveyCorrelation<?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlCorrelationFactory.class);
		
	private Correlation q;
	
	public XmlCorrelationFactory(Correlation q)
	{
		this.q=q;
	}
	
	public Correlation build(CORRELATION ejb)
	{		
		Correlation xml = new Correlation();
		if(q.isSetId()){xml.setId(ejb.getId());}
		return xml;
	}
	
	public static Correlation id()
	{
		Correlation xml = new Correlation();
		xml.setId(0);
		return xml;
	}
	
	public static <T extends EjbWithId> Correlation build(Class<?> c, T t)
	{
		Correlation xml = new Correlation();
		xml.setId(t.getId());
		xml.setType(c.getName());
		return xml;
	}
}