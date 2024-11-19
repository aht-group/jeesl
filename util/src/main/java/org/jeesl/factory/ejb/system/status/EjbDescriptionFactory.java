package org.jeesl.factory.ejb.system.status;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.exlp.util.jx.JaxbUtil;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.txt.system.status.TxtStatusFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleManager;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.model.xml.io.locale.status.Description;
import org.jeesl.model.xml.io.locale.status.Descriptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbDescriptionFactory<D extends JeeslDescription> implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(EjbDescriptionFactory.class);
	
    private final Class<D> cD;
	
    public static <D extends JeeslDescription> EjbDescriptionFactory<D> factory(final Class<D> cD) {return new EjbDescriptionFactory<D>(cD);}
    public static <D extends JeeslDescription> EjbDescriptionFactory<D> instance(final Class<D> cD) {return new EjbDescriptionFactory<>(cD);}
    private EjbDescriptionFactory(final Class<D> cD)
    {
        this.cD = cD;
    } 
    
    public <LOC extends JeeslLocale<?,D,LOC,?>> Map<String,D> build(JeeslLocaleManager<LOC> lp, Descriptions xDescriptions) throws JeeslConstraintViolationException
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
		if(Objects.isNull(description.getKey())) {throw new JeeslConstraintViolationException("Key not set: "+JaxbUtil.toString(description));}
		if(Objects.isNull(description.getValue())) {throw new JeeslConstraintViolationException("Value not set: "+JaxbUtil.toString(description));}
    		return create(description.getKey(),description.getValue());
	}
    
	public D build(String key, String value)
	{
		D d = null;
		try {d = create(key,value);}
		catch (JeeslConstraintViolationException e) {e.printStackTrace();}
		return d;
	}
	public D create(String key, String value) throws JeeslConstraintViolationException
	{
		if(key==null){throw new JeeslConstraintViolationException("Key not set");}
		if(value==null){throw new JeeslConstraintViolationException("Value not set");}
		D d = null;
		try
		{
			d = cD.getDeclaredConstructor().newInstance();
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		catch (IllegalArgumentException e) {e.printStackTrace();}
		catch (InvocationTargetException e) {e.printStackTrace();}
		catch (NoSuchMethodException e) {e.printStackTrace();}
		catch (SecurityException e) {e.printStackTrace();}
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
		if(descriptions!=null && Objects.nonNull(descriptions.getDescription())) {return create(descriptions.getDescription());}
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
	
	public <LOC extends JeeslLocale<?,D,LOC,?>> Map<String,D> build(JeeslLocaleProvider<LOC> lp)
	{
		Map<String,D> map = new HashMap<String,D>();
		logger.info("build Map<String,L> for "+lp.getLocales().size());
		for(LOC loc : lp.getLocales())
		{
			try {map.put(loc.getCode(), this.create(loc.getCode(),""));}
			catch (JeeslConstraintViolationException e) {e.printStackTrace();}
		}
		return map;
	}
	
	public <LOC extends JeeslLocale<?,D,LOC,?>> Map<String,D> buildEmpty(List<LOC> locales)
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
			try
			{
				map.put(key, create(key, original.get(key).getLang()));
			}
			catch (JeeslConstraintViolationException e) {e.printStackTrace();}
		}
		return map;
	}
	
	public <L extends JeeslLang> Map<String,D> convert(Map<String,L> langs)
	{
		Map<String,D> descriptions = new HashMap<>(); 
		for(String key : langs.keySet())
		{
			descriptions.put(key,this.build(key,langs.get(key).getLang()));
		}
		return descriptions;
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
	
	public <T extends EjbWithDescription<D>, LOC extends JeeslLocale<?,D,LOC,?>> T persistMissingLangs(JeeslFacade fUtils, JeeslLocaleProvider<LOC> lp, T ejb)
	{
		if(Objects.isNull(ejb.getDescription())) {ejb.setDescription(new HashMap<>());}
		try
		{
			boolean hasChanges = false;
			for(LOC locale : lp.getLocales())
			{
				if(!ejb.getDescription().containsKey(locale.getCode()))
				{
					
					D d = create(locale.getCode(), "");
					if(EjbIdFactory.isSaved(ejb)) {d = fUtils.persist(d);}
					ejb.getDescription().put(locale.getCode(), d);
					hasChanges=true;
				}
			}
			if(EjbIdFactory.isSaved(ejb) && hasChanges)
			{
				ejb = fUtils.update(ejb);
			}
		}
		catch (JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
		return ejb;
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
		if(Objects.isNull(ejb.getDescription())) {ejb.setDescription(new HashMap<>());}
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
	
	public <LOC extends JeeslLocale<?,D,LOC,?>> Map<String,D> persistMissingLangs(JeeslFacade fUtils, JeeslLocaleProvider<LOC> lp, Map<String,D> map)
	{
		for(LOC loc : lp.getLocales())
		{
			if(!map.containsKey(loc.getCode()))
			{
				try
				{
					D d = fUtils.persist(create(loc.getCode(), ""));
					map.put(loc.getCode(), d);
				}
				catch (JeeslConstraintViolationException e) {e.printStackTrace();}
			}
		}
		return map;
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
	
	public <W extends EjbWithDescription<D>> void update(W ejb, Descriptions descriptions)
	{
		if(Objects.nonNull(descriptions))
		{
			for(Description xml : descriptions.getDescription())
			{
				if(ejb.getDescription().containsKey(xml.getKey()))
				{
					ejb.getDescription().get(xml.getKey()).setLang(xml.getValue());
				}
			}
		}
	}
}