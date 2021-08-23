package org.jeesl.util.comparator.ejb.module.survey;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurveyAnswerComparator<ANSWER extends JeeslSurveyAnswer<?,?,?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(SurveyAnswerComparator.class);

    public enum Type {position};
    
    public Comparator<ANSWER> factory(Type type)
    {
        Comparator<ANSWER> c = null;
        SurveyAnswerComparator<ANSWER> factory = new SurveyAnswerComparator<ANSWER>();
        switch (type)
        {
            case position: c = factory.new PositionComparator();break;
        }

        return c;
    }

    private class PositionComparator implements Comparator<ANSWER>
    {
        public int compare(ANSWER a, ANSWER b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(a.getQuestion().getSection().getPosition(),b.getQuestion().getSection().getPosition());
			  ctb.append(a.getQuestion().getPosition(),b.getQuestion().getPosition());
			  ctb.append(a.getId(),b.getId());
			  return ctb.toComparison();
        }
    }
}