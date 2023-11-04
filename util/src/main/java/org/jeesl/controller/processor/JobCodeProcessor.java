package org.jeesl.controller.processor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.exlp.util.io.JsonUtil;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.factory.json.system.io.report.JsonFlatFigureFactory;
import org.jeesl.model.json.JsonFlatFigure;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class JobCodeProcessor
{
	private JsonFlatFigure json;
	
	public JobCodeProcessor()
	{

	}
		
	public void reset()
	{
		json = JsonFlatFigureFactory.build();
	}
	
	public void setLocaleCode(String localeCode){json.setS1(localeCode);}
	
	public void setDates(Date...dates) throws UtilsConfigurationException
	{
		if(dates.length>0){json.setDate1(dates[0]);}
		if(dates.length>1){json.setDate2(dates[1]);}
		
		if(dates.length>2){throw new UtilsConfigurationException("Date handling for more than 2 dates not implemented");}
	}
	
	public void setId1(Long id)
	{
		json.setIds1(Arrays.asList(id));
	}
	public void setIds1(List<Long> list)
	{
		json.setIds1(list);
	}
	
	public String jsonToString() throws UtilsConfigurationException
	{
		try {return JsonUtil.toString(json);}
		catch (JsonProcessingException e) {throw new UtilsConfigurationException(e.getMessage());}
	}
	
	public void init(String json) throws UtilsConfigurationException
	{
		try{this.json = JsonUtil.read(JsonFlatFigure.class,json);}
		catch (JsonParseException e) {throw new UtilsConfigurationException(e.getMessage());}
		catch (JsonMappingException e) {throw new UtilsConfigurationException(e.getMessage());}
		catch (IOException e) {throw new UtilsConfigurationException(e.getMessage());}
	}
	
	public Date getDate(int index) throws UtilsConfigurationException
	{
		if(index==0){return json.getDate1();}
		else if(index==1){return json.getDate2();}
		throw new UtilsConfigurationException("Date handling for more than 2 dates not implemented");
	}
	
	public List<Long> getIdList(int index)
	{
		return json.getIds1();
	}
}