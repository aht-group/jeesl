package org.jeesl.controller.io.db.updater;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslDbCodeEjbUpdater<C extends EjbWithCode>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslDbCodeEjbUpdater.class);
	
	final Class<C> codeClass;
	
	private Map<String,C> ejbInDb;

	public JeeslDbCodeEjbUpdater(final Class<C> codeClass)
    {
        this.codeClass = codeClass;
        ejbInDb = new Hashtable<String,C>();
    } 
	
	public static <C extends EjbWithCode> JeeslDbCodeEjbUpdater<C> instance(final Class<C> codeClass)
	{
		return new JeeslDbCodeEjbUpdater<C>(codeClass);
	}
	
	public void clear() {ejbInDb.clear();}
	
	public void dbEjbs(List<C> list)
	{
		for(C c : list){ejbInDb.put(c.getCode(), c);}
	}
	
	public void dbEjbs(JeeslFacade fUtils)
	{
		for(C c : fUtils.all(codeClass)){ejbInDb.put(c.getCode(), c);}
	}
	
	public void handled(String code)
	{
		if(ejbInDb.containsKey(code))
		{
			ejbInDb.remove(code);
		}
	}
	
	public void handled(C ejb)
	{
		if(ejbInDb.containsKey(ejb.getCode()))
		{
			ejbInDb.remove(ejb.getCode());
		}
	}
	
	public List<C> getEjbForRemove()
	{
		List<C> result = new ArrayList<C>();
		for(String key : ejbInDb.keySet())
		{
			result.add(ejbInDb.get(key));
		}
		return result;
	}
	
	public void remove(JeeslFacade fUtils)
	{
		if(!getEjbForRemove().isEmpty())
		{
			List<String> codes = new ArrayList<String>();
			for(C pc : getEjbForRemove()){codes.add(pc.getCode());}
			
			StringBuffer sb = new StringBuffer();
			sb.append("Removing "+getEjbForRemove().size()+" from "+codeClass.getSimpleName());
			sb.append(" (").append(StringUtils.join(codes, ",")).append(")");

			logger.info(sb.toString());
			for(C pc : getEjbForRemove())
			{
				if(pc instanceof EjbRemoveable)
				{
					try {fUtils.rm((EjbRemoveable)pc);}
					catch (JeeslConstraintViolationException e) {e.printStackTrace();}
				}
				else
				{
					logger.warn(pc.getClass().getSimpleName()+" does not implement "+EjbRemoveable.class.getSimpleName());
				}
			}
		}	
	}
}