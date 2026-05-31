package org.jeesl.factory.ejb.io.attribute;

import java.lang.reflect.InvocationTargetException;

import org.jeesl.factory.builder.io.IoAttributeFactoryBuilder;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSection;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbAttributeSectionFactory<L extends JeeslLang, 
									SET extends JeeslAttributeSet<L,?,?,?,?>,
									SCT extends JeeslAttributeSection<L,SET>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbAttributeSectionFactory.class);
	
	private final IoAttributeFactoryBuilder<L,?,?,?,?,?,?,SET,SCT,?,?,?> fbAttribute;
    
	public EjbAttributeSectionFactory(IoAttributeFactoryBuilder<L,?,?,?,?,?,?,SET,SCT,?,?,?> fbAttribute)
	{       
        this.fbAttribute = fbAttribute;
	}
	
	public SCT build(SET set)
	{
		SCT ejb = null;
	
		try
		{
			ejb = fbAttribute.getClassSection().getDeclaredConstructor().newInstance();
			ejb.setSet(set);
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
		}
		
		return ejb;
	}

}