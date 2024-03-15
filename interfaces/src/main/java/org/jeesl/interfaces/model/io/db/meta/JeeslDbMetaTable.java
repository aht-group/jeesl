package org.jeesl.interfaces.model.io.db.meta;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.db.meta.with.JeeslDbMetaWithSnapshots;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslDbMetaTable<SYSTEM extends JeeslIoSsiSystem<?,?>,
									SNAP extends JeeslDbMetaSnapshot<SYSTEM,SCHEMA,?,?,?>,
									SCHEMA extends JeeslDbMetaSchema<SYSTEM,SNAP>>
					extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,
							EjbWithNonUniqueCode,
							JeeslDbMetaWithSnapshots<SNAP>
{
	public static enum Attributes{system,code,snapshots};
	
	SYSTEM getSystem();
	void setSystem(SYSTEM system);
	
	SCHEMA getSchema();
	void setSchema(SCHEMA schema);
}