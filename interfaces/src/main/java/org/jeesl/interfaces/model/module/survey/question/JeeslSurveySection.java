package org.jeesl.interfaces.model.module.survey.question;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplate;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.bool.EjbWithRendered;
import org.jeesl.interfaces.model.with.primitive.bool.EjbWithVisible;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithLevel;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithRemark;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslSurveySection<L extends JeeslLang, D extends JeeslDescription,
									TEMPLATE extends JeeslSurveyTemplate<L,D,?,TEMPLATE,?,?,?,SECTION,?,?>,
									SECTION extends JeeslSurveySection<L,D,TEMPLATE,SECTION,QUESTION>,
									QUESTION extends JeeslSurveyQuestion<L,D,SECTION,?,?,?,?,?,?,?,?>>
			extends Serializable,EjbSaveable,EjbRemoveable
			,EjbWithCode,EjbWithRemark,EjbWithPosition,EjbWithLevel,EjbWithVisible,EjbWithRendered,
					EjbWithLang<L>,EjbWithDescription<D>
{
	public enum Attributes{template,visible,position}
	
	TEMPLATE getTemplate();
	void setTemplate(TEMPLATE template);
	
	SECTION getSection();
	void setSection(SECTION section);
	
	List<SECTION> getSections();
	void setSections(List<SECTION> sections);
	
	List<QUESTION> getQuestions();
	void setQuestions(List<QUESTION> questions);
	
	String getColumnClasses();
	void setColumnClasses(String columnClasses);
	
	Double getScoreLimit();
	void setScoreLimit(Double scoreLimit);
	
	Double getScoreNormalize();
	void setScoreNormalize(Double scoreNormalize);
	

	Boolean getShowCode();
	void setShowCode(Boolean showCode);
	
	Boolean getShowTopic();
	void setShowTopic(Boolean showTopic);
	
	Boolean getShowQuestion();
	void setShowQuestion(Boolean showQuestion);
	
	Boolean getShowRemark();
	void setShowRemark(Boolean showRemark);
	
	
	Boolean getShowLineBreakQuestion();
	void setShowLineBreakQuestion(Boolean showLineBreakQuestion);
	
	Boolean getShowLineBreakRemark();
	void setShowLineBreakRemark(Boolean showLineBreakRemark);

}