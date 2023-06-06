package org.jeesl.factory.ejb.io.attribute;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jeesl.interfaces.model.module.attribute.JeeslAttributeContainer;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.module.attribute.JeeslWithAttributeContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbAttributeContainerFactory<SET extends JeeslAttributeSet<?,?,?,?,?>,
											CONTAINER extends JeeslAttributeContainer<SET,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbAttributeContainerFactory.class);
	
	private final Class<CONTAINER> cContainer;
    
	public EjbAttributeContainerFactory(final Class<CONTAINER> cContainer)
	{       
        this.cContainer = cContainer;
	}
    
	public CONTAINER build(SET set)
	{
		CONTAINER ejb = null;
		try
		{
			ejb = cContainer.newInstance();
			ejb.setSet(set);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public <W extends JeeslWithAttributeContainer<CONTAINER>> Map<W,CONTAINER> toMapContainer(Collection<W> list)
	{
		Map<W,CONTAINER> result = new HashMap<W,CONTAINER>();
		for(W w : list) {result.put(w,w.getAttributeContainer());}
		return result;
	}
}