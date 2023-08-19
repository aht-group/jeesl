package org.jeesl.interfaces.model.system.security.doc;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.cms.JeeslIoCms;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsSection;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslSecurityOnlineHelp<V extends JeeslSecurityView<?,?,?,?,?,?>,
										DOC extends JeeslIoCms<?,?,?,?,SEC>,
										SEC extends JeeslIoCmsSection<?,SEC>>
			extends Serializable,EjbSaveable,EjbWithPositionVisible,EjbWithParentAttributeResolver,EjbRemoveable
{
	public enum Attributes{view}
	
	V getView();
	void setView(V view);
	
	DOC getDocument();
	void setDocument(DOC document);
	
	SEC getSection();
	void setSection(SEC section);
}