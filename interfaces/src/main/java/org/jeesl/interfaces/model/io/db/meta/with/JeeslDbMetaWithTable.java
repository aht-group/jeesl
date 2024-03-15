package org.jeesl.interfaces.model.io.db.meta.with;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslDbMetaWithTable<TAB extends JeeslDbMetaTable<?,?,?>>
					extends Serializable,EjbWithId
{
	TAB getTable();
	void setTable(TAB table);
}