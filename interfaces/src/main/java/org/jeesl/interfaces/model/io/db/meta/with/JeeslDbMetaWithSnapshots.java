package org.jeesl.interfaces.model.io.db.meta.with;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSnapshot;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslDbMetaWithSnapshots<SNAP extends JeeslDbMetaSnapshot<?,?,?,?,?>>
					extends Serializable,EjbWithId
{
	List<SNAP> getSnapshots();
	void setSnapshots(List<SNAP> snapshots);
}