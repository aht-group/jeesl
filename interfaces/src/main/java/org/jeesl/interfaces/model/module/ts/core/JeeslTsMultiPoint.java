package org.jeesl.interfaces.model.module.ts.core;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionParent;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslTsMultiPoint <L extends JeeslLang, D extends JeeslDescription,
									SCOPE extends JeeslTsScope<L,D,?,?,UNIT,?,?>,
									UNIT extends JeeslStatus<L,D,UNIT>,
									G extends JeeslGraphic<?,?,?>>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,EjbWithCode,EjbWithNonUniqueCode,
				EjbWithParentAttributeResolver,EjbWithPositionParent,
				EjbWithLang<L>,EjbWithDescription<D>
{
	public enum Attributes{scope,code,position,visible}

	SCOPE getScope();
	void setScope(SCOPE scope);

	UNIT getUnit();
	void setUnit(UNIT unit);

	Boolean getVisible();
	void setVisible(Boolean visible);

	String getColor1();
	void setColor1(String color1);
}