package org.jeesl.util.query.xml.module;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.jeesl.factory.xml.module.survey.XmlAnswerFactory;
import org.jeesl.factory.xml.module.survey.XmlCorrelationFactory;
import org.jeesl.factory.xml.module.survey.XmlDataFactory;
import org.jeesl.factory.xml.module.survey.XmlOptionFactory;
import org.jeesl.factory.xml.module.survey.XmlOptionsFactory;
import org.jeesl.factory.xml.module.survey.XmlQuestionFactory;
import org.jeesl.factory.xml.module.survey.XmlScoreFactory;
import org.jeesl.factory.xml.module.survey.XmlSurveyFactory;
import org.jeesl.factory.xml.module.survey.XmlTemplateFactory;
import org.jeesl.factory.xml.system.lang.XmlDescriptionFactory;
import org.jeesl.factory.xml.system.status.XmlCategoryFactory;
import org.jeesl.factory.xml.system.status.XmlStatusFactory;
import org.jeesl.factory.xml.system.util.text.XmlRemarkFactory;
import org.jeesl.model.xml.jeesl.QuerySurvey;
import org.jeesl.model.xml.module.survey.Answer;
import org.jeesl.model.xml.module.survey.Cell;
import org.jeesl.model.xml.module.survey.Column;
import org.jeesl.model.xml.module.survey.Data;
import org.jeesl.model.xml.module.survey.Matrix;
import org.jeesl.model.xml.module.survey.Option;
import org.jeesl.model.xml.module.survey.Options;
import org.jeesl.model.xml.module.survey.Question;
import org.jeesl.model.xml.module.survey.Row;
import org.jeesl.model.xml.module.survey.Score;
import org.jeesl.model.xml.module.survey.Section;
import org.jeesl.model.xml.module.survey.Survey;
import org.jeesl.model.xml.module.survey.Surveys;
import org.jeesl.model.xml.module.survey.Template;

import net.sf.ahtutils.factory.xml.status.XmlUnitFactory;
import net.sf.ahtutils.xml.aht.Query;
import net.sf.exlp.util.DateUtil;

public class XmlSurveyQuery
{
	public static enum Key {exTemplate,rTemplate,exSurveys,exSurvey,surveyAnswers,rSection}
	
	private static Map<Key,Query> mQueries;
	
	public static Query get(Key key){return get(key,null);}
	public static Query get(Key key,String lang)
	{
		if(mQueries==null){mQueries = new Hashtable<Key,Query>();}
		if(!mQueries.containsKey(key))
		{
			Query q = new Query();
			switch(key)
			{
				case exTemplate: q.setTemplate(exTemplate());break;
				case rTemplate: q.setTemplate(rTemplate());break;
				case exSurvey: q.setSurvey(exSurvey());break;
				case exSurveys: q.setSurveys(exSurveys());break;
				case surveyAnswers: q.setAnswer(surveyAnswers());
			}
			mQueries.put(key, q);
		}
		Query q = mQueries.get(key);
		q.setLang(lang);
		return q;
	}
	
	public static Template exTemplate()
	{		
		Template xml = new Template();
		xml.setId(0l);
		xml.setCode("");
		xml.setDescription(XmlDescriptionFactory.build(""));
		xml.setRemark(XmlRemarkFactory.build(""));
		xml.setCategory(XmlCategoryFactory.create(""));
		xml.setStatus(XmlStatusFactory.create(""));
		xml.getSection().add(exSection());
//		xml.getSection().get(0).getSection().add(exSection());
		return xml;
	}
	
	public static Template rTemplate()
	{		
		Template xml = new Template();
		xml.setCode("");
		xml.getSection().add(rSection());
		return xml;
	}
	
	public static Section exSection()
	{		
		Section xml = rSection();
		xml.getQuestion().clear();
		xml.getQuestion().add(exQuestion());
		return xml;
	}
	
	public static Section rSection()
	{		
		Section xml = new Section();
		xml.setId(0l);
		xml.setCode("");
		xml.setDescription(XmlDescriptionFactory.build(""));
		xml.setRemark(XmlRemarkFactory.build(""));
		xml.getQuestion().add(rQuestion());
		return xml;
	}
	
	public static Question exQuestion()
	{		
		Question xml = new Question();
		xml.setId(0l);
		xml.setPosition(0);
		xml.setVisible(true);
		xml.setCode("");
		xml.setTopic("");
		xml.setUnit(XmlUnitFactory.build(""));
		xml.setQuestion(net.sf.ahtutils.factory.xml.text.XmlQuestionFactory.build(""));
		xml.setRemark(XmlRemarkFactory.build(""));
		
		xml.setShowBoolean(true);
		xml.setShowInteger(true);
		xml.setShowDouble(true);
		xml.setShowText(true);
		xml.setShowScore(true);
		xml.setShowRemark(true);
		xml.setShowSelectOne(true);
		
		Score score = XmlScoreFactory.build();
		score.setMax(0d);
		xml.setScore(score);
		
		xml.setOptions(exOptions());
		
		return xml;
	}
	
	public static Question rQuestion()
	{		
		Question xml = new Question();
		xml.setId(0l);
		xml.setCode("");
		xml.setTopic("");
		return xml;
	}
	
