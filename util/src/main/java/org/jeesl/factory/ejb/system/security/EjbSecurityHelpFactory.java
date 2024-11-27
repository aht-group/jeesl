package org.jeesl.factory.ejb.system.security;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.jeesl.factory.ejb.util.EjbPositionFactory;
import org.jeesl.interfaces.model.io.cms.JeeslIoCms;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsSection;
import org.jeesl.interfaces.model.system.security.doc.JeeslSecurityOnlineHelp;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSecurityHelpFactory <V extends JeeslSecurityView<?,?,?,?,?,?>,
										OH extends JeeslSecurityOnlineHelp<V,DOC,SEC>,
										DOC extends JeeslIoCms<?,?,?,?,SEC>,
										SEC extends JeeslIoCmsSection<?,SEC>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbSecurityHelpFactory.class);
	
	private final Class<OH> cOh;
    
    public EjbSecurityHelpFactory(final Class<OH> cOh)
    {
		this.cOh = cOh;
    } 
      
    public OH build(V view, DOC document, SEC section, List<OH> list)
    {
    	OH ejb = null;
    	
    	try
    	{
			ejb = cOh.getDeclaredConstructor().newInstance();
			ejb.setView(view);
			ejb.setDocument(document);
			ejb.setSection(section);
			EjbPositionFactory.next(ejb,list);
		}
    	catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		catch (IllegalArgumentException e) {e.printStackTrace();}
		catch (InvocationTargetException e) {e.printStackTrace();}
		catch (NoSuchMethodException e) {e.printStackTrace();}
		catch (SecurityException e) {e.printStackTrace();}
    	
    	return ejb;
    }
}