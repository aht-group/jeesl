package org.jeesl.interfaces.model.io.crypto;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.status.JeeslWithStatus;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoCryptoKey<USER extends JeeslSimpleUser,
									STATUS extends JeeslIoCryptoKeyStatus<?,?,STATUS,?>>
						extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,
								EjbWithPosition,JeeslWithStatus<STATUS>,EjbWithParentAttributeResolver
								
{	
	public enum Attributes{user}
		
	USER getUser();
	void setUser(USER user);
	
	LocalDateTime getRecord();
	void setRecord(LocalDateTime record);
	
	String getPwdSalt();
	void setPwdSalt(String pwdSalt);
	
	String getMemoIv();
	void setMemoIv(String memoIv);
	
	String getMemoText();
	void setMemoText(String memoText);
	
	String getMemoCypher();
	void setMemoCypher(String memoCypher);
}