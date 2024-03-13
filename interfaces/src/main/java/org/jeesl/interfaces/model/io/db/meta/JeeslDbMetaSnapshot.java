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
									SCHEMA extends JeeslDbMetaSchema<SYSTEM,?>,
									TAB extends JeeslDbMetaTable<SYSTEM,?,SCHEMA>,
									COL extends JeeslDbMetaColumn<?,TAB,?>,
									CON extends JeeslDbMetaConstraint<?,TAB,COL,?,?>>
					extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,
						JeeslWithRecordDateTime,EjbWithName,EjbWithParentAttributeResolver
{
	public static enum Attributes{system,record};
	
	SYSTEM getSystem();
	void setSystem(SYSTEM system);
	
	List<SCHEMA> getSchemas();
	void setSchemas(List<SCHEMA> schemas);
	
	List<TAB> getTables();
	void setTables(List<TAB> tables);
	
	List<COL> getColumns();
	void setColumns(List<COL> columns);
	
	List<CON> getConstraints();
	void setConstraints(List<CON> constraints);
}