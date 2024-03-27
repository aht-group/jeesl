package org.jeesl.factory.xml.module.survey;

import java.util.Objects;

import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.factory.xml.system.util.text.XmlRemarkFactory;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScheme;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScore;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOptionSet;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionElement;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionUnit;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.io.db.query.QuerySurvey;
import org.jeesl.model.xml.module.survey.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.factory.xml.status.XmlUnitFactory;

public class XmlQuestionFactory<L extends JeeslLang, D extends JeeslDescription,
								SCHEME extends JeeslSurveyScheme<L,D,?,SCORE>,
								QUESTION extends JeeslSurveyQuestion<L,D,?,?,?,QE,SCORE,UNIT,OPTIONS,OPTION,?>,
								QE extends JeeslSurveyQuestionElement<L,D,QE,?>,
								SCORE extends JeeslSurveyScore<L,D,SCHEME,QUESTION>,
								UNIT extends JeeslSurveyQuestionUnit<L,D,UNIT,?>,
								OPTIONS extends JeeslSurveyOptionSet<L,D,?,OPTION>,
								OPTION extends JeeslSurveyOption<L,D>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlQuestionFactory.class);
		
	private JeeslSurveyCoreFacade<L,D,?,?,?,SCHEME,?,?,?,?,QUESTION,SCORE,?,?,?,OPTIONS,OPTION,?> fSurvey;
	
	private String localeCode;
	private Question q;
	
	private XmlScoreFactory<SCHEME,QUESTION,SCORE> xfScore;
	private XmlOptionsFactory<L,D,QUESTION,OPTION> xfOptions;
		
	public XmlQuestionFactory(QuerySurvey q){this(q.getLocaleCode(),q.getQuestion());}
	public XmlQuestionFactory(String localeCode, Question q)
	{
		this.localeCode=localeCode;
		this.q=q;
		if(Objects.nonNull(q.getScore())) {xfScore = new XmlScoreFactory<SCHEME,QUESTION,SCORE>(q.getScore());}
		if(Objects.nonNull(q.getOptions())) {xfOptions = new XmlOptionsFactory<L,D,QUESTION,OPTION>(localeCode,q.getOptions());}
	}
	
	public void lazyLoad(JeeslSurveyCoreFacade<L,D,?,?,?,SCHEME,?,?,?,?,QUESTION,SCORE,?,?,?,OPTIONS,OPTION,?> fSurvey)
	{
		this.fSurvey=fSurvey;
	}
	
	public Question build(QUESTION ejb)
	{
		if(fSurvey!=null){ejb = fSurvey.load(ejb);}
		
		Question xml = new Question();
		if(Objects.nonNull(q.getId())) {xml.setId(ejb.getId());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(ejb.getPosition());}
		if(Objects.nonNull(q.isVisible())) {xml.setVisible(ejb.isVisible());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getTopic())) {xml.setTopic(ejb.getTopic());}
		
		// MultiLang Issue
		//	if(q.isSetQuestion() && ejb.getQuestion()!=null){xml.setQuestion(net.sf.ahtutils.factory.xml.text.XmlQuestionFactory.build(ejb.getQuestion()));}
		if(Objects.nonNull(q.getQuestion()) && ejb.getName().containsKey(localeCode)){xml.setQuestion(net.sf.ahtutils.factory.xml.text.XmlQuestionFactory.build(ejb.getName().get(localeCode).getLang()));}
		if(Objects.nonNull(q.getRemark()) && ejb.getRemark()!=null){xml.setRemark(XmlRemarkFactory.build(ejb.getRemark()));}
		
		if(Objects.nonNull(q.getUnit()) && ejb.getUnit()!=null)
		{
			XmlUnitFactory f = new XmlUnitFactory(localeCode,q.getUnit());
			xml.setUnit(f.build(ejb.getUnit()));
		}
		
		if(Objects.nonNull(q.isShowBoolean())) {if(ejb.getShowBoolean()!=null){xml.setShowBoolean(ejb.getShowBoolean());}else{xml.setShowBoolean(false);}}
		if(Objects.nonNull(q.isShowInteger())) {if(ejb.getShowInteger()!=null){xml.setShowInteger(ejb.getShowInteger());}else{xml.setShowInteger(false);}}
		if(Objects.nonNull(q.isShowDouble())) {if(ejb.getShowDouble()!=null){xml.setShowDouble(ejb.getShowDouble());}else{xml.setShowDouble(false);}}
		if(Objects.nonNull(q.isShowText())) {if(ejb.getShowText()!=null){xml.setShowText(ejb.getShowText());}else{xml.setShowText(false);}}
		if(Objects.nonNull(q.isShowScore())) {if(ejb.getShowScore()!=null){xml.setShowScore(ejb.getShowScore());}else{xml.setShowScore(false);}}
		if(Objects.nonNull(q.isShowRemark())) {if(ejb.getShowRemark()!=null){xml.setShowRemark(ejb.getShowRemark());}else{xml.setShowRemark(false);}}
		
		if(Objects.nonNull(q.getScore())) {xml.setScore(xfScore.build(ejb));}
		
		if(Objects.nonNull(q.isShowSelectOne())) {if(ejb.getShowSelectOne()!=null){xml.setShowSelectOne(ejb.getShowSelectOne());}else{xml.setShowSelectOne(false);}}
		
		if(Objects.nonNull(q.getOptions())) {xml.setOptions(xfOptions.build(ejb));}
		
		return xml;
	}
	
	public static Question id() {return id(0);}
	public static Question id(long id)
	{
		Question xml = new Question();
		xml.setId(id);
		return xml;
	}
}