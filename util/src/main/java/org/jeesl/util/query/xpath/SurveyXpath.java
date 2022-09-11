package org.jeesl.util.query.xpath;

import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.jeesl.model.xml.module.survey.Correlation;
import org.jeesl.model.xml.system.security.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class SurveyXpath
{
	final static Logger logger = LoggerFactory.getLogger(SurveyXpath.class);
	
	public static Correlation getMenuItem(Correlation correlation,Class<?> t) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		String type = t.getName();
		JXPathContext context = JXPathContext.newContext(correlation);
		
		StringBuffer sb = new StringBuffer();
		sb.append("/correlation[@type='").append(type).append("']");
		
		@SuppressWarnings("unchecked")
		List<Correlation> listResult = (List<Correlation>)context.selectNodes(sb.toString());
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+Correlation.class.getSimpleName()+" for type="+type);}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+View.class.getSimpleName()+" for type="+type);}
		return listResult.get(0);
	}
}