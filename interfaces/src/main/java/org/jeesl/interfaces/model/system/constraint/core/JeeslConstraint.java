package org.jeesl.interfaces.model.system.constraint.core;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNrString;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLangDescription;
import org.jeesl.interfaces.model.with.system.status.JeeslWithLevel;
import org.jeesl.interfaces.model.with.system.status.JeeslWithType;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslConstraint<L extends JeeslLang, D extends JeeslDescription,
									SCOPE extends JeeslConstraintScope<L,D,CATEGORY>,
									CATEGORY extends JeeslStatus<L,D,CATEGORY>,
									CONSTRAINT extends JeeslConstraint<L,D,SCOPE,CATEGORY,CONSTRAINT,LEVEL,TYPE,RESOLUTION>,
									LEVEL extends JeeslConstraintLevel<L,D,LEVEL,?>,
									TYPE extends JeeslStatus<L,D,TYPE>,
									RESOLUTION extends JeeslConstraintResolution<L,D,CONSTRAINT>>
			extends Serializable,EjbWithId,
					EjbRemoveable,EjbPersistable,EjbSaveable,
					EjbWithNonUniqueCode,EjbWithNrString,
					EjbWithPosition,
					EjbWithParentAttributeResolver,
					EjbWithLangDescription<L,D>,
					JeeslWithType<TYPE>,
					JeeslWithLevel<LEVEL>
{
	public static enum Attributes{scope,code};
	public enum Constraint {constraintNotFound}
	
	
	SCOPE getScope();
	void setScope(SCOPE scope);
	
	String getContextMessage();
	void setContextMessage(String contextMessage);
}