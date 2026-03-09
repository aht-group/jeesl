package org.jeesl.api.rest.extern;

import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.json.ssi.openmeteo.OpenMeteoMeta;
import org.jeesl.model.json.ssi.openmeteo.OpenMeteoObservation;

//@Path("")
public interface OpenMeteoRest
{
	@GET
    @Path("/data/dwd_icon_d2/static/meta.json")
	@Produces(MediaType.TEXT_PLAIN)
    String metaTxt();
	
	@GET
    @Path("/data/dwd_icon_d2/static/meta.json")
	@Produces(MediaType.APPLICATION_JSON)
	OpenMeteoMeta meta();
	
    @GET
    @Path("/v1/forecast")
    OpenMeteoObservation forecast(
        @QueryParam("latitude")   double latitude,
        @QueryParam("longitude")  double longitude,
        @QueryParam("hourly")     @Encoded String hourly,
        @QueryParam("start_date") String startDate,
        @QueryParam("end_date")   String endDate,
        @QueryParam("timezone")   @Encoded String timezone
    );
    
    @GET
    @Path("/v1/archive")
    OpenMeteoObservation archive(
        @QueryParam("latitude")   double latitude,
        @QueryParam("longitude")  double longitude,
        @QueryParam("hourly")     @Encoded String hourly,
        @QueryParam("start_date") String startDate,
        @QueryParam("end_date")   String endDate,
        @QueryParam("timezone")   @Encoded String timezone
    );
}