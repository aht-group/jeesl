package org.jeesl.interfaces.model.io.db.pg.statement;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.date.jt.JeeslWithRecordDateTime;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
public interface JeeslDbStatement extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,
										EjbWithNonUniqueCode,JeeslWithRecordDateTime
{
	public static enum Attributes{xx};
	
	
}