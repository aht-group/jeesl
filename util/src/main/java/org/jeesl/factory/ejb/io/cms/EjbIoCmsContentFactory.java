package org.jeesl.factory.ejb.io.cms;

import org.jeesl.interfaces.model.io.cms.JeeslIoCmsContent;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsElement;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoCmsContentFactory<LOC extends JeeslStatus<?,?,LOC>,
									E extends JeeslIoCmsElement<?,?,?,?,C,?>,
									C extends JeeslIoCmsContent<?,E,MT>,
									MT extends JeeslIoMarkupType<?,?,MT,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoCmsContentFactory.class);
	
    private final Class<C> cC;
	
    public EjbIoCmsContentFactory(final Class<C> cC)
    {
        this.cC = cC;
    }
	
	public C build(E element, LOC locale, String value, MT markup)
	{
		C c = null;
		try
		{
			c = cC.newInstance();
			c.setElement(element);
			c.setLkey(locale.getCode());
		 	c.setLang(value);
		 	c.setMarkup(markup);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
   
		return c;
	}
}