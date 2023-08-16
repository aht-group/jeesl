package org.jeesl.jsf.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractEjbCodeConverter <I extends EjbWithCode> implements Converter
{    
	final static Logger logger = LoggerFactory.getLogger(AbstractEjbCodeConverter.class);
	
	private Class<I> clEjb;
	
	public AbstractEjbCodeConverter(final Class<I> clEjb)
	{
		this.clEjb=clEjb;
	}
	
	private AbstractEjbCodeConverter() {}
	public void setClEjb(Class<I> clEjb) {this.clEjb = clEjb;}	
	
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue)
    {
    	submittedValue = submittedValue.trim();
//    	logger.warn(clEjb.getSimpleName()+" submittedValue: "+submittedValue);
        if (submittedValue.equals("")) {return null;}
        else
        {  
            try
            {   
            	String[] token = submittedValue.split("-");
                long id = Long.valueOf(token[0]);
                String code = token[1];
//                logger.warn("CREATING WITH CODE: "+id+"-"+code);
                I ejb = clEjb.newInstance();
                ejb.setId(id);
                ejb.setCode(code);
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
//        	logger.warn("Returning NULL");
        	return "";
        }
        else
        {
        	EjbWithCode ejb = (EjbWithCode)value;
//        	logger.warn(clEjb.getSimpleName()+" return: "+ejb.getId());
        	return ejb.getId()+"-"+ejb.getCode(); 
        }  
    }  
}  