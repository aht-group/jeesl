package org.jeesl.factory.ejb.system.security;

import java.util.List;

import org.jeesl.interfaces.model.system.security.page.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSecurityActionTemplateFactory <
										 C extends JeeslSecurityCategory<?,?>,
										
										 AT extends JeeslSecurityTemplate<?,?,C>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbSecurityActionTemplateFactory.class);
	
	private final Class<AT> cTemplate;
	
    public EjbSecurityActionTemplateFactory(Class<AT> clActionTemplate)
    {
        this.cTemplate = clActionTemplate;
    } 
    
    public AT build(C category, String code, List<AT> list){return build(category,code,list.size()+1);}
    public AT build(C category, String code){return build(category,code,1);}
    private AT build(C category, String code, int position)
    {
    	AT ejb = null;
    	
    	try
    	{
			ejb = cTemplate.newInstance();
			ejb.setCategory(category);
			ejb.setCode(code);
			ejb.setPosition(1);
			ejb.setVisible(false);
			ejb.setDocumentation(false);
		}
    	catch (InstantiationException e) {e.printStackTrace();}
    	catch (IllegalAccessException e) {e.printStackTrace();}
    	
    	return ejb;
    }
}