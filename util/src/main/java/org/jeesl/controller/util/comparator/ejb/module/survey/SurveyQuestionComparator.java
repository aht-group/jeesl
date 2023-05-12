package org.jeesl.controller.util.comparator.ejb.module.survey;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurveyQuestionComparator<QUESTION extends JeeslSurveyQuestion<?,?,?,?,?,?,?,?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(SurveyQuestionComparator.class);

    public enum Type {position};
    
    public Comparator<QUESTION> factory(Type type)
    {
        Comparator<QUESTION> c = null;
        SurveyQuestionComparator<QUESTION> factory = new SurveyQuestionComparator<QUESTION>();
        switch (type)
        {
            case position: c = factory.new PositionComparator();break;
        }

        return c;
    }

    private class PositionComparator implements Comparator<QUESTION>
    {
        public int compare(QUESTION a, QUESTION b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(a.getSection().getPosition(),b.getSection().getPosition());
			  ctb.append(a.getPosition(),b.getPosition());
			  ctb.append(a.getId(),b.getId());
			  return ctb.toComparison();
        }
    }
}