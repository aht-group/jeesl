package org.jeesl.interfaces.model.io.db.meta;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
public interface JeeslDbMetaColumn<SYSTEM extends JeeslIoSsiSystem<?,?>,
										MS extends JeeslDbMetaSnapshot<SYSTEM,MT,?>,
										MT extends JeeslDbMetaTable<SYSTEM,MS>>
					extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,
							EjbWithNonUniqueCode
{
	public static enum Attributes{system,table,code};
	
	SYSTEM getSystem();
	void setSystem(SYSTEM system);
	
	MT getTable();
	void setTable(MT table);
}