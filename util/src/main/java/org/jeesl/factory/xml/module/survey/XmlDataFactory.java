package org.jeesl.factory.xml.module.survey;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.api.facade.module.survey.JeeslSurveyTemplateFacade;
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
import org.jeesl.model.xml.jeesl.QuerySurvey;
import org.jeesl.model.xml.module.survey.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlDataFactory<L extends JeeslLang,D extends JeeslDescription,
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
							OPTIONS extends JeeslSurveyOptionSet<L,D,TEMPLATE,OPTION>,OPTION extends JeeslSurveyOption<L,D>,
							CORRELATION extends JeeslSurveyCorrelation<DATA>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlDataFactory.class);
		
	private JeeslSurveyCoreFacade<L,D,?,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> fSurvey;
	private JeeslSurveyTemplateFacade<L,D,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,OPTIONS,OPTION> fTemplate;
	
	private XmlSurveyFactory<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> xfSurvey;
	private XmlCorrelationFactory<CORRELATION> xfCorrelation;
	private XmlAnswerFactory<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> xfAnswer;
	private XmlSectionFactory<L,D,SCHEME,SECTION,QUESTION,QE,SCORE,UNIT,OPTIONS,OPTION> xfSection;
	
	private Data q;
	
	public XmlDataFactory(QuerySurvey query){this(query.getLocaleCode(),query.getData());}
	public XmlDataFactory(String localeCode, Data q)
	{
		this.q=q;
		
		if(Objects.nonNull(q.getSurvey())) {xfSurvey = new XmlSurveyFactory<>(localeCode,q.getSurvey());}
		if(Objects.nonNull(q.getCorrelation())) {xfCorrelation = new XmlCorrelationFactory<>(q.getCorrelation());}
		if(ObjectUtils.isNotEmpty(q.getSection())) {xfSection = new XmlSectionFactory<>(localeCode,q.getSection().get(0));}
		if(ObjectUtils.isNotEmpty(q.getAnswer())) {xfAnswer = new XmlAnswerFactory<>(localeCode,q.getAnswer().get(0));}
	}
	
	public void lazyLoad(JeeslSurveyCoreFacade<L,D,?,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> fSurvey,
						JeeslSurveyTemplateFacade<L,D,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,OPTIONS,OPTION> fTemplate)
	{
		this.fTemplate=fTemplate;
		this.fSurvey=fSurvey;
		if(ObjectUtils.isNotEmpty(q.getSection())) {xfSection.lazyLoad(fSurvey);}
		if(ObjectUtils.isNotEmpty(q.getAnswer())) {xfAnswer.lazyLoad(fSurvey);}
	}
	
	public Data build(DATA ejb)
	{		
		if(fSurvey!=null){ejb = fSurvey.load(ejb);}
		
		Data xml = build();
		if(Objects.nonNull(q.getId())) {xml.setId(ejb.getId());}
		
		if(Objects.nonNull(q.getSurvey())) {xml.setSurvey(xfSurvey.build(ejb.getSurvey()));}
		if(Objects.nonNull(q.getCorrelation())) {xml.setCorrelation(xfCorrelation.build(ejb.getCorrelation()));}
		
		if(ObjectUtils.isNotEmpty(q.getSection()))
		{
			TEMPLATE template = ejb.getSurvey().getTemplate();
			if(fTemplate!=null){template = fTemplate.load(template,false,false);}
			for(SECTION section : template.getSections())
			{
				xml.getSection().add(xfSection.build(section));
			}
		}
		
		if(ObjectUtils.isNotEmpty(q.getAnswer()))
		{
			for(ANSWER answer : ejb.getAnswers())
			{
				xml.getAnswer().add(xfAnswer.build(answer));
			}
		}
		
		
		
		return xml;
	}
	
	public static Data id()
	{
		Data xml = build();
		xml.setId(0l);
		return xml;
	}
	
	public static Data build()
	{
		Data xml = new Data();
		return xml;
	}
}