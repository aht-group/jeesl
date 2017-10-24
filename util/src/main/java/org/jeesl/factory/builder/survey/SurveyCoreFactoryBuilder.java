package org.jeesl.factory.builder.survey;

import org.jeesl.api.bean.JeeslSurveyBean;
import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.controller.handler.SurveyHandler;
import org.jeesl.controller.processor.survey.SurveyScoreProcessor;
import org.jeesl.factory.ejb.module.survey.EjbSurveyAnswerFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyDataFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyMatrixFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyOptionFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyOptionSetFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyQuestionFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveySchemeFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyScoreFactory;
import org.jeesl.factory.json.system.survey.JsonSurveyFactory;
import org.jeesl.factory.txt.module.survey.TxtOptionFactory;
import org.jeesl.factory.txt.module.survey.TxtSurveyAnswerFactory;
import org.jeesl.factory.txt.module.survey.TxtSurveySectionFactory;
import org.jeesl.interfaces.model.module.survey.analysis.JeeslSurveyAnalysis;
import org.jeesl.interfaces.model.module.survey.analysis.JeeslSurveyAnalysisQuestion;
import org.jeesl.interfaces.model.module.survey.analysis.JeeslSurveyAnalysisTool;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurvey;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScheme;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScore;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplate;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateVersion;
import org.jeesl.interfaces.model.module.survey.correlation.JeeslSurveyCorrelation;
import org.jeesl.interfaces.model.module.survey.correlation.JeeslSurveyDomain;
import org.jeesl.interfaces.model.module.survey.correlation.JeeslSurveyDomainPath;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyData;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyMatrix;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOptionSet;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.system.io.revision.JeeslRevisionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.bean.FacesMessageBean;
import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;

