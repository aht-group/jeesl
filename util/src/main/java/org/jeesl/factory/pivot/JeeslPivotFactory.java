package org.jeesl.factory.pivot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathNotFoundException;
import org.jeesl.interfaces.controller.handler.JeeslTranslationProvider;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.metachart.factory.json.pivot.McPivotFactory;
import org.metachart.model.json.pivot.PivotField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslPivotFactory<LOC extends JeeslLocale<?,?,LOC,?>, RE extends JeeslRevisionEntity<?,?,?,?,?,?>> extends McPivotFactory
{
	final static Logger logger = LoggerFactory.getLogger(JeeslPivotFactory.class);
	
	private final JeeslTranslationProvider<LOC> labelResolver;
	
	private final String localeCode;
	
	private final Map<Class<?>,String> mapFieldId;
	
	public JeeslPivotFactory(String localeCode, JeeslTranslationProvider<LOC> labelResolver)
	{
		this.localeCode=localeCode;
		this.labelResolver=labelResolver;
		
		mapFieldId = new HashMap<Class<?>,String>();
		this.clear();
	}
	
	public void clear()
	{
		super.clear();
		if(mapFieldId!=null) {mapFieldId.clear();}
	}
	
	public <E extends Enum<E>> void addXPathField(Class<?> c, Collection<?> collection, E code)
	{
		PivotField json = new PivotField();
		json.setMap(new HashMap<Long,String>());
		json.setId("id"+(container.getFieldList().size()+1));
		json.setLabel(labelResolver.tlEntity(localeCode,c));		
		
		String xpath = labelResolver.xpAttribute(localeCode,c,code);
		for(Object o : collection)
		{
			EjbWithId ejb = (EjbWithId)o;
//			logger.info(o.getClass().getSimpleName()+" id:"+ejb.getId()+" xpath:"+xpath);
			
			StringBuffer sb = new StringBuffer();
			JXPathContext context = JXPathContext.newContext(ejb);
			try
			{
				sb.append(context.getValue(xpath).toString());
			}
			catch (JXPathNotFoundException e)
			{
				logger.warn("No Value for "+xpath+" in "+o.toString());
			}
			json.getMap().put(ejb.getId(),sb.toString());
		}
		
		container.getFieldList().add(json);
		mapFieldId.put(c, json.getId());
	}
	
	public <E extends Enum<E>> void addStatusField(Class<?> c, Collection<?> collection)
	{
		logger.info(c.getSimpleName()+" size"+collection.size());
		
		PivotField json = new PivotField();
		json.setMap(new HashMap<Long,String>());
		json.setId("id"+(container.getFieldList().size()+1));
		json.setLabel(labelResolver.tlEntity(localeCode,c));		
		
		String xpath = "name[@name='"+localeCode+"']/lang";
//		xpath = "name/de/lang";
		for(Object o : collection)
		{
			EjbWithId ejb = (EjbWithId)o;
			JXPathContext context = JXPathContext.newContext(o);	
			json.getMap().put(ejb.getId(),context.getValue(xpath).toString());
		}
		
		container.getFieldList().add(json);
		mapFieldId.put(c, json.getId());
	}
	
	public void rows(Class<?>... rows)
	{
		for(Class<?> c : rows)
		{
			container.getFields().getRows().add(mapFieldId.get(c));
		}
	}
	
	public void columns(Class<?>... columns)
	{
		for(Class<?> c : columns)
		{
			container.getFields().getColumns().add(mapFieldId.get(c));
		}
	}
}