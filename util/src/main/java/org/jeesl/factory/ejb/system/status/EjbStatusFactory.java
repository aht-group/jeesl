package org.jeesl.factory.ejb.system.status;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Description;
import net.sf.ahtutils.xml.status.Descriptions;
import net.sf.ahtutils.xml.status.Lang;
import net.sf.ahtutils.xml.status.Langs;
import net.sf.ahtutils.xml.status.Status;
import net.sf.exlp.util.xml.JaxbUtil;

public class EjbStatusFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbStatusFactory.class);
	
	private final Class<D> cD;
	private final Class<S> cStatus;
	private List<String> localeCodes;

    private final EjbLangFactory<L> efLang;
    private final EjbDescriptionFactory<D> efDescription;
	
    public static <S extends JeeslStatus<L,D,S>, L extends JeeslLang, D extends JeeslDescription> EjbStatusFactory<L,D,S>
		instance(final Class<S> cStatus, final Class<L> cL, final Class<D> cD, List<String> localeCodes)
	{
		return new EjbStatusFactory<L,D,S>(cStatus,cL,cD,localeCodes);
	}
    public static <S extends JeeslStatus<L,D,S>, L extends JeeslLang, D extends JeeslDescription> EjbStatusFactory<L,D,S>
		instance(final Class<S> cStatus, final Class<L> cLang, final Class<D> descriptionClass)
	{
    	return new EjbStatusFactory<L,D,S>(cStatus, cLang, descriptionClass);
	}
    
    
    public EjbStatusFactory(final Class<S> cStatus, final Class<L> cL, final Class<D> cD)
    {
        this.cStatus = cStatus;
        this.cD = cD;
        
        efLang = EjbLangFactory.instance(cL);
        efDescription = EjbDescriptionFactory.factory(cD);
    }
    
    public EjbStatusFactory(final Class<S> cStatus, final Class<L> cL, final Class<D> cD, List<String> localeCodes)
    {
    	this.localeCodes=localeCodes;
        this.cStatus = cStatus;
        this.cD = cD;
        
        efLang = EjbLangFactory.instance(cL);
        efDescription = EjbDescriptionFactory.factory(cD);
    }
    
    public <E extends Enum<E>> S build(E code){return create(code.toString());}
    
	public S create(Status status) throws JeeslConstraintViolationException
	{
		if(Objects.isNull(status.getLangs())) {throw new JeeslConstraintViolationException("No <langs> available for "+JaxbUtil.toString(status));}
        S s=null;
		try
		{
			s = cStatus.newInstance();
			s.setCode(status.getCode());
			if(status.isSetPosition()){s.setPosition(status.getPosition());}
			else{s.setPosition(0);}
			
			Langs langs = XmlLangsFactory.build();
			if(localeCodes==null) {langs.getLang().addAll(status.getLangs().getLang());}
			else
			{
				for(Lang l : status.getLangs().getLang())
				{
					if(localeCodes.contains(l.getKey())) {langs.getLang().add(l);}
				}
			}
			
			Descriptions descriptions = XmlDescriptionsFactory.build();
			if(Objects.nonNull(status.getDescriptions()))
			{
				if(localeCodes==null) {descriptions.getDescription().addAll(status.getDescriptions().getDescription());}
				else
				{
					for(Description d : status.getDescriptions().getDescription())
					{
						if(localeCodes.contains(d.getKey())) {descriptions.getDescription().add(d);}
					}
				}
			}
			
			s.setName(efLang.getLangMap(langs));
			s.setDescription(efDescription.create(descriptions));
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
        return s;
    }
	
	public S create(String code){return create(code,null);}
	public S create(String code, String[] langKeys)
	{
        S s;
		try
		{
			s = cStatus.newInstance();
			s.setCode(code);
			if(langKeys!=null){s.setName(efLang.createEmpty(langKeys));}
			if(langKeys!=null){s.setDescription(efDescription.createEmpty(langKeys));}
		}
		catch (InstantiationException e) {throw new RuntimeException(e);}
		catch (IllegalAccessException e) {throw new RuntimeException(e);}
        
        return s;
    }
	public <LOC extends JeeslLocale<L,D,LOC,?>> S build(String code, List<LOC> locales)
	{
		S s;
		try
		{
			s = cStatus.newInstance();
			s.setCode(code);
			s.setName(efLang.buildEmpty(locales));
			s.setDescription(efDescription.buildEmpty(locales));
		}
		catch (InstantiationException e) {throw new RuntimeException(e);}
		catch (IllegalAccessException e) {throw new RuntimeException(e);}
        
        return s;
	}
	
	
	public S id(int id) {return id((long)id);}
	public S id(long id)
	{
        S s;
		try
		{
			s = cStatus.newInstance();
			s.setId(id);
		}
		catch (InstantiationException e) {throw new RuntimeException(e);}
		catch (IllegalAccessException e) {throw new RuntimeException(e);}
        
        return s;
    }
	
	public Map<String,D> getDescriptionMap(Descriptions desciptions) throws InstantiationException, IllegalAccessException
	{
		if(desciptions!=null && desciptions.isSetDescription())
		{
			return getDescriptionMap(desciptions.getDescription());
		}
		else
		{
			return new Hashtable<String,D>();
		}
	}
	
	private Map<String,D> getDescriptionMap(List<Description> descList) throws InstantiationException, IllegalAccessException
	{
		Map<String,D> mapDesc = new Hashtable<String,D>();
		for(Description xmlD : descList)
		{
			D d = cD.newInstance();
			d.setLkey(xmlD.getKey());
			d.setLang(xmlD.getValue().trim());
			mapDesc.put(d.getLkey(), d);
		}
		return mapDesc;
	}
	
	public static <S extends JeeslStatus<L,D,S>, L extends JeeslLang, D extends JeeslDescription>
		S build(final Class<S> cStatus, final Class<L> cLang, final Class<D> descriptionClass, String[] localeCodes, long id, String code, String name)
	{
		EjbStatusFactory<L,D,S> f = EjbStatusFactory.instance(cStatus, cLang, descriptionClass);
		S status = f.create(code, localeCodes);
		status.setId(id);
		for(String localeCode : localeCodes)
		{
			status.getName().get(localeCode).setLang(name);
		}
		return status;
	}
	
	@SuppressWarnings("unchecked")
	public List<S> toList(List<EjbWithId> years)
	{
		List<S> list = new ArrayList<S>();
		for(EjbWithId year : years) {list.add((S)year);}
		return list;
	}
}