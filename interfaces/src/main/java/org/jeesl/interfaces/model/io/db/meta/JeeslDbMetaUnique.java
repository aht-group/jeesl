package org.jeesl.interfaces.model.io.db.meta;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslDbMetaUnique<COL extends JeeslDbMetaColumn<?,?,?>,
									CON extends JeeslDbMetaConstraint<?,?,COL,?,?>>
					extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,
							EjbWithPosition
{
	public static enum Attributes{constraint,column};
	
	CON getConstraint();
	void setConstraint(CON constraint);
	
	COL getColumn();
	void setColumn(COL column);
}