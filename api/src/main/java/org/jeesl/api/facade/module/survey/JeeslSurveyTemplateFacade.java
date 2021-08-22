package org.jeesl.api.facade.module.survey;

import org.jeesl.interfaces.facade.JeeslFacade;
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

public interface JeeslSurveyTemplateFacade <L extends JeeslLang, D extends JeeslDescription,
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
	extends JeeslFacade
{	
	TEMPLATE load(TEMPLATE template, boolean withQuestions, boolean withOptions);
	TEMPLATE fcSurveyTemplate(TC category, TS status);
	TEMPLATE fcSurveyTemplate(TC category, VERSION version, TS status, VERSION nestedVersion);
}