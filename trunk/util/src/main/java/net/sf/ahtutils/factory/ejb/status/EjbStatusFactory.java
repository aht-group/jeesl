package net.sf.ahtutils.factory.ejb.status;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.sf.ahtutils.exception.ejb.UtilsConstraintViolationException;
import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.xml.status.Description;
import net.sf.ahtutils.xml.status.Descriptions;
import net.sf.ahtutils.xml.status.Status;
import net.sf.exlp.util.xml.JaxbUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbStatusFactory<S extends UtilsStatus<S,L,D>, L extends UtilsLang, D extends UtilsDescription>
{
	final static Logger logger = LoggerFactory.getLogger(EjbStatusFactory.class);
	
	final Class<S> statusClass;
	final Class<D> cDescription;
    
    private EjbLangFactory<L> efLang;
    private EjbDescriptionFactory<D> efDescription;
	
    public EjbStatusFactory(final Class<S> statusClass, final Class<L> cLang, final Class<D> cDescription)
    {
        this.statusClass = statusClass;
        this.cDescription = cDescription;
        
        efLang = EjbLangFactory.createFactory(cLang);
        efDescription = EjbDescriptionFactory.createFactory(cDescription);
    } 
    
    public static <S extends UtilsStatus<S,L,D>, L extends UtilsLang, D extends UtilsDescription> EjbStatusFactory<S, L, D>
    		createFactory(final Class<S> statusClass, final Class<L> cLang, final Class<D> descriptionClass)
    {
        return new EjbStatusFactory<S, L, D>(statusClass, cLang, descriptionClass);
    }
    
	public S create(Status status) throws InstantiationException, IllegalAccessException, UtilsConstraintViolationException
	{
		if(!status.isSetLangs()){throw new UtilsConstraintViolationException("No <langs> available for "+JaxbUtil.toString(status));}
        S s = statusClass.newInstance();
        s.setCode(status.getCode());
        if(status.isSetPosition()){s.setPosition(status.getPosition());}
        else{s.setPosition(0);}
        s.setName(efLang.getLangMap(status.getLangs()));
        s.setDescription(efDescription.create(status.getDescriptions()));
        return s;
    }
	
	public S create(String code){return create(code,null);}
	public S create(String code, String[] langKeys)
	{
        S s;
		try
		{
			s = statusClass.newInstance();
			s.setCode(code);
			if(langKeys!=null){s.setName(efLang.createEmpty(langKeys));}
			if(langKeys!=null){s.setDescription(efDescription.createEmpty(langKeys));}
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
	
	public Map<String,D> getDescriptionMap(List<Description> descList) throws InstantiationException, IllegalAccessException
	{
		Map<String,D> mapDesc = new Hashtable<String,D>();
		for(Description xmlD : descList)
		{
			D d = cDescription.newInstance();
			d.setLkey(xmlD.getKey());
			d.setLang(xmlD.getValue().trim());
			mapDesc.put(d.getLkey(), d);
		}
		return mapDesc;
	}
}