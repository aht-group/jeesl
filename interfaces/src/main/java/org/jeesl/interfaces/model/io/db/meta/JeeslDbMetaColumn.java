package org.jeesl.interfaces.model.io.db.meta;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.db.meta.with.JeeslDbMetaWithSnapshots;
import org.jeesl.interfaces.model.io.db.meta.with.JeeslDbMetaWithTable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.status.JeeslWithType;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslAttributes
@DownloadJeeslDescription
public interface JeeslDbMetaColumn<SNAP extends JeeslDbMetaSnapshot<?,?,TAB,?,?>,
										TAB extends JeeslDbMetaTable<?,SNAP,?>,
										COLT extends JeeslDbMetaColumnType<?,?,COLT,?>>
					extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,
							EjbWithNonUniqueCode,JeeslWithType<COLT>,
							JeeslDbMetaWithSnapshots<SNAP>,JeeslDbMetaWithTable<TAB>
{
	public static enum Attributes{table,code,snapshots};
}