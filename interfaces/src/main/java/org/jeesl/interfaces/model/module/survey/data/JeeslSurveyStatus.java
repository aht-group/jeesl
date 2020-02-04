package org.jeesl.interfaces.model.module.survey.data;

import java.io.Serializable;

import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusFixedCode;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.option.JeeslOptionRestDownload;
import org.jeesl.interfaces.model.with.code.EjbWithCode;

import net.sf.ahtutils.interfaces.model.crud.EjbPersistable;

public interface JeeslSurveyStatus <L extends JeeslLang, D extends JeeslDescription,
									S extends JeeslStatus<S,L,D>,
									G extends JeeslGraphic<L,D,?,?,?>>
					extends Serializable,EjbPersistable,
								EjbWithCode,JeeslStatusFixedCode,
								JeeslOptionRestDownload,EjbWithCodeGraphic<G>,
								JeeslStatus<S,L,D>
{	
	public static enum Code{open,preparation};
}