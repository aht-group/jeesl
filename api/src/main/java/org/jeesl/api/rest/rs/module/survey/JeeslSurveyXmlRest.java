package org.jeesl.api.rest.rs.module.survey;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.xml.module.survey.Survey;

public interface JeeslSurveyXmlRest
{
	@GET @Path("/xml/structure/{id:[1-9][0-9]*}") @Produces(MediaType.APPLICATION_XML)
	Survey surveyStructure(@PathParam("id") long id);
	
	@GET @Path("/xml/answers/{id:[1-9][0-9]*}") @Produces(MediaType.APPLICATION_XML)
	Survey surveyAnswers(@PathParam("id") long id);
	
	@GET @Path("/json/question/units") @Produces(MediaType.APPLICATION_XML)
	org.jeesl.model.xml.xsd.Container surveyQuestionUnits();
}