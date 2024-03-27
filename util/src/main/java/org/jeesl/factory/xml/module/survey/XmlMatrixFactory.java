package org.jeesl.factory.xml.module.survey;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.factory.ejb.module.survey.EjbSurveyMatrixFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyOptionFactory;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyMatrix;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.model.pojo.map.generic.Nested2Map;
import org.jeesl.model.xml.module.survey.Column;
import org.jeesl.model.xml.module.survey.Matrix;
import org.jeesl.model.xml.module.survey.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlMatrixFactory<
				
				QUESTION extends JeeslSurveyQuestion<?,?,?,?,?,?,?,?,?,OPTION,?>,

				ANSWER extends JeeslSurveyAnswer<?,?,QUESTION,MATRIX,?,OPTION>,
				MATRIX extends JeeslSurveyMatrix<?,?,ANSWER,OPTION>,
				
				
				OPTION extends JeeslSurveyOption<?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlMatrixFactory.class);
	
	private JeeslSurveyCoreFacade<?,?,?,?,?,?,?,?,?,?,?,QUESTION,?,?,ANSWER,MATRIX,?,?,OPTION,?> fSurvey;
		
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
	
	public void lazyLoad(JeeslSurveyCoreFacade<?,?,?,?,?,?,?,?,?,?,?,QUESTION,?,?,ANSWER,MATRIX,?,?,OPTION,?> fSurvey)
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
					MATRIX cell = map.get(eRow,eColumn);
					logger.info("Nul? "+(cell!=null));
					xColumn.setCell(xfCell.build(cell));
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