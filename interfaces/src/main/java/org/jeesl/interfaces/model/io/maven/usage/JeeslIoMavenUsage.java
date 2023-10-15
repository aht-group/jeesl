package org.jeesl.interfaces.model.io.maven.usage;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenVersion;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslMavenScope;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoMavenUsage <VERSION extends JeeslIoMavenVersion<?,?,?>,
									SCOPE extends JeeslMavenScope<?,?,SCOPE,?>,
									MODULE extends JeeslIoMavenModule<MODULE,?,?,?,?>>
									extends Serializable,EjbWithId,EjbRemoveable,EjbPersistable,EjbSaveable,
											EjbWithParentAttributeResolver
{
	public static enum Attributes{module,version,scopes};
	
	MODULE getModule();
	void setModule(MODULE module);
	
	VERSION getVersion();
	void setVersion(VERSION version);
	
	List<SCOPE> getScopes();
	void setScopes(List<SCOPE> scopes);
}