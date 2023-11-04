package org.jeesl.jsf.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

import org.apache.commons.beanutils.PropertyUtils;
import org.exlp.util.io.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FacesComponent(value="org.jeesl.jsf.components.JsonPropertyEditor")
public class JsonPropertyEditor extends UIInput implements NamingContainer 
{
	final static Logger logger = LoggerFactory.getLogger(JsonPropertyEditor.class);
	
	private Map<String, Object> properties; public Map<String, Object> getProperties() {return properties;} public void setProperties(Map<String, Object> properties) {this.properties = properties;}
	private List<String> propertyNames; public List<String> getPropertyNames() {return propertyNames;} public void setPropertyNames(List<String> propertyNames) {this.propertyNames = propertyNames;}
	private Object jsonObject; public Object getJsonObject() {return jsonObject;} public void setJsonObject(Object jsonObject) {this.jsonObject = jsonObject;}
	private String value; public void setValue(String value) {this.value = value;}
	
	@Override
    public String getFamily() 
	{
        return UINamingContainer.COMPONENT_FAMILY;
    }
	
    public void init() throws IOException
    {
		try {
			Class targetClass = Class.forName(getAttributes().get("for").toString());
			if (jsonObject == null)
			{
				//logger.info("No object present - constructing new one.");
				jsonObject = (Object) targetClass.newInstance();
				//logger.info("JSON Object is of type: " +jsonObject);
				properties = new HashMap<>();
				propertyNames = new ArrayList<>();
				for (PropertyDescriptor desc : PropertyUtils.getPropertyDescriptors(jsonObject))
				{
					if (desc.getWriteMethod() != null)
					{
						Object currentValue = "";
						try {
							currentValue = desc.getReadMethod().invoke(jsonObject);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
							logger.error(ex.getMessage());
						}
						if (currentValue == null) {currentValue="";}
						properties.put(desc.getName(), currentValue);
						propertyNames.add(desc.getName());
					}
					else
					{
						logger.warn("No SETTER available for property " +desc.getName() +" - skipping");
					}
				}
			}	
			else
			{
			//	logger.info("State restored.");
			}
			//logger.info("Properties has " +properties.size() +" items: " +properties.toString());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
			logger.error(ex.getMessage());
		}
	}
	
    @Override
    public Object getSubmittedValue() 
	{
        return value;
    }

    @Override
    protected Object getConvertedValue(FacesContext context, Object submittedValue) 
	{
		return submittedValue;
    }

	public void process(javax.faces.event.AjaxBehaviorEvent event) throws JsonProcessingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		for (String propertyName : properties.keySet())
		{
			Object argument = null;
			try {
				argument = properties.get(propertyName);
				if (argument!=null)
				{
					PropertyUtils.setProperty(jsonObject, propertyName, argument);
				}
			}
			catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
			{
				logger.error(e.getStackTrace().toString());
				logger.error(e.getMessage());
			}
		}
		value = JsonUtil.toString(jsonObject);
		this.getAttributes().put("value", value);
	}	
	
	@Override
	public Object saveState(FacesContext context)
	{
		Object[] rtrn = new Object[3];
		if (this.isRendered())
		{
			rtrn[0] = jsonObject;
			rtrn[1] = value;
			rtrn[2] = properties;
		}
		if (properties==null)
		{
			logger.error("NULL in properties");
		}
		return rtrn;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void restoreState(FacesContext context, Object state)
	{
		if(this.isRendered())
		{
			Object[] storedState = (Object[]) state;
		    try
			{
				jsonObject       = (Object)					storedState[0];
				value			 = (String)					storedState[1];
				properties		 = (Map<String, Object>)	storedState[2];
			}
			catch (Exception e)
			{
				logger.error("Exception when restoring: " +e.getMessage());
			}
			//logger.info("RESTORE Properties has " +properties.size() +" items: " +properties.toString());
		}
	}
}