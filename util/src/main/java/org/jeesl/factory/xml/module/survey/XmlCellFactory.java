package org.jeesl.factory.xml.module.survey;

import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyMatrix;
import org.jeesl.model.xml.module.survey.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlCellFactory<MATRIX extends JeeslSurveyMatrix<?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlCellFactory.class);
	
	
	private String localeCode;
			
	public XmlCellFactory(String localeCode)
	{
		this.localeCode=localeCode;
	}
	
	public Cell build(MATRIX cell)
	{
		Cell xml = new Cell();
		
		if(BooleanComparator.active(cell.getAnswer().getQuestion().getShowBoolean()) && cell.getValueBoolean()!=null){xml.setLabel(cell.getValueBoolean().toString());xml.setValueBoolean(cell.getValueBoolean());}
		if(BooleanComparator.active(cell.getAnswer().getQuestion().getShowInteger()) && cell.getValueNumber()!=null){xml.setLabel(cell.getValueNumber().toString());xml.setValueNumber(cell.getValueNumber());}
		if(BooleanComparator.active(cell.getAnswer().getQuestion().getShowDouble()) && cell.getValueDouble()!=null){xml.setLabel(cell.getValueDouble().toString());xml.setValueDouble(cell.getValueDouble());}
		if(BooleanComparator.active(cell.getAnswer().getQuestion().getShowText()) && cell.getValueText()!=null){xml.setLabel(cell.getValueText());xml.setValueText(cell.getValueText());}
	
		if(BooleanComparator.active(cell.getAnswer().getQuestion().getShowSelectOne()) && cell.getOption()!=null)
		{
			xml.setLabel(cell.getOption().getName().get(localeCode).getLang());
			xml.setValueOption(cell.getOption().getCode());
		}
		
		return xml;
	}
}