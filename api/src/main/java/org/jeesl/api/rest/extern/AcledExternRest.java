package org.jeesl.api.rest.extern;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.json.ssi.acled.JsonAcledCountriesResponse;
import org.jeesl.model.json.ssi.acled.JsonAcledResponse;
import org.jeesl.model.json.ssi.acled.JsonAcledTokenResponse;

public interface AcledExternRest
{
	@POST @Path("/oauth/token")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	JsonAcledTokenResponse authToken(@FormParam("username") String username, @FormParam("password") String password, @FormParam("grant_type") String grantType, @FormParam("client_id") String clientId);

	@GET @Path("/api/country/read")
	@Produces(MediaType.APPLICATION_JSON)
	JsonAcledCountriesResponse countries(@HeaderParam("Authorization") String authorization);

    @GET @Path("/api/country/read")
	@Produces(MediaType.APPLICATION_JSON)
    JsonAcledCountriesResponse countriesByIso3(@HeaderParam("Authorization") String authorization, @QueryParam("iso3") String iso3Filter);


    @GET @Path("/api/region/read")
	@Produces(MediaType.APPLICATION_JSON)
    JsonAcledResponse regions(@HeaderParam("Authorization") String authorization);

    @GET @Path("/api/acled/read")
  	@Produces(MediaType.APPLICATION_JSON)
	JsonAcledResponse incidentsByCountry(@HeaderParam("Authorization") String authorization, @QueryParam("country") String filterCountry);

    @GET @Path("/api/acled/read")
  	@Produces(MediaType.APPLICATION_JSON)
	JsonAcledResponse incidentsByCountryBetween(@HeaderParam("Authorization") String authorization, @QueryParam("country") String filterCountry, @QueryParam("event_date") String dates, @QueryParam("event_date_where") String where);

    @GET @Path("/api/acled/read")
  	@Produces(MediaType.APPLICATION_JSON)
	JsonAcledResponse incidentsByCountryYear(@HeaderParam("Authorization") String authorization, @QueryParam("country") String filterCountry, @QueryParam("year") Integer year);



}