package org.jeesl.factory.xml.module.survey;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.exlp.util.system.DateUtil;
import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.api.facade.module.survey.JeeslSurveyTemplateFacade;
import org.jeesl.factory.xml.system.status.XmlStatusFactory;
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
import org.jeesl.model.xml.module.survey.Data;
import org.jeesl.model.xml.module.survey.Survey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlSurveyFactory<L extends JeeslLang,D extends JeeslDescription,
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
	final static Logger logger = LoggerFactory.getLogger(XmlSurveyFactory.class);
	
	private JeeslSurveyCoreFacade<L,D,?,SURVEY,SS,SCHEME,VERSION,TC,SECTION,QUESTION,SCORE,ANSWER,MATRIX,DATA,CORRELATION> fSurvey;
	
	private final String localeCode;
	private final Survey q;
	
	private XmlStatusFactory<L,D,SS> xfStatus;
	private XmlDataFactory<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> xfData;
	private XmlTemplateFactory<L,D,SURVEY,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,OPTIONS,OPTION> xfTemplate;
	
	public XmlSurveyFactory(String localeCode, Survey q)
	{
		this.localeCode=localeCode;
		this.q=q;
		if(Objects.nonNull(q.getStatus())) {xfStatus = new XmlStatusFactory<>(q.getStatus());}
		if(Objects.nonNull(q.getTemplate())) {xfTemplate = new XmlTemplateFactory<>(localeCode,q.getTemplate());}
		if(ObjectUtils.isNotEmpty(q.getData())) {xfData = new XmlDataFactory<>(localeCode,q.getData().get(0));}
	}
	
	public void lazyLoad(JeeslSurveyTemplateFacade<SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,OPTIONS,OPTION> fTemplate,
				JeeslSurveyCoreFacade<L,D,?,SURVEY,SS,SCHEME,VERSION,TC,SECTION,QUESTION,SCORE,ANSWER,MATRIX,DATA,CORRELATION> fSurvey,
				Class<SURVEY> cSurvey,
				Class<SECTION> cSection
				)
	{
		this.fSurvey=fSurvey;
		
		if(ObjectUtils.isNotEmpty(q.getData())) {xfData.lazyLoad(fSurvey,fTemplate);}
	}
	
	public Survey build(SURVEY ejb)
	{
		if(fSurvey!=null){ejb = fSurvey.load(ejb);}
		
		Survey xml = new Survey();
		if(Objects.nonNull(q.getId())) {xml.setId(ejb.getId());}
		if(Objects.nonNull(q.getName())) {xml.setName(ejb.getName().get(localeCode).getLang());}
		if(Objects.nonNull(q.getValidFrom())) {xml.setValidFrom(DateUtil.toXmlGc(ejb.getStartDate()));}
		if(Objects.nonNull(q.getValidTo())) {xml.setValidTo(DateUtil.toXmlGc(ejb.getEndDate()));}
		
		if(Objects.nonNull(q.getStatus())) {xml.setStatus(xfStatus.build(ejb.getStatus()));}
		if(Objects.nonNull(q.getTemplate())) {xml.setTemplate(xfTemplate.build(ejb.getTemplate()));}
		
		if(ObjectUtils.isNotEmpty(q.getData()))
		{
			for(DATA data : ejb.getSurveyData())
			{
				xml.getData().add(xfData.build(data));
			}
		}
		return xml;
	}
	
	public static Survey build() {return new Survey();}
	public static Survey id()
	{
		Survey xml = build();
		xml.setId(0l);
		return xml;
	}
	public static Survey build(Data data)
	{
		Survey xml = build();
		xml.getData().add(data);
		return xml;
	}
}