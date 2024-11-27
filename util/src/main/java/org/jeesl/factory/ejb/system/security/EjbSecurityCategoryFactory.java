package org.jeesl.factory.ejb.system.security;

import java.lang.reflect.InvocationTargetException;

import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.util.with.JeeslSecurityWithCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSecurityCategoryFactory <C extends JeeslSecurityCategory<?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbSecurityCategoryFactory.class);
	
    private final Class<C> cCategory;
    
    public EjbSecurityCategoryFactory(final Class<C> cCategory)
    {
        this.cCategory = cCategory;
    } 
    
    public C create(String code, String type)
    {
    	C ejb = null;
    	
    	try
    	{
			ejb = cCategory.getDeclaredConstructor().newInstance();
			ejb.setCode(code);
			ejb.setType(type);
			ejb.setPosition(1);
			ejb.setVisible(true);
		}
    	catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		catch (IllegalArgumentException e) {e.printStackTrace();}
		catch (InvocationTargetException e) {e.printStackTrace();}
		catch (NoSuchMethodException e) {e.printStackTrace();}
		catch (SecurityException e) {e.printStackTrace();}
    	
    	return ejb;
    }
    
    public static <C extends JeeslSecurityCategory<?,?>, WC extends JeeslSecurityWithCategory<C>> String toType(Class<WC> cWithCategory)
    {
    	String type = null;
		if(cWithCategory.getSimpleName().contains("Usecase")){type=JeeslSecurityCategory.Type.usecase.toString();}
		else if(cWithCategory.getSimpleName().contains("Role")){type=JeeslSecurityCategory.Type.role.toString();}
		else if(cWithCategory.getSimpleName().contains("View")){type=JeeslSecurityCategory.Type.view.toString();}
		else if(cWithCategory.getSimpleName().contains("Action")){type=JeeslSecurityCategory.Type.action.toString();}
		return type;
    }
}