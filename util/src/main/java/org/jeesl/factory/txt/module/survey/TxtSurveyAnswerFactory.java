package org.jeesl.factory.txt.module.survey;

import java.util.Objects;

import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyMatrix;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtSurveyAnswerFactory<L extends JeeslLang, D extends JeeslDescription,
									QUESTION extends JeeslSurveyQuestion<L,D,?,?,?,?,?,?,?,OPTION,?>,
									ANSWER extends JeeslSurveyAnswer<L,D,QUESTION,MATRIX,?,OPTION>,
									MATRIX extends JeeslSurveyMatrix<L,D,ANSWER,OPTION>,
									OPTION extends JeeslSurveyOption<L,D>
									>
{
	final static Logger logger = LoggerFactory.getLogger(TxtSurveyAnswerFactory.class);
	
	public TxtSurveyAnswerFactory()
	{
		
	}
	
	public String build(ANSWER ejb)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("[").append(ejb.getId()).append("]");
		
		sb.append(" Q:"); if(Objects.nonNull(ejb.getQuestion())) {sb.append(ejb.getQuestion().getId());} else {sb.append("NULL");}
		
		if(BooleanComparator.active(ejb.getQuestion().getShowBoolean()) && Objects.nonNull(ejb.getValueBoolean())) {sb.append(" boolean:").append(ejb.getValueBoolean());}
		if(ejb.getValueNumber()!=null){sb.append(" number:").append(ejb.getValueNumber());}
		
		if(Objects.nonNull(ejb.getOption())) {sb.append(" option:").append(ejb.getOption().getId());}
		
		if(Objects.nonNull(ejb.getRemark())) {sb.append(" remark:").append(ejb.getRemark());}
		
		return sb.toString();
	}
	
	public static <L extends JeeslLang, D extends JeeslDescription,
					ANSWER extends JeeslSurveyAnswer<L,D,?,?,?,?>
					>
		String debug(ANSWER answer)
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("[").append(answer.getId()).append("]");
		
		if(answer.getValueBoolean()!=null){sb.append(" boolean:").append(answer.getValueBoolean());}
		if(answer.getValueNumber()!=null){sb.append(" number:").append(answer.getValueNumber());}
		
		return sb.toString();
	}
}