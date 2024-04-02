package org.jeesl.factory.sql.module;

import java.util.HashMap;
import java.util.Map;

import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.survey.SurveyAnalysisFactoryBuilder;
import org.jeesl.factory.builder.module.survey.SurveyCoreFactoryBuilder;
import org.jeesl.interfaces.model.io.domain.JeeslDomain;
import org.jeesl.interfaces.model.io.domain.JeeslDomainPath;
import org.jeesl.interfaces.model.io.domain.JeeslDomainQuery;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.module.survey.analysis.JeeslSurveyAnalysis;
import org.jeesl.interfaces.model.module.survey.analysis.JeeslSurveyAnalysisQuestion;
import org.jeesl.interfaces.model.module.survey.analysis.JeeslSurveyAnalysisTool;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurvey;
import org.jeesl.interfaces.model.module.survey.correlation.JeeslSurveyCorrelation;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyData;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlSurveyAnalysisFactory <SURVEY extends JeeslSurvey<?,?,?,?,DATA>,
									  QUESTION extends JeeslSurveyQuestion<?,?,?,?,?,?,?,?,?,?,?>,
									  ANSWER extends JeeslSurveyAnswer<?,?,QUESTION,?,DATA,?>,
									  DATA extends JeeslSurveyData<?,?,SURVEY,ANSWER,CORRELATION>,
									  CORRELATION extends JeeslSurveyCorrelation<DATA>,
									  DOMAIN extends JeeslDomain<?,DENTITY>,
									  QUERY extends JeeslDomainQuery<?,?,DOMAIN,PATH>,
									  PATH extends JeeslDomainPath<?,?,QUERY,DENTITY,DATTRIBUTE>,
									  DENTITY extends JeeslRevisionEntity<?,?,?,?,DATTRIBUTE,?>,
									  DATTRIBUTE extends JeeslRevisionAttribute<?,?,DENTITY,?,?>,
									  ANALYSIS extends JeeslSurveyAnalysis<?,?,?,DOMAIN,DENTITY,DATTRIBUTE>,
									  AQ extends JeeslSurveyAnalysisQuestion<?,?,QUESTION,ANALYSIS>,
									  TOOL extends JeeslSurveyAnalysisTool<?,?,?,QUERY,DATTRIBUTE,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(SqlSurveyAnalysisFactory.class);

	private boolean explainAnalyse = false;
	private boolean debugOnInfo = true;
	private final Map<String,String> mapTable;
	
	private final SurveyCoreFactoryBuilder<?,?,SURVEY,?,?,?,?,?,?,?,QUESTION,ANSWER,?,DATA,?,CORRELATION> fbCore;
	private final SurveyAnalysisFactoryBuilder<?,?,?,QUESTION,?,?,ANSWER,?,DATA,?,CORRELATION,?,?,?,?,?,?,?,TOOL,?,?> fbAnalysis;
	
	public SqlSurveyAnalysisFactory(SurveyCoreFactoryBuilder<?,?,SURVEY,?,?,?,?,?,?,?,QUESTION,ANSWER,?,DATA,?,CORRELATION> fbCore,
									SurveyAnalysisFactoryBuilder<?,?,?,QUESTION,?,?,ANSWER,?,DATA,?,CORRELATION,DOMAIN,?,?,?,?,?,?,TOOL,?,?> fbAnalysis)
	{
		this.fbCore=fbCore;
		this.fbAnalysis=fbAnalysis;
		
		mapTable = new HashMap<String,String>();
	}
	
	private String getTableName(String c)
	{
		if(!mapTable.containsKey(c))
		{
			try
			{
				Class<?> cl = Class.forName(c);
				mapTable.put(cl.getName(), ReflectionUtil.toTable(cl));
			}
			catch (ClassNotFoundException e) {e.printStackTrace();}
			catch (JeeslNotFoundException e) {e.printStackTrace();}
		}
		return mapTable.get(c);
	}
	
	public String bool(QUESTION question, SURVEY survey, TOOL tool)
	{
		if(debugOnInfo) {logger.info("Bulding Bool Query for q:"+question.toString());}
		CorrelationInfo info = build(tool);
		
		StringBuilder sb = new StringBuilder();
		if(explainAnalyse) {sb.append("EXPLAIN ANALYSE ");}
		sb.append("SELECT ").append(info.tbAnswer).append(".question_id as questionId");
		sb.append(", ").append(info.tbAnswer).append(".").append(JeeslSurveyAnswer.Attributes.valueBoolean.toString());
		sb.append(", COUNT(").append(info.tbAnswer).append(".").append(JeeslSurveyAnswer.Attributes.valueBoolean.toString()).append(") as counter");
		if(info.getCorrelationColumn().length()>0) {sb.append(", ").append(info.getCorrelationColumn()).append(" as correlationId");}
		sb.append("\n");
		
		sb.append("FROM ").append(getTableName(fbCore.getClassAnswer().getName())).append("\n");
		sb.append("  INNER JOIN ").append(info.tbData).append(" ON ").append(info.tbData).append(".id=").append(getTableName(fbCore.getClassAnswer().getName())).append(".data_id\n");
		sb.append("  INNER JOIN ").append(info.tbSurvey).append(" ON ").append(info.tbSurvey).append(".id=").append(info.tbData).append(".survey_id\n");
		sb.append("  INNER JOIN ").append(info.tbCorrelation).append(" ON ").append(info.tbCorrelation).append(".id=").append(info.tbData).append(".correlation_id\n");
		sb.append(info.getCorrelationJoin());
		
		sb.append("WHERE (").append(info.tbSurvey).append(".id in (").append(survey.getId()).append("))\n");
		sb.append("  AND ").append(info.tbAnswer).append(".question_id=").append(question.getId()).append("\n");
		sb.append("GROUP BY ").append(info.tbAnswer).append(".question_id, ");
							  sb.append(info.tbAnswer).append(".").append(JeeslSurveyAnswer.Attributes.valueBoolean.toString());
		this.addCorrelationGrouping(info,sb);
		return sb.toString();
	}
	
	public String option(QUESTION question, SURVEY survey, TOOL tool)
	{
		if(debugOnInfo) {logger.info("Bulding Option Query for q:"+question.toString());}
		CorrelationInfo info = build(tool);
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ").append(info.tbAnswer).append(".question_id as questionId");
		sb.append(", ").append(info.tbAnswer).append(".option_id as optionId");
		sb.append(", COUNT(").append(info.tbAnswer).append(".option_id) as counter");
		if(info.getCorrelationColumn().length()>0) {sb.append(", ").append(info.getCorrelationColumn()).append(" as correlationId");}
		sb.append("\n");
		
		sb.append("FROM ").append(getTableName(fbCore.getClassAnswer().getName())).append("\n");
		sb.append("  INNER JOIN ").append(info.tbData).append(" ON ").append(info.tbData).append(".id=").append(getTableName(fbCore.getClassAnswer().getName())).append(".data_id\n");
		sb.append("  INNER JOIN ").append(info.tbSurvey).append(" ON ").append(info.tbSurvey).append(".id=").append(info.tbData).append(".survey_id\n");
		sb.append("  INNER JOIN ").append(info.tbCorrelation).append(" ON ").append(info.tbCorrelation).append(".id=").append(info.tbData).append(".correlation_id\n");
		sb.append(info.getCorrelationJoin());
		sb.append("  ,SurveyOption opt\n");
		
		sb.append("WHERE ").append(info.tbAnswer).append(".option_id=opt.id\n");
		sb.append("  AND (").append(info.tbSurvey).append(".id in (").append(survey.getId()).append("))\n");
		sb.append("  AND ").append(info.tbAnswer).append(".question_id=").append(question.getId()).append("\n");
		sb.append("GROUP BY ").append(info.tbAnswer).append(".question_id, ");
							  sb.append(info.tbAnswer).append(".option_id");
		this.addCorrelationGrouping(info,sb);
		return sb.toString();
	}
	
	private CorrelationInfo build(TOOL tool)
	{	
		CorrelationInfo info = new CorrelationInfo();
		
		StringBuilder sbCorelationColumn = new StringBuilder();
		StringBuilder sbCorelationJoin = new StringBuilder();
		
		if(tool!=null && tool.getQuery()!=null)
		{
			sbCorelationColumn.append(getTableName(tool.getAnalysisQuestion().getAnalysis().getEntity().getCode())).append(".id");
			
			sbCorelationJoin.append("INNER JOIN ").append(getTableName(tool.getAnalysisQuestion().getAnalysis().getEntity().getCode())).append(" ON ").append(getTableName(tool.getAnalysisQuestion().getAnalysis().getEntity().getCode())).append(".id=").append(info.tbCorrelation).append(".id\n");
			
			for(int i=0;i<tool.getQuery().getPaths().size();i++)
			{
				PATH p = tool.getQuery().getPaths().get(i);
				sbCorelationJoin.append("    INNER JOIN ").append(getTableName(p.getEntity().getCode())).append(" ON ");
				
				if(i==0)
				{
					sbCorelationJoin.append(getTableName(p.getEntity().getCode())).append(".id");
					sbCorelationJoin.append("=");
					sbCorelationJoin.append(getTableName(tool.getAnalysisQuestion().getAnalysis().getEntity().getCode())).append(".");
					sbCorelationJoin.append(tool.getAttribute().getCode()).append("_id");
					sbCorelationJoin.append("\n");
				}
				else
				{
					PATH previousPath = tool.getQuery().getPaths().get(i-1);
					
					if(BooleanComparator.active(previousPath.getAttribute().getRelationOwner()))
					{
						sbCorelationJoin.append(getTableName(p.getEntity().getCode())).append(".id");
						sbCorelationJoin.append("=");
						sbCorelationJoin.append(getTableName(previousPath.getEntity().getCode())).append(".");
						sbCorelationJoin.append(previousPath.getAttribute().getCode()).append("_id\n");
					}
					else
					{
						sbCorelationJoin.append(getTableName(p.getEntity().getCode())).append(".");
						try
						{
							sbCorelationJoin.append(ReflectionUtil.getReverseMapping(previousPath.getEntity().getCode(), "", p.getEntity().getCode()));
							sbCorelationJoin.append("_id");
						}
						catch (JeeslNotFoundException e) {e.printStackTrace();}
						sbCorelationJoin.append("=");
						sbCorelationJoin.append(getTableName(previousPath.getEntity().getCode())).append(".id\n");
					}
				}
				sbCorelationColumn.setLength(0);
				if(p.getAttribute().getRelation()==null)
				{
					sbCorelationColumn.append(getTableName(p.getEntity().getCode())).append(".id");
				}
				else
				{
					sbCorelationColumn.append(getTableName(p.getEntity().getCode())).append(".").append(p.getAttribute().getCode()).append("_id");
				}
			}
		}
		
		info.setCorrelationColumn(sbCorelationColumn.toString());
		info.setCorrelationJoin(sbCorelationJoin.toString());
		return info;
	}
	
	private void addCorrelationGrouping(CorrelationInfo info, StringBuilder sb)
	{
		if(info.getCorrelationColumn().length()>0)
		{
			sb.append(", ").append(info.getCorrelationColumn());
		}
	}
	
	private class CorrelationInfo
	{
		private String correlationColumn; public String getCorrelationColumn() {return correlationColumn;} public void setCorrelationColumn(String correlationColumn) {this.correlationColumn = correlationColumn;}
		private String correlationJoin; public String getCorrelationJoin() {return correlationJoin;} public void setCorrelationJoin(String correlationJoin) {this.correlationJoin = correlationJoin;}
		
		public String tbAnswer;
		public String tbData;
		public String tbSurvey;
		public String tbCorrelation;
		
		public CorrelationInfo()
		{
			tbAnswer = getTableName(fbCore.getClassAnswer().getName());
			tbData = getTableName(fbCore.getClassData().getName());
			tbSurvey = getTableName(fbCore.getClassSurvey().getName());
			tbCorrelation = getTableName(fbCore.getClassCorrelation().getName());
		}
	}
}