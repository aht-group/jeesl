package org.jeesl.factory.ejb.system.security;

import java.util.List;

import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSecurityViewFactory <C extends JeeslSecurityCategory<?,?>,
										 V extends JeeslSecurityView<?,?,C,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbSecurityViewFactory.class);
	
    private final Class<V> cView;
      
    public EjbSecurityViewFactory(final Class<V> cView)
    {
        this.cView = cView;
    } 
    
    public V build(C category, String code, List<V> list)
    {
    		V ejb = null;

    		try
    		{
			ejb = cView.newInstance();
			ejb.setCategory(category);
			ejb.setCode(code);
			if(list==null){ejb.setPosition(1);}
			else{ejb.setPosition(list.size()+1);}
    		}
    	catch (InstantiationException e) {e.printStackTrace();}
    	catch (IllegalAccessException e) {e.printStackTrace();}
    	
    	return ejb;
    }
    
    public V clone(V view)
    {
    		V ejb = null;
	    	
	    	try
	    	{
			ejb = cView.newInstance();
			ejb.setCategory(view.getCategory());
			ejb.setCode(view.getCode()+"Clone");
			ejb.setPosition(view.getPosition());
			ejb.setVisible(view.isVisible());
			ejb.setAccessLogin(view.getAccessLogin());
			ejb.setAccessPublic(view.getAccessPublic());
			ejb.setDocumentation(view.getDocumentation());
			ejb.setPackageName(view.getPackageName());
			ejb.setUrlBase(view.getUrlBase());
			ejb.setUrlMapping(view.getUrlMapping());
			ejb.setViewPattern(view.getViewPattern());
		}
	    	catch (InstantiationException e) {e.printStackTrace();}
	    	catch (IllegalAccessException e) {e.printStackTrace();}
	    	
	    	return ejb;
    }
}