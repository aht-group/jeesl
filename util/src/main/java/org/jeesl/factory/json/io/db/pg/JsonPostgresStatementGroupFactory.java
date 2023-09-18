package org.jeesl.factory.json.io.db.pg;

import java.util.Objects;

import org.jeesl.factory.json.io.ssi.core.JsonSsiSystemFactory;
import org.jeesl.interfaces.model.io.db.pg.statement.JeeslDbStatementGroup;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.model.json.io.db.pg.statement.JsonPostgresStatementGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonPostgresStatementGroupFactory<SYSTEM extends JeeslIoSsiSystem<?,?>,
											SG extends JeeslDbStatementGroup<SYSTEM>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonPostgresStatementGroupFactory.class);

	private final JsonPostgresStatementGroup q;
	
	private JsonSsiSystemFactory<SYSTEM> jfSystem;

	public JsonPostgresStatementGroupFactory(JsonPostgresStatementGroup q)
	{
		this.q=q;
		if(Objects.nonNull(q.getSystem())) {jfSystem = new JsonSsiSystemFactory<>(q.getSystem());}
	}

	public static JsonPostgresStatementGroup build() {return new JsonPostgresStatementGroup();}

	public JsonPostgresStatementGroup build(SG ejb)
	{
		JsonPostgresStatementGroup json = JsonPostgresStatementGroupFactory.build();
		
		if(Objects.nonNull(q.getCode())) {json.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getName())) {json.setName(ejb.getName());}
		if(Objects.nonNull(q.getRemark())) {json.setRemark(ejb.getRemark());}
		
		if(Objects.nonNull(q.getSystem())) {json.setSystem(jfSystem.build(ejb.getSystem()));}
		return json;
	}
}