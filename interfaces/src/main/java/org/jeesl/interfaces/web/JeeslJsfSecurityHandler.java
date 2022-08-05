package org.jeesl.interfaces.web;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityArea;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.web.security.JeeslSecurityHandler;

public interface JeeslJsfSecurityHandler <R extends JeeslSecurityRole<?,?,?,V,U,A,USER>,
											V extends JeeslSecurityView<?,?,?,R,U,A>,
											U extends JeeslSecurityUsecase<?,?,?,R,V,A>,
											A extends JeeslSecurityAction<?,?,R,V,U,?>,
											AR extends JeeslSecurityArea<?,?,V>,
											USER extends JeeslUser<R>
										>
			extends Serializable,JeeslSecurityHandler
{
	List<R> getRoles();
	
	Map<R,Boolean> getMapHasRole();
	boolean hasRole(R role);
	
//	<E extends Enum<E>> boolean allowSuffixCode(E actionCode);
	boolean allow(String actionCode);
	String getPageCode();
}