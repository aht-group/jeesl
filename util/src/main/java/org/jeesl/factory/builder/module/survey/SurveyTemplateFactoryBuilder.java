package org.jeesl.factory.builder.module.survey;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.module.survey.EjbSurveyConditionFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyOptionFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyOptionSetFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyQuestionFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveySchemeFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyScoreFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveySectionFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyTemplateFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyTemplateVersionFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyValidationAlgorithmFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyValidationFactory;
import org.jeesl.factory.txt.module.survey.TxtSurveyQuestionFactory;
import org.jeesl.factory.txt.module.survey.TxtSurveySectionFactory;
import org.jeesl.factory.txt.module.survey.TxtSurveyTemplateFactory;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScheme;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScore;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplate;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateCategory;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateStatus;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateVersion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyCondition;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOptionSet;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionType;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionUnit;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidation;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidationAlgorithm;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurveyTemplateFactoryBuilder<L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslStatus<L,D,LOC>,
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
				QE extends JeeslSurveyQuestionType<L,D,QE,?>,
				SCORE extends JeeslSurveyScore<L,D,SCHEME,QUESTION>,
				UNIT extends JeeslSurveyQuestionUnit<L,D,UNIT,?>,
				OPTIONS extends JeeslSurveyOptionSet<L,D,TEMPLATE,OPTION>,
				OPTION extends JeeslSurveyOption<L,D>
				>
		extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(SurveyTemplateFactoryBuilder.class);
	
	private final Class<LOC> cLoc; public Class<LOC> getClassLocale() {return cLoc;}
	private final Class<SCHEME> cScheme; public Class<SCHEME> getClassScheme() {return cScheme;}
	private final Class<VALGORITHM> cValgorithm; public Class<VALGORITHM> getClassValidationAlgorithm() {return cValgorithm;}
	private final Class<TEMPLATE> cTemplate; public Class<TEMPLATE> getClassTemplate() {return cTemplate;}
	private final Class<VERSION> cVersion; public Class<VERSION> getClassVersion() {return cVersion;}
	private final Class<TS> cTs; public Class<TS> getClassTemplateStatus() {return cTs;}
	private final Class<TC> cTc; public Class<TC> getClassTemplateCategory() {return cTc;}
	private final Class<SECTION> cSection; public Class<SECTION> getClassSection() {return cSection;}
	private final Class<QUESTION> cQuestion; public Class<QUESTION> getClassQuestion() {return cQuestion;}
	private final Class<CONDITION> cCondition; public Class<CONDITION> getClassCondition() {return cCondition;}
	private final Class<VALIDATION> cValidation; public Class<VALIDATION> getClassValidation() {return cValidation;}
	private final Class<QE> cElement; public Class<QE> getClassElement(){return cElement;}
	private final Class<SCORE> cScore; public Class<SCORE> getClassScore(){return cScore;}
	private final Class<UNIT> cUnit; public Class<UNIT> getClassUnit() {return cUnit;}
	private final Class<OPTIONS> cOptions; public Class<OPTIONS> getOptionSetClass() {return cOptions;}
	private final Class<OPTION> cOption; public Class<OPTION> getClassOption() {return cOption;}

	public SurveyTemplateFactoryBuilder(final Class<L> cL, final Class<D> cD, final Class<LOC> cLoc, final Class<SCHEME> cScheme, final Class<VALGORITHM> cValgorithm, final Class<TEMPLATE> cTemplate, final Class<VERSION> cVersion, final Class<TS> cTs, final Class<TC> cTc, final Class<SECTION> cSection, final Class<QUESTION> cQuestion,
			final Class<CONDITION> cCondition, final Class<VALIDATION> cValidation,
			final Class<QE> cElement, final Class<SCORE> cScore, final Class<UNIT> cUnit, final Class<OPTIONS> cOptions, final Class<OPTION> cOption)
	{
		super(cL,cD);
		this.cLoc = cLoc;
		this.cScheme = cScheme;
		this.cValgorithm = cValgorithm;
		this.cTemplate = cTemplate;
		this.cVersion = cVersion;
		this.cTs = cTs;
		this.cTc = cTc;
		this.cSection = cSection;
		this.cQuestion = cQuestion;
		this.cCondition = cCondition;
		this.cValidation = cValidation;
		this.cElement = cElement;
		this.cScore = cScore;
		this.cUnit = cUnit;
        this.cOptions = cOptions;
        this.cOption = cOption;
	}
	
	public EjbSurveySchemeFactory<SCHEME,TEMPLATE> efScheme() {return new EjbSurveySchemeFactory<SCHEME,TEMPLATE>(cScheme);}
	
	public EjbSurveyScoreFactory<QUESTION,SCORE> score()
	{
		return new EjbSurveyScoreFactory<QUESTION,SCORE>(cScore);
	}
	
	public EjbSurveyTemplateFactory<TEMPLATE,TS,TC,SECTION,QUESTION> template() {return new EjbSurveyTemplateFactory<>(cTemplate);}
	public EjbSurveyTemplateVersionFactory<VERSION> version() {return new EjbSurveyTemplateVersionFactory<VERSION>(cVersion);}
	
	public EjbSurveySectionFactory<L,D,TEMPLATE,SECTION> section() {return new EjbSurveySectionFactory<L,D,TEMPLATE,SECTION>(cSection);}
	public EjbSurveyQuestionFactory<SECTION,QUESTION,UNIT,OPTIONS,OPTION> question() {return EjbSurveyQuestionFactory.instance(cQuestion);}
	
	public EjbSurveyOptionFactory<QUESTION,OPTION> efOption() {return new EjbSurveyOptionFactory<>(cOption);}
	public EjbSurveyOptionSetFactory<TEMPLATE,OPTIONS> efOptionSet() {return new EjbSurveyOptionSetFactory<>(cOptions);}
	
	public EjbSurveyConditionFactory<QUESTION,CONDITION,QE> ejbCondition(){return new EjbSurveyConditionFactory<QUESTION,CONDITION,QE>(cCondition);}
	public EjbSurveyValidationFactory<QUESTION,VALIDATION> ejbValidation() {return new EjbSurveyValidationFactory<QUESTION,VALIDATION>(cValidation);}
	
	public TxtSurveySectionFactory<L,D,SECTION> txtSection() {return new TxtSurveySectionFactory<>();}
	public TxtSurveyQuestionFactory<L,D,QUESTION,OPTION> txtQuestion(String localeCode) {return new TxtSurveyQuestionFactory<>(localeCode);}
	
	public EjbSurveyValidationAlgorithmFactory<VALGORITHM> ejbAlgorithm()
	{
		return new EjbSurveyValidationAlgorithmFactory<VALGORITHM>(cValgorithm);
	}
	
	public TxtSurveyTemplateFactory<TEMPLATE,VERSION> txtTemplate(String localeCode) {return new TxtSurveyTemplateFactory<>(localeCode);}
}