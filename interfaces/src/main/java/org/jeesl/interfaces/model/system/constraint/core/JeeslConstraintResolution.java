package org.jeesl.interfaces.model.system.constraint.core;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLangDescription;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslConstraintResolution<L extends JeeslLang, D extends JeeslDescription,
									CONSTRAINT extends JeeslConstraint<L,D,?,?,CONSTRAINT,?,?,?>>
			extends EjbWithId,
					EjbSaveable,EjbRemoveable,
					EjbWithPosition,
					EjbWithParentAttributeResolver,
					EjbWithLangDescription<L,D>
{
	public static enum Attributes{constraint};
	
	CONSTRAINT getConstraint();
	void setConstraint(CONSTRAINT constraint);
}