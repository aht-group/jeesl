package org.jeesl.factory.ejb.system.util;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.property.JeeslProperty;
import org.jeesl.model.xml.system.util.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbPropertyFactory<L extends JeeslLang, D extends JeeslDescription,
								C extends JeeslStatus<L,D,C>,
								P extends JeeslProperty<L,D,C,P>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbPropertyFactory.class);
	
	final Class<P> cProperty;
    
	public EjbPropertyFactory(final Class<P> cProperty)
	{       
        this.cProperty = cProperty;
	}
	
	public static <L extends JeeslLang, D extends JeeslDescription,
					C extends JeeslStatus<L,D,C>,
					P extends JeeslProperty<L,D,C,P>>
			EjbPropertyFactory<L,D,C,P> factory(final Class<P> cProperty)
	{
		return new EjbPropertyFactory<L,D,C,P>(cProperty);
	}
    
	public P build(Property property)
	{
		return build(property.getKey(),property.getValue(),property.isFrozen());
    }
	
	public P build(String code, String value){return build(code,value,false);}
	public P build(String code, String value, boolean frozen)
	{
		P ejb = null;
		try
		{
			ejb = cProperty.newInstance();
			ejb.setKey(code);
			ejb.setValue(value);
			ejb.setFrozen(frozen);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}