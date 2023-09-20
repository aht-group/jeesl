package org.jeesl.factory.json.io.db.pg;

import java.util.Objects;

import org.jeesl.factory.json.io.ssi.core.JsonSsiHostFactory;
import org.jeesl.interfaces.model.io.db.pg.statement.JeeslDbStatement;
import org.jeesl.interfaces.model.io.db.pg.statement.JeeslDbStatementGroup;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.model.json.io.db.pg.statement.JsonPostgresStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonPostgresStatementFactory<HOST extends JeeslIoSsiHost<?,?,?>,
											ST extends JeeslDbStatement<HOST,SG>,
											SG extends JeeslDbStatementGroup<?>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonPostgresStatementFactory.class);

	private final JsonPostgresStatement q;
	
	private JsonSsiHostFactory<HOST> jfHost;

	public JsonPostgresStatementFactory(JsonPostgresStatement q)
	{
		this.q=q;
		if(Objects.nonNull(q.getHost())) {jfHost = new JsonSsiHostFactory<>(q.getHost());}
	}

	public static JsonPostgresStatement build() {return new JsonPostgresStatement();}

	public JsonPostgresStatement build(ST ejb)
	{
		JsonPostgresStatement json = JsonPostgresStatementFactory.build();
		
		if(Objects.nonNull(q.getCode())) {json.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getRemark())) {json.setRemark(ejb.getRemark());}
		if(Objects.nonNull(q.getRecord())) {json.setRecord(ejb.getRecord());}
		
		if(Objects.nonNull(q.getHost())) {json.setHost(jfHost.build(ejb.getHost()));}
		
		if(Objects.nonNull(q.getRows()) ) {json.setRows(Integer.valueOf(ejb.getRows()).longValue());}
		if(Objects.nonNull(q.getCalls()) ) {json.setCalls(Integer.valueOf(ejb.getCalls()).longValue());}
		
		if(Objects.nonNull(q.getAverage()) ) {json.setAverage(ejb.getAverage());}
		if(Objects.nonNull(q.getTotal()) ) {json.setTotal(ejb.getTotal());}
		
		if(Objects.nonNull(q.getSql()) ) {json.setSql(ejb.getSql());}
		
		
		
		return json;
	}
}