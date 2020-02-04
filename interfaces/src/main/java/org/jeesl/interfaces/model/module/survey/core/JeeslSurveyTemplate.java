package org.jeesl.interfaces.model.module.survey.core;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.module.survey.analysis.JeeslSurveyAnalysis;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOptionSet;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.status.JeeslWithStatus;
import org.jeesl.interfaces.model.with.status.UtilsWithCategory;
import org.jeesl.interfaces.model.with.text.EjbWithRemark;

import net.sf.ahtutils.interfaces.model.behaviour.EjbSaveable;
import net.sf.ahtutils.interfaces.model.crud.EjbRemoveable;
import net.sf.ahtutils.model.interfaces.with.EjbWithId;
import net.sf.ahtutils.model.interfaces.with.EjbWithName;
import net.sf.ahtutils.model.interfaces.with.EjbWithRecord;

public interface JeeslSurveyTemplate<L extends JeeslLang, D extends JeeslDescription,
										SCHEME extends JeeslSurveyScheme<L,D,TEMPLATE,?>,
										TEMPLATE extends JeeslSurveyTemplate<L,D,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,OPTIONS,ANALYSIS>,
										VERSION extends JeeslSurveyTemplateVersion<L,D,TEMPLATE>,
										TS extends JeeslSurveyTemplateStatus<L,D,TS,?>,
										TC extends JeeslSurveyTemplateCategory<L,D,TC,?>,
										SECTION extends JeeslSurveySection<L,D,TEMPLATE,SECTION,?>,
										OPTIONS extends JeeslSurveyOptionSet<L,D,TEMPLATE,?>,
										ANALYSIS extends JeeslSurveyAnalysis<L,D,TEMPLATE,?,?,?>>
			extends EjbWithId,EjbWithRecord,EjbWithName,EjbWithRemark,Serializable,EjbRemoveable,EjbSaveable,
						JeeslWithStatus<TS>,
						UtilsWithCategory<L,D,TC>
{
	enum Attributes {category,version,status}
	
	VERSION getVersion();
	void setVersion(VERSION version);
	
	List<SECTION> getSections();
	void setSections(List<SECTION> sections);
	
	List<SCHEME> getSchemes();
	void setSchemes(List<SCHEME> schemes);
	
	List<OPTIONS> getOptionSets();
	void setOptionSets(List<OPTIONS> optionSets);
	
	TEMPLATE getNested();
	void setNested(TEMPLATE nested);
}