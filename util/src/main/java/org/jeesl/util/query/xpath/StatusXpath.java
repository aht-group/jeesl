package org.jeesl.util.query.xpath;

import java.util.List;

import net.sf.ahtutils.xml.aht.Aht;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

import org.apache.commons.jxpath.JXPathContext;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.model.xml.io.locale.status.Description;
import org.jeesl.model.xml.io.locale.status.Descriptions;
import org.jeesl.model.xml.io.locale.status.Lang;
import org.jeesl.model.xml.io.locale.status.Langs;
import org.jeesl.model.xml.io.locale.status.Status;
import org.jeesl.model.xml.io.locale.status.Translation;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.io.locale.status.Type;
import org.jeesl.model.xml.io.locale.status.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatusXpath
{
	final static Logger logger = LoggerFactory.getLogger(StatusXpath.class);
	
	public static synchronized Lang getLang(Langs langs,String key) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(langs);
		
		StringBuffer sb = new StringBuffer();
		sb.append("lang[@key='").append(key).append("']");
		
		@SuppressWarnings("unchecked")
		List<Lang> listResult = (List<Lang>)context.selectNodes(sb.toString());
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+Lang.class.getSimpleName()+" for key="+key+" in "+JaxbUtil.toString(langs));}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+Lang.class.getSimpleName()+" for key="+key);}
		return listResult.get(0);
	}
	
	public static synchronized Description getDescription(Descriptions descriptions,String key) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(descriptions);
		
		StringBuffer sb = new StringBuffer();
		sb.append("description[@key='").append(key).append("']");
		
		@SuppressWarnings("unchecked")
		List<Description> listResult = (List<Description>)context.selectNodes(sb.toString());
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+Description.class.getSimpleName()+" for key="+key);}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+Description.class.getSimpleName()+" for key="+key);}
		return listResult.get(0);
	}
	
	public static synchronized Translation getTranslation(Translations translations,String key) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(translations);
		
		StringBuffer sb = new StringBuffer();
		sb.append("translation[@key='").append(key).append("']");
		
		@SuppressWarnings("unchecked")
		List<Translation> listResult = (List<Translation>)context.selectNodes(sb.toString());
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+Translation.class.getSimpleName()+" for key="+key);}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+Translation.class.getSimpleName()+" for key="+key);}
		return listResult.get(0);
	}
	
	public static synchronized Lang getLang(Translations translations,String keyTranslation, String keyLang) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Translation translation = getTranslation(translations,keyTranslation);
		try
		{
			Lang lang = getLang(translation.getLangs(),keyLang);
			return lang;
		}
		catch (ExlpXpathNotFoundException e) {throw new ExlpXpathNotFoundException(translation.getKey()+" "+e.getMessage());}
		catch (ExlpXpathNotUniqueException e) {throw e;}
	}
	
	public static synchronized Type getType(Types types,String key) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(types);
		
		StringBuffer sb = new StringBuffer();
		sb.append("type[@key='").append(key).append("']");
		
		@SuppressWarnings("unchecked")
		List<Type> listResult = (List<Type>)context.selectNodes(sb.toString());
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+Type.class.getSimpleName()+" for key="+key);}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+Type.class.getSimpleName()+" for key="+key);}
		return listResult.get(0);
	}
	
	public static Status getStatus(List<Status> list, String code) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Aht aht = new Aht();
		aht.getStatus().addAll(list);
		JXPathContext context = JXPathContext.newContext(aht);
		
		StringBuffer sb = new StringBuffer();
		sb.append("status[@code='").append(code).append("']");
		
		@SuppressWarnings("unchecked")
		List<Status> listResult = (List<Status>)context.selectNodes(sb.toString());
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+Status.class.getSimpleName()+" for code="+code);}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+Status.class.getSimpleName()+" for code="+code);}
		return listResult.get(0);
	}
	
	public static String getTranslationString(List<Status> list, String code, String langKey) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Status status = StatusXpath.getStatus(list, code);
		Lang lang = StatusXpath.getLang(status.getLangs(), langKey);
		return lang.getTranslation();
	}
}