package org.jeesl.factory.ejb.system.status;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.txt.system.status.TxtStatusFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.JeeslLocaleProvider;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Description;
import net.sf.ahtutils.xml.status.Descriptions;
import net.sf.ahtutils.xml.status.Lang;
import net.sf.ahtutils.xml.status.Langs;
import net.sf.exlp.util.xml.JaxbUtil;

public class EjbDescriptionFactory<D extends JeeslDescription>
{
	final static Logger logger = LoggerFactory.getLogger(EjbDescriptionFactory.class);
	
    private final Class<D> cD;
	
    public EjbDescriptionFactory(final Class<D> cD)
    {
        this.cD = cD;
    } 
    
    public static <D extends JeeslDescription> EjbDescriptionFactory<D> factory(final Class<D> cD)
    {
        return new EjbDescriptionFactory<D>(cD);
    }
    
    public <LOC extends JeeslLocale<?,D,LOC,?>> Map<String,D> build(JeeslLocaleProvider<LOC> lp, Descriptions xDescriptions) throws JeeslConstraintViolationException
	{
		Map<String,D> map = new Hashtable<String,D>();
		if(xDescriptions!=null && !xDescriptions.getDescription().isEmpty())
		{
			for(Description xLang : xDescriptions.getDescription())
			{
				if(lp.hasLocale(xLang.getKey())) {map.put(xLang.getKey(),create(xLang));}
			}
		}
		for(String key : lp.getLocaleCodes())
		{
			if(!map.containsKey(key)) {map.put(key,build(key,""));}
		}
		return map; 
	}
	
	public D create(Description description) throws JeeslConstraintViolationException
	{
		if(!description.isSetKey()){throw new JeeslConstraintViolationException("Key not set: "+JaxbUtil.toString(description));}
		if(!description.isSetValue()){throw new JeeslConstraintViolationException("Value not set: "+JaxbUtil.toString(description));}
    		return create(description.getKey(),description.getValue());
	}
    
	public D build(String key, String value)
	{
		D d = null;
		try {
			d = create(key,value);
		} catch (JeeslConstraintViolationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d;
	}
	public D create(String key, String value) throws JeeslConstraintViolationException
	{
		if(key==null){throw new JeeslConstraintViolationException("Key not set");}
		if(value==null){throw new JeeslConstraintViolationException("Value not set");}
		D d = null;
		try
		{
			d = cD.newInstance();
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
    	d.setLang(value);
    	d.setLkey(key);
    	return d;
	}
	
	public Map<String,D> build(D description) 
	{
		Map<String,D> map = new HashMap<String,D>();
		map.put(description.getLkey(),description);
		return map;
	}
	
	public Map<String,D> create(Descriptions descriptions) throws JeeslConstraintViolationException
	{
		if(descriptions!=null && descriptions.isSetDescription()){return create(descriptions.getDescription());}
		else{return  new Hashtable<String,D>();}
	}
	
	public Map<String,D> create(List<Description> lDescriptions) throws JeeslConstraintViolationException
	{
		Map<String,D> map = new Hashtable<String,D>();
		for(Description desc : lDescriptions)
		{
			D d = create(desc);
			map.put(d.getLkey(), d);
		}
		return map;
	}
	
	public <S extends JeeslStatus<L,D,S>, L extends JeeslLang> Map<String,D> createEmpty(List<S> locales)
	{
		return createEmpty(TxtStatusFactory.toCodes(locales).toArray(new String[0]));
	}
	
	public Map<String,D> createEmpty(String[] keys)
	{
		Map<String,D> map = new Hashtable<String,D>();
		for(String key : keys)
		{
			try
			{
				map.put(key, create(key,""));
			}
			catch (JeeslConstraintViolationException e) {e.printStackTrace();}
		}
		return map;
	}
	
	public Map<String,D> clone(Map<String,D> original) 
	{
		Map<String,D> map = new HashMap<String,D>();
		for(String key : original.keySet())
		{
			try {
				map.put(key, create(key, original.get(key).getLang()));
			} catch (JeeslConstraintViolationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return map;
	}
	
	public <M extends EjbWithDescription<D>> void rmDescription(JeeslFacade fUtils, M ejb)
	{
		Map<String,D> descMap = ejb.getDescription();
		ejb.setDescription(null);
		
		try{ejb=fUtils.update(ejb);}
		catch (JeeslConstraintViolationException e) {logger.error("",e);}
		catch (JeeslLockingException e) {logger.error("",e);}
		
		if(descMap!=null)
		{
			for(D desc : descMap.values())
			{
				try {fUtils.rm(desc);}
				catch (JeeslConstraintViolationException e) {logger.error("",e);}
			}
		}
	}
	
	public <T extends EjbWithDescription<D>, S extends JeeslStatus<L,D,S>, L extends JeeslLang> T persistMissingLangs(JeeslFacade fUtils, List<S> locales, T ejb)
	{
		return persistMissingLangs(fUtils,TxtStatusFactory.toCodes(locales).toArray(new String[0]),ejb);
	}
	
	public <T extends EjbWithDescription<D>> T persistMissingLangsForCode(JeeslFacade fUtils, List<String> codes, T ejb)
	{
		String[] localeCodes = new String[codes.size()];
		for(int i=0;i<codes.size();i++)
		{
			localeCodes[i] = codes.get(i);
		}
		return persistMissingLangs(fUtils,localeCodes,ejb);
	}
	
	public <T extends EjbWithDescription<D>> T persistMissingLangs(JeeslFacade fUtils, String[] keys, T ejb)
	{
		for(String key : keys)
		{
			if(!ejb.getDescription().containsKey(key))
			{
				try
				{
					D d = fUtils.persist(create(key, ""));
					ejb.getDescription().put(key, d);
					ejb = fUtils.update(ejb);
				}
				catch (JeeslConstraintViolationException e) {e.printStackTrace();}
				catch (JeeslLockingException e) {e.printStackTrace();}
			}
		}
		return ejb;
	}
	
	public Map<String,D> persistMissingLangs(JeeslFacade fUtils, String[] keys, Map<String,D> map)
	{
		for(String key : keys)
		{
			if(!map.containsKey(key))
			{
				try
				{
					D d = fUtils.persist(create(key, ""));
					map.put(key, d);
				}
				catch (JeeslConstraintViolationException e) {e.printStackTrace();}
			}
		}
		return map;
	}
}