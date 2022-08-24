package org.jeesl.interfaces.model.io.ssi.core;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLangDescription;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoSsiHost <L extends JeeslLang, D extends JeeslDescription,
									SYSTEM extends JeeslIoSsiSystem<?,?>>
							extends Serializable,EjbSaveable,
									EjbWithId,EjbWithCode,EjbWithParentAttributeResolver,
									EjbWithLangDescription<L,D>,
									EjbWithPosition
{
	public enum Attributes{system}
	
	SYSTEM getSystem();
	void setSystem(SYSTEM system);
	
	String getFqdn();
	void setFqdn(String fqdn);
	
	String getIpAddr();
	void setIpAddr(String ipAddr);
	
	String getHardwareManufacturer();
	void setHardwareManufacturer(String hardwareManufacturer);

	String getHardwareModel();
	void setHardwareModel(String hardwareModel);
	
	String getMemory();
	void setMemory(String memory);
}