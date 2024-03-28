package org.jeesl.web.rest.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.api.facade.module.survey.JeeslSurveyTemplateFacade;
import org.jeesl.api.rest.rs.module.survey.JeeslSurveyJsonRest;
import org.jeesl.api.rest.rs.module.survey.JeeslSurveyRestExport;
import org.jeesl.api.rest.rs.module.survey.JeeslSurveyRestImport;
import org.jeesl.api.rest.rs.module.survey.JeeslSurveyXmlRest;
import org.jeesl.controller.monitoring.counter.DataUpdateTracker;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.survey.SurveyCoreFactoryBuilder;
import org.jeesl.factory.builder.module.survey.SurveyTemplateFactoryBuilder;
import org.jeesl.factory.ejb.module.survey.EjbSurveyAnswerFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyDataFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyMatrixFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyQuestionFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveySectionFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyTemplateFactory;
import org.jeesl.factory.ejb.system.status.EjbStatusFactory;
import org.jeesl.factory.json.jeesl.JsonContainerFactory;
import org.jeesl.factory.json.module.survey.JsonSurveyAnswerFactory;
import org.jeesl.factory.json.module.survey.JsonSurveyFactory;
import org.jeesl.factory.json.module.survey.JsonSurveyQuestionFactory;
import org.jeesl.factory.json.module.survey.JsonTemplateFactory;
import org.jeesl.factory.xml.jeesl.XmlContainerFactory;
import org.jeesl.factory.xml.module.survey.XmlAnswerFactory;
import org.jeesl.factory.xml.module.survey.XmlSurveyFactory;
import org.jeesl.factory.xml.module.survey.XmlTemplateFactory;
import org.jeesl.factory.xml.system.io.sync.XmlMapperFactory;
import org.jeesl.factory.xml.system.status.XmlStatusFactory;
import org.jeesl.factory.xml.system.status.XmlTypeFactory;
import org.jeesl.interfaces.model.io.domain.JeeslDomain;
import org.jeesl.interfaces.model.io.domain.JeeslDomainPath;
import org.jeesl.interfaces.model.io.domain.JeeslDomainQuery;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.module.survey.analysis.JeeslSurveyAnalysis;
import org.jeesl.interfaces.model.module.survey.analysis.JeeslSurveyAnalysisQuestion;
import org.jeesl.interfaces.model.module.survey.analysis.JeeslSurveyAnalysisTool;
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
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyCondition;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOptionSet;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionElement;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionUnit;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidation;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidationAlgorithm;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.json.module.survey.data.JsonAnswer;
import org.jeesl.model.json.system.status.JsonContainer;
import org.jeesl.model.xml.io.locale.status.Status;
import org.jeesl.model.xml.io.ssi.sync.DataUpdate;
import org.jeesl.model.xml.module.survey.Answer;
import org.jeesl.model.xml.module.survey.Correlation;
import org.jeesl.model.xml.module.survey.Data;
import org.jeesl.model.xml.module.survey.Question;
import org.jeesl.model.xml.module.survey.Section;
import org.jeesl.model.xml.module.survey.Survey;
import org.jeesl.model.xml.module.survey.Surveys;
import org.jeesl.model.xml.module.survey.Template;
import org.jeesl.model.xml.module.survey.Templates;
import org.jeesl.model.xml.xsd.Container;
import org.jeesl.util.db.updater.JeeslDbStatusUpdater;
import org.jeesl.util.query.json.JsonStatusQueryProvider;
import org.jeesl.util.query.json.JsonSurveyQueryProvider;
import org.jeesl.util.query.xml.XmlStatusQuery;
import org.jeesl.util.query.xml.module.XmlSurveyQuery;
import org.jeesl.web.rest.AbstractJeeslRestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.aht.Aht;

