package org.jeesl.factory.xml.module.survey;

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
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.module.survey.Data;
import org.jeesl.model.xml.module.survey.Survey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.DateUtil;

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
	
	private JeeslSurveyCoreFacade<L,D,?,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> fSurvey;
	private Class<SURVEY> cSurvey;
	
	private final String localeCode;
	private final Survey q;
	
	private XmlStatusFactory<SS,L,D> xfStatus;
	private XmlDataFactory<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> xfData;
	
	public XmlSurveyFactory(String localeCode, Survey q)
	{
		this.localeCode=localeCode;
		this.q=q;
		if(q.isSetData()){xfData = new XmlDataFactory<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION>(localeCode,q.getData().get(0));}
		if(q.isSetStatus()){xfStatus = new XmlStatusFactory<SS,L,D>(q.getStatus());}
	}
	
	public void lazyLoad(JeeslSurveyTemplateFacade<L,D,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,OPTIONS,OPTION> fTemplate,
				JeeslSurveyCoreFacade<L,D,?,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> fSurvey,Class<SURVEY> cSurvey,Class<SECTION> cSection,Class<DATA> cData)
	{
		this.fSurvey=fSurvey;
		this.cSurvey=cSurvey;
		
		if(q.isSetData()){xfData.lazyLoad(fSurvey,fTemplate,cData);}
	}
	
	public Survey build(SURVEY ejb)
	{
		if(fSurvey!=null){ejb = fSurvey.load(ejb);}
		
		Survey xml = new Survey();
		if(q.isSetId()){xml.setId(ejb.getId());}
		if(q.isSetName()){xml.setName(ejb.getName().get(localeCode).getLang());}
		if(q.isSetValidFrom()){xml.setValidFrom(DateUtil.toXmlGc(ejb.getStartDate()));}
		if(q.isSetValidTo()){xml.setValidTo(DateUtil.toXmlGc(ejb.getEndDate()));}
		
		if(q.isSetStatus()) {xml.setStatus(xfStatus.build(ejb.getStatus()));}
		
		if(q.isSetTemplate())
		{
			XmlTemplateFactory<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> f = new XmlTemplateFactory<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION>(localeCode,q.getTemplate());
//			if(fSurvey!=null){f.lazyLoad(fSurvey,cSection);}
			xml.setTemplate(f.build(ejb.getTemplate()));
		}
		
		if(q.isSetData())
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
		xml.setId(0);
		return xml;
	}
	public static Survey build(Data data)
	{
		Survey xml = build();
		xml.getData().add(data);
		return xml;
	}
}