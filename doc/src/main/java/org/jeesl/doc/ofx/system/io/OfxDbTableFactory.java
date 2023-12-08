package org.jeesl.doc.ofx.system.io;

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
		XmlTableFactory xt = XmlTableFactory.instance("Connections");
		xt.header("#").header("xact_start").header("query");
		
		for(JsonPostgresConnection c : json.getConnections())
		{
			xt.cell(c.getId());
			xt.cell(c.getTransaction());
			xt.cell(c.getSql());
			xt.row();
		}
		
		return xt.getTable();
	}
}