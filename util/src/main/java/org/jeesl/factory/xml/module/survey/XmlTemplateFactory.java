package org.jeesl.factory.xml.module.survey;

import java.util.Objects;

import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.api.facade.module.survey.JeeslSurveyTemplateFacade;
import org.jeesl.factory.xml.system.lang.XmlDescriptionFactory;
import org.jeesl.factory.xml.system.status.XmlCategoryFactory;
import org.jeesl.factory.xml.system.status.XmlStatusFactory;
import org.jeesl.factory.xml.system.util.text.XmlRemarkFactory;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurvey;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScheme;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScore;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplate;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateCategory;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateStatus;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateVersion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOptionSet;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionElement;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionUnit;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.jeesl.QuerySurvey;
import org.jeesl.model.xml.module.survey.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlTemplateFactory<L extends JeeslLang,D extends JeeslDescription,
				SURVEY extends JeeslSurvey<L,D,?,TEMPLATE,?>,
				
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
				
				
				OPTIONS extends JeeslSurveyOptionSet<L,D,TEMPLATE,OPTION>,
				OPTION extends JeeslSurveyOption<L,D>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlTemplateFactory.class);
	
	private JeeslSurveyTemplateFacade<L,D,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,OPTIONS,OPTION> fTemplate;

	private final Template q;
	
	private XmlStatusFactory<L,D,TS> xfStatus;
	private XmlCategoryFactory<L,D,TC> xfCategory;
	private XmlSectionFactory<L,D,SCHEME,SECTION,QUESTION,QE,SCORE,UNIT,OPTIONS,OPTION> xfSection;
	
	public XmlTemplateFactory(QuerySurvey q){this(q.getLocaleCode(),q.getTemplate());}
	public XmlTemplateFactory(String localeCode, Template q)
	{
		this.q=q;
		if(q.isSetStatus()) {xfStatus = new XmlStatusFactory<>(q.getStatus());}
		if(q.isSetCategory()) {xfCategory = new XmlCategoryFactory<>(q.getCategory());}
		if(q.isSetSection()) {xfSection  = new XmlSectionFactory<>(localeCode,q.getSection().get(0));}	
	}
	
	public void lazyLoad(JeeslSurveyTemplateFacade<L,D,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,OPTIONS,OPTION> fTemplate,
						JeeslSurveyCoreFacade<L,D,?,SURVEY,?,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,?,?,?,OPTIONS,OPTION,?> fSurvey)
	{
		this.fTemplate=fTemplate;
		if(Objects.nonNull(xfSection)) {xfSection.lazyLoad(fSurvey);}
	}
	
	public Template build(TEMPLATE ejb)
	{
		if(Objects.nonNull(fTemplate)){ejb = fTemplate.load(ejb,false,false);}
		
		Template xml = new Template();
		if(q.isSetId()){xml.setId(ejb.getId());}
		
		if(q.isSetDescription()){xml.setDescription(XmlDescriptionFactory.build(ejb.getName()));}
		if(q.isSetRemark() && ejb.getRemark()!=null){xml.setRemark(XmlRemarkFactory.build(ejb.getRemark()));}
		
		if(q.isSetCategory()){xml.setCategory(xfCategory.build(ejb.getCategory()));}
		if(q.isSetStatus()){xml.setStatus(xfStatus.build(ejb.getStatus()));}
		
		if(q.isSetSection())
		{			
			for(SECTION section : ejb.getSections())
			{
				xml.getSection().add(xfSection.build(section));
			}
		}

		return xml;
	}
	
	public static Template id(){return id(0);}
	public static Template id(long id)
	{
		Template xml = new Template();
		xml.setId(id);
		return xml;
	}
}