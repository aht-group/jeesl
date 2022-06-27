package org.jeesl.interfaces.model.module.survey.core;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.date.ju.EjbWithRecord;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithRefId;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslSurveyTemplateVersion<L extends JeeslLang, D extends JeeslDescription,
											TEMPLATE extends JeeslSurveyTemplate<L,D,?,TEMPLATE,?,?,?,?,?,?>>
			extends Serializable,EjbSaveable,EjbRemoveable,
					EjbWithRecord,EjbWithRefId,
					EjbWithLang<L>,EjbWithDescription<D>
{
	enum Attributes {template,record,refId}
	
	TEMPLATE getTemplate();
	void setTemplate(TEMPLATE template);
}