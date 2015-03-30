package net.sf.ahtutils.model.interfaces.security;

import java.util.List;

import net.sf.ahtutils.interfaces.model.behaviour.EjbSaveable;
import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.model.interfaces.crud.EjbRemoveable;
import net.sf.ahtutils.model.interfaces.idm.UtilsUser;
import net.sf.ahtutils.model.interfaces.with.EjbWithCode;
import net.sf.ahtutils.model.interfaces.with.EjbWithDescription;
import net.sf.ahtutils.model.interfaces.with.EjbWithLang;

public interface UtilsSecurityRole<L extends UtilsLang,
						 		   D extends UtilsDescription, 
						 		   C extends UtilsSecurityCategory<L,D,C,R,V,U,A,USER>,
						 		   R extends UtilsSecurityRole<L,D,C,R,V,U,A,USER>,
						 		   V extends UtilsSecurityView<L,D,C,R,V,U,A,USER>,
						 		   U extends UtilsSecurityUsecase<L,D,C,R,V,U,A,USER>,
						 		   A extends UtilsSecurityAction<L,D,C,R,V,U,A,USER>,
						 		   USER extends UtilsUser<L,D,C,R,V,U,A,USER>>
			extends EjbWithCode,EjbWithLang<L>,EjbWithDescription<D>,
					UtilsSecurityWithCategory<L,D,C,R,V,U,A,USER>,
					UtilsSecurityWithViews<L,D,C,R,V,U,A,USER>,
					UtilsSecurityWithActions<L,D,C,R,V,U,A,USER>,
					EjbSaveable,EjbRemoveable
{
	public static final String extractId = "securityRoles";
	
	public List<U> getUsecases();
	public void setUsecases(List<U> usecases);
	
	List<USER> getUsers();
	void setUsers(List<USER> users);
	

}