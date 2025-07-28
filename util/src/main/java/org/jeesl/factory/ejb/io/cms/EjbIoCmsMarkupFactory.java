package org.jeesl.factory.ejb.io.cms;

import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoCmsMarkupFactory<M extends JeeslIoMarkup<MT>,
									MT extends JeeslIoMarkupType<?,?,MT,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoCmsMarkupFactory.class);
	
	private final static String unicodeStx = "\u0002";
	
	private final Class<M> cMarkup;
	
	public static <M extends JeeslIoMarkup<MT>, MT extends JeeslIoMarkupType<?,?,MT,?>>
			 EjbIoCmsMarkupFactory<M,MT> instance(final Class<M> cMarkup)
	 {
		return new EjbIoCmsMarkupFactory<>(cMarkup);
	 }
	
    public EjbIoCmsMarkupFactory(final Class<M> cMarkup)
    {
        this.cMarkup = cMarkup;
    } 
 
    public M build(MT type)
    {
    	M markup = null;
		try
		{
			markup = cMarkup.getDeclaredConstructor().newInstance();
	    	markup.setLkey(JeeslLocale.none);
	    	markup.setType(type);
	    	markup.setContent("");
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		catch (IllegalArgumentException e) {e.printStackTrace();}
		catch (InvocationTargetException e) {e.printStackTrace();}
		catch (NoSuchMethodException e) {e.printStackTrace();}
		catch (SecurityException e) {e.printStackTrace();}

    	return markup;
    }
    
    public static <M extends JeeslIoMarkup<?>> boolean stxRemove(M m)
    {
    	if(Objects.isNull(m.getContent())) {return false;}
    	else if (m.getContent().contains(unicodeStx))
		{
            m.setContent(m.getContent().replaceAll(unicodeStx, ""));
            return true;
        }
    	
    	return false;
    }
}