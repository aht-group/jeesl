package org.jeesl.interfaces.model.system.constraint;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.option.JeeslOptionUploadable;
import org.jeesl.interfaces.model.system.locale.option.JeeslRestDownloadOption;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusFixedCode;

public interface JeeslConstraintType <L extends JeeslLang,D extends JeeslDescription,
										S extends JeeslStatus<L,D,S>,
										G extends JeeslGraphic<L,D,?,?,?>>
									extends Serializable,EjbPersistable,JeeslStatusFixedCode,
												JeeslRestDownloadOption,JeeslOptionUploadable,
												EjbWithCodeGraphic<G>,
												JeeslStatus<L,D,S>
{
	public static String xmlResourceContainer = "jeesl/db/system/constraint/type.xml";
	
	public static enum Code{unique,precondition};
}