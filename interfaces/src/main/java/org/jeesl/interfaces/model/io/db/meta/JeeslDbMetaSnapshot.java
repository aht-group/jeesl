package org.jeesl.interfaces.model.io.db.meta;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.date.jt.JeeslWithRecordDateTime;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
public interface JeeslDbMetaSnapshot<SYSTEM extends JeeslIoSsiSystem<?,?>,
									MT extends JeeslDbMetaTable<SYSTEM,?>,
									MC extends JeeslDbMetaConstraint<SYSTEM,?,MT>>
					extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,
						JeeslWithRecordDateTime,EjbWithName,EjbWithParentAttributeResolver
{
	public static enum Attributes{system,record};
	
	SYSTEM getSystem();
	void setSystem(SYSTEM system);
	
	List<MT> getTables();
	void setTables(List<MT> tables);
	
	List<MC> getConstraints();
	void setConstraints(List<MC> constraints);
}