package org.jeesl.factory.ejb.io.cms;

import java.util.List;

import org.jeesl.factory.ejb.util.EjbPositionFactory;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsElement;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsSection;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoCmsElementFactory <L extends JeeslLang,
								S extends JeeslIoCmsSection<L,S>,
								E extends JeeslIoCmsElement<?,S,?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoCmsElementFactory.class);
	
	private final Class<E> cElement;

	public EjbIoCmsElementFactory(final Class<E> cElement)
	{
        this.cElement = cElement;
	}
 
	public E build(S section, List<E> list)
	{
		E ejb = null;
		try
		{
			ejb = cElement.newInstance();
			ejb.setSection(section);
			EjbPositionFactory.calcNext(ejb,list);
			ejb.setVisible(true);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public void update(S src, S dst)
	{
		dst.setSection(src.getSection());
		dst.setPosition(src.getPosition());
		dst.setName(src.getName());
	}
}