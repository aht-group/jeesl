package net.sf.ahtutils.jsf.util;

import java.util.Hashtable;
import java.util.Map;

import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.user.JeeslIdentity;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;

public class SecurityActionManager <L extends UtilsLang,
									D extends UtilsDescription,
									C extends JeeslSecurityCategory<L,D>,
									R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
									V extends JeeslSecurityView<L,D,C,R,U,A>,
									U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
									A extends JeeslSecurityAction<L,D,R,V,U,AT>,
									AT extends JeeslSecurityTemplate<L,D,C>,
									USER extends JeeslUser<R>>
{
	final static Logger logger = LoggerFactory.getLogger(SecurityActionManager.class);
	
	public static <L extends UtilsLang,
		   D extends UtilsDescription, 
		   C extends JeeslSecurityCategory<L,D>,
		   R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
		   V extends JeeslSecurityView<L,D,C,R,U,A>,
		   U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
		   A extends JeeslSecurityAction<L,D,R,V,U,AT>,
		   AT extends JeeslSecurityTemplate<L,D,C>,
		   USER extends JeeslUser<R>>
		SecurityActionManager<L,D,C,R,V,U,A,AT,USER>
		factory(JeeslSecurityFacade<L,D,C,R,V,U,A,AT,?,USER> fSecurity,final Class<V> cView, String viewId, JeeslIdentity<R,V,U,A,USER> identity) throws JeeslNotFoundException
	{
		return new SecurityActionManager<L,D,C,R,V,U,A,AT,USER>(fSecurity,cView,viewId,identity);
	}
	
	private Map<String,Boolean> allowed;
	
	public SecurityActionManager(JeeslSecurityFacade<L,D,C,R,V,U,A,AT,?,USER> fSecurity, final Class<V> cView, String viewId, JeeslIdentity<R,V,U,A,USER> identity) throws JeeslNotFoundException
	{
		allowed = new Hashtable<String,Boolean>();
		V view = fSecurity.fByCode(cView,viewId);
		view = fSecurity.load(cView, view);
		for(A a : view.getActions())
		{
			allowed.put(a.getCode(), identity.hasAction(a.getCode()));
		}
	}
	
	public Map<String, Boolean> getAllowed() {return allowed;}
}