public class SurveyCoreFactoryBuilder<L extends UtilsLang, D extends UtilsDescription,
				SURVEY extends JeeslSurvey<L,D,SS,TEMPLATE,DATA>,
				SS extends UtilsStatus<SS,L,D>,
				SCHEME extends JeeslSurveyScheme<L,D,TEMPLATE,SCORE>,
				TEMPLATE extends JeeslSurveyTemplate<L,D,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,OPTIONS,ANALYSIS>,
				VERSION extends JeeslSurveyTemplateVersion<L,D,TEMPLATE>,
				TS extends UtilsStatus<TS,L,D>,
				TC extends UtilsStatus<TC,L,D>,
				SECTION extends JeeslSurveySection<L,D,TEMPLATE,SECTION,QUESTION>,
				QUESTION extends JeeslSurveyQuestion<L,D,SECTION,QE,SCORE,UNIT,OPTIONS,OPTION,AQ>,
				QE extends UtilsStatus<QE,L,D>,
				SCORE extends JeeslSurveyScore<L,D,SCHEME,QUESTION>,
				UNIT extends UtilsStatus<UNIT,L,D>,
				ANSWER extends JeeslSurveyAnswer<L,D,QUESTION,MATRIX,DATA,OPTION>,
				MATRIX extends JeeslSurveyMatrix<L,D,ANSWER,OPTION>,
				DATA extends JeeslSurveyData<L,D,SURVEY,ANSWER,CORRELATION>,
				OPTIONS extends JeeslSurveyOptionSet<L,D,TEMPLATE,OPTION>,
				OPTION extends JeeslSurveyOption<L,D>,
				CORRELATION extends JeeslSurveyCorrelation<L,D,DATA>,
				DOMAIN extends JeeslSurveyDomain<L,D,DOMAIN,PATH,DENTITY,ANALYSIS,AQ,AT,ATT>,
				PATH extends JeeslSurveyDomainPath<L,D,DOMAIN,PATH,DENTITY,ANALYSIS,AQ,AT,ATT>,
				DENTITY extends JeeslRevisionEntity<L,D,?,?,?,?,?,DENTITY,?,?,?>,
				ANALYSIS extends JeeslSurveyAnalysis<L,D,TEMPLATE>,
				AQ extends JeeslSurveyAnalysisQuestion<L,D,QUESTION,ANALYSIS>,
				AT extends JeeslSurveyAnalysisTool<L,D,QE,AQ,ATT>,
				ATT extends UtilsStatus<ATT,L,D>>
{
	final static Logger logger = LoggerFactory.getLogger(SurveyCoreFactoryBuilder.class);
	
	private final Class<L> cL;
	private final Class<D> cD;
	private final Class<SURVEY> cSurvey;
	private final Class<SS> cSs; public Class<SS> getClassSurveyStatus() {return cSs;}
	private final Class<SCHEME> cScheme;
	private final Class<TEMPLATE> cTemplate;
	private final Class<VERSION> cVersion;
	private final Class<SECTION> cSection; public Class<SECTION> getClassSection() {return cSection;}
	private final Class<QUESTION> cQuestion; public Class<QUESTION> getClassQuestion() {return cQuestion;}
	private final Class<SCORE> cScore;
	private final Class<UNIT> cUnit; public Class<UNIT> getClassUnit() {return cUnit;}
	private final Class<ANSWER> cAnswer;
	private final Class<MATRIX> cMatrix;
	private final Class<DATA> cData; 
	private final Class<OPTIONS> cOptions; public Class<OPTIONS> getOptionSetClass() {return cOptions;}
	private final Class<OPTION> cOption; public Class<OPTION> getOptionClass() {return cOption;}

	public SurveyCoreFactoryBuilder(final Class<L> cL, final Class<D> cD, final Class<SURVEY> cSurvey, final Class<SS> cSs, final Class<SCHEME> cScheme, final Class<TEMPLATE> cTemplate, final Class<VERSION> cVersion, final Class<SECTION> cSection, final Class<QUESTION> cQuestion, final Class<SCORE> cScore, final Class<UNIT> cUnit, final Class<ANSWER> cAnswer, final Class<MATRIX> cMatrix, final Class<DATA> cData, final Class<OPTIONS> cOptions, final Class<OPTION> cOption)
	{
		this.cL = cL;
		this.cD = cD;
		this.cSurvey = cSurvey;
		this.cSs = cSs;
		this.cScheme = cScheme;
		this.cTemplate = cTemplate;
		this.cVersion = cVersion;
		this.cSection = cSection;
		this.cQuestion = cQuestion;
		this.cScore = cScore;
		this.cUnit = cUnit;
        this.cAnswer = cAnswer;
        this.cMatrix = cMatrix;
        this.cData = cData;
        this.cOptions = cOptions;
        this.cOption = cOption;
	}
	
	public static <L extends UtilsLang, D extends UtilsDescription,
					SURVEY extends JeeslSurvey<L,D,SS,TEMPLATE,DATA>,
					SS extends UtilsStatus<SS,L,D>,
					SCHEME extends JeeslSurveyScheme<L,D,TEMPLATE,SCORE>, 
					TEMPLATE extends JeeslSurveyTemplate<L,D,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,OPTIONS,ANALYSIS>,
					VERSION extends JeeslSurveyTemplateVersion<L,D,TEMPLATE>,
					TS extends UtilsStatus<TS,L,D>,
					TC extends UtilsStatus<TC,L,D>,
					SECTION extends JeeslSurveySection<L,D,TEMPLATE,SECTION,QUESTION>,
					QUESTION extends JeeslSurveyQuestion<L,D,SECTION,QE,SCORE,UNIT,OPTIONS,OPTION,AQ>,
					QE extends UtilsStatus<QE,L,D>,
					SCORE extends JeeslSurveyScore<L,D,SCHEME,QUESTION>,
					UNIT extends UtilsStatus<UNIT,L,D>,
					ANSWER extends JeeslSurveyAnswer<L,D,QUESTION,MATRIX,DATA,OPTION>,
					MATRIX extends JeeslSurveyMatrix<L,D,ANSWER,OPTION>,
					DATA extends JeeslSurveyData<L,D,SURVEY,ANSWER,CORRELATION>,
					OPTIONS extends JeeslSurveyOptionSet<L,D,TEMPLATE,OPTION>,
					OPTION extends JeeslSurveyOption<L,D>,
					CORRELATION extends JeeslSurveyCorrelation<L,D,DATA>,
					DOMAIN extends JeeslSurveyDomain<L,D,DOMAIN,PATH,DENTITY,ANALYSIS,AQ,AT,ATT>,
					PATH extends JeeslSurveyDomainPath<L,D,DOMAIN,PATH,DENTITY,ANALYSIS,AQ,AT,ATT>,
					DENTITY extends JeeslRevisionEntity<L,D,?,?,?,?,?,DENTITY,?,?,?>,
					ANALYSIS extends JeeslSurveyAnalysis<L,D,TEMPLATE>, AQ extends JeeslSurveyAnalysisQuestion<L,D,QUESTION,ANALYSIS>, AT extends JeeslSurveyAnalysisTool<L,D,QE,AQ,ATT>, ATT extends UtilsStatus<ATT,L,D>>
		SurveyCoreFactoryBuilder<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION,DOMAIN,PATH,DENTITY,ANALYSIS,AQ,AT,ATT>
		factory(final Class<L> cL, final Class<D> cD, final Class<SURVEY> cSurvey, final Class<SS> cSs, final Class<SCHEME> cScheme, final Class<TEMPLATE> cTemplate, final Class<VERSION> cVersion, final Class<SECTION> cSection, final Class<QUESTION> cQuestion, final Class<SCORE> cScore, final Class<UNIT> cUnit, final Class<ANSWER> cAnswer, final Class<MATRIX> cMatrix, final Class<DATA> cData, final Class<OPTIONS> cOptions, final Class<OPTION> cOption)
	{
		return new SurveyCoreFactoryBuilder<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION,DOMAIN,PATH,DENTITY,ANALYSIS,AQ,AT,ATT>(cL,cD,cSurvey,cSs,cScheme,cTemplate,cVersion,cSection,cQuestion,cScore,cUnit,cAnswer,cMatrix,cData,cOptions,cOption);
	}
	
	public EjbSurveyFactory<L,D,SURVEY,SS,TEMPLATE> survey()
	{
		return new EjbSurveyFactory<L,D,SURVEY,SS,TEMPLATE>(cL,cD,cSurvey);
	}
	
	public EjbSurveySchemeFactory<SCHEME,TEMPLATE> scheme()
	{
		return new EjbSurveySchemeFactory<SCHEME,TEMPLATE>(cScheme);
	}


	
	public EjbSurveyOptionFactory<QUESTION,OPTION> ejbOption()
	{
		return new EjbSurveyOptionFactory<QUESTION,OPTION>(cOption);
	}

	
	public EjbSurveyQuestionFactory<L,D,SECTION,QUESTION,QE,UNIT,OPTIONS,OPTION> question()
	{
		return new EjbSurveyQuestionFactory<L,D,SECTION,QUESTION,QE,UNIT,OPTIONS,OPTION>(cQuestion);
	}
	
	public EjbSurveyScoreFactory<QUESTION,SCORE> score()
	{
		return new EjbSurveyScoreFactory<QUESTION,SCORE>(cScore);
	}
	
	public EjbSurveyAnswerFactory<SECTION,QUESTION,ANSWER,MATRIX,DATA,OPTION> answer()
	{
		return new EjbSurveyAnswerFactory<SECTION,QUESTION,ANSWER,MATRIX,DATA,OPTION>(cAnswer);
	}
	
	public EjbSurveyMatrixFactory<ANSWER,MATRIX,OPTION> ejbMatrix()
	{
		return new EjbSurveyMatrixFactory<ANSWER,MATRIX,OPTION>(cMatrix);
	}
	
	public EjbSurveyOptionSetFactory<TEMPLATE,OPTIONS> optionSet()
	{
		return new EjbSurveyOptionSetFactory<TEMPLATE,OPTIONS>(cOptions);
	}
	
	public EjbSurveyOptionFactory<QUESTION,OPTION> option()
	{
		return new EjbSurveyOptionFactory<QUESTION,OPTION>(cOption);
	}
	
	public EjbSurveyDataFactory<SURVEY,DATA,CORRELATION> data()
	{
		return new EjbSurveyDataFactory<SURVEY,DATA,CORRELATION>(cData);
	}
	
	public TxtSurveySectionFactory<L,D,SECTION> txtSection()
	{
		return new TxtSurveySectionFactory<L,D,SECTION>();
	}
	
	public TxtSurveyAnswerFactory<L,D,ANSWER,MATRIX,OPTION> txtAnswer()
	{
		return new TxtSurveyAnswerFactory<L,D,ANSWER,MATRIX,OPTION>();
	}
	
	public TxtOptionFactory<L,D,OPTION> txtOption(String localeCode)
	{
		return new TxtOptionFactory<L,D,OPTION>(localeCode);
	}
	
	public SurveyScoreProcessor<QUESTION,ANSWER> scoreProcessor()
	{
		return new SurveyScoreProcessor<QUESTION,ANSWER>();
	}
	
	public SurveyHandler<SURVEY,TEMPLATE,TC,SECTION,QUESTION,ANSWER,MATRIX,DATA,OPTION,CORRELATION> handler(FacesMessageBean bMessage, final JeeslSurveyCoreFacade<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> fSurvey, JeeslSurveyBean<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION,DOMAIN,PATH,DENTITY,ANALYSIS,AQ,AT,ATT> bSurvey)
	{
		return new SurveyHandler<SURVEY,TEMPLATE,TC,SECTION,QUESTION,ANSWER,MATRIX,DATA,OPTION,CORRELATION>(bMessage,fSurvey,bSurvey,this);
	}
	
	public JsonSurveyFactory<L,D,SURVEY,SS> surveyJson(String localeCode, org.jeesl.model.json.survey.Survey q)
	{
		return new JsonSurveyFactory<L,D,SURVEY,SS>(localeCode,q);
	}
}