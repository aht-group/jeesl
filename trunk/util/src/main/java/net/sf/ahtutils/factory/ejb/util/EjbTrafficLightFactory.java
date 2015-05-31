package net.sf.ahtutils.factory.ejb.util;

import net.sf.ahtutils.exception.ejb.UtilsConstraintViolationException;
import net.sf.ahtutils.factory.ejb.status.EjbDescriptionFactory;
import net.sf.ahtutils.factory.ejb.status.EjbLangFactory;
import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.interfaces.model.util.UtilsTrafficLight;
import net.sf.ahtutils.xml.utils.TrafficLight;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbTrafficLightFactory<L extends UtilsLang,D extends UtilsDescription,SCOPE extends UtilsStatus<SCOPE,L,D>,LIGHT extends UtilsTrafficLight<L,D,SCOPE>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbTrafficLightFactory.class);
	
	final Class<L> cLang;
	final Class<D> cDescription;
	final Class<LIGHT> cLight;
	
    private EjbLangFactory<L> efLang;
    private EjbDescriptionFactory<D> efDescription;
    
	public EjbTrafficLightFactory(final Class<L> cLang,final Class<D> cDescription,final Class<LIGHT> cLight)
	{       
		this.cLang = cLang;
		this.cDescription = cDescription;
        this.cLight = cLight;
        
        efLang = EjbLangFactory.createFactory(cLang);
        efDescription = EjbDescriptionFactory.createFactory(cDescription);
	}
	
	public static <L extends UtilsLang,D extends UtilsDescription,SCOPE extends UtilsStatus<SCOPE,L,D>,LIGHT extends UtilsTrafficLight<L,D,SCOPE>>
		EjbTrafficLightFactory<L,D,SCOPE,LIGHT> factory(final Class<L> cLang,final Class<D> cDescription,final Class<LIGHT> cLight)
	{
		return new EjbTrafficLightFactory<L,D,SCOPE,LIGHT>(cLang,cDescription,cLight);
	}
    
	public LIGHT build(SCOPE scope,TrafficLight light)
	{
		LIGHT ejb = null;
		try
		{
			ejb = cLight.newInstance();
			ejb.setScope(scope);
			ejb.setThreshold(light.getThreshold());
			
	        ejb.setColorBackground(light.getColorBackground());
	        ejb.setColorText(light.getColorText());
	        
	        ejb.setName(efLang.getLangMap(light.getLangs()));
	        ejb.setDescription(efDescription.create(light.getDescriptions()));
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		catch (UtilsConstraintViolationException e) {e.printStackTrace();}
        
        return ejb;
    }
	
	public LIGHT build(String[] langKeys,SCOPE scope)
	{
		LIGHT ejb;
		try
		{
			ejb = cLight.newInstance();
			ejb.setScope(scope);
			ejb.setColorText("FFFFFF");
			if(langKeys!=null){ejb.setName(efLang.createEmpty(langKeys));}
			if(langKeys!=null){ejb.setDescription(efDescription.createEmpty(langKeys));}
		}
		catch (InstantiationException e) {throw new RuntimeException(e);}
		catch (IllegalAccessException e) {throw new RuntimeException(e);}
		
		return ejb;
	}

}