	private static Options exOptions()
	{
		Options xml = XmlOptionsFactory.build();
		xml.getOption().add(exOption());
		return xml;
	}
	
	private static Option exOption()
	{
		Option xml = XmlOptionFactory.build();
		xml.setCode("");
		xml.setLabel("");
		
		return xml;
	}
	
	public static Surveys exSurveys()
	{				
		Surveys xml = new Surveys();
		xml.getSurvey().add(XmlSurveyFactory.id());
		
		return xml;
	}
	
	public static Survey exSurvey()
	{				
		Survey xml = new Survey();
		xml.setId(0l);
		xml.setName("");
		xml.setValidFrom(DateUtil.toXmlGc(new Date()));
		xml.setValidTo(DateUtil.toXmlGc(new Date()));
		xml.setStatus(XmlStatusFactory.create(""));
		xml.setTemplate(XmlTemplateFactory.id());
		xml.getData().add(exData());
		return xml;
	}
	
	private static Data exData()
	{		
		Data xml = XmlDataFactory.id();
		xml.setCorrelation(XmlCorrelationFactory.id());
		xml.getAnswer().add(exAnswer());
		return xml;
	}
	
	private static Answer exAnswer()
	{
		Answer xml = XmlAnswerFactory.id();
		xml.setQuestion(org.jeesl.factory.xml.module.survey.XmlQuestionFactory.id());
		xml.setValueBoolean(true);
		xml.setValueNumber(0);
		return xml;
	}
	
	private static Answer surveyAnswers()
	{
		Data data = XmlDataFactory.id();
		data.setCorrelation(XmlCorrelationFactory.id());
		
		Answer xml = XmlAnswerFactory.id();
		xml.setQuestion(org.jeesl.factory.xml.module.survey.XmlQuestionFactory.id());
		xml.setValueBoolean(true);
		xml.setValueNumber(0);
		xml.setData(data);
		return xml;
	}
	
	
	//JEESL
	public static enum KeyJeesl {dataWithStructure,answer,exTemplate,template,questionCode,dataWithAnswerQuestionCode,dataWithAnswerAndQuestion}
	
	private static Map<KeyJeesl,QuerySurvey> mQueriesJeesl;
	
	public static QuerySurvey getJeesl(KeyJeesl key){return getJeesl(key,null);}
	public static QuerySurvey getJeesl(KeyJeesl key,String localeCode)
	{
		if(mQueriesJeesl==null){mQueriesJeesl = new Hashtable<KeyJeesl,QuerySurvey>();}
		if(!mQueriesJeesl.containsKey(key))
		{
			QuerySurvey q = new QuerySurvey();
			switch(key)
			{
				case dataWithStructure: q.setData(dataWithStructure());break;
				case dataWithAnswerQuestionCode: q.setData(dataWithAnswerQuestionCode());break;
				case dataWithAnswerAndQuestion: q.setData(dataWithAnswerAndQuestion());break;
				case answer: q.setAnswer(answer());break;
				case questionCode: q.setQuestion(question());break;
				case exTemplate: q.setTemplate(exTemplate());break;
				case template: q.setTemplate(rTemplate());break;
			}
			mQueriesJeesl.put(key, q);
		}
		QuerySurvey q = mQueriesJeesl.get(key);
		q.setLocaleCode(localeCode);
		return q;
	}
	
	private static Data dataWithStructure()
	{	
		Data xml = new Data();
		xml.setId(0l);
		xml.getSection().add(exSection());
		return xml;
	}
	
	private static Data dataWithAnswerQuestionCode()
	{	
		Question question = new Question();
		question.setCode("");
		
		Answer answer = answer();
		answer.setQuestion(question);
		
		Data xml = new Data();
		xml.setId(0l);
		xml.getAnswer().add(answer);
		return xml;
	}
	
	private static Data dataWithAnswerAndQuestion()
	{	
		Question question = new Question();
		question.setCode("");
		question.setQuestion(net.sf.ahtutils.factory.xml.text.XmlQuestionFactory.build(""));
		
		Answer answer = answer();
		answer.setQuestion(question);
		
		Data xml = new Data();
		xml.setId(0l);
		xml.getAnswer().add(answer);
		return xml;
	}
	
	private static Question question()
	{
		Question xml = XmlQuestionFactory.id();
		xml.setCode("");
		return xml;
	}
	
	private static Answer answer()
	{
		Answer xml = XmlAnswerFactory.id();
		xml.setValueBoolean(true);
		xml.setValueNumber(0);
		xml.setValueDouble(0d);
		xml.setValueDate(DateUtil.toXmlGc(new Date()));
		xml.setScore(0d);
		xml.setAnswer(net.sf.ahtutils.factory.xml.text.XmlAnswerFactory.build(""));
		xml.setRemark(XmlRemarkFactory.build(""));
		
		Option option = new Option();
		option.setId(0l);
		option.setCode("");
		option.setLabel("");
		xml.setOption(option);
		
		Cell cell = new Cell();cell.setLabel("");
		Column column = new Column();column.setLabel("");column.setCell(cell);
		Row row = new Row();row.setLabel("");row.getColumn().add(column);
		Matrix matrix = new Matrix();
		matrix.getRow().add(row);
		xml.setMatrix(matrix);
		
		return xml;
	}
}