package org.jeesl.factory.json.system.io.db;

import java.math.BigInteger;

import org.jeesl.controller.processor.finance.AmountRounder;
import org.jeesl.model.json.io.db.pg.JsonPostgresStatement;

public class JsonPostgresStatementFactory
{
	public static JsonPostgresStatement build(int number, Object[] array)
	{
		String datName = null;
		String query = null; 
		
		Long rowCount = null;
		Long calls = null;
		Double totalTime = null;
		Double avgTime = null;
		
		
		if(array[0]!=null){datName = (String)array[0];}
		if(array[1]!=null){query = (String)array[1];}
		
		if(array[2]!=null){rowCount = ((BigInteger)array[2]).longValue();}
		if(array[3]!=null){calls = ((BigInteger)array[3]).longValue();}
		
		if(array[4]!=null){totalTime = (Double)array[4];}
		if(array[5]!=null){avgTime = (Double)array[5];}
		
		return build(number,datName,query,rowCount,calls,totalTime,avgTime);
	}
	
	public static JsonPostgresStatement build(int number, String datName, String query, Long rowCount, Long calls, Double totalTime, Double avgTime)
	{
		JsonPostgresStatement json = new JsonPostgresStatement();
		json.setId(number);
		
//		if(datName!=null){json.setG1(datName);}
		if(query!=null){json.setSql(query);}
		
		if(rowCount!=null) {json.setRows(rowCount);}
		if(calls!=null) {json.setCalls(calls);}
		
		if(totalTime!=null) {json.setTotal(AmountRounder.one(totalTime));}
		if(avgTime!=null) {json.setAverage(AmountRounder.one(avgTime));}
		
		return json;
	}
}