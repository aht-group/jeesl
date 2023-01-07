package org.jeesl.doc.ofx.system.io;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.model.json.io.db.pg.JsonPostgres;
import org.jeesl.model.json.io.db.pg.JsonPostgresConnection;
import org.openfuxml.content.table.Table;
import org.openfuxml.factory.xml.table.XmlTableFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OfxDbTableFactory
{	
	final static Logger logger = LoggerFactory.getLogger(OfxDbTableFactory.class);
		
	public OfxDbTableFactory()
	{
		
	}
	
	public static Table connections(JsonPostgres json)
	{
		List<String> fileds = new ArrayList<String>();
		fileds.add("#");
		fileds.add("xact_start");
		fileds.add("query");
		
		List<Object[]> data = new ArrayList<Object[]>();
		for(JsonPostgresConnection c : json.getConnections())
		{
			Object[] o = new Object[3];
			o[0] = c.getId();
			o[1] = c.getTransaction();
			o[2] = c.getSql();
			data.add(o);
		}
		
		return XmlTableFactory.build(fileds,data);
	}
}