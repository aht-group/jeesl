package org.jeesl.api.rest.ssi.acled;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.json.ssi.acled.JsonAcledResponse;

public interface AcledExternRest
{	
	@GET @Path("/country/read")
	@Produces(MediaType.APPLICATION_JSON)
    JsonAcledResponse countries(@QueryParam("key") String key, @QueryParam("email") String email);
	
    @GET @Path("/country/read")
	@Produces(MediaType.APPLICATION_JSON)
    JsonAcledResponse countriesByIso3(@QueryParam("key") String key, @QueryParam("email") String email, @QueryParam("iso3") String iso3Filter);
	
	
    @GET @Path("/region/read")
	@Produces(MediaType.APPLICATION_JSON)
    JsonAcledResponse regions(@QueryParam("key") String key, @QueryParam("email") String email);

    
    @GET @Path("/acled/read")
  	@Produces(MediaType.APPLICATION_JSON)
	JsonAcledResponse incidentsByIso(@QueryParam("key") String key, @QueryParam("email") String email, @QueryParam("iso") String filterIso);
        
    @GET @Path("/acled/read")
  	@Produces(MediaType.APPLICATION_JSON)
	JsonAcledResponse incidentsByIsoBetween(@QueryParam("key") String key, @QueryParam("email") String email, @QueryParam("iso") String filterIso, @QueryParam("event_date") String dates, @QueryParam("event_date_where") String where);
   
}