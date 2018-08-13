package org.jeesl.controller.processor.survey;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jeesl.controller.processor.finance.AmountRounder;
import org.jeesl.factory.ejb.module.survey.EjbSurveyAnswerFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyQuestionFactory;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.util.comparator.pojo.BooleanComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.StringUtil;

public class SurveyScoreProcessor <SECTION extends JeeslSurveySection<?,?,?,SECTION,QUESTION>,
									QUESTION extends JeeslSurveyQuestion<?,?,SECTION,?,?,?,?,?,?>,
									ANSWER extends JeeslSurveyAnswer<?,?,QUESTION,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(SurveyScoreProcessor.class);
	
	private EjbSurveyQuestionFactory<?,?,SECTION,QUESTION,?,?,?,?> efQuestion;
	private EjbSurveyAnswerFactory<SECTION,QUESTION,ANSWER,?,?,?> efAnswer;
	
	public SurveyScoreProcessor(EjbSurveyQuestionFactory<?,?,SECTION,QUESTION,?,?,?,?> efQuestion,
								EjbSurveyAnswerFactory<SECTION,QUESTION,ANSWER,?,?,?> efAnswer)
	{
		this.efQuestion=efQuestion;
		this.efAnswer=efAnswer;
	}
	
	public double score(Map<QUESTION,ANSWER> answers) {return score(answers,null);}
	public double score(Map<QUESTION,ANSWER> answers, Map<SECTION,Boolean> mapContext)
	{		
		List<QUESTION> questions = new ArrayList<QUESTION>(answers.keySet());
		List<SECTION> sections = efQuestion.toSection(questions);
		
		logger.info(StringUtil.stars());
		logger.info("Processing "+sections.size()+" sections with "+questions.size()+" questions");
		
		BigDecimal result = new BigDecimal(0);
		for(SECTION section : sections)
		{
			boolean visible = true;
			if(mapContext!=null) {visible = mapContext.containsKey(section) && mapContext.get(section);}
			
			if(visible)
			{
				BigDecimal sectionScore = new BigDecimal(0);
				double maxScore = 0;
				
				for(QUESTION q : efQuestion.toSectionQuestions(section, questions))
				{
					if(q.getMaxScore()!=null) {maxScore = maxScore + q.getMaxScore();}
				}
				
				for(ANSWER a : efAnswer.toSectionAnswers(section,answers))
				{
					if(a.getQuestion().getCalculateScore()!=null && a.getQuestion().getCalculateScore() && a.getScore()!=null)
					{
						if(a.getScore()!=null)
						{
							sectionScore = sectionScore.add(new BigDecimal(a.getScore()));
						}
						if(BooleanComparator.active(a.getQuestion().getBonusScore()) && a.getScoreBonus()!=null)
						{
							sectionScore = sectionScore.add(new BigDecimal(a.getScoreBonus()));
						}
					}
				}

				StringBuffer sb = new StringBuffer();
				sb.append("Score for Section ");
				sb.append(section.toString());
				
				if(section.getScoreNormalize()!=null)
				{
					double x = sectionScore.doubleValue() * section.getScoreNormalize() / maxScore;
					sb.append(" Normalizing to "+section.getScoreNormalize()+" max:"+maxScore+" for:"+sectionScore+" normalized:"+AmountRounder.two(x));
					result = result.add(new BigDecimal(x));
				}
				else
				{
					sb.append(" Sum: ").append(sectionScore);
					result = result.add(sectionScore);
				}
				logger.info(sb.toString());
			}
		}

		return AmountRounder.two(result.doubleValue());
	}
}