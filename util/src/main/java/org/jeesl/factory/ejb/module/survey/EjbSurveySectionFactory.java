package org.jeesl.factory.ejb.module.survey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplate;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.module.survey.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSurveySectionFactory<L extends JeeslLang, D extends JeeslDescription,
				TEMPLATE extends JeeslSurveyTemplate<L,D,?,TEMPLATE,?,?,?,SECTION,?,?>,
				SECTION extends JeeslSurveySection<L,D,TEMPLATE,SECTION,?>
>
{
	final static Logger logger = LoggerFactory.getLogger(EjbSurveySectionFactory.class);
	
	final Class<SECTION> cSection;
    
	public EjbSurveySectionFactory(final Class<SECTION> cSection)
	{       
        this.cSection = cSection;
	}
	    
	public SECTION build(TEMPLATE template,Section xSection)
	{
		SECTION eSection = build(template,xSection.getPosition());
		return eSection;
	}
	
	public SECTION build(TEMPLATE template, int position)
	{
		SECTION ejb = null;
		try
		{
			ejb = cSection.newInstance();
			ejb.setTemplate(template);
			ejb.setPosition(position);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public SECTION build(SECTION parent){return build(parent,0);}
	public SECTION build(SECTION parent, int position)
	{
		SECTION ejb = null;
		try
		{
			ejb = cSection.newInstance();
			ejb.setSection(parent);
			ejb.setPosition(position);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public Map<TEMPLATE,List<SECTION>> loadMap(JeeslSurveyCoreFacade<L,D,?,?,?,?,TEMPLATE,?,?,?,SECTION,?,?,?,?,?,?,?,?> fSurvey)
	{
		Map<TEMPLATE,List<SECTION>> map = new HashMap<TEMPLATE,List<SECTION>>();
		for(SECTION s : fSurvey.allOrderedPosition(cSection))
		{
			if(!map.containsKey(s.getTemplate())){map.put(s.getTemplate(),new ArrayList<SECTION>());}
			if(s.isVisible())
			{
				map.get(s.getTemplate()).add(s);
			}
		}
		return map;
	}
}