package org.jeesl.interfaces.model.system.security.page;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.util.with.JeeslSecurityWithActions;
import org.jeesl.interfaces.model.system.security.util.with.JeeslSecurityWithCategory;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslSecurityView<L extends JeeslLang, D extends JeeslDescription,
								   C extends JeeslSecurityCategory<L,D>,
								   R extends JeeslSecurityRole<L,D,C,?,U,A>,
								   U extends JeeslSecurityUsecase<L,D,C,R,?,A>,
								   A extends JeeslSecurityAction<L,D,R,?,U,?>
								   >
			extends Serializable,EjbPersistable,EjbSaveable,EjbRemoveable,
					EjbWithCode,EjbWithPositionVisible,EjbWithParentAttributeResolver,
					EjbWithLang<L>,EjbWithDescription<D>,
					JeeslSecurityWithCategory<C>,
					JeeslSecurityWithActions<A>
{
	public enum Attributes {category,roles}
	public static final String extractId = "securityViews";
	public enum Code{sSecPageErrorExpired,sSecPageLoginRequired}

	public Boolean getAccessPublic();
	public void setAccessPublic(Boolean accessPublic);
	
	public Boolean getAccessLogin();
	public void setAccessLogin(Boolean accessLogin);
	
	public String getPackageName();
	public void setPackageName(String packageName);
	
	public String getViewPattern();
	public void setViewPattern(String viewPattern);
	
	public String getUrlMapping();
	public void setUrlMapping(String urlMapping);
	
	public String getUrlBase();
	public void setUrlBase(String urlBase);
	
	public List<R> getRoles();
	public void setRoles(List<R> roles);
	
	public List<U> getUsecases();
	public void setUsecases(List<U> usecases);
	
	public Boolean getDocumentation();
	public void setDocumentation(Boolean documentation);
	
	public Boolean getRedirect();
	public void setRedirect(Boolean redirect);
	
	public Boolean getMaintenance();
	public void setMaintenance(Boolean maintenance);
}