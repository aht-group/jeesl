package net.sf.ahtutils.interfaces.model.util;

import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;

import net.sf.ahtutils.model.interfaces.with.EjbWithId;

public interface UtilsStaffPool<L extends JeeslLang,
								   D extends JeeslDescription,
								   C extends JeeslSecurityCategory<L,D>,
								   R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
								   V extends JeeslSecurityView<L,D,C,R,U,A>,
								   U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
								   A extends JeeslSecurityAction<L,D,R,V,U,AT>,
								   AT extends JeeslSecurityTemplate<L,D,C>,
								   P extends EjbWithId,
								   E extends EjbWithId,
								   USER extends JeeslUser<R>>
			extends EjbWithId
{
	public R getRole();
	public void setRole(R role);
	
	public P getPool();
	public void setPool(P pool);
	
	public E getEntity();
	public void setEntity(E entity);
}