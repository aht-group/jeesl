package org.jeesl.interfaces.model.module.survey.question;

import java.io.Serializable;

import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;

import net.sf.ahtutils.interfaces.model.crud.EjbPersistable;

public interface JeeslSurveyQuestionUnit <L extends JeeslLang, D extends JeeslDescription,
											S extends JeeslStatus<S,L,D>,
											G extends JeeslGraphic<L,D,?,?,?>>
					extends Serializable,EjbPersistable,
								EjbWithCodeGraphic<G>,
								JeeslStatus<S,L,D>
{	

}