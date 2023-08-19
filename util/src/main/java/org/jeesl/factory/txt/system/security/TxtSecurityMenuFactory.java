package org.jeesl.factory.txt.system.security;

import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtSecurityMenuFactory <L extends JeeslLang, D extends JeeslDescription,
										 C extends JeeslSecurityCategory<L,D>,
										 R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
										 V extends JeeslSecurityView<L,D,C,R,U,A>,
										 U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
										 A extends JeeslSecurityAction<L,D,R,V,U,AT>,
										 AT extends JeeslSecurityTemplate<L,D,C>,
										 CTX extends JeeslSecurityContext<L,D>,
										 M extends JeeslSecurityMenu<L,V,CTX,M>,
										 USER extends JeeslUser<R>>
{
	final static Logger logger = LoggerFactory.getLogger(TxtSecurityMenuFactory.class);
    
    public TxtSecurityMenuFactory()
    {
    	
    } 
    
    public String code(M menu)
    {
	    	StringBuilder sb = new StringBuilder();
	    	if(menu.getView()!=null) {sb.append(menu.getView().getCode());}
	    	else {sb.append(JeeslSecurityMenu.keyRoot);}
	    	return sb.toString();
    }
}