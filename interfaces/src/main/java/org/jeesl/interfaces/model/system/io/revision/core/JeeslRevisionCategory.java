package org.jeesl.interfaces.model.system.io.revision.core;

import java.io.Serializable;

import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.option.JeeslOptionRestDownload;
import org.jeesl.interfaces.model.system.with.code.EjbWithCode;

import net.sf.ahtutils.interfaces.model.crud.EjbPersistable;
import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.interfaces.model.status.UtilsStatusFixedCode;

public interface JeeslRevisionCategory <L extends UtilsLang, D extends UtilsDescription,
								S extends UtilsStatus<S,L,D>,
								G extends JeeslGraphic<L,D,?,?,?>>
							extends Serializable,EjbPersistable,
									EjbWithCode,UtilsStatusFixedCode,
									JeeslOptionRestDownload,
									EjbWithCodeGraphic<G>,UtilsStatus<S,L,D>
{	
	public enum Code{pdf,docx,xlsx,xml,csv,txt,jpg,png,zip,unknown}
}