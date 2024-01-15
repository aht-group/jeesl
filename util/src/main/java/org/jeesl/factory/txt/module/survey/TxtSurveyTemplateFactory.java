package org.jeesl.factory.txt.module.survey;

import java.util.Objects;

import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplate;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtSurveyTemplateFactory <TEMPLATE extends JeeslSurveyTemplate<?,?,?,TEMPLATE,VERSION,?,?,?,?,?>,
										VERSION extends JeeslSurveyTemplateVersion<?,?,TEMPLATE>>
{
	final static Logger logger = LoggerFactory.getLogger(TxtSurveyTemplateFactory.class);
		
	private final String localeCode;
	
	public TxtSurveyTemplateFactory(final String localeCode)
	{
		this.localeCode=localeCode;
	}
	
	public String debug(TEMPLATE template)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(template.getName());
		sb.append(" ").append(template.getRemark());
		sb.append(" ").append(template.getCategory().getName().get(localeCode).getLang());
		return sb.toString();
	}
	
	public String idPath(TEMPLATE template)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("T:").append(template.getId());
		sb.append(" TV:"); if(Objects.nonNull(template.getVersion())) {sb.append(template.getVersion().getId());} else {sb.append("--");}
		sb.append(" VT:"); if(Objects.nonNull(template.getVersion().getTemplate())) {sb.append(template.getVersion().getTemplate().getId());} else {sb.append("--");}
		
		
		return sb.toString();
	}
}