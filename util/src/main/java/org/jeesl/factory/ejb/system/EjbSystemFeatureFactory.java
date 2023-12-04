package org.jeesl.factory.ejb.system;

import java.util.Objects;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.property.JeeslSystemFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSystemFeatureFactory<F extends JeeslSystemFeature>
{
	final static Logger logger = LoggerFactory.getLogger(EjbSystemFeatureFactory.class);
	
	private final Class<F> cFeature;
    
	public static <F extends JeeslSystemFeature> EjbSystemFeatureFactory<F> instance(final Class<F> cFeature)
	{
		return new EjbSystemFeatureFactory<>(cFeature);
	}
	public EjbSystemFeatureFactory(final Class<F> cFeature)
	{  
        this.cFeature = cFeature;
	}
	
	public static <F extends JeeslSystemFeature>
		EjbSystemFeatureFactory<F> factory(final Class<F> cFeature)
	{
		return new EjbSystemFeatureFactory<F>(cFeature);
	}
    
	public F build()
	{
		F ejb = null;
		try
		{
			ejb = cFeature.newInstance();
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public <E extends Enum<E>> boolean active(JeeslFacade facade, E code)
	{
		F feature = facade.fByEnum(cFeature,code);
		if(Objects.isNull(feature)) {return false;}
		else {return feature.isVisible();}
			
		
	}
}