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

public interface JeeslSurveyTemplateFacade <SCHEME extends JeeslSurveyScheme<?,?,TEMPLATE,SCORE>,
									TEMPLATE extends JeeslSurveyTemplate<?,?,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,OPTIONS,?>,
									VERSION extends JeeslSurveyTemplateVersion<?,?,TEMPLATE>,
									TS extends JeeslSurveyTemplateStatus<?,?,TS,?>,
									TC extends JeeslSurveyTemplateCategory<?,?,TC,?>,
									SECTION extends JeeslSurveySection<?,?,TEMPLATE,SECTION,QUESTION>,
									QUESTION extends JeeslSurveyQuestion<?,?,SECTION,?,?,QE,SCORE,?,OPTIONS,OPTION,?>,
									QE extends JeeslSurveyQuestionElement<?,?,QE,?>,
									SCORE extends JeeslSurveyScore<?,?,SCHEME,QUESTION>,
									
									OPTIONS extends JeeslSurveyOptionSet<?,?,TEMPLATE,OPTION>,
									OPTION extends JeeslSurveyOption<?,?>>
	extends JeeslFacade
{
	OPTIONS loadSurveyOptions(OPTIONS options);
	
	TEMPLATE load(TEMPLATE template, boolean withQuestions, boolean withOptions);
	TEMPLATE fcSurveyTemplate(TC category, TS status);
	TEMPLATE fcSurveyTemplate(TC category, VERSION version, TS status, VERSION nestedVersion);
}