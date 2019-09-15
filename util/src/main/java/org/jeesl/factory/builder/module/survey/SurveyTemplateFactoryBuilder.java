package org.jeesl.factory.builder.module.survey;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.module.survey.EjbSurveyConditionFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyOptionFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyQuestionFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveySchemeFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyScoreFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveySectionFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyTemplateFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyTemplateVersionFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyValidationAlgorithmFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyValidationFactory;
import org.jeesl.factory.txt.module.survey.TxtSurveyQuestionFactory;
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
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionElement;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionUnit;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidation;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidationAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;

public class SurveyTemplateFactoryBuilder<L extends UtilsLang, D extends UtilsDescription, LOC extends UtilsStatus<LOC,L,D>,
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
				OPTIONS extends JeeslSurveyOptionSet<L,D,TEMPLATE,OPTION>,
				OPTION extends JeeslSurveyOption<L,D>
				>
		extends AbstractFactoryBuilder<L,D>
{
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
	
	public EjbSurveySchemeFactory<SCHEME,TEMPLATE> scheme()
	{
		return new EjbSurveySchemeFactory<SCHEME,TEMPLATE>(cScheme);
	}
	
	public EjbSurveyScoreFactory<QUESTION,SCORE> score()
	{
		return new EjbSurveyScoreFactory<QUESTION,SCORE>(cScore);
	}
	
	public EjbSurveyTemplateFactory<L,D,TEMPLATE,TS,TC,SECTION,QUESTION> template()
	{
		return new EjbSurveyTemplateFactory<L,D,TEMPLATE,TS,TC,SECTION,QUESTION>(cTemplate);
	}
	
	public EjbSurveyTemplateVersionFactory<VERSION> version()
	{
		return new EjbSurveyTemplateVersionFactory<VERSION>(cVersion);
	}
	
	public EjbSurveySectionFactory<L,D,TEMPLATE,SECTION> section()
	{
		return new EjbSurveySectionFactory<L,D,TEMPLATE,SECTION>(cSection);
	}
	
	public EjbSurveyQuestionFactory<L,D,SECTION,QUESTION,QE,UNIT,OPTIONS,OPTION> question()
	{
		return new EjbSurveyQuestionFactory<L,D,SECTION,QUESTION,QE,UNIT,OPTIONS,OPTION>(cQuestion);
	}
	
	public EjbSurveyOptionFactory<QUESTION,OPTION> ejbOption()
	{
		return new EjbSurveyOptionFactory<QUESTION,OPTION>(cOption);
	}
	
	public EjbSurveyConditionFactory<QUESTION,CONDITION,QE> ejbCondition(){return new EjbSurveyConditionFactory<QUESTION,CONDITION,QE>(cCondition);}
	public EjbSurveyValidationFactory<QUESTION,VALIDATION> ejbValidation() {return new EjbSurveyValidationFactory<QUESTION,VALIDATION>(cValidation);}
	
	public TxtSurveyQuestionFactory<L,D,QUESTION,OPTION> txtQuestion(String localeCode)
	{
		return new TxtSurveyQuestionFactory<L,D,QUESTION,OPTION>(localeCode);
	}
	
	public EjbSurveyValidationAlgorithmFactory<VALGORITHM> ejbAlgorithm()
	{
		return new EjbSurveyValidationAlgorithmFactory<VALGORITHM>(cValgorithm);
	}
	
	public TxtSurveyTemplateFactory<TEMPLATE> txtTemplate(String localeCode)
	{
		return new TxtSurveyTemplateFactory<TEMPLATE>(localeCode);
	}
}