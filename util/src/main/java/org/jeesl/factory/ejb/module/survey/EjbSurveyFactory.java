package org.jeesl.factory.ejb.module.survey;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.factory.ejb.system.status.EjbDescriptionFactory;
import org.jeesl.factory.ejb.system.status.EjbLangFactory;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurvey;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplate;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.survey.Survey;

public class EjbSurveyFactory<L extends JeeslLang, D extends JeeslDescription,
				SURVEY extends JeeslSurvey<L,D,SS,TEMPLATE,?>,
				SS extends JeeslSurveyStatus<L,D,SS,?>,
				TEMPLATE extends JeeslSurveyTemplate<L,D,?,TEMPLATE,?,?,?,?,?,?>
				>
{
	final static Logger logger = LoggerFactory.getLogger(EjbSurveyFactory.class);
	
	private final Class<SURVEY> cSurvey;
    
	private final EjbLangFactory<L> efLang;
	private final EjbDescriptionFactory<D> efDescription;
	
	public EjbSurveyFactory(final Class<L> cL, final Class<D> cD, final Class<SURVEY> cSurvey)
	{       
        this.cSurvey = cSurvey;
        
        efLang = EjbLangFactory.factory(cL);
        efDescription = EjbDescriptionFactory.factory(cD);
	}
	    
	public SURVEY build(TEMPLATE template, SS status, Survey survey)
	{
		String[] locales = {"en"};
		logger.warn("NYI handling of lcoales");
		return build(locales,template,status,
						survey.getValidFrom().toGregorianCalendar().getTime(),
						survey.getValidTo().toGregorianCalendar().getTime());
	}
	
	public SURVEY build(String[] locales, TEMPLATE template,SS status)
	{
		return build(locales,template,status,null,null);
	}
	
	public SURVEY build(String[] locales, TEMPLATE template,SS status, Date validFrom,Date validTo)
	{
		SURVEY ejb = null;
		try
		{
			ejb = cSurvey.newInstance();
			ejb.setName(efLang.createEmpty(locales));
			ejb.setDescription(efDescription.createEmpty(locales));
			ejb.setTemplate(template);
			ejb.setStatus(status);
			ejb.setStartDate(validFrom);
			ejb.setEndDate(validTo);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public Map<Long,SURVEY> toMapId(List<SURVEY> surveys)
	{
		Map<Long,SURVEY> map = new HashMap<Long,SURVEY>();
		for(SURVEY s : surveys) {map.put(s.getId(),s);}
		return map;
	}
}