public class SurveyRestService <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslStatus<L,D,LOC>,
				SURVEY extends JeeslSurvey<L,D,SS,TEMPLATE,DATA>,
				SS extends JeeslSurveyStatus<L,D,SS,?>,
				SCHEME extends JeeslSurveyScheme<L,D,TEMPLATE,SCORE>,
				VALGORITHM extends JeeslSurveyValidationAlgorithm<L,D>,
				TEMPLATE extends JeeslSurveyTemplate<L,D,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,OPTIONS,ANALYSIS>,
				VERSION extends JeeslSurveyTemplateVersion<L,D,TEMPLATE>,
				TS extends JeeslSurveyTemplateStatus<L,D,TS,?>,
				TC extends JeeslSurveyTemplateCategory<L,D,TC,?>,
				SECTION extends JeeslSurveySection<L,D,TEMPLATE,SECTION,QUESTION>,
				QUESTION extends JeeslSurveyQuestion<L,D,SECTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION,AQ>,
				CONDITION extends JeeslSurveyCondition<QUESTION,QE,OPTION>,
				VALIDATION extends JeeslSurveyValidation<L,D,QUESTION,VALGORITHM>,
				QE extends JeeslSurveyQuestionElement<L,D,QE,?>,
				SCORE extends JeeslSurveyScore<L,D,SCHEME,QUESTION>,
				UNIT extends JeeslSurveyQuestionUnit<L,D,UNIT,?>,
				ANSWER extends JeeslSurveyAnswer<L,D,QUESTION,MATRIX,DATA,OPTION>,
				MATRIX extends JeeslSurveyMatrix<L,D,ANSWER,OPTION>,
				DATA extends JeeslSurveyData<L,D,SURVEY,ANSWER,CORRELATION>,
				OPTIONS extends JeeslSurveyOptionSet<L,D,TEMPLATE,OPTION>,
				OPTION extends JeeslSurveyOption<L,D>,
				CORRELATION extends JeeslSurveyCorrelation<DATA>,
				DOMAIN extends JeeslDomain<L,DENTITY>,
				QUERY extends JeeslDomainQuery<L,D,DOMAIN,PATH>,
				PATH extends JeeslDomainPath<L,D,QUERY,DENTITY,?>,
				DENTITY extends JeeslRevisionEntity<L,D,?,?,?,?>,
				ANALYSIS extends JeeslSurveyAnalysis<L,D,TEMPLATE,DOMAIN,DENTITY,?>,
				AQ extends JeeslSurveyAnalysisQuestion<L,D,QUESTION,ANALYSIS>,
				AT extends JeeslSurveyAnalysisTool<L,D,QE,QUERY,?,AQ,?>>
			extends AbstractJeeslRestHandler<L,D>	
			implements JeeslSurveyRestExport,JeeslSurveyRestImport,JeeslSurveyJsonRest,JeeslSurveyXmlRest
{
	final static Logger logger = LoggerFactory.getLogger(SurveyRestService.class);
	
	private JeeslSurveyTemplateFacade<SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,OPTIONS,OPTION> fTemplate;
	private JeeslSurveyCoreFacade<L,D,SURVEY,SS,SCHEME,VERSION,TC,SECTION,QUESTION,ANSWER,MATRIX,DATA,CORRELATION> fSurvey;
	
	private final SurveyTemplateFactoryBuilder<L,D,LOC,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION> fbTemplate;
	private final SurveyCoreFactoryBuilder<L,D,LOC,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> fbCore;
	
	private JsonContainerFactory jfContainer;
//	private JsonSurveyFactory<L,D,SURVEY,SS> jfSurvey;
	
	private XmlContainerFactory xfContainer;
	private XmlStatusFactory<L,D,SS> xfStatus;
	private final XmlStatusFactory<L,D,TC> xfTemplateCategory;
	private final XmlStatusFactory<L,D,TS> xfTemplateStatus;
	private XmlTemplateFactory<L,D,SURVEY,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,OPTIONS,OPTION> xfTemplate;
	private XmlSurveyFactory<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> xfSurveys;
	private XmlSurveyFactory<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> xfSurvey;
	private XmlAnswerFactory<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> xfAnswer;
	
	private EjbSurveyTemplateFactory<L,D,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION> efTemlate;
	private EjbSurveySectionFactory<L,D,TEMPLATE,SECTION> efSection;
	private EjbSurveyQuestionFactory<SECTION,QUESTION,UNIT,OPTIONS,OPTION> efQuestion;
	private EjbSurveyFactory<L,D,SURVEY,SS,TEMPLATE> efSurvey;
	private EjbSurveyDataFactory<SURVEY,DATA,CORRELATION> efData;
	private EjbSurveyAnswerFactory<SECTION,QUESTION,ANSWER,MATRIX,DATA,OPTION> efAnswer;
	private EjbSurveyMatrixFactory<ANSWER,MATRIX,OPTION> efMatrix;
	
	private final JsonSurveyQuestionFactory<L,D,VALGORITHM,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,ANSWER,OPTION> jfQuestion;
	private final JsonSurveyAnswerFactory<L,D,ANSWER,MATRIX,DATA,OPTION> jfAnswer;
	
	public SurveyRestService(JeeslSurveyCoreFacade<L,D,SURVEY,SS,SCHEME,VERSION,TC,SECTION,QUESTION,ANSWER,MATRIX,DATA,CORRELATION> fSurvey,
			SurveyTemplateFactoryBuilder<L,D,LOC,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION> fbTemplate,
			SurveyCoreFactoryBuilder<L,D,LOC,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> fbCore,
			JeeslSurveyTemplateFacade<SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,OPTIONS,OPTION> fTemplate)
	{
		super(fSurvey,fbTemplate.getClassL(),fbTemplate.getClassD());
		this.fTemplate=fTemplate;
		this.fSurvey=fSurvey;
		this.fbCore=fbCore;
		this.fbTemplate=fbTemplate;
	
		String localeCode = "en";
		jfContainer = new JsonContainerFactory(localeCode,JsonStatusQueryProvider.statusIdCodeLabel());
		jfAnswer = new JsonSurveyAnswerFactory<>(localeCode,JsonSurveyQueryProvider.answers());
		jfQuestion = new JsonSurveyQuestionFactory<>(localeCode,JsonSurveyQuestionFactory.id(1));
		
		xfContainer = new XmlContainerFactory(XmlStatusQuery.get(XmlStatusQuery.Key.StatusExport).getStatus());
		
		xfTemplateCategory = new XmlStatusFactory<>(XmlStatusQuery.get(XmlStatusQuery.Key.StatusExport).getStatus());
		xfTemplateStatus = new XmlStatusFactory<>(XmlStatusQuery.get(XmlStatusQuery.Key.StatusExport).getStatus());
		xfStatus = new XmlStatusFactory<>(XmlStatusQuery.get(XmlStatusQuery.Key.StatusExport).getStatus());
		
		xfTemplate = new XmlTemplateFactory<>(localeCode,XmlSurveyQuery.exTemplate());
		xfTemplate.lazyLoad(fTemplate,fSurvey);
		
		xfSurveys = new XmlSurveyFactory<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION>(localeCode,XmlSurveyQuery.get(XmlSurveyQuery.Key.exSurveys).getSurveys().getSurvey().get(0));
		
		xfSurvey = new XmlSurveyFactory<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION>(localeCode,XmlSurveyQuery.get(XmlSurveyQuery.Key.exSurvey).getSurvey());
		xfSurvey.lazyLoad(fTemplate,fSurvey,fbCore.getClassSurvey(),fbTemplate.getClassSection());
		
		xfAnswer = new XmlAnswerFactory<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION>(XmlSurveyQuery.get(XmlSurveyQuery.Key.surveyAnswers));
		
		efTemlate = fbTemplate.template();
		efSection = fbTemplate.section();
		efQuestion = fbTemplate.question();
		efSurvey = fbCore.survey();
		efData = fbCore.data();
		efAnswer = fbCore.answer();
		efMatrix = fbCore.ejbMatrix();
	}
	
	public static <L extends JeeslLang, D extends JeeslDescription,LOC extends JeeslStatus<L,D,LOC>,
					SURVEY extends JeeslSurvey<L,D,SS,TEMPLATE,DATA>,
					SS extends JeeslSurveyStatus<L,D,SS,?>,
					SCHEME extends JeeslSurveyScheme<L,D,TEMPLATE,SCORE>,
					VALGORITHM extends JeeslSurveyValidationAlgorithm<L,D>,
					TEMPLATE extends JeeslSurveyTemplate<L,D,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,OPTIONS,ANALYSIS>,
					VERSION extends JeeslSurveyTemplateVersion<L,D,TEMPLATE>,
					TS extends JeeslSurveyTemplateStatus<L,D,TS,?>,
					TC extends JeeslSurveyTemplateCategory<L,D,TC,?>,
					SECTION extends JeeslSurveySection<L,D,TEMPLATE,SECTION,QUESTION>,
					QUESTION extends JeeslSurveyQuestion<L,D,SECTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION,AQ>,
					CONDITION extends JeeslSurveyCondition<QUESTION,QE,OPTION>,
					VALIDATION extends JeeslSurveyValidation<L,D,QUESTION,VALGORITHM>,
					QE extends JeeslSurveyQuestionElement<L,D,QE,?>,
					SCORE extends JeeslSurveyScore<L,D,SCHEME,QUESTION>,
					UNIT extends JeeslSurveyQuestionUnit<L,D,UNIT,?>,
					ANSWER extends JeeslSurveyAnswer<L,D,QUESTION,MATRIX,DATA,OPTION>, MATRIX extends JeeslSurveyMatrix<L,D,ANSWER,OPTION>,
					DATA extends JeeslSurveyData<L,D,SURVEY,ANSWER,CORRELATION>, OPTIONS extends JeeslSurveyOptionSet<L,D,TEMPLATE,OPTION>,
					OPTION extends JeeslSurveyOption<L,D>,
					OT extends JeeslStatus<L,D,OT>,
					CORRELATION extends JeeslSurveyCorrelation<DATA>,
					DOMAIN extends JeeslDomain<L,DENTITY>,
					QUERY extends JeeslDomainQuery<L,D,DOMAIN,PATH>,
					PATH extends JeeslDomainPath<L,D,QUERY,DENTITY,?>,
					DENTITY extends JeeslRevisionEntity<L,D,?,?,?,?>,
					ANALYSIS extends JeeslSurveyAnalysis<L,D,TEMPLATE,DOMAIN,DENTITY,?>,
					AQ extends JeeslSurveyAnalysisQuestion<L,D,QUESTION,ANALYSIS>,
					AT extends JeeslSurveyAnalysisTool<L,D,QE,QUERY,?,AQ,?>>
		SurveyRestService<L,D,LOC,SURVEY,SS,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION,DOMAIN,QUERY,PATH,DENTITY,ANALYSIS,AQ,AT>
			factory(JeeslSurveyCoreFacade<L,D,SURVEY,SS,SCHEME,VERSION,TC,SECTION,QUESTION,ANSWER,MATRIX,DATA,CORRELATION> fSurvey,
					SurveyTemplateFactoryBuilder<L,D,LOC,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION> ffTemplate,
					SurveyCoreFactoryBuilder<L,D,LOC,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> fbCore,
					JeeslSurveyTemplateFacade<SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,OPTIONS,OPTION> fTemplate)
	{
		return new SurveyRestService<>(fSurvey,ffTemplate,fbCore,fTemplate);
	}

	@Override public Aht exportSurveyTemplateCategory()
	{
		Aht aht = new Aht();
		for(TC ejb : fSurvey.allOrderedPosition(fbTemplate.getClassTemplateCategory())){aht.getStatus().add(xfTemplateCategory.build(ejb));}
		return aht;
	}

	@Override public Aht exportSurveyTemplateStatus()
	{
		Aht aht = new Aht();
		for(TS ejb : fSurvey.allOrderedPosition(fbTemplate.getClassTemplateStatus())){aht.getStatus().add(xfTemplateStatus.build(ejb));}
		return aht;
	}

	@Override public Container surveyQuestionUnits() {return xfContainer.build(fSurvey.allOrderedPosition(fbTemplate.getClassUnit()));}
	
	
	@Override public Aht exportSurveyStatus()
	{
		Aht aht = new Aht();
		for(SS ejb : fSurvey.allOrderedPosition(fbCore.getClassSurveyStatus())){aht.getStatus().add(xfStatus.build(ejb));}
		return aht;
	}

	@Override
	public Templates exportSurveyTemplates()
	{
		Templates xml = new Templates();
		for(TEMPLATE template : fSurvey.all(fbTemplate.getClassTemplate()))
		{
			xml.getTemplate().add(xfTemplate.build(template));
		}
		return xml;
	}
	
	@Override
	public Surveys exportSurveys()
	{
		Surveys xml = new Surveys();
		for(SURVEY survey : fSurvey.all(fbCore.getClassSurvey()))
		{
			xml.getSurvey().add(xfSurveys.build(survey));
		}
		return xml;
	}

	@Override
	public Survey exportSurvey(long id)
	{
		try
		{
			SURVEY ejb = fSurvey.find(fbCore.getClassSurvey(),id);
			return xfSurvey.build(ejb);
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
		return new Survey();
	}
	
	@Override
	public Correlation exportSurveyCorrelations()
	{
		logger.warn("This method should be never used here! You have to implement your project-specific method!");
		return null;
	}
	
	//*******************************************************************************************
	
	@Override public DataUpdate importSurveyTemplateCategory(Aht categories){return importStatus(fbTemplate.getClassTemplateCategory(),cL,cD,categories,null);}
	@Override public DataUpdate importSurveyTemplateStatus(Aht categories){return importStatus(fbTemplate.getClassTemplateStatus(),cL,cD,categories,null);}
	@Override public DataUpdate importSurveyUnits(Aht categories){return importStatus(fbTemplate.getClassUnit(),cL,cD,categories,null);}
	@Override public DataUpdate importSurveyStatus(Aht categories){return importStatus(fbCore.getClassSurveyStatus(),cL,cD,categories,null);}

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <S extends JeeslStatus<L,D,S>, P extends JeeslStatus<L,D,P>> DataUpdate importStatus(Class<S> clStatus, Class<L> clLang, Class<D> clDescription, Aht container, Class<P> clParent)
    {
    	for(Status xml : container.getStatus()){xml.setGroup(clStatus.getSimpleName());}
		JeeslDbStatusUpdater asdi = new JeeslDbStatusUpdater();
        asdi.setStatusEjbFactory(EjbStatusFactory.instance(clStatus, clLang, clDescription));
        asdi.setFacade(fSurvey);
        DataUpdate dataUpdate = asdi.iuStatus(container.getStatus(), clStatus, clLang, clParent);
        asdi.deleteUnusedStatus(clStatus, clLang, clDescription);
        return dataUpdate;
    }

	@Override
	public DataUpdate importSurveyTemplates(Templates templates)
	{
		DataUpdateTracker dut = new DataUpdateTracker(true);
		dut.setType(XmlTypeFactory.build(fbTemplate.getClassTemplate().getName(),"DB Import"));
		for(Template xTemplate : templates.getTemplate())
		{
			try
			{
				TS status = fSurvey.fByCode(fbTemplate.getClassTemplateStatus(),xTemplate.getStatus().getCode());
				TC category = fSurvey.fByCode(fbTemplate.getClassTemplateCategory(),xTemplate.getCategory().getCode());
				TEMPLATE eTemplate = efTemlate.build(category,status,xTemplate);
				eTemplate = fSurvey.persist(eTemplate);
				dut.getUpdate().getMapper().add(XmlMapperFactory.create(fbTemplate.getClassTemplate(), xTemplate.getId(), eTemplate.getId()));
				
				for(Section xSection : xTemplate.getSection())
				{
					SECTION eSection = efSection.build(eTemplate,xSection);
					eSection = fSurvey.persist(eSection);
					for(Question xQuestion : xSection.getQuestion())
					{
						UNIT unit = fSurvey.fByCode(fbTemplate.getClassUnit(),xQuestion.getUnit().getCode());
						QUESTION eQuestion = efQuestion.build(eSection,unit,xQuestion);
						eQuestion = fSurvey.persist(eQuestion);
						dut.getUpdate().getMapper().add(XmlMapperFactory.create(fbTemplate.getClassQuestion(), xQuestion.getId(), eQuestion.getId()));
					}
				}
				
				dut.success();
			}
			catch (JeeslNotFoundException e) {dut.fail(e,true);}
			catch (JeeslConstraintViolationException e) {dut.fail(e,true);}
		}
		return dut.toDataUpdate();
	}

	@Override
	public DataUpdate importSurvey(Survey survey)
	{
		DataUpdateTracker dut = new DataUpdateTracker(true);
		dut.setType(XmlTypeFactory.build(fbCore.getClassSurvey().getName(),"DB Import"));
		
		try
		{
			SS status = fSurvey.fByCode(fbCore.getClassSurveyStatus(),survey.getStatus().getCode());
			TEMPLATE template = fSurvey.find(fbTemplate.getClassTemplate(),survey.getTemplate().getId());
			SURVEY eSurvey = efSurvey.build(template,status,survey);
			eSurvey = fSurvey.persist(eSurvey);
			
			for(Data xData : survey.getData())
			{
				CORRELATION eCorrelation = fSurvey.find(fbCore.getClassCorrelation(),xData.getCorrelation().getId());
				DATA eData = efData.build(eSurvey,eCorrelation);
				eData = fSurvey.saveData(eData);
				logger.trace("EDATA: "+eData.toString());
				
				for(Answer xAnswer : xData.getAnswer())
				{
					QUESTION eQuestion = fSurvey.find(fbTemplate.getClassQuestion(),xAnswer.getQuestion().getId());
					ANSWER eAnswer = efAnswer.build(eQuestion,eData,xAnswer);
					eAnswer = fSurvey.persist(eAnswer);
				}
			}
			
			dut.success();
		}
		catch (JeeslNotFoundException e) {dut.fail(e,true);}
		catch (JeeslConstraintViolationException e) {dut.fail(e,true);}
		catch (JeeslLockingException e) {dut.fail(e,true);}
		
		return dut.toDataUpdate();
	}
	
	// Survey
	
//	@Override
	public Survey surveyStructure(long id)
	{
		Survey xml = new Survey();
		try
		{
			TEMPLATE ejb = fSurvey.find(fbTemplate.getClassTemplate(),id);
			xml.setTemplate(xfTemplate.build(ejb));
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
		return xml;
	}
	
	@Override public org.jeesl.model.json.module.survey.data.JsonSurvey surveyStructureJson(String localeCode, long id)
	{
		JsonSurveyFactory<L,D,SURVEY,SS> jfSurvey = new JsonSurveyFactory<L,D,SURVEY,SS>(localeCode,JsonSurveyQueryProvider.survey());
		JsonTemplateFactory<L,D,SURVEY,SS,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION> jfTemplate = new JsonTemplateFactory<>(localeCode,JsonSurveyQueryProvider.templateExport(),fbTemplate,fTemplate); 
		
		org.jeesl.model.json.module.survey.data.JsonSurvey jSurvey = JsonSurveyFactory.build();
		try
		{
			SURVEY eSurvey = fSurvey.find(fbCore.getClassSurvey(),id);
			jSurvey = jfSurvey.build(eSurvey);
			TEMPLATE eTemplate = fSurvey.find(fbTemplate.getClassTemplate(),eSurvey.getTemplate());
			
			org.jeesl.model.json.survey.JsonTemplate jTemplate = jfTemplate.build(eTemplate);
			jTemplate.setCode("primary");
			jSurvey.setTemplate(jTemplate);
			
			jSurvey.setTemplates(new ArrayList<org.jeesl.model.json.survey.JsonTemplate>());
			jSurvey.getTemplates().add(jTemplate);
			if(eTemplate.getNested()!=null)
			{
				org.jeesl.model.json.survey.JsonTemplate jNested = jfTemplate.build(eTemplate.getNested());
				jNested.setCode("nested");
				jSurvey.getTemplates().add(jNested);
			}
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
		
		return jSurvey;
	}
	
	public Survey surveyAnswers(long id)
	{
		Survey xml = new Survey();
		Data data = new Data();
		try
		{
			SURVEY survey = fSurvey.find(fbCore.getClassSurvey(),id);
			for(ANSWER answer : fSurvey.fAnswers(survey))
			{
				data.getAnswer().add(xfAnswer.build(answer));
			}
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
		xml.getData().add(data);
		return xml;
	}
	
	public org.jeesl.model.json.module.survey.data.JsonData jsonAnswers(DATA data)
	{
		org.jeesl.model.json.module.survey.data.JsonData result = new org.jeesl.model.json.module.survey.data.JsonData();
		List<ANSWER> answers = fSurvey.fAnswers(Arrays.asList(data));
		Map<ANSWER,List<MATRIX>> map = efMatrix.toMapAnswer(fSurvey.fCells(answers));
		
		for(ANSWER a : answers)
		{
			if(map.containsKey(a)) {a.setMatrix(map.get(a));}
			else {a.setMatrix(new ArrayList<MATRIX>());}
			
			JsonAnswer jAnswer = jfAnswer.build(a);
			jAnswer.setQuestion(jfQuestion.build(a.getQuestion()));
			
			result.getAnswers().add(jAnswer);
		}

		return result;
	}
	
	@Override public JsonContainer surveyQuestionUnitsJson() {return jfContainer.build(fSurvey.allOrderedPosition(fbTemplate.getClassUnit()));}
}