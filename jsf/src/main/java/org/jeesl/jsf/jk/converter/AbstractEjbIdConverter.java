package org.jeesl.jsf.jk.converter;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractEjbIdConverter <T extends EjbWithId> implements Converter<T>
{    
	final static Logger logger = LoggerFactory.getLogger(AbstractEjbIdConverter.class);
	
	private Class<T> clEjb;
	private static boolean jeeslDebug = false;

	public AbstractEjbIdConverter(final Class<T> clEjb)
	{
		this.clEjb=clEjb;
	}
	
	public AbstractEjbIdConverter(){}
	public void setClEjb(Class<T> clEjb) {this.clEjb = clEjb;}	
	
    @Override public T getAsObject(FacesContext facesContext, UIComponent component, String submittedValue)
    {
    	submittedValue = submittedValue.trim();
    	if(jeeslDebug) {logger.warn(clEjb.getSimpleName()+" getAsObject submittedValue: "+submittedValue);}
        if (submittedValue.equals("")) {return null;}
        else
        {  
            try
            {   
                long id = Long.valueOf(submittedValue);
                T ejb = clEjb.newInstance();
                ejb.setId(id);
                if(jeeslDebug) {logger.warn(clEjb.getSimpleName()+" getAsObject return "+ejb.toString());}
                return ejb;
            }
            catch(NumberFormatException e)
            {
	            	String errMsg = "NumberFormatException for "+clEjb.getSimpleName()+", not a valid id (submitted: "+submittedValue+")";
	            	logger.error(errMsg);
	            	throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", errMsg));
            }
            catch (InstantiationException e)
            {
	            	String errMsg = "InstantiationException for "+clEjb.getSimpleName()+": "+e.getMessage()+" (submitted: "+submittedValue+")";
	            	logger.error(errMsg);
	            	throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", errMsg));
            }
            catch (IllegalAccessException e)
            {
	            	String errMsg = "IllegalAccessException for "+clEjb.getSimpleName()+": "+e.getMessage()+" (submitted: "+submittedValue+")";
	            	logger.error(errMsg);
	            	throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", errMsg));
            }
        }
    }  
  
    @Override public String getAsString(FacesContext facesContext, UIComponent component, T value)
    { 
		if(jeeslDebug) {logger.warn(clEjb.getSimpleName()+" value: "+value);}
        if (value == null || value.equals(""))
        {
	        	logger.warn("Returning NULL");
	        	return "";
        }
        else
        {
        		EjbWithId ejb = (EjbWithId)value;
        		if(jeeslDebug) {logger.warn(clEjb.getSimpleName()+" return: "+ejb.getId());}
        		return ""+ejb.getId(); 
        }  
    }  
}  