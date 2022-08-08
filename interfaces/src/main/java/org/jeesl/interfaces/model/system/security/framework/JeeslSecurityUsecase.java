package org.jeesl.interfaces.model.system.security.framework;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.io.label.download.JeeslRestDownloadEntityAttributes;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.with.JeeslSecurityWithActions;
import org.jeesl.interfaces.model.system.security.with.JeeslSecurityWithCategory;
import org.jeesl.interfaces.model.system.security.with.JeeslSecurityWithViews;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslSecurityUsecase<L extends JeeslLang, D extends JeeslDescription, 
 									  C extends JeeslSecurityCategory<L,D>,
 									  R extends JeeslSecurityRole<L,D,C,V,?,A,?>,
 									  V extends JeeslSecurityView<L,D,C,R,?,A>,
 									  A extends JeeslSecurityAction<L,D,R,V,?,?>
 									 >
			extends Serializable,EjbPersistable,EjbWithCode,EjbSaveable,EjbRemoveable,
					EjbWithPositionVisible,EjbWithParentAttributeResolver,
					EjbWithLang<L>,EjbWithDescription<D>,
					JeeslSecurityWithCategory<C>,
					JeeslSecurityWithViews<V>,
					JeeslSecurityWithActions<A>,
					JeeslRestDownloadEntityAttributes
{
	public static final String extractId = "securityUsecases";
	
	public List<R> getRoles();
	public void setRoles(List<R> roles);
	
	public Boolean getDocumentation();
	public void setDocumentation(Boolean documentation);
}