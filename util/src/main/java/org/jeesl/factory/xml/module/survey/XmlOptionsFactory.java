package org.jeesl.factory.xml.module.survey;

import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.module.survey.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlOptionsFactory <L extends JeeslLang, D extends JeeslDescription,
								QUESTION extends JeeslSurveyQuestion<L,D,?,?,?,?,?,?,?,OPTION,?>,
								OPTION extends JeeslSurveyOption<L,D>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlOptionsFactory.class);
	
	final Options q;
	
	private XmlOptionFactory<L,D,OPTION> xfOption;
	
	public XmlOptionsFactory(String localeCode, Options q)
	{
		this.q=q;
		if(q.isSetOption()) {xfOption = new XmlOptionFactory<L,D,OPTION>(localeCode,q.getOption().get(0));}
	}
	
	public static Options build() {return new Options();}
	
	public Options build(QUESTION ejb)
	{
		Options xml = build();
		
		if(ejb.getOptions()!=null && !ejb.getOptions().isEmpty())
		{
			for(OPTION option : ejb.getOptions())
			{
				xml.getOption().add(xfOption.build(option));
			}
		}
		
		return xml;
	}
}