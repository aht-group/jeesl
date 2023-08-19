package org.jeesl.interfaces.bean.op;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;

import net.sf.ahtutils.interfaces.controller.handler.op.user.OpUserSelectionHandler;

public interface OpUserBean <
//L extends JeeslLang, D extends JeeslDescription,C extends JeeslSecurityCategory<L,D>,
//							R extends JeeslSecurityRole<?,?,?,V,U,A,USER>,
//							V extends JeeslSecurityView<?,?,?,R,U,A>,
//							U extends JeeslSecurityUsecase<?,?,?,R,V,A>,
//							A extends JeeslSecurityAction<?,?,R,V,U,AT>,
//							AT extends JeeslSecurityTemplate<?,?,?>,
							USER extends JeeslUser<?>>
{
	OpUserSelectionHandler<USER> getOpUserHandler();
	void selectOpUser(USER user) throws JeeslLockingException, JeeslConstraintViolationException;
}