package org.jeesl.interfaces.model.io.fr;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.model.with.system.status.JeeslWithType;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslFileStorage<L extends JeeslLang, D extends JeeslDescription,
									SYSTEM extends JeeslIoSsiSystem<L,D>,
									STYPE extends JeeslFileStorageType<L,D,STYPE,?>,
									ENGINE extends JeeslFileStorageEngine<L,D,ENGINE,?>>
							extends Serializable,EjbWithId,
								EjbSaveable,EjbRemoveable,
								EjbWithCode,EjbWithPosition,
								JeeslWithType<STYPE>,
								EjbWithLang<L>,EjbWithDescription<D>
				
{	
	ENGINE getEngine();
	void setEngine(ENGINE engines);
	
	String getJson();
	void setJson(String json);
	
	Boolean getKeepRemoved();
	void setKeepRemoved(Boolean keepRemoved);
	
	boolean isFrozen();
	void setFrozen(boolean frozen);
	
	Long getFileSizeLimit();
	void setFileSizeLimit(Long fileSizeLimit);
}