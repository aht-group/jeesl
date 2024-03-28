package org.jeesl.interfaces.model.module.survey.question;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.survey.analysis.JeeslSurveyAnalysisQuestion;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScore;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.bool.EjbWithRendered;
import org.jeesl.interfaces.model.with.primitive.bool.EjbWithVisible;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithRemark;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslSurveyQuestion<L extends JeeslLang, D extends JeeslDescription,
										SECTION extends JeeslSurveySection<L,D,?,SECTION,?>,
										CONDITION extends JeeslSurveyCondition<?,QE,OPTION>,
										VALIDATION extends JeeslSurveyValidation<L,D,?,?>,
										QE extends JeeslSurveyQuestionType<L,D,QE,?>,
										SCORE extends JeeslSurveyScore<L,D,?,?>,
										UNIT extends JeeslSurveyQuestionUnit<L,D,UNIT,?>,
										OPTIONS extends JeeslSurveyOptionSet<L,D,?,OPTION>,
										OPTION extends JeeslSurveyOption<L,D>,
										AQ extends JeeslSurveyAnalysisQuestion<L,D,?,?>
										>
			extends Serializable,EjbWithCode,EjbWithRemark,EjbWithPosition,EjbWithVisible,EjbSaveable,EjbRemoveable,
					EjbWithRendered,EjbWithParentAttributeResolver,
					EjbWithLang<L>,EjbWithDescription<D>
{
	public enum Attributes{section,visible,position,optionSet}
	
	public enum Elements{selectOne};
	public enum AttributeTypeCode{text,number,bool};
	
	SECTION getSection();
	void setSection(SECTION section);
	
	String getTopic();
	void setTopic(String topic);
	
	public Map<String,D> getText();
	public void setText(Map<String,D> name);
	
	String getQuestion();
	void setQuestion(String question);
	
	UNIT getUnit();
	void setUnit(UNIT unit); 
	
	OPTIONS getOptionSet();
	void setOptionSet(OPTIONS optionSet);
	
	List<OPTION> getOptions();
	void setOptions(List<OPTION> options);
	boolean verified();
	
	Boolean getCalculateScore();
	void setCalculateScore(Boolean calculateScore);
	
	Double getMinScore();
	void setMinScore(Double maxScore);
	
	Double getMaxScore();
	void setMaxScore(Double maxScore);
	
	Boolean getBonusScore();
	void setBonusScore(Boolean bonusScore);
	
	Boolean getShowBoolean();
	void setShowBoolean(Boolean showBoolean);
	
	Boolean getShowDate();
	void setShowDate(Boolean showDate);
	
	Boolean getShowInteger();
	void setShowInteger(Boolean showInteger);
	
	Boolean getShowDouble();
	void setShowDouble(Boolean showDouble);
	
	Boolean getShowText();
	void setShowText(Boolean showText);
	
	Boolean getShowScore();
	void setShowScore(Boolean showScore);
	
	Boolean getShowRemark();
	void setShowRemark(Boolean showRemark);
	
	Boolean getShowSelectOne();
	void setShowSelectOne(Boolean showSelectOne);
	
	Boolean getShowSelectMulti();
	void setShowSelectMulti(Boolean showSelectMulti);
	
	Boolean getShowMatrix();
	void setShowMatrix(Boolean showMatrix);
	
	List<SCORE> getScores();
	void setScores(List<SCORE> scores);
	
	String getRenderCondition();
	void setRenderCondition(String renderCondition);
	
	List<CONDITION> getConditions();
	void setConditions(List<CONDITION> conditions);
	
	Boolean getMandatory();
	void setMandatory(Boolean mandatory);
	
	Boolean getShowEmptyOption();
	void setShowEmptyOption(Boolean showEmptyOption);
	
	List<VALIDATION> getValidations();
	void setValidations(List<VALIDATION> validations);
}