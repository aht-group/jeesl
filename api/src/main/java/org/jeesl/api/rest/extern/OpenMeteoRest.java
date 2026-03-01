package org.jeesl.api.rest.extern;

import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.jeesl.model.json.ssi.openmeteo.OpenMeteoObservation;

@Path("/v1")
public interface OpenMeteoRest {

    @GET
    @Path("/forecast")
    OpenMeteoObservation getForecast(
        @QueryParam("latitude")   double latitude,
        @QueryParam("longitude")  double longitude,
        @QueryParam("hourly")    @Encoded String hourly,
        @QueryParam("start_date") String startDate,
        @QueryParam("end_date")   String endDate,
        @QueryParam("timezone")  @Encoded String timezone
    );
}