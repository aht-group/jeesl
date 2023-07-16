package org.jeesl.interfaces.model.io.db.meta;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.status.JeeslWithType;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
public interface JeeslDbMetaColumn<MS extends JeeslDbMetaSnapshot<?,MT,?,?>,
										MT extends JeeslDbMetaTable<?,MS>,
										COLT extends JeeslDbMetaColumnType<?,?,COLT,?>>
					extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,
							EjbWithNonUniqueCode,JeeslWithType<COLT>
{
	public static enum Attributes{table,code};
	
	MT getTable();
	void setTable(MT table);
}