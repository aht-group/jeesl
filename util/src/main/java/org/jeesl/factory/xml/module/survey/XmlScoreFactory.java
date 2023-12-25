package org.jeesl.factory.xml.module.survey;

import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScheme;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScore;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.model.xml.module.survey.Score;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlScoreFactory<
				SCHEME extends JeeslSurveyScheme<?,?,?,SCORE>,
				
				QUESTION extends JeeslSurveyQuestion<?,?,?,?,?,?,SCORE,?,?,?,?>,
				
				SCORE extends JeeslSurveyScore<?,?,SCHEME,QUESTION>
				>
{
	final static Logger logger = LoggerFactory.getLogger(XmlScoreFactory.class);
	
//	private JeeslSurveyFacade<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION,DOMAIN,PATH,DENTITY,ANALYSIS,AQ,AT,ATT> fSurvey;
//	private Class<SURVEY> cSurvey;
	
	private final Score q;
		
	public XmlScoreFactory(Score q)
	{
		this.q=q;
	}
	
	public Score build(QUESTION ejb)
	{
		Score xml = build();
		
		if(q.isSetMax() && ejb.getMaxScore()!=null){xml.setMax(ejb.getMaxScore());}
		
		return xml;
	}
	
	public static Score build()
	{
		Score xml = new Score();
		return xml;
	}
}