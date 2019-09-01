package org.jeesl.web.mbean.prototype.module.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jeesl.api.bean.JeeslSurveyBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.survey.JeeslSurveyAnalysisFacade;
import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.api.facade.module.survey.JeeslSurveyTemplateFacade;
import org.jeesl.api.facade.system.JeeslJobFacade;
import org.jeesl.controller.handler.module.survey.SurveyAnalysisCacheHandler;
import org.jeesl.controller.handler.sb.SbSingleHandler;
import org.jeesl.controller.processor.survey.analysis.SurveySelectOneProcessor;
import org.jeesl.factory.builder.module.survey.SurveyAnalysisFactoryBuilder;
import org.jeesl.factory.builder.module.survey.SurveyCoreFactoryBuilder;
import org.jeesl.factory.builder.module.survey.SurveyTemplateFactoryBuilder;
import org.jeesl.factory.ejb.system.io.domain.EjbSurveyDomainQueryFactory;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.json.module.survey.JsonSurveyValueFactory;
import org.jeesl.factory.json.module.survey.JsonSurveyValuesFactory;
import org.jeesl.factory.json.system.io.report.JsonFlatFigureFactory;
import org.jeesl.factory.json.system.io.report.JsonFlatFiguresFactory;
import org.jeesl.factory.mc.survey.McOptionDataSetFactory;
import org.jeesl.interfaces.bean.sb.SbSingleBean;
import org.jeesl.interfaces.factory.txt.JeeslReportAggregationLevelFactory;
import org.jeesl.interfaces.model.module.survey.analysis.JeeslSurveyAnalysis;
import org.jeesl.interfaces.model.module.survey.analysis.JeeslSurveyAnalysisQuestion;
import org.jeesl.interfaces.model.module.survey.analysis.JeeslSurveyAnalysisTool;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurvey;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScheme;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScore;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplate;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateVersion;
import org.jeesl.interfaces.model.module.survey.correlation.JeeslSurveyCorrelation;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyData;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyMatrix;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyCondition;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOptionSet;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidation;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyValidationAlgorithm;
import org.jeesl.interfaces.model.system.io.domain.JeeslDomain;
import org.jeesl.interfaces.model.system.io.domain.JeeslDomainPath;
import org.jeesl.interfaces.model.system.io.domain.JeeslDomainQuery;
import org.jeesl.interfaces.model.system.io.revision.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.system.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.job.JeeslJobCache;
import org.jeesl.interfaces.model.system.job.JeeslJobTemplate;
import org.jeesl.model.json.JsonFlatFigure;
import org.jeesl.model.json.JsonFlatFigures;
import org.jeesl.model.json.module.survey.JsonSurveyValue;
import org.jeesl.model.json.module.survey.JsonSurveyValues;
import org.jeesl.model.pojo.map.generic.Nested2Map;
import org.metachart.xml.chart.Ds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.exception.ejb.UtilsNotFoundException;
import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.model.interfaces.with.EjbWithId;
import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public abstract class AbstractSurveyReportBean <L extends UtilsLang, D extends UtilsDescription, LOC extends UtilsStatus<LOC,L,D>,
						SURVEY extends JeeslSurvey<L,D,SS,TEMPLATE,DATA>,
						SS extends UtilsStatus<SS,L,D>,
						SCHEME extends JeeslSurveyScheme<L,D,TEMPLATE,SCORE>,
						VALGORITHM extends JeeslSurveyValidationAlgorithm<L,D>,
						TEMPLATE extends JeeslSurveyTemplate<L,D,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,OPTIONS,ANALYSIS>,
						VERSION extends JeeslSurveyTemplateVersion<L,D,TEMPLATE>,
						TS extends UtilsStatus<TS,L,D>,
						TC extends UtilsStatus<TC,L,D>,
						SECTION extends JeeslSurveySection<L,D,TEMPLATE,SECTION,QUESTION>,
						QUESTION extends JeeslSurveyQuestion<L,D,SECTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION,AQ>,
						CONDITION extends JeeslSurveyCondition<QUESTION,QE,OPTION>,
						VALIDATION extends JeeslSurveyValidation<L,D,QUESTION,VALGORITHM>,
						QE extends UtilsStatus<QE,L,D>,
						SCORE extends JeeslSurveyScore<L,D,SCHEME,QUESTION>,
						UNIT extends UtilsStatus<UNIT,L,D>,
						ANSWER extends JeeslSurveyAnswer<L,D,QUESTION,MATRIX,DATA,OPTION>,
						MATRIX extends JeeslSurveyMatrix<L,D,ANSWER,OPTION>,
						DATA extends JeeslSurveyData<L,D,SURVEY,ANSWER,CORRELATION>,
						OPTIONS extends JeeslSurveyOptionSet<L,D,TEMPLATE,OPTION>,
						OPTION extends JeeslSurveyOption<L,D>,
						CORRELATION extends JeeslSurveyCorrelation<DATA>,
						DOMAIN extends JeeslDomain<L,DENTITY>,
						QUERY extends JeeslDomainQuery<L,D,DOMAIN,PATH>,
						PATH extends JeeslDomainPath<L,D,QUERY,DENTITY,DATTRIBUTE>,
						DENTITY extends JeeslRevisionEntity<L,D,?,?,DATTRIBUTE>,
						DATTRIBUTE extends JeeslRevisionAttribute<L,D,DENTITY,?,?>,
						ANALYSIS extends JeeslSurveyAnalysis<L,D,TEMPLATE,DOMAIN,DENTITY,DATTRIBUTE>,
						AQ extends JeeslSurveyAnalysisQuestion<L,D,QUESTION,ANALYSIS>,
						TOOL extends JeeslSurveyAnalysisTool<L,D,QE,QUERY,DATTRIBUTE,AQ,ATT>,
						ATT extends UtilsStatus<ATT,L,D>,
						TOOLCACHETEMPLATE extends JeeslJobTemplate<L,D,?,?,?>,
						CACHE extends JeeslJobCache<TOOLCACHETEMPLATE,?>>
					extends AbstractSurveyBean<L,D,LOC,SURVEY,SS,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,ANALYSIS,AQ,TOOL,ATT,TOOLCACHETEMPLATE,CACHE>
					implements Serializable,SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractSurveyReportBean.class);

	private JeeslJobFacade<L,D,TOOLCACHETEMPLATE,?,?,?,?,?,?,?,?,CACHE,?,?> fJob;
	
	private McOptionDataSetFactory<OPTION> mfOption;	
	private final EjbSurveyDomainQueryFactory<L,D,DOMAIN,QUERY,PATH> efDomainQuery;
	
	private final Map<QUESTION,Ds> mapDsOption; public Map<QUESTION,Ds> getMapDsOption() {return mapDsOption;}
	private final Map<QUESTION,List<TOOL>> mapTool; public Map<QUESTION,List<TOOL>> getMapTool() {return mapTool;}
	private final Map<SECTION,List<QUESTION>> mapQuestion; public Map<SECTION,List<QUESTION>> getMapQuestion() {return mapQuestion;}
	
	private final Map<TOOL,String> mapToolAttributeXpath; public Map<TOOL,String> getMapToolAttributeXpath() {return mapToolAttributeXpath;}
	private final Map<TOOL,DATTRIBUTE> mapToolPathAttribute; public Map<TOOL,DATTRIBUTE> getMapToolPathAttribute() {return mapToolPathAttribute;}
	private final Map<TOOL,List<JsonSurveyValue>> mapToolPathEntities; public Map<TOOL,List<JsonSurveyValue>> getMapToolPathEntities() {return mapToolPathEntities;}
	private final Map<TOOL,Nested2Map<JsonSurveyValue,JsonSurveyValue,JsonSurveyValue>> mapToolPathValue; public Map<TOOL,Nested2Map<JsonSurveyValue,JsonSurveyValue,JsonSurveyValue>> getMapToolPathValue() {return mapToolPathValue;}

	private final Map<TOOL,List<JsonSurveyValue>> mapToolOption; public Map<TOOL,List<JsonSurveyValue>> getMapToolOption() {return mapToolOption;}
	private final Map<TOOL,JsonFlatFigures> mapToolTableOptionGlobal; public Map<TOOL,JsonFlatFigures> getMapToolTableOptionGlobal() {return mapToolTableOptionGlobal;}

	private final Map<TOOL,List<JsonSurveyValue>> mapToolBoolean; public Map<TOOL,List<JsonSurveyValue>> getMapToolBoolean() {return mapToolBoolean;}
	
	private final Map<TOOL,JsonFlatFigures> mapToolTableText; public Map<TOOL,JsonFlatFigures> getMapToolTableText() {return mapToolTableText;}
	private final Map<TOOL,JsonFlatFigures> mapToolTableRemark; public Map<TOOL,JsonFlatFigures> getMapToolTableRemark() {return mapToolTableRemark;}

	protected final SbSingleHandler<ANALYSIS> sbhAnalysis; public SbSingleHandler<ANALYSIS> getSbhAnalysis() {return sbhAnalysis;}
	
	private SurveySelectOneProcessor<L,D,LOC,SURVEY,SS,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,ANALYSIS,AQ,TOOL,ATT,TOOLCACHETEMPLATE,CACHE> s1Processor;

	public AbstractSurveyReportBean(SurveyTemplateFactoryBuilder<L,D,LOC,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,OPTIONS,OPTION> fbTemplate,
			SurveyCoreFactoryBuilder<L,D,LOC,SURVEY,SS,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION,ATT> fbCore,
			SurveyAnalysisFactoryBuilder<L,D,TEMPLATE,QUESTION,QE,SCORE,ANSWER,MATRIX,DATA,OPTION,CORRELATION,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,ANALYSIS,AQ,TOOL,ATT,TOOLCACHETEMPLATE> fbAnalysis)
	{
		super(fbTemplate,fbCore,fbAnalysis);
		
		efDomainQuery = fbAnalysis.ejbDomainQuery();
		
		mapQuestion = new HashMap<SECTION,List<QUESTION>>();
		mapTool = new HashMap<QUESTION,List<TOOL>>();
		mapDsOption = new HashMap<QUESTION,Ds>();
		
		mapToolAttributeXpath = new HashMap<TOOL,String>();
		mapToolPathAttribute = new HashMap<TOOL,DATTRIBUTE>();
		mapToolPathEntities = new HashMap<TOOL,List<JsonSurveyValue>>();
		mapToolPathValue = new HashMap<TOOL,Nested2Map<JsonSurveyValue,JsonSurveyValue,JsonSurveyValue>>();
		
		mapToolOption = new HashMap<TOOL,List<JsonSurveyValue>>();
		mapToolTableOptionGlobal = new HashMap<TOOL,JsonFlatFigures>();
		
		mapToolBoolean = new HashMap<TOOL,List<JsonSurveyValue>>(); 
		mapToolTableText = new HashMap<TOOL,JsonFlatFigures>();
		mapToolTableRemark = new HashMap<TOOL,JsonFlatFigures>();
		
		sbhAnalysis = new SbSingleHandler<ANALYSIS>(fbAnalysis.getClassAnalysis(),this);
		sections = new ArrayList<SECTION>();
	}
	
	protected void initSuperReport(String userLocale, String[] localeCodes, JeeslFacesMessageBean bMessage,
			JeeslSurveyTemplateFacade<L,D,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,OPTIONS,OPTION> fCore,
			JeeslSurveyCoreFacade<L,D,LOC,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION> fSurvey,
			JeeslSurveyAnalysisFacade<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,ANALYSIS,AQ,TOOL,ATT> fAnalysis,
			JeeslJobFacade<L,D,TOOLCACHETEMPLATE,?,?,?,?,?,?,?,?,CACHE,?,?> fJob,
			final JeeslSurveyBean<L,D,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION,ATT> bSurvey,
			JeeslReportAggregationLevelFactory tfName)
	{
		super.initSuperSurvey(new ArrayList<String>(Arrays.asList(localeCodes)),bMessage,fCore,fSurvey,fAnalysis,bSurvey);
		this.fJob=fJob;
		cacheHandler = new SurveyAnalysisCacheHandler<L,D,LOC,SURVEY,SS,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,ANALYSIS,AQ,TOOL,ATT,TOOLCACHETEMPLATE,CACHE>(fJob,fAnalysis);
		s1Processor = new SurveySelectOneProcessor<L,D,LOC,SURVEY,SS,SCHEME,VALGORITHM,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,CONDITION,VALIDATION,QE,SCORE,UNIT,ANSWER,MATRIX,DATA,OPTIONS,OPTION,CORRELATION,DOMAIN,QUERY,PATH,DENTITY,DATTRIBUTE,ANALYSIS,AQ,TOOL,ATT,TOOLCACHETEMPLATE,CACHE>(bSurvey,cacheHandler,efTool,tfName);
		
		mfOption = new McOptionDataSetFactory<OPTION>(tfName);
		initPageSettings();
		
		sbhCategory.silentCallback();
	}
	
	@Override public void selectSbSingle(EjbWithId ejb)
	{
		if(debugOnInfo) {logger.info("sb" + (ejb!=null));}
		if(ejb!=null)
		{
			if(fbTemplate.getClassTemplateCategory().isAssignableFrom(ejb.getClass()))
			{
				List<TC> categories = new ArrayList<TC>();categories.add(sbhCategory.getSelection());
				sbhSurvey.setList(fCore.fSurveysForCategories(categories));
				
				for(SURVEY s : sbhSurvey.getList())
				{
					logger.warn(s.toString()+" "+s.getTemplate().getCategory());
				}
				
				logger.info(AbstractLogMessage.reloaded(fbCore.getClassSurvey(), sbhSurvey.getList())+" for category="+sbhCategory.getSelection().getCode());
				sbhSurvey.silentCallback();
			}
			else if(fbCore.getClassSurvey().isAssignableFrom(ejb.getClass()))
			{
				sbhAnalysis.setList(fCore.allForParent(fbAnalysis.getClassAnalysis(), sbhSurvey.getSelection().getTemplate()));
				logger.info(AbstractLogMessage.reloaded(fbAnalysis.getClassAnalysis(), sbhAnalysis.getList()));
				sbhAnalysis.silentCallback();
			}
			else if(fbAnalysis.getClassAnalysis().isAssignableFrom(ejb.getClass()))
			{
				reloadAnalysis();
			}
		}
	}
	
	private void reloadAnalysis()
	{
		if(debugOnInfo) {logger.info("Reload Analysis");}
		reloadSurvey();
	}
	
	private void reloadSurvey()
	{
		if(debugOnInfo) {logger.info("Reload Survey: "+sbhSurvey.getSelection().toString());}
		mapDsOption.clear();
		sections.clear();
		mapQuestion.clear();
		mapTool.clear();
		
		mapToolAttributeXpath.clear();
		mapToolOption.clear();
		mapToolPathEntities.clear();
		mapToolPathValue.clear();
		
		mapToolBoolean.clear();
		
		mapToolTableOptionGlobal.clear();
		mapToolTableText.clear();
		
		for(SECTION section : bSurvey.getMapSection().get(sbhSurvey.getSelection().getTemplate()))
		{
			mapQuestion.put(section,new ArrayList<QUESTION>());
			for(QUESTION q : bSurvey.getMapQuestion().get(section))
			{
				try
				{
					AQ analysis = fAnalysis.fAnalysis(sbhAnalysis.getSelection(), q);
					List<TOOL> tools = new ArrayList<TOOL>();
					mapQuestion.get(section).add(q);
					for(TOOL tool : fCore.allForParent(fbAnalysis.getClassAnalysisTool(), analysis))
					{
						if(tool.isVisible())
						{
							if(debugOnInfo) {logger.info("Crunching data for tool:"+tool.toString()+" "+tool.getElement().getCode());}
							
							Set<Long> optionIds = new HashSet<Long>();
							mapToolPathEntities.put(tool,new ArrayList<JsonSurveyValue>());
							tool = fAnalysis.load(tool);
							
							if(tool.getElement().getCode().equals(JeeslSurveyAnalysisTool.Elements.selectOne.toString()))
							{
								if(debugOnInfo) {logger.info("Handling :"+JeeslSurveyAnalysisTool.Elements.selectOne+" withDomainQuery:"+efTool.withDomainQuery(tool));}
								JsonFlatFigures ff = cacheHandler.surveyStatisticOption(q, sbhSurvey.getSelection(), tool);
								
								for(JsonFlatFigure f : ff.getFigures())
								{
									optionIds.add(f.getL2());
								}
								mapToolTableOptionGlobal.put(tool,ff);
								
								if(!efTool.withDomainQuery(tool))
								{
									Ds ds2 = mfOption.build(ff,bSurvey.getMapOption().get(q));
									mapDsOption.put(q,ds2);
								}
								else
								{
									Nested2Map<JsonSurveyValue,JsonSurveyValue,JsonSurveyValue> nm2 = new Nested2Map<JsonSurveyValue,JsonSurveyValue,JsonSurveyValue>();
									long idValue = 1;
									Set<Long> pathIds = new HashSet<Long>();
									for(JsonFlatFigure f : ff.getFigures())
									{
										idValue++;
										JsonSurveyValue vOption = JsonSurveyValueFactory.build(f.getL2());
										JsonSurveyValue vPath = JsonSurveyValueFactory.build(f.getL4());
										JsonSurveyValue vCount = JsonSurveyValueFactory.build(idValue,f.getL3());
										nm2.put(vOption, vPath, vCount);
										pathIds.add(vPath.getId());
									}
									mapToolPathValue.put(tool,nm2);
									buildPathEntities(tool,pathIds);
								}
								
								mapToolOption.put(tool,JsonSurveyValueFactory.build(optionIds));
							}
							
							else if(tool.getElement().getCode().equals(JeeslSurveyAnalysisTool.Elements.bool.toString()))
							{
								if(debugOnInfo) {logger.info("Handling :"+JeeslSurveyAnalysisTool.Elements.bool+" withDomainQuery:"+efTool.withDomainQuery(tool));}
								JsonSurveyValues ff = cacheHandler.surveyStatisticBoolean(q, sbhSurvey.getSelection(),tool);
								
								if(efTool.withDomainQuery(tool))
								{
									mapToolOption.put(tool,JsonSurveyValuesFactory.buildBooleanList());
									Nested2Map<JsonSurveyValue,JsonSurveyValue,JsonSurveyValue> nm2 = new Nested2Map<JsonSurveyValue,JsonSurveyValue,JsonSurveyValue>();
									long idValue = 1;
									Set<Long> pathIds = new HashSet<Long>();
									for(JsonSurveyValue v : ff.getValues())
									{
										idValue++;
										JsonSurveyValue vOption = JsonSurveyValueFactory.build(v.getBool());
										JsonSurveyValue vPath = JsonSurveyValueFactory.build(v.getPathId());
										JsonSurveyValue vCount = JsonSurveyValueFactory.build(idValue,v.getCount());
										nm2.put(vOption, vPath, vCount);
										pathIds.add(vPath.getId());
									}
									mapToolPathValue.put(tool,nm2);
									buildPathEntities(tool,pathIds);
								}
								else
								{
									EjbIdFactory.setNegativeIds(ff.getValues());
									mapToolOption.put(tool,ff.getValues());
								}
							}
							
							else if(tool.getElement().getCode().equals(JeeslSurveyAnalysisTool.Elements.text.toString()))
							{
								JsonFlatFigures f = JsonFlatFiguresFactory.build();
								for(ANSWER a : fCore.fAnswers(sbhSurvey.getSelection(),q))
								{
									if(a.getValueText()!=null && a.getValueText().trim().length()>0)
									{
										f.getFigures().add(JsonFlatFigureFactory.build(a.getValueText()));
									}
								}
							}
							
							else if(tool.getElement().getCode().equals(JeeslSurveyAnalysisTool.Elements.remark.toString()))
							{
								JsonFlatFigures f = JsonFlatFiguresFactory.build();
								for(ANSWER a : fCore.fAnswers(sbhSurvey.getSelection(),q))
								{
									if(a.getRemark()!=null && a.getRemark().trim().length()>0)
									{
										f.getFigures().add(JsonFlatFigureFactory.build(a.getRemark()));
									}
								}
								mapToolTableRemark.put(tool,f);
							}
							
							tools.add(tool);
						}
					}
					
					mapTool.put(q,tools);
				}
				catch (UtilsNotFoundException e) {}
			}
			if(!mapQuestion.get(section).isEmpty()) {sections.add(section);}
		}
	}
	
	private void buildPathEntities(TOOL tool, Set<Long> pathIds)
	{
		PATH path = efDomainQuery.toEffectiveAttribute(tool.getQuery());
		
		String xpath;
		if(path.getAttribute().getXpath().startsWith(path.getAttribute().getCode()))
		{
			int indexStart = path.getAttribute().getCode().length()+1;
			int indexEnd = path.getAttribute().getXpath().length();
			logger.info("subString: "+indexStart+" - "+indexEnd);
			xpath = path.getAttribute().getXpath().substring(indexStart, indexEnd);
		}
		else {xpath = path.getAttribute().getXpath();}
		if(debugOnInfo) {logger.info("XPath for:"+tool.toString()+": "+xpath);}
		
		mapToolAttributeXpath.put(tool,xpath);
		mapToolPathAttribute.put(tool, efDomainQuery.toEffectiveAttribute(tool.getQuery()).getAttribute());
		
		try {mapToolPathEntities.get(tool).addAll(JsonSurveyValueFactory.build(fAnalysis,pathIds,path));}
		catch (ClassNotFoundException e) {e.printStackTrace();}
	}
}