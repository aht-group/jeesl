package org.jeesl.interfaces.model.system.constraint.algorithm;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionParent;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLangDescription;
import org.jeesl.interfaces.model.with.system.status.JeeslWithCategory;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslConstraintAlgorithm<L extends JeeslLang, D extends JeeslDescription,
									CATEGORY extends JeeslConstraintAlgorithmGroup<L,D,CATEGORY,?>
									>
			extends Serializable,EjbSaveable,EjbRemoveable,
					EjbWithId,EjbWithCode,
					JeeslWithCategory<CATEGORY>,
					EjbWithPosition,EjbWithPositionParent,
					EjbWithLangDescription<L,D>
{
	public enum Attributes{category}
}