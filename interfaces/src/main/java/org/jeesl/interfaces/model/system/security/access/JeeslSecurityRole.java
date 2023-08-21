package org.jeesl.interfaces.model.system.security.access;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.util.with.JeeslSecurityWithActions;
import org.jeesl.interfaces.model.system.security.util.with.JeeslSecurityWithCategory;
import org.jeesl.interfaces.model.system.security.util.with.JeeslSecurityWithViews;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslSecurityRole<L extends JeeslLang, D extends JeeslDescription, 
						 		   C extends JeeslSecurityCategory<L,D>,
						 		   V extends JeeslSecurityView<L,D,C,?,U,A>,
						 		   U extends JeeslSecurityUsecase<L,D,C,?,V,A>,
						 		   A extends JeeslSecurityAction<L,D,?,V,U,?>>
			extends Serializable,EjbPersistable,EjbSaveable,EjbRemoveable,
					EjbWithPositionVisible,EjbWithParentAttributeResolver,
					EjbWithCode,EjbWithLang<L>,EjbWithDescription<D>,
					JeeslSecurityWithCategory<C>,
					JeeslSecurityWithViews<V>,
					JeeslSecurityWithActions<A>
					
{
	public enum Developer{devLogger}
	
	public static final String extractId = "securityRoles";
	public enum Attributes{category}
	
	public List<U> getUsecases();
	public void setUsecases(List<U> usecases);
	
//	List<USER> getUsers();
//	void setUsers(List<USER> users);
	
	public Boolean getDocumentation();
	public void setDocumentation(Boolean documentation);
}