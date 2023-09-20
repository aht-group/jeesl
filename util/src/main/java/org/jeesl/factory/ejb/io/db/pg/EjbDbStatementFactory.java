package org.jeesl.factory.ejb.io.db.pg;

import java.time.LocalDateTime;
import java.util.UUID;

import org.jeesl.interfaces.model.io.db.pg.statement.JeeslDbStatement;
import org.jeesl.interfaces.model.io.db.pg.statement.JeeslDbStatementGroup;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.model.json.io.db.pg.statement.JsonPostgresStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbDbStatementFactory <HOST extends JeeslIoSsiHost<?,?,?>,
									ST extends JeeslDbStatement<HOST,SG>,
									SG extends JeeslDbStatementGroup<?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbDbStatementFactory.class);
	
	private final Class<ST> cStatement;
    
	public EjbDbStatementFactory(final Class<ST> cStatement)
	{       
        this.cStatement = cStatement;
	}
	
	public ST build(HOST host, SG group, LocalDateTime record, JsonPostgresStatement json)
	{
		ST ejb = null;
		try
		{
			 ejb = cStatement.newInstance();
			 
			 ejb.setCode(UUID.randomUUID().toString());
			 ejb.setHost(host);
			 ejb.setGroup(group);
			 ejb.setRecord(record);
			 
			 ejb.setRows(Long.valueOf(json.getRows()).intValue());
			 ejb.setCalls(Long.valueOf(json.getCalls()).intValue());
			 ejb.setAverage(json.getAverage());
			 ejb.setTotal(json.getTotal());
			 ejb.setSql(json.getSql());
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}