package org.jeesl.factory.xml.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.openfuxml.interfaces.configuration.OfxTranslationProvider;
import org.openfuxml.util.translation.OfxDefaultTranslationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.report.Label;
import net.sf.ahtutils.xml.report.Labels;

public class XmlLabelsFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlLabelsFactory.class);
		
	public static Labels build()
	{
		Labels xml = new Labels();
		return xml;
	}
	
	public static <S extends JeeslStatus<L,D,S>, L extends JeeslLang, D extends JeeslDescription>
		void aggregationGroups(String localeCode, Labels labels, List<S> aggregations)
	{
		for(int i=1;i<=aggregations.size();i++)
		{
			S s = aggregations.get(i-1);
			Label label = XmlLabelFactory.build("labelLevel"+i, s.getName().get(localeCode).getLang());
			labels.getLabel().add(label);
		}
	}
	
	public static void aggregationHeader(String lang, Labels labels, Map<Long,String> mapAggregationLabels)
	{
		for(Long i : mapAggregationLabels.keySet())
		{
			Label label = XmlLabelFactory.build("financeLevel"+i, mapAggregationLabels.get(i));
			labels.getLabel().add(label);
		}
	}
	
	public static void aggregationHeader(String lang, Labels labels, List<String> header)
	{
		for(int i=1;i<=header.size();i++)
		{
			Label label = XmlLabelFactory.build("financeLevel"+i, header.get(header.size()-i));
			labels.getLabel().add(label);
		}
	}
	
	public static <RE extends JeeslRevisionEntity<?,?,?,?,RA,?>, RA extends JeeslRevisionAttribute<?,?,RE,?,?>>
		Labels build(RE entity, String localeCode, String scope)
	{
		Labels xml = build();
		for(RA attribute : entity.getAttributes())
		{
			if(attribute.getName().containsKey(localeCode))
			{
				xml.getLabel().add(XmlLabelFactory.build(scope,attribute.getCode(),attribute.getName().get(localeCode).getLang()));
			}
		}
		return xml;
	}
	
	public static Map<String,String> toMap(Labels labels){return toMap(labels,null);}
	public static Map<String,String> toMap(Labels labels, String scope)
	{
		Map<String,String> map = new HashMap<String,String>();
		for(Label label : labels.getLabel())
		{
			if(scope==null){map.put(label.getKey(),label.getValue());}
			else if(label.getScope().equals(scope)){map.put(label.getKey(),label.getValue());}
		}
		return map;
	}
	
	public static OfxTranslationProvider toTranslationProvider(String localeCode, Labels labels)
	{
		OfxDefaultTranslationProvider tp = new OfxDefaultTranslationProvider();
		for(Label label : labels.getLabel())
		{
			tp.addTranslation(localeCode, label.getScope(), label.getKey(), label.getValue());
		}
		return tp;
	}
}