package org.jeesl.factory.ejb.system.security;

import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSecurityUsecaseFactory < C extends JeeslSecurityCategory<?,?>,
										 U extends JeeslSecurityUsecase<?,?,C,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbSecurityUsecaseFactory.class);
	
    private final Class<U> cUsecase;
    
    public EjbSecurityUsecaseFactory(final Class<U> cUsecase)
    {
        this.cUsecase = cUsecase;
    } 
    
    public U build(C category, String code)
    {
    	U ejb = null;
    	
    	try
    	{
			ejb = cUsecase.newInstance();
			ejb.setCategory(category);
			ejb.setCode(code);
			ejb.setPosition(1);
			ejb.setVisible(true);
		}
    	catch (InstantiationException e) {e.printStackTrace();}
    	catch (IllegalAccessException e) {e.printStackTrace();}
    	
    	return ejb;
    }
}