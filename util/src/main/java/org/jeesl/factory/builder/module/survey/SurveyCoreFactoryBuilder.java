package org.jeesl.factory.builder.module.survey;

import org.jeesl.api.bean.JeeslSurveyBean;
import org.jeesl.api.bean.module.survey.JeeslSurveyCache;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.controller.handler.module.survey.SurveyConditionalHandler;
import org.jeesl.controller.handler.module.survey.SurveyHandler;
import org.jeesl.controller.handler.module.survey.SurveyOptionHandler;
import org.jeesl.controller.handler.module.survey.SurveyValidationHandler;
import org.jeesl.controller.processor.survey.SurveyScoreProcessor;
import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.module.survey.EjbSurveyAnswerFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyCorrelationFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyDataFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyMatrixFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyOptionFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyOptionSetFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyQuestionFactory;
import org.jeesl.factory.json.module.survey.JsonSurveyAnswerFactory;
import org.jeesl.factory.json.module.survey.JsonSurveyFactory;
import org.jeesl.factory.txt.module.survey.TxtOptionFactory;
import org.jeesl.factory.txt.module.survey.TxtSurveyAnswerFactory;
import org.jeesl.factory.txt.module.survey.TxtSurveyFactory;
import org.jeesl.factory.txt.module.survey.TxtSurveyQuestionFactory;
import org.jeesl.factory.txt.module.survey.TxtSurveySectionFactory;
import org.jeesl.interfaces.controller.handler.module.survey.JeeslSurveyHandlerCallback;
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
import org.jeesl.util.filter.ejb.module.survey.EjbSurveyAnswerFilter;
import org.jeesl.util.filter.ejb.module.survey.EjbSurveyQuestionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurveyCoreFactoryBuilder<L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslStatus<L,D,LOC>,
				SURVEY extends JeeslSurvey<L,D,SS,TEMPLATE,DATA>,
				SS extends JeeslSurveyStatus<L,D,SS,?>,
				SCHEME extends JeeslSurveyScheme<L,D,TEMPLATE,SCORE>,
				VALGORITHM extends JeeslSurveyValidationAlgorithm<L,D>,
				TEMPLATE extends JeeslSurveyTemplate<L,D,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,OPTIONS,?>,
				VERSION extends JeeslSurveyTemplateVersion<L,D,TEMPLATE>,
				TS extends JeeslSurveyTemplateStatus<L,D,TS,?>,
				TC extends JeeslSurveyTemplateCategory<L,D,TC,?>,
				SECTION extends JeeslSurveySection<L,D,TEMPLATE,SECTION,QUESTION>,
				QUESTION extends JeeslSurveyQuestion<L,D,SECTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION,?>,
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
				ATT extends JeeslStatus<L,D,ATT>>
	extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(SurveyCoreFactoryBuilder.class);
	
	private final Class<SURVEY> cSurvey; public Class<SURVEY> getClassSurvey() {return cSurvey;}
	private final Class<SS> cSs; public Class<SS> getClassSurveyStatus() {return cSs;}
	private final Class<SECTION> cSection; public Class<SECTION> getClassSection() {return cSection;}
	private final Class<QUESTION> cQuestion; public Class<QUESTION> getClassQuestion() {return cQuestion;}
	private final Class<ANSWER> cAnswer; public Class<ANSWER> getClassAnswer() {return cAnswer;}
	private final Class<MATRIX> cMatrix; public Class<MATRIX> getClassMatrix() {return cMatrix;}
	private final Class<DATA> cData; public Class<DATA> getClassData() {return cData;}
	private final Class<OPTIONS> cOptions; public Class<OPTIONS> getOptionSetClass() {return cOptions;}
	private final Class<OPTION> cOption; public Class<OPTION> getOptionClass() {return cOption;}
	private final Class<CORRELATION> cCorrelation; public Class<CORRELATION> getClassCorrelation() {return cCorrelation;} 

	public SurveyCoreFactoryBuilder(final Class<L> cL, final Class<D> cD, final Class<SURVEY> cSurvey, final Class<SS> cSs, final Class<SECTION> cSection, final Class<QUESTION> cQuestion, final Class<ANSWER> cAnswer, final Class<MATRIX> cMatrix, final Class<DATA> cData, final Class<OPTIONS> cOptions, final Class<OPTION> cOption, final Class<CORRELATION> cCorrelation)
	{
		super(cL,cD);
		this.cSurvey = cSurvey;
		this.cSs = cSs;
		this.cSection = cSection;
		this.cQuestion = cQuestion;
        this.cAnswer = cAnswer;
        this.cMatrix = cMatrix;
        this.cData = cData;
        this.cOptions = cOptions;
        this.cOption = cOption;
        this.cCorrelation = cCorrelation;
	}
	
	public EjbSurveyFactory<L,D,SURVEY,SS,TEMPLATE> survey() {return new EjbSurveyFactory<L,D,SURVEY,SS,TEMPLATE>(cL,cD,cSurvey);}
	public EjbSurveyAnswerFactory<SECTION,QUESTION,ANSWER,MATRIX,DATA,OPTION> answer() {return new EjbSurveyAnswerFactory<SECTION,QUESTION,ANSWER,MATRIX,DATA,OPTION>(cQuestion,cAnswer,cOption);}
	
	public EjbSurveyQuestionFactory<L,D,SECTION,QUESTION,QE,UNIT,OPTIONS,OPTION> ejbQuestion()
	{
		return new EjbSurveyQuestionFactory<L,D,SECTION,QUESTION,QE,UNIT,OPTIONS,OPTION>(cQuestion);
	}
	
	public EjbSurveyCorrelationFactory<ANSWER,DATA,CORRELATION> ejbCorrelation()
	{
		return new EjbSurveyCorrelationFactory<ANSWER,DATA,CORRELATION>();
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
	
	public TxtSurveyFactory<L,D,SURVEY,TEMPLATE> txtSurvey(final String localeCode)
	{
		return new TxtSurveyFactory<L,D,SURVEY,TEMPLATE>(localeCode);
	}
	public TxtSurveySectionFactory<L,D,SECTION> txtSection()
	{
		return new TxtSurveySectionFactory<L,D,SECTION>();
	}
	
	public TxtSurveyQuestionFactory<L,D,QUESTION,OPTION> txtQuestion(String localeCode) {return new TxtSurveyQuestionFactory<>(localeCode);}
	public TxtSurveyAnswerFactory<L,D,QUESTION,ANSWER,MATRIX,OPTION> txtAnswer() {return new TxtSurveyAnswerFactory<>();}
	
	public TxtOptionFactory<L,D,OPTION> txtOption(String localeCode) {return new TxtOptionFactory<L,D,OPTION>(localeCode);}
	
	public EjbSurveyAnswerFilter<SECTION,QUESTION,ANSWER> ejbFilterAnswer() {return new EjbSurveyAnswerFilter<>();}
	
	public SurveyScoreProcessor<SECTION,QUESTION,ANSWER> scoreProcessor()
	{
		return new SurveyScoreProcessor<>(ejbQuestion(),ejbFilterAnswer());
	}
	
	public SurveyHandler<L,D,SURVEY,TEMPLATE,TC,SECTION,QUESTION,CONDITION,VALIDATION,ANSWER,MATRIX,DATA,OPTION,CORRELATION> handler(JeeslSurveyHandlerCallback<SECTION> callback, JeeslFacesMessageBean bMessage, final JeeslSurveyCoreFacade<L,D,LOC,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> fSurvey, JeeslSurveyBean<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION,ATT> bSurvey)
	{
		return new SurveyHandler<>(callback,this,bMessage,fSurvey,bSurvey,bSurvey);
	}
	
	public SurveyConditionalHandler<TEMPLATE,SECTION,QUESTION,CONDITION,ANSWER,OPTION> conditional(JeeslSurveyCache<TEMPLATE,SECTION,QUESTION,CONDITION,VALIDATION> cache)
	{
		return new SurveyConditionalHandler<TEMPLATE,SECTION,QUESTION,CONDITION,ANSWER,OPTION>(this,cache);
	}
	public SurveyValidationHandler<L,D,TEMPLATE,SECTION,QUESTION,VALIDATION,ANSWER,OPTION> validation(JeeslSurveyCache<TEMPLATE,SECTION,QUESTION,CONDITION,VALIDATION> cache)
	{
		return new SurveyValidationHandler<L,D,TEMPLATE,SECTION,QUESTION,VALIDATION,ANSWER,OPTION>(cache);
	}
	
//	public JeeslSurveyCacheFacadeBean<L,D,LOC,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> cache();
	
	public JsonSurveyFactory<L,D,SURVEY,SS> surveyJson(String localeCode, org.jeesl.model.json.survey.JsonSurvey q)
	{
		return new JsonSurveyFactory<L,D,SURVEY,SS>(localeCode,q);
	}
	
	public JsonSurveyAnswerFactory<L,D,VALGORITHM,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION> jsonAnswer(String localeCode, org.jeesl.model.json.survey.Answer q)
	{
		return new JsonSurveyAnswerFactory<L,D,VALGORITHM,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION>(localeCode,q);
	}
	
	public SurveyOptionHandler<QUESTION,OPTION> eHandlerOption() {return new SurveyOptionHandler<>();}
	
	public EjbSurveyQuestionFilter<QUESTION> eFilterQuestion() {return new EjbSurveyQuestionFilter<>();}
}