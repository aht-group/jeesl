package org.jeesl.interfaces.model.io.db.pg.statement;

import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
public interface JeeslDbStatementGroup <SYSTEM extends JeeslIoSsiSystem<?,?>>
							extends EjbWithId,EjbSaveable,EjbRemoveable,
									EjbWithParentAttributeResolver,
									EjbWithPosition,EjbWithCode,EjbWithName
									
{
	public static enum Attributes{system};
	
	SYSTEM getSystem();
	void setSystem(SYSTEM system);
	
	String getRemark();
	void setRemark(String remark);
}