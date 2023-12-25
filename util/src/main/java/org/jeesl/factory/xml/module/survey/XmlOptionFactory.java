package org.jeesl.factory.xml.module.survey;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.module.survey.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlOptionFactory<L extends JeeslLang, D extends JeeslDescription, OPTION extends JeeslSurveyOption<L,D>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlOptionFactory.class);
		
	private final String localeCode;
	private final Option q;
	
	public XmlOptionFactory(Option q){this(null,q);}
	
	public XmlOptionFactory(String localeCode, Option q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public static Option build() {return new Option();}
	
	public Option build(OPTION ejb)
	{
		Option xml = build();
		if(Objects.nonNull(q.getId())) {xml.setId(ejb.getId());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(ObjectUtils.allNotNull(q.getLabel(),localeCode) && ejb.getName().containsKey(localeCode)) {xml.setLabel(ejb.getName().get(localeCode).getLang());}
		
		return xml;
	}
}