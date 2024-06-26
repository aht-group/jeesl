package org.jeesl.factory.xlsx.module.survey;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.factory.xlsx.io.report.XlsCellFactory;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyMatrix;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XlsSurveyAnswerFactory <L extends JeeslLang,
							QUESTION extends JeeslSurveyQuestion<L,?,?,?,?,?,?,?,?,OPTION,?>,
							ANSWER extends JeeslSurveyAnswer<L,?,QUESTION,MATRIX,?,OPTION>,
							MATRIX extends JeeslSurveyMatrix<L,?,ANSWER,OPTION>,
							OPTION extends JeeslSurveyOption<L,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XlsSurveyAnswerFactory.class);

	private final String localeCode;
	private final CellStyle style;
	
	public XlsSurveyAnswerFactory(String localeCode, CellStyle style)
	{
		this.localeCode=localeCode;
		this.style=style;
	}
	
	public void build(Row row, MutableInt colNr, ANSWER answer)
	{
		if(BooleanComparator.active(answer.getQuestion().getShowBoolean())){XlsCellFactory.build(row, colNr, style, answer.getValueBoolean(), 1);}
		else if(BooleanComparator.active(answer.getQuestion().getShowDouble())){XlsCellFactory.build(row, colNr, style, answer.getValueDouble(), 1);}
		else if(BooleanComparator.active(answer.getQuestion().getShowInteger())){XlsCellFactory.build(row, colNr, style, answer.getValueNumber(), 1);}
		else if(BooleanComparator.active(answer.getQuestion().getShowText())){XlsCellFactory.build(row, colNr, style, answer.getValueText(), 1);}
		else if(BooleanComparator.active(answer.getQuestion().getShowSelectOne()))
		{
			if(answer.getOption()!=null)
			{
				if(localeCode!=null && answer.getOption().getName().containsKey(localeCode)) {XlsCellFactory.build(row, colNr, style, answer.getOption().getName().get(localeCode).getLang(), 1);}
				else {XlsCellFactory.build(row, colNr, style, answer.getOption().getCode(), 1);}
			}
			else{XlsCellFactory.build(row, colNr, style, "", 1);}
		}
		else if(BooleanComparator.active(answer.getQuestion().getShowDate())){XlsCellFactory.build(row, colNr, style, answer.getValueDate(), 1);}
		else
		{
			XlsCellFactory.build(row, colNr, style, "XXXXX", 1);
		}
	}
	
	public void build(Row row, MutableInt colNr, QUESTION question, MATRIX matrix)
	{
		if(BooleanComparator.active(question.getShowBoolean())){XlsCellFactory.build(row, colNr, style, matrix.getValueBoolean(), 1);}
		else if(BooleanComparator.active(question.getShowDouble())){XlsCellFactory.build(row, colNr, style, matrix.getValueDouble(), 1);}
		else if(BooleanComparator.active(question.getShowInteger())){XlsCellFactory.build(row, colNr, style, matrix.getValueNumber(), 1);}
		else if(BooleanComparator.active(question.getShowText())){XlsCellFactory.build(row, colNr, style, matrix.getValueText(), 1);}
		else if(BooleanComparator.active(question.getShowSelectOne()))
		{
			if(matrix.getOption()!=null){XlsCellFactory.build(row, colNr, style, matrix.getOption().getCode(), 1);}
			else{XlsCellFactory.build(row, colNr, style, "", 1);}
		}
//		else if(BooleanComparator.active(question.getShowDate())){XlsCellFactory.build(row, colNr, style, matrix.getValueDate(), 1);}
		else
		{
			XlsCellFactory.build(row, colNr, style, "XXXXX", 1);
		}
	}
}