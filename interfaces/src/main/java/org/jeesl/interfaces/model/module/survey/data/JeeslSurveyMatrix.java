package org.jeesl.interfaces.model.module.survey.data;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslSurveyMatrix<L extends JeeslLang, D extends JeeslDescription,
									ANSWER extends JeeslSurveyAnswer<L,D,?,?,?,OPTION>,
									OPTION extends JeeslSurveyOption<L,D>>
			extends Serializable,EjbSaveable,EjbWithParentAttributeResolver
{
	public enum Attributes{answer}
	
	ANSWER getAnswer();
	void setAnswer(ANSWER answer);
	
	OPTION getRow();
	void setRow(OPTION row);
	
	OPTION getColumn();
	void setColumn(OPTION column);
	
	OPTION getOption();
	void setOption(OPTION column);
	
	Boolean getValueBoolean();
	void setValueBoolean(Boolean valueBoolean);
	
	Double getValueDouble();
	void setValueDouble(Double valueDouble);
	
	Integer getValueNumber();
	void setValueNumber(Integer valueNumber);
	
	String getValueText();
	void setValueText(String valueText);
}