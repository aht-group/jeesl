package org.jeesl.interfaces.model.io.ssi.core;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.bool.EjbWithVisibleMigration;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoSsiCredential <SYSTEM extends JeeslIoSsiSystem<?,?>>
								extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,
										EjbWithCode,EjbWithPosition,EjbWithParentAttributeResolver,
										EjbWithVisibleMigration
{	
	public enum Attributes {system}
	
	public SYSTEM getSystem();
	public void setSystem(SYSTEM system);

	String getUser();
	void setUser(String user);
	
	String getPwd();
	void setPwd(String pwd);
	
	String getToken();
	void setToken(String token);
	
	String getUrl();
	void setUrl(String url);
	
	boolean isEncrypted();
	void setEncrypted(boolean encrypted);
}