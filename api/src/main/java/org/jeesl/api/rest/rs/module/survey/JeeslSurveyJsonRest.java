package org.jeesl.api.rest.rs.module.survey;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.json.system.status.JsonContainer;

public interface JeeslSurveyJsonRest
{	
	@GET @Path("/json/question/units") @Produces(MediaType.APPLICATION_JSON)
	JsonContainer surveyQuestionUnitsJson();
	
	@GET @Path("/json/structure/{locale}/{id:[1-9][0-9]*}") @Produces(MediaType.APPLICATION_JSON)
	org.jeesl.model.json.survey.JsonSurvey surveyStructureJson(@PathParam("locale") String localeCode, @PathParam("id") long id);
}