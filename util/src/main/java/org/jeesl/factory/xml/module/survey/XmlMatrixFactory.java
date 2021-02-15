package org.jeesl.factory.xml.module.survey;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.factory.ejb.module.survey.EjbSurveyMatrixFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyOptionFactory;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurvey;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScheme;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyScore;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplate;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateCategory;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateStatus;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateVersion;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyData;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyMatrix;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyStatus;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOptionSet;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionElement;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestionUnit;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.pojo.map.generic.Nested2Map;
import org.jeesl.model.xml.module.survey.Column;
import org.jeesl.model.xml.module.survey.Matrix;
import org.jeesl.model.xml.module.survey.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlMatrixFactory<L extends JeeslLang,D extends JeeslDescription,
				SURVEY extends JeeslSurvey<L,D,SS,TEMPLATE,DATA>,
				SS extends JeeslSurveyStatus<L,D,SS,?>,
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
				ANSWER extends JeeslSurveyAnswer<L,D,QUESTION,MATRIX,DATA,OPTION>,
				MATRIX extends JeeslSurveyMatrix<L,D,ANSWER,OPTION>,
				DATA extends JeeslSurveyData<L,D,SURVEY,ANSWER,?>,
				OPTIONS extends JeeslSurveyOptionSet<L,D,TEMPLATE,OPTION>,OPTION extends JeeslSurveyOption<L,D>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlMatrixFactory.class);
	
	private JeeslSurveyCoreFacade<L,D,?,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,?,OPTIONS,OPTION,?> fSurvey;
//	private Class<SURVEY> cSurvey;
		
	private String localeCode;
	@SuppressWarnings("unused")
	private final Matrix q;
	
	private EjbSurveyOptionFactory<QUESTION,OPTION> efOption;
	private EjbSurveyMatrixFactory<ANSWER,MATRIX,OPTION> efMatrix;
	
	private XmlCellFactory<MATRIX> xfCell;
		
	public XmlMatrixFactory(String localeCode, Matrix q)
	{
		this.localeCode=localeCode;
		this.q=q;
		efOption = new EjbSurveyOptionFactory<QUESTION,OPTION>(null);
		efMatrix = new EjbSurveyMatrixFactory<ANSWER,MATRIX,OPTION>(null);
		xfCell = new XmlCellFactory<MATRIX>(localeCode);
	}
	
	public void lazyLoad(JeeslSurveyCoreFacade<L,D,?,SURVEY,SS,SCHEME,TEMPLATE,VERSION,TS,TC,SECTION,QUESTION,QE,SCORE,UNIT,ANSWER,MATRIX,?,OPTIONS,OPTION,?> fSurvey)
	{
		this.fSurvey=fSurvey;
	}
	
	public Matrix build(ANSWER answer)
	{
		QUESTION question = answer.getQuestion();
		
		List<OPTION> options = new ArrayList<>();
		if(fSurvey!=null)
		{
			answer = fSurvey.load(answer);
			question = fSurvey.load(question);
			options.addAll(question.getOptions());
		}
		else
		{
			options.addAll(answer.getQuestion().getOptions());
		}
		
		List<OPTION> rows = efOption.toRows(question.getOptions());
		List<OPTION> columns = efOption.toColumns(question.getOptions());
		Nested2Map<OPTION,OPTION,MATRIX> map = efMatrix.build(answer.getMatrix());
		
		Matrix xml = build();
		for(OPTION eRow : rows)
		{
			Row xRow = row(eRow);
			for(OPTION eColumn : columns)
			{
				Column xColumn = column(eColumn);
				if(map.containsKey(eRow,eColumn))
				{
					xColumn.setCell(xfCell.build(map.get(eRow,eColumn)));
				}
				
				
				xRow.getColumn().add(xColumn);
			}
			
			xml.getRow().add(xRow);
		}
		
		return xml;
		
	}
	
	public static Matrix build(){return new Matrix();}
	
	private Row row(OPTION option)
	{
		Row row = new Row();
		row.setCode(option.getCode());
		if(localeCode!=null && option.getName()!=null && option.getName().containsKey(localeCode))
		{
			row.setLabel(option.getName().get(localeCode).getLang());
		}
		else {row.setLabel(option.getCode());}
		return row;
	}
	private Column column(OPTION option)
	{
		Column col = new Column();
		col.setCode(option.getCode());
		if(localeCode!=null && option.getName()!=null && option.getName().containsKey(localeCode))
		{
			col.setLabel(option.getName().get(localeCode).getLang());
		}
		else {col.setLabel(option.getCode());}
		return col;
	}
}