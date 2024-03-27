package org.jeesl.factory.xml.module.survey;

import java.util.Objects;

import org.exlp.util.system.DateUtil;
import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.factory.xml.system.util.text.XmlRemarkFactory;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurvey;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScheme;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScore;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplate;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateCategory;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateStatus;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateVersion;
import org.jeesl.interfaces.model.module.survey.correlation.JeeslSurveyCorrelation;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyData;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyMatrix;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyStatus;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOptionSet;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionElement;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionUnit;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.io.db.query.QuerySurvey;
import org.jeesl.model.xml.module.survey.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.aht.Query;

public class XmlAnswerFactory<L extends JeeslLang,D extends JeeslDescription,
								SURVEY extends JeeslSurvey<L,D,SS,TEMPLATE,DATA>,
								SS extends JeeslSurveyStatus<L,D,SS,?>,
								SCHEME extends JeeslSurveyScheme<L,D,TEMPLATE,SCORE>,
								TEMPLATE extends JeeslSurveyTemplate<L,D,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,OPTIONS,?>,
								VERSION extends JeeslSurveyTemplateVersion<L,D,TEMPLATE>,
								TS extends JeeslSurveyTemplateStatus<L,D,TS,?>,
								TC extends JeeslSurveyTemplateCategory<L,D,TC,?>,
								SECTION extends JeeslSurveySection<L,D,TEMPLATE,SECTION,QUESTION>,
								QUESTION extends JeeslSurveyQuestion<L,D,SECTION,?,?,QE,SCORE,UNIT,OPTIONS,OPTION,?>,
								QE extends JeeslSurveyQuestionElement<L,D,QE,?>,
								SCORE extends JeeslSurveyScore<L,D,SCHEME,QUESTION>,
								UNIT extends JeeslSurveyQuestionUnit<L,D,UNIT,?>,
								ANSWER extends JeeslSurveyAnswer<L,D,QUESTION,MATRIX,DATA,OPTION>,
								MATRIX extends JeeslSurveyMatrix<L,D,ANSWER,OPTION>,
								DATA extends JeeslSurveyData<L,D,SURVEY,ANSWER,CORRELATION>,
								OPTIONS extends JeeslSurveyOptionSet<L,D,TEMPLATE,OPTION>,
								OPTION extends JeeslSurveyOption<L,D>,
								CORRELATION extends JeeslSurveyCorrelation<DATA>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlAnswerFactory.class);
		
	private Answer q;
	
	private XmlQuestionFactory<L,D,SCHEME,QUESTION,QE,SCORE,UNIT,OPTIONS,OPTION> xfQuestion;
	private XmlDataFactory<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> xfData;
	private XmlOptionFactory<L,D,OPTION> xfOption;
	private XmlMatrixFactory<QUESTION,ANSWER,MATRIX,OPTION> xfMatrix;
	
	public XmlAnswerFactory(Query q){this(q.getLang(),q.getAnswer());}
	public XmlAnswerFactory(QuerySurvey q){this(q.getLocaleCode(),q.getAnswer());}
	public XmlAnswerFactory(String localeCode, Answer q)
	{
		this.q=q;
		
		if(Objects.nonNull(q.getQuestion())) {xfQuestion = new XmlQuestionFactory<L,D,SCHEME,QUESTION,QE,SCORE,UNIT,OPTIONS,OPTION>(localeCode,q.getQuestion());}
		if(Objects.nonNull(q.getData())) {xfData = new XmlDataFactory<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION>(localeCode,q.getData());}
		if(Objects.nonNull(q.getOption())) {xfOption = new XmlOptionFactory<L,D,OPTION>(localeCode,q.getOption());}
		if(Objects.nonNull(q.getMatrix())) {xfMatrix = new XmlMatrixFactory<>(localeCode,q.getMatrix());}
	}
	
	public void lazyLoad(JeeslSurveyCoreFacade<L,D,?,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> fSurvey)
	{
		if(Objects.nonNull(q.getMatrix())) {xfMatrix.lazyLoad(fSurvey);}
	}
	
	public Answer build(ANSWER ejb)
	{		
		Answer xml = new Answer();
		if(Objects.nonNull(q.getId())) {xml.setId(ejb.getId());}
		
		if(Objects.nonNull(q.getQuestion())) {xml.setQuestion(xfQuestion.build(ejb.getQuestion()));}
		if(Objects.nonNull(q.getData())) {xml.setData(xfData.build(ejb.getData()));}
		
		if(Objects.nonNull(q.isValueBoolean()) && ejb.getQuestion().getShowBoolean() && ejb.getValueBoolean()!=null){xml.setValueBoolean(ejb.getValueBoolean());}
		if(Objects.nonNull(q.getValueNumber()) && ejb.getQuestion().getShowInteger() && ejb.getValueNumber()!=null){xml.setValueNumber(ejb.getValueNumber());}
		if(Objects.nonNull(q.getValueDouble()) && ejb.getQuestion().getShowDouble() && ejb.getValueDouble()!=null){xml.setValueDouble(ejb.getValueDouble());}
		if(Objects.nonNull(q.getValueDate()) && BooleanComparator.active(ejb.getQuestion().getShowDate()) && ejb.getValueDate()!=null){xml.setValueDate(DateUtil.toXmlGc(ejb.getValueDate()));}
		
		if(Objects.nonNull(q.getScore()) && BooleanComparator.active(ejb.getQuestion().getShowScore()) && ejb.getScore()!=null){xml.setScore(ejb.getScore());}
		if(Objects.nonNull(q.getAnswer()) && ejb.getQuestion().getShowText() && ejb.getValueText()!=null){xml.setAnswer(net.sf.ahtutils.factory.xml.text.XmlAnswerFactory.build(ejb.getValueText()));}
		if(Objects.nonNull(q.getRemark()) && ejb.getQuestion().getShowRemark() && ejb.getRemark()!=null){xml.setRemark(XmlRemarkFactory.build(ejb.getRemark()));}
	
		if(Objects.nonNull(q.getOption()) && BooleanComparator.active(ejb.getQuestion().getShowSelectOne())){if(ejb.getOption()!=null) {xml.setOption(xfOption.build(ejb.getOption()));}}
		if(Objects.nonNull(q.getMatrix()) && BooleanComparator.active(ejb.getQuestion().getShowMatrix())){xml.setMatrix(xfMatrix.build(ejb));}
		
		return xml;
	}
	
	public static Answer id()
	{
		Answer xml = new Answer();
		xml.setId(0l);
		return xml;
	}
}