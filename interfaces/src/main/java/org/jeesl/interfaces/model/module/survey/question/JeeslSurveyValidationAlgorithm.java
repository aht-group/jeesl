package org.jeesl.interfaces.model.module.survey.question;

import java.io.Serializable;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.with.code.EjbWithCode;

import net.sf.ahtutils.interfaces.model.behaviour.EjbSaveable;
import net.sf.ahtutils.interfaces.model.crud.EjbRemoveable;
import net.sf.ahtutils.interfaces.model.with.position.EjbWithPositionVisible;
import net.sf.ahtutils.model.interfaces.with.EjbWithDescription;
import net.sf.ahtutils.model.interfaces.with.EjbWithLang;

public interface JeeslSurveyValidationAlgorithm<L extends JeeslLang, D extends JeeslDescription>
			extends Serializable,EjbSaveable,EjbRemoveable,
					EjbWithCode,EjbWithPositionVisible,EjbWithLang<L>,EjbWithDescription<D>
{
	public enum Attributes{validation}

	String getConfig();
	void setConfig(String config);
}