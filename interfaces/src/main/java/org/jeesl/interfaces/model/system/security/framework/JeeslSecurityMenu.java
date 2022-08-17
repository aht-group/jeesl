package org.jeesl.interfaces.model.system.security.framework;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.bool.EjbWithVisibleMigration;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslSecurityMenu<L extends JeeslLang,
									V extends JeeslSecurityView<L,?,?,?,?,?>,
									CTX extends JeeslSecurityContext<L,?>,
									M extends JeeslSecurityMenu<L,V,CTX,M>>
			extends Serializable,EjbSaveable,EjbRemoveable,
						EjbWithPosition,EjbWithLang<L>,
						EjbWithParentAttributeResolver,
						EjbWithVisibleMigration
						
{
	public static final String extractId = "securityMenu";
	public static final String keyRoot = "root";
	public enum Attributes{context,parent,view}
	
	CTX getContext();
	void setContext(CTX context);
	
	M getParent();
	void setParent(M menu);
	
	V getView();
	void setView(V view);
}