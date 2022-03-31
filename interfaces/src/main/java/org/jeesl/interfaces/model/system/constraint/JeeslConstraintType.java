package org.jeesl.interfaces.model.system.constraint;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.option.JeeslRestDownloadOption;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusFixedCode;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;

public interface JeeslConstraintType <S extends JeeslStatus<L,D,S>,L extends JeeslLang,D extends JeeslDescription,G extends JeeslGraphic<L,D,?,?,?>>
									extends Serializable,EjbPersistable,JeeslStatusFixedCode,JeeslRestDownloadOption,EjbWithCodeGraphic<G>
{
	public static String xmlResourceContainer = "jeesl/db/system/constraint/type.xml";
	
	public static enum Code{unique,precondition};
}