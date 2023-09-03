package org.jeesl.jsf.jx.util;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComponentAttribute
{
	final static Logger logger = LoggerFactory.getLogger(ComponentAttribute.class);
	
	public static boolean getBoolean(String attribute, FacesContext context, UIComponent component) throws JeeslNotFoundException
	{
		String value = get(attribute,null,context,component);
		if(value==null){throw new JeeslNotFoundException("No attribute in component: "+attribute);}
		return new Boolean(value);
	}
	
	public static <E extends Enum<E>> boolean getBoolean(E attribute, boolean defaultValue, FacesContext context, UIComponent component)
	{
		return getBoolean(attribute.toString(), defaultValue, context, component);
	}
	private static boolean getBoolean(String attribute, boolean defaultValue, FacesContext context, UIComponent component)
	{
		String value = get(attribute,null,context,component);
		if(value==null) {value=""+defaultValue;}
		return new Boolean(value);
	}
	
	public static int getInteger(String attribute, int defaultValue, FacesContext context, UIComponent component)
	{
		String value = get(attribute,null,context,component);
		if(value==null){value=""+defaultValue;}
		return new Integer(value);
	}
	public static int getInteger(String attribute, FacesContext context, UIComponent component) throws JeeslNotFoundException
	{
		String value = get(attribute,null,context,component);
		if(value==null){throw new JeeslNotFoundException("No attribute in component: "+attribute);}
		return new Integer(value);
	}
	
	public static long getLong(String attribute, boolean defaultValue, FacesContext context, UIComponent component)
	{
		String value = get(attribute,null,context,component);
		if(value==null){value=""+defaultValue;}
		return new Long(value);
	}
	
	public static long getLong(String attribute, FacesContext context, UIComponent component) throws JeeslNotFoundException
	{
		String value = get(attribute,null,context,component);
		if(value==null){throw new JeeslNotFoundException("No attribute in component: "+attribute);}
		return new Long(value);
	}
	
	public static double getDouble(String attribute, double defaultValue, FacesContext context, UIComponent component)
	{
		String value = get(attribute,null,context,component);
		if(value==null){value=""+defaultValue;}
		return new Double(value);
	}
	
	public static double getDouble(String attribute, FacesContext context, UIComponent component) throws JeeslNotFoundException
	{
		String value = get(attribute,null,context,component);
		if(value==null){throw new JeeslNotFoundException("No attribute in component: "+attribute);}
		return new Double(value);
	}
	
	public static String get(String attribute, FacesContext context, UIComponent component) throws JeeslNotFoundException
	{
		String value = get(attribute,null,context,component);
		if(value==null){throw new JeeslNotFoundException("No attribute in component: "+attribute);}
		return value;
	}
	
	public static <E extends Enum<E>> String get(E attribute, String defaultValue, FacesContext context, UIComponent component) {return get(attribute.toString(), defaultValue, context, component);}
	public static String get(String attribute, String defaultValue, FacesContext context, UIComponent component)
	{
		String value = null;
		if(component.getAttributes().containsKey(attribute))
		{
			value = component.getAttributes().get(attribute).toString();
//			logger.info("Value for "+attribute+" found in component.map: "+value);
		}
		else
		{
//			logger.info("Searching ValueExpression for "+attribute);
			ValueExpression ve = component.getValueExpression(attribute);
			if(ve!=null)
			{
				Object o = ve.getValue(context.getELContext());
				if(o!=null) {value = o.toString();}
//				logger.info("Found ValuieExpression: "+value);
			}
//			logger.info("Value is now: "+value);
		}
		if(value==null){value=defaultValue;}
		return value;
	}
	
	public static <E extends Enum<E>> Object getObject(E attribute, String defaultValue, FacesContext context, UIComponent component) {return getObject(attribute.toString(), defaultValue, context, component);}
	public static Object getObject(String attribute, Object defaultValue, FacesContext context, UIComponent component)
	{
		Object value = null;
		if(component.getAttributes().containsKey(attribute))
		{
			value = component.getAttributes().get(attribute);
//			logger.info("Value for "+attribute+" found in component.map: "+value);
		}
		else
		{
//			logger.info("Searching ValueExpression for "+attribute);
			ValueExpression ve = component.getValueExpression(attribute);
			if(ve!=null)
			{
				value=ve.getValue(context.getELContext());
//				logger.info("Found ValuieExpression: "+value);
			}
//			logger.info("Value is now: "+value);
		}
		if(value==null){value=defaultValue;}
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends EjbWithId> T getObject(Class<T> c, String attribute, FacesContext context, UIComponent component)
	{
		T t = null;
		ValueExpression ve = component.getValueExpression(attribute);
		if(ve!=null)
		{
			t = (T)ve.getValue(context.getELContext());
		}
		return t;
	}
	
	public static <E extends Enum<E>> boolean available(E attribute, FacesContext context, UIComponent component)
	{
		if(component.getAttributes().containsKey(attribute.toString()))
		{
//			logger.info(attribute+" is a Attribute");
			return true;
		} 
		else
		{
			ValueExpression ve = component.getValueExpression(attribute.toString());
			if(ve!=null)
			{
//				logger.info(attribute+" is a ValueExpression");
				return true;
			}
		}
//		logger.info(attribute+" is not available");
		return false;
	}
}