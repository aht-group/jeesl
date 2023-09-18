package org.jeesl.util.query.json;

import java.time.LocalDateTime;

import org.jeesl.factory.json.io.ssi.core.JsonSsiSystemFactory;
import org.jeesl.model.json.io.db.pg.statement.JsonPostgresStatement;
import org.jeesl.model.json.io.db.pg.statement.JsonPostgresStatementGroup;

public class JsonIoDbQueryProvider
{
	public static JsonPostgresStatementGroup statementGroup()
	{				
		JsonPostgresStatementGroup json = new JsonPostgresStatementGroup();
		json.setCode("");
		json.setName("");
		json.setRemark("");
		
		json.setSystem(JsonSsiSystemFactory.build(""));
		
		return json;
	}
	
	public static JsonPostgresStatement statement()
	{				
		JsonPostgresStatement json = new JsonPostgresStatement();
		json.setCode("");
		json.setRemark("");
		json.setRecord(LocalDateTime.now());
		
		json.setRows(0l);
		json.setCalls(0l);
		json.setAverage(0d);
		json.setTotal(0d);

		json.setSql("");
		json.setRemark("");
		
		return json;
	}
}