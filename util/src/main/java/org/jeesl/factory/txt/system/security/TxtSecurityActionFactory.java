package org.jeesl.factory.txt.system.security;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtSecurityActionFactory <L extends JeeslLang, D extends JeeslDescription,
										 V extends JeeslSecurityView<L,D,?,?,?,A>,
										 A extends JeeslSecurityAction<L,D,?,V,?,AT>,
										 AT extends JeeslSecurityTemplate<L,D,?>>
{
	final static Logger logger = LoggerFactory.getLogger(TxtSecurityActionFactory.class);
    
    public TxtSecurityActionFactory()
    {

    } 
    
    public String code(V view, AT template)
    {
	    	StringBuffer sb = new StringBuffer();
	    	sb.append(view.getCode());
	    	sb.append(template.getCode().substring(template.getCode().lastIndexOf("."), template.getCode().length()));
	    	return sb.toString();
    }
}