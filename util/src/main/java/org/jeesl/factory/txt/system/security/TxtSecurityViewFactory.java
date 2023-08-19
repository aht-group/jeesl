package org.jeesl.factory.txt.system.security;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtSecurityViewFactory <L extends JeeslLang,
										 D extends JeeslDescription,
										 C extends JeeslSecurityCategory<L,D>,
										 V extends JeeslSecurityView<L,D,C,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(TxtSecurityViewFactory.class);
    
    public TxtSecurityViewFactory(String localeCode)
    {
    	
    } 
    
    public String debug(V view)
    {
    	StringBuffer sb = new StringBuffer();
    	sb.append(view.getCategory().getPosition()).append(".").append(view.getPosition());
    	sb.append(" ").append(view.getCode());
    	return sb.toString();
    }
}