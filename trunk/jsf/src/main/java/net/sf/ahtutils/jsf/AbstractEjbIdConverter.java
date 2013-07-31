package net.sf.ahtutils.jsf;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import net.sf.ahtutils.model.interfaces.with.EjbWithId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractEjbIdConverter <I extends EjbWithId> implements Converter
{    
	final static Logger logger = LoggerFactory.getLogger(AbstractEjbIdConverter.class);
	
	private Class<I> clEjb;

	public AbstractEjbIdConverter(final Class<I> clEjb)
	{
		this.clEjb=clEjb;
	}
	
	public AbstractEjbIdConverter()
	{

	}
	public void setClEjb(Class<I> clEjb) {this.clEjb = clEjb;}	
	
	
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue)
    {
    	submittedValue = submittedValue.trim();
//    	logger.warn(clEjb.getSimpleName()+" getAsObject submittedValue: "+submittedValue);
        if (submittedValue.equals("")) {return null;}
        else
        {  
            try
            {   
                long id = Long.valueOf(submittedValue);
                I ejb = clEjb.newInstance();
                ejb.setId(id);
//                logger.warn(clEjb.getSimpleName()+" getAsObject return "+ejb);
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
  
    public String getAsString(FacesContext facesContext, UIComponent component, Object value)
    { 
//  	logger.warn(clEjb.getSimpleName()+" value: "+value);
        if (value == null || value.equals(""))
        {
        	logger.warn("Returning NULL");
        	return "";
        }
        else
        {
        	EjbWithId ejb = (EjbWithId)value;
//        	logger.warn(clEjb.getSimpleName()+" return: "+ejb.getId());
        	return ""+ejb.getId(); 
        }  
    }  
}  