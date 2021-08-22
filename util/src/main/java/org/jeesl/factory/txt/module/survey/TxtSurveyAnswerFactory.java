package org.jeesl.factory.txt.module.survey;

import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyMatrix;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtSurveyAnswerFactory<L extends JeeslLang, D extends JeeslDescription,
									ANSWER extends JeeslSurveyAnswer<L,D,?,MATRIX,?,OPTION>,
									MATRIX extends JeeslSurveyMatrix<L,D,ANSWER,OPTION>,
									OPTION extends JeeslSurveyOption<L,D>
									>
{
	final static Logger logger = LoggerFactory.getLogger(TxtSurveyAnswerFactory.class);
	
	public String build(ANSWER answer)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("[").append(answer.getId()).append("]");
		
		if(answer.getValueBoolean()!=null){sb.append(" boolean:").append(answer.getValueBoolean());}
		if(answer.getValueNumber()!=null){sb.append(" number:").append(answer.getValueNumber());}
		
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