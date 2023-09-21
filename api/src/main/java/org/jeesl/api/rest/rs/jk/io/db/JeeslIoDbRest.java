package org.jeesl.api.rest.rs.jk.io.db;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.exlp.model.xml.io.Dir;
import org.jeesl.api.rest.i.io.JeeslIoDbRestInterface;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaSnapshot;
import org.jeesl.model.json.io.db.pg.statement.JsonPostgresStatementGroup;
import org.jeesl.model.json.io.ssi.update.JsonSsiUpdate;

import net.sf.ahtutils.xml.sync.DataUpdate;

@Path("/rest/jeesl/io/db")
public interface JeeslIoDbRest extends JeeslIoDbRestInterface
{	
	@POST @Path("/upload") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate uploadDumps(Dir directory);
	
	@POST @Path("/upload/meta/snapshot")
	@Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
	JsonSsiUpdate uploadMetaSnapshot(JsonPostgresMetaSnapshot snapshot);
	
	@POST @Path("/upload/statement/group")
	@Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
	JsonSsiUpdate  uploadStatementGroup(JsonPostgresStatementGroup group);
}