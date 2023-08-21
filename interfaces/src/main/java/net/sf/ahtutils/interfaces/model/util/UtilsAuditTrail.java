package net.sf.ahtutils.interfaces.model.util;

import java.util.Date;

import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;

public interface UtilsAuditTrail<L extends JeeslLang,
								 D extends JeeslDescription,
								 C extends JeeslSecurityCategory<L,D>,
								 R extends JeeslSecurityRole<L,D,C,V,U,A>,
								 V extends JeeslSecurityView<L,D,C,R,U,A>,
								 U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
								 A extends JeeslSecurityAction<L,D,R,V,U,AT>,
								 AT extends JeeslSecurityTemplate<L,D,C>,
								 USER extends JeeslUser<R>,
								 TY extends JeeslStatus<L,D,TY>>
					extends EjbWithId
{
	public USER getUser();
	public void setUser(USER user);
	
	public TY getType();
	public void setType(TY type);
	
	public Date getRecord();
	public void setRecord(Date record);
}