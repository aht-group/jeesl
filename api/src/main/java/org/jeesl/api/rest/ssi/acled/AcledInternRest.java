package org.jeesl.api.rest.ssi.acled;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.json.ssi.acled.JsonAcledContainer;
import org.jeesl.model.json.system.io.ssi.update.JsonSsiUpdate;

@Path("/rest/ssi/acled")
public interface AcledInternRest
{	
	@POST @Path("/country") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	JsonSsiUpdate countries(JsonAcledContainer container);
	
	@POST @Path("/admin1") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	JsonSsiUpdate admin1(JsonAcledContainer container);
	
	@POST @Path("/actors") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	JsonSsiUpdate actors(JsonAcledContainer container);
	
	@POST @Path("/sources") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	JsonSsiUpdate sources(JsonAcledContainer container);
	
	@POST @Path("/incidents") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	JsonSsiUpdate incidents(JsonAcledContainer container);
}