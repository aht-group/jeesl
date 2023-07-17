package org.jeesl.interfaces.model.io.db.meta;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
public interface JeeslDbMetaConstraint<SNAP extends JeeslDbMetaSnapshot<?,TAB,?,?>,
										TAB extends JeeslDbMetaTable<?,SNAP>>
					extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,
							EjbWithNonUniqueCode
{
	public static enum Attributes{table,code,snapshots};
	
	TAB getTable();
	void setTable(TAB table);
	
	List<SNAP> getSnapshots();
	void setSnapshots(List<SNAP> snapshots);
}