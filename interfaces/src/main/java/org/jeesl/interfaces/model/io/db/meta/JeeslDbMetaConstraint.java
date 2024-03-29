package org.jeesl.interfaces.model.io.db.meta;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.io.db.meta.with.JeeslDbMetaWithSnapshots;
import org.jeesl.interfaces.model.io.db.meta.with.JeeslDbMetaWithTable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.status.JeeslWithType;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslDbMetaConstraint<SNAP extends JeeslDbMetaSnapshot<?,?,TAB,?,?>,
										TAB extends JeeslDbMetaTable<?,SNAP,?>,
										COL extends JeeslDbMetaColumn<SNAP,TAB,?>,
										CONT extends JeeslDbMetaConstraintType<?,?,CONT,?>,
										UNQ extends JeeslDbMetaUnique<COL,?>>
					extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,
							EjbWithNonUniqueCode,JeeslWithType<CONT>,
							JeeslDbMetaWithSnapshots<SNAP>,JeeslDbMetaWithTable<TAB>
{
	public static enum Attributes{table,code,snapshots,uniques};
	
	COL getColumnLocal();
	void setColumnLocal(COL columnLocal);
	
	COL getColumnRemote();
	void setColumnRemote(COL columnRemote);
	
	List<UNQ> getUniques();
	void setUniques(List<UNQ> uniques);
}