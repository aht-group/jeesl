package org.jeesl.controller.handler.module.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.factory.ejb.util.EjbCodeFactory;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// This class helps to find s1o for Survey imports
public class SurveyOptionHandler<QUESTION extends JeeslSurveyQuestion<?,?,?,?,?,?,?,?,?,OPTION,?>,
								OPTION extends JeeslSurveyOption<?,?>>
	implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(SurveyOptionHandler.class);
	private static final long serialVersionUID = 1L;
	
	private final Map<QUESTION,Map<String,OPTION>> mapOption;
	
	public SurveyOptionHandler()
	{
		mapOption = new HashMap<>();
	}
	
	public void clear() {mapOption.clear();}
	
	public void addOptionsUniqueCode(QUESTION question, List<OPTION> options)
	{
		mapOption.put(question,EjbCodeFactory.toMapNonUniqueCode(options));
	}
	
	public void addOptionsRowColCellCode(QUESTION question, List<OPTION> options)
	{
		
	}
	
	public void toMapRowColCode(QUESTION question, List<OPTION> options)
    {
    	Map<String,OPTION> map = new Hashtable<>();
    	for(OPTION o : options)
    	{
    		map.put(this.toPrefixCode(o),o);
    	}
    	mapOption.put(question,map);
    }
	
	public OPTION toOption(QUESTION question, String code) throws JeeslConstraintViolationException
	{
		if(code==null)
		{
			if(BooleanComparator.active(question.getMandatory())){throw new JeeslConstraintViolationException("Answer is mandatory");}
			else {return null;}
			
		}
		
		if(mapOption.containsKey(question))
		{
			if(mapOption.get(question).containsKey(code)) {return mapOption.get(question).get(code);}
			else {throw new JeeslConstraintViolationException("No Option for question:{"+question.toString()+"} with code:"+code);}
		}
		else {throw new JeeslConstraintViolationException("Nothing defined for "+question.toString());}
	}
	
	public List<OPTION> toRows(QUESTION question)
	{
		List<OPTION> list = new ArrayList<>();
		if(mapOption.containsKey(question))
		{
			for(OPTION o : mapOption.get(question).values())
			{
				if(o.getRow()) {list.add(o);}
			}
		}
		else {logger.warn("No Options for "+question.toString());}
		return list;
	}
	public List<OPTION> toCols(QUESTION question)
	{
		List<OPTION> list = new ArrayList<>();
		if(mapOption.containsKey(question))
		{
			for(OPTION o : mapOption.get(question).values())
			{
				if(o.getCol()) {list.add(o);}
			}
		}
		else {logger.warn("No Options for "+question.toString());}
		return list;
	}
	public List<OPTION> toCells(QUESTION question)
	{
		List<OPTION> list = new ArrayList<>();
		if(mapOption.containsKey(question))
		{
			for(OPTION o : mapOption.get(question).values())
			{
				if(o.getCell()) {list.add(o);}
			}
		}
		else {logger.warn("No Options for "+question.toString());}
		return list;
	}
	public Map<String,OPTION> toMapCode(List<OPTION> list)
	{
		Map<String,OPTION> map = new HashMap<>();
		for(OPTION o : list) {map.put(o.getCode(),o);}
		return map;
	}
	
	
	public OPTION toCol(List<OPTION> options, String code)
	{
		for(OPTION o : options) {if(o.getCode().equals(code)) {return o;}}
		logger.warn("No Options for "+code.toString());
		return null;
	}
	
	
	
	public String toPrefixCode(OPTION o)
    {
		StringBuilder sb = new StringBuilder();
		if(o.getRow()) {sb.append("r");}
		if(o.getCol()) {sb.append("c");}
		if(o.getCell()) {sb.append("x");}
		sb.append(":").append(o.getCode());
		return sb.toString();
    }
}