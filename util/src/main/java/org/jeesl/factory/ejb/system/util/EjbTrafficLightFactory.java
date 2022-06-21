package org.jeesl.factory.ejb.system.util;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.factory.ejb.system.status.EjbDescriptionFactory;
import org.jeesl.factory.ejb.system.status.EjbLangFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLightScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.utils.TrafficLight;

public class EjbTrafficLightFactory<L extends JeeslLang,D extends JeeslDescription,
									SCOPE extends JeeslTrafficLightScope<L,D,SCOPE,?>,
									LIGHT extends JeeslTrafficLight<L,D,SCOPE>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbTrafficLightFactory.class);
	
	private final Class<LIGHT> cLight;
	
    private final EjbLangFactory<L> efLang;
    private final EjbDescriptionFactory<D> efDescription;
    
	public EjbTrafficLightFactory(final Class<L> cLang,final Class<D> cDescription,final Class<LIGHT> cLight)
	{       
        this.cLight = cLight;
        
        efLang = EjbLangFactory.instance(cLang);
        efDescription = EjbDescriptionFactory.factory(cDescription);
	}
	
	public static <L extends JeeslLang,D extends JeeslDescription,
					SCOPE extends JeeslTrafficLightScope<L,D,SCOPE,?>,
					LIGHT extends JeeslTrafficLight<L,D,SCOPE>>
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
		catch (JeeslConstraintViolationException e) {e.printStackTrace();}
        
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