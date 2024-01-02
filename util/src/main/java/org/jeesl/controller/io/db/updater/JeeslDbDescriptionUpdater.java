package org.jeesl.controller.io.db.updater;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.ejb.system.status.EjbDescriptionFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.model.xml.io.locale.status.Description;
import org.jeesl.model.xml.io.locale.status.Descriptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslDbDescriptionUpdater<C extends EjbWithDescription<D>, D extends JeeslDescription>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslDbDescriptionUpdater.class);
	
	final Class<C> cEjb;
	final Class<D> cD;
	
	private EjbDescriptionFactory<D> efDescription;

	public JeeslDbDescriptionUpdater(final Class<C> cEjb, final Class<D> cD)
    {
        this.cEjb = cEjb;
        this.cD = cD;
        
        efDescription = EjbDescriptionFactory.factory(cD);
    } 
	
	public static <C extends EjbWithDescription<D>, D extends JeeslDescription> JeeslDbDescriptionUpdater<C,D> factory(final Class<C> cEjb, final Class<D> cD)
	{
		return new JeeslDbDescriptionUpdater<C,D>(cEjb,cD);
	}

	public C handle(JeeslFacade fUtils,C ejb, Descriptions descriptions) throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(ejb.getDescription()==null)
		{
			ejb.setDescription(efDescription.create(descriptions));
		}
		else
		{
			ejb = remove(fUtils,ejb,descriptions);
			ejb = add(fUtils,ejb,descriptions);
			ejb = update(fUtils,ejb,descriptions);
		}
		return ejb;
	}
	
	public C handle(JeeslFacade fUtils, C ejb, String[] localeCodes)
	{
		return efDescription.persistMissingLangs(fUtils, localeCodes, ejb);
	}
	
	private C remove(JeeslFacade fUtils,C ejb, Descriptions descriptions) throws JeeslConstraintViolationException
	{
		Set<String> actualInXml = new HashSet<String>();
		List<String> obsoleteInEjb = new ArrayList<String>();
		
		for(Description xDescription : descriptions.getDescription()){actualInXml.add(xDescription.getKey());}
		for(String key : ejb.getDescription().keySet()){if(!actualInXml.contains(key)){obsoleteInEjb.add(key);}}
		
		int before = 0;
		int after = 0;
		if(ejb.getDescription()!=null){before = ejb.getDescription().size();}
		
		for(String key : obsoleteInEjb)
		{
			D eDescription = ejb.getDescription().get(key);
			ejb.getDescription().remove(key);
			fUtils.rm(eDescription);
		}
		if(ejb.getDescription()!=null){after = ejb.getDescription().size();}
		logger.debug("Removed "+obsoleteInEjb.size()+" Before:"+before+" After:"+after);
		
		return ejb;
	}
	
	private C add(JeeslFacade fUtils,C ejb, Descriptions descriptions) throws JeeslConstraintViolationException
	{
		Set<String> actualInXml = new HashSet<String>();
		for(Description xDescription : descriptions.getDescription()){actualInXml.add(xDescription.getKey());}
		
		List<String> list = new ArrayList<String>(actualInXml);
		
		String[] keys = new String[list.size()];
		for(int i=0;i<list.size();i++){keys[i] = list.get(i);}
		
		int before = 0;
		int after = 0;
		if(ejb.getDescription()!=null){before = ejb.getDescription().size();}
		
		ejb = efDescription.persistMissingLangs(fUtils, keys, ejb);
		
		if(ejb.getDescription()!=null){after = ejb.getDescription().size();}
		logger.debug("Added "+keys.length+" Before:"+before+" After:"+after);
		return ejb;
	}
	
	private C update(JeeslFacade fUtils,C ejb, Descriptions descriptions) throws JeeslConstraintViolationException, JeeslLockingException
	{
		for(Description xDescription : descriptions.getDescription())
		{
			if(ejb.getDescription()==null){logger.warn("ejb.getDescription()==null "+ejb.toString());}
			else
			{
				D eDescription = ejb.getDescription().get(xDescription.getKey());
				eDescription.setLang(xDescription.getValue());
				eDescription = fUtils.save(eDescription);
				ejb.getDescription().put(eDescription.getLkey(), eDescription);
			}
		}
		return ejb;
	}
}