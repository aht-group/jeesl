package org.jeesl.interfaces.model.module.survey.analysis;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.domain.JeeslDomainQuery;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;

public interface JeeslSurveyAnalysisTool<L extends JeeslLang, D extends JeeslDescription,
											QE extends JeeslStatus<L,D,QE>,
											QUERY extends JeeslDomainQuery<L,D,?,?>,
											DATTRIBUTE extends JeeslRevisionAttribute<L,D,?,?,?>,
											AQ extends JeeslSurveyAnalysisQuestion<L,D,?,?>,
											ATT extends JeeslStatus<L,D,ATT>>
			extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,
						EjbWithParentAttributeResolver,EjbWithPositionVisible
{
	public enum Attributes{analysisQuestion}
	public enum Elements{selectOne,bool,text,remark}
	
	AQ getAnalysisQuestion();
	void setAnalysisQuestion(AQ analysisQuestion);
	
	ATT getType();
	void setType(ATT type);
	
	QE getElement();
	void setElement(QE element);
	
	DATTRIBUTE getAttribute();
	void setAttribute(DATTRIBUTE attribute);
	
	QUERY getQuery();
	void setQuery(QUERY query);
	
	Integer getCacheExpire();
	void setCacheExpire(Integer cacheExpire);
}