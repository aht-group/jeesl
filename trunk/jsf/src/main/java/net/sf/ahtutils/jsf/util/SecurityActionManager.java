package net.sf.ahtutils.jsf.util;

import java.util.Hashtable;
import java.util.Map;

import net.sf.ahtutils.exception.ejb.UtilsNotFoundException;
import net.sf.ahtutils.interfaces.facade.UtilsSecurityFacade;
import net.sf.ahtutils.model.interfaces.idm.UtilsIdentity;
import net.sf.ahtutils.model.interfaces.idm.UtilsUser;
import net.sf.ahtutils.model.interfaces.security.UtilsSecurityAction;
import net.sf.ahtutils.model.interfaces.security.UtilsSecurityCategory;
import net.sf.ahtutils.model.interfaces.security.UtilsSecurityRole;
import net.sf.ahtutils.model.interfaces.security.UtilsSecurityUsecase;
import net.sf.ahtutils.model.interfaces.security.UtilsSecurityView;
import net.sf.ahtutils.model.interfaces.status.UtilsDescription;
import net.sf.ahtutils.model.interfaces.status.UtilsLang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityActionManager <L extends UtilsLang,
									D extends UtilsDescription,
									C extends UtilsSecurityCategory<L,D,C,R,V,U,A,USER>,
									R extends UtilsSecurityRole<L,D,C,R,V,U,A,USER>,
									V extends UtilsSecurityView<L,D,C,R,V,U,A,USER>,
									U extends UtilsSecurityUsecase<L,D,C,R,V,U,A,USER>,
									A extends UtilsSecurityAction<L,D,C,R,V,U,A,USER>,
									USER extends UtilsUser<L,D,C,R,V,U,A,USER>>
{
	final static Logger logger = LoggerFactory.getLogger(SecurityActionManager.class);
	
	public static <L extends UtilsLang,
		   D extends UtilsDescription, 
		   C extends UtilsSecurityCategory<L,D,C,R,V,U,A,USER>,
		   R extends UtilsSecurityRole<L,D,C,R,V,U,A,USER>,
		   V extends UtilsSecurityView<L,D,C,R,V,U,A,USER>,
		   U extends UtilsSecurityUsecase<L,D,C,R,V,U,A,USER>,
		   A extends UtilsSecurityAction<L,D,C,R,V,U,A,USER>,
		   USER extends UtilsUser<L,D,C,R,V,U,A,USER>>
		SecurityActionManager<L,D,C,R,V,U,A,USER>
		factory(UtilsSecurityFacade fSecurity,final Class<V> cView, String viewId, UtilsIdentity<L,D,C,R,V,U,A,USER> identity) throws UtilsNotFoundException
	{
		return new SecurityActionManager<L,D,C,R,V,U,A,USER>(fSecurity,cView,viewId,identity);
	}
	
	private Map<String,Boolean> allowed;
	
	public SecurityActionManager(UtilsSecurityFacade fSecurity, final Class<V> cView, String viewId, UtilsIdentity<L,D,C,R,V,U,A,USER> identity) throws UtilsNotFoundException
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