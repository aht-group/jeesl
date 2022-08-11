package org.jeesl.factory.builder.module.survey;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.domain.EjbSurveyDomainFactory;
import org.jeesl.factory.ejb.io.domain.EjbSurveyDomainPathFactory;
import org.jeesl.factory.ejb.io.domain.EjbSurveyDomainQueryFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyAnalysisFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyAnalysisQuestionFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyAnalysisToolFactory;
import org.jeesl.interfaces.model.io.domain.JeeslDomain;
import org.jeesl.interfaces.model.io.domain.JeeslDomainPath;
import org.jeesl.interfaces.model.io.domain.JeeslDomainQuery;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.module.survey.analysis.JeeslSurveyAnalysis;
import org.jeesl.interfaces.model.module.survey.analysis.JeeslSurveyAnalysisQuestion;
import org.jeesl.interfaces.model.module.survey.analysis.JeeslSurveyAnalysisTool;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScore;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplate;
import org.jeesl.interfaces.model.module.survey.correlation.JeeslSurveyCorrelation;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyData;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyMatrix;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionElement;
import org.jeesl.interfaces.model.system.job.JeeslJobTemplate;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurveyAnalysisFactoryBuilder<L extends JeeslLang, D extends JeeslDescription,
				TEMPLATE extends JeeslSurveyTemplate<L,D,?,TEMPLATE,?,?,?,?,?,ANALYSIS>,
				QUESTION extends JeeslSurveyQuestion<L,D,?,?,?,QE,SCORE,?,?,OPTION,AQ>,
				QE extends JeeslSurveyQuestionElement<L,D,QE,?>,
				SCORE extends JeeslSurveyScore<L,D,?,QUESTION>,
				ANSWER extends JeeslSurveyAnswer<L,D,QUESTION,MATRIX,DATA,OPTION>,
				MATRIX extends JeeslSurveyMatrix<L,D,ANSWER,OPTION>,
				DATA extends JeeslSurveyData<L,D,?,ANSWER,CORRELATION>,
				OPTION extends JeeslSurveyOption<L,D>,
				CORRELATION extends JeeslSurveyCorrelation<DATA>,
				DOMAIN extends JeeslDomain<L,DENTITY>,
				QUERY extends JeeslDomainQuery<L,D,DOMAIN,PATH>,
				PATH extends JeeslDomainPath<L,D,QUERY,DENTITY,DATTRIBUTE>,
				DENTITY extends JeeslRevisionEntity<L,D,?,?,DATTRIBUTE,?>,
				DATTRIBUTE extends JeeslRevisionAttribute<L,D,DENTITY,?,?>,
				ANALYSIS extends JeeslSurveyAnalysis<L,D,TEMPLATE,DOMAIN,DENTITY,DATTRIBUTE>,
				AQ extends JeeslSurveyAnalysisQuestion<L,D,QUESTION,ANALYSIS>,
				TOOL extends JeeslSurveyAnalysisTool<L,D,QE,QUERY,DATTRIBUTE,AQ,TOOLT>,
				TOOLT extends JeeslStatus<L,D,TOOLT>,
				TOOLCACHETEMPLATE extends JeeslJobTemplate<L,D,?,?,?,?>>
			extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(SurveyAnalysisFactoryBuilder.class);
	
	private final Class<ANSWER> cAnswer; public Class<ANSWER> getClassAnswer() {return cAnswer;}
	private final Class<DATA> cData; public Class<DATA> getClassData() {return cData;}
	private final Class<DOMAIN> cDomain; public Class<DOMAIN> getClassDomain() {return cDomain;}
	private final Class<QUERY> cDomainQuery; public Class<QUERY> getClassDomainQuery() {return cDomainQuery;}
	private final Class<PATH> cDomainPath; public Class<PATH> getClassDomainPath() {return cDomainPath;}
	private final Class<DENTITY> cDomainEntity; public Class<DENTITY> getClassDomainEntity() {return cDomainEntity;}
	private final Class<DATTRIBUTE> cDomainAttribute; public Class<DATTRIBUTE> getClassDomainAttribute() {return cDomainAttribute;}
	private final Class<ANALYSIS> cAnalysis; public Class<ANALYSIS> getClassAnalysis() {return cAnalysis;}
	private final Class<AQ> cAq; public Class<AQ> getClassAnalysisQuestion() {return cAq;}
	private final Class<TOOL> cTool; public Class<TOOL> getClassAnalysisTool() {return cTool;}
	private final Class<TOOLT> cToolType; public Class<TOOLT> getAttClass() {return cToolType;}
	private final Class<TOOLCACHETEMPLATE> cJobTemplate; public Class<TOOLCACHETEMPLATE> getClassJobTemplate(){return cJobTemplate;}

	public SurveyAnalysisFactoryBuilder(final Class<L> cL, final Class<D> cD, final Class<ANSWER> cAnswer, final Class<DATA> cData, final Class<DOMAIN> cDomain, final Class<QUERY> cDomainQuery, final Class<PATH> cDomainPath, final Class<DENTITY> cDomainEntity, final Class<DATTRIBUTE> cDomainAttribute, final Class<ANALYSIS> cAnalysis, final Class<AQ> cAq, final Class<TOOL> cTool, final Class<TOOLT> cToolType, final Class<TOOLCACHETEMPLATE> cJobTemplate)
	{
		super(cL,cD);
		this.cAnswer=cAnswer;
		this.cData=cData;
		this.cDomain=cDomain;
		this.cDomainQuery=cDomainQuery;
		this.cDomainPath=cDomainPath;
		this.cDomainEntity=cDomainEntity;
		this.cDomainAttribute=cDomainAttribute;
		this.cAq=cAq;
		this.cAnalysis=cAnalysis;
		this.cTool=cTool;
        this.cToolType = cToolType;
        this.cJobTemplate=cJobTemplate;
	}
	
	public EjbSurveyDomainFactory<L,D,DOMAIN,DENTITY> ejbDomain()
	{
		return new EjbSurveyDomainFactory<L,D,DOMAIN,DENTITY>(cDomain);
	}
	
	public EjbSurveyDomainQueryFactory<L,D,DOMAIN,QUERY,PATH> ejbDomainQuery()
	{
		return new EjbSurveyDomainQueryFactory<L,D,DOMAIN,QUERY,PATH>(cDomainQuery);
	}
	
	public EjbSurveyDomainPathFactory<L,D,QUERY,PATH,DENTITY,DATTRIBUTE> ejbDomainPath()
	{
		return new EjbSurveyDomainPathFactory<L,D,QUERY,PATH,DENTITY,DATTRIBUTE>(cDomainPath);
	}
	
	public EjbSurveyAnalysisFactory<TEMPLATE,ANALYSIS> ejbAnalysis()
	{
		return new EjbSurveyAnalysisFactory<TEMPLATE,ANALYSIS>(cAnalysis);
	}
	
	public EjbSurveyAnalysisQuestionFactory<L,D,QUESTION,ANALYSIS,AQ> ejbAnalysisQuestion()
	{
		return new EjbSurveyAnalysisQuestionFactory<L,D,QUESTION,ANALYSIS,AQ>(cAq);
	}
	
	public EjbSurveyAnalysisToolFactory<L,D,AQ,TOOL,TOOLT> ejbAnalysisTool()
	{
		return new EjbSurveyAnalysisToolFactory<L,D,AQ,TOOL,TOOLT>(cTool);
	}
}