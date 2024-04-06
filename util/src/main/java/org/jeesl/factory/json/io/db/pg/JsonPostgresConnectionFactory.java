package org.jeesl.factory.json.io.db.pg;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

import org.jeesl.model.json.io.db.pg.JsonPostgresConnection;

public class JsonPostgresConnectionFactory 
{
	public static JsonPostgresConnection build(int number, Object[] array)
	{
		Timestamp tsTransaction = null;
		Timestamp tsQuery = null;
		Timestamp tsState = null;
		Boolean waiting = null;
		String state = null;
		String query = null;
		 
		if(array[0]!=null) {tsTransaction = (Timestamp)array[0];}
		if(array[1]!=null) {tsQuery = (Timestamp)array[1];}
		if(array[2]!=null) {tsState = (Timestamp)array[2];}
		if(array[3]!=null) {state = (String)array[3];}
		if(array[4]!=null) {query = (String)array[4];}
		
		JsonPostgresConnection json = build(number,tsTransaction,tsQuery,tsState,waiting,state,query);
		
		if(Objects.nonNull(array[5])) {json.setBackend((String)array[5]);}
		if(Objects.nonNull(array[6])) {json.setWaitEvent((String)array[6]);}
        
		return json;
	}
	
	private static JsonPostgresConnection build(int number, Timestamp tsTransaction, Timestamp tsQuery, Timestamp tsState, Boolean waiting, String state, String query)
	{
		JsonPostgresConnection json = new JsonPostgresConnection();
		json.setId(number);
		
		if(tsTransaction!=null){json.setTransaction(new Date(tsTransaction.getTime()));}
		if(tsQuery!=null){json.setQuery(new Date(tsQuery.getTime()));}
		if(tsState!=null){json.setChange(new Date(tsState.getTime()));}
//		
//		if(waiting!=null){json.setB1(waiting);}
//		
		if(state!=null){json.setState(state);}
		if(query!=null){json.setSql(query);}
		
		return json;
	}
}