package org.jeesl.api.rest.rs.module.survey;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.xml.module.survey.Correlation;
import org.jeesl.model.xml.module.survey.Survey;
import org.jeesl.model.xml.module.survey.Surveys;
import org.jeesl.model.xml.module.survey.Templates;

import net.sf.ahtutils.xml.aht.Aht;

public interface JeeslSurveyRestExport
{
	@GET @Path("/survey/template/category") @Produces(MediaType.APPLICATION_XML)
	Aht exportSurveyTemplateCategory();
	
	@GET @Path("/survey/template/status") @Produces(MediaType.APPLICATION_XML)
	Aht exportSurveyTemplateStatus();
	
	@GET @Path("/survey/question/unit") @Produces(MediaType.APPLICATION_XML)
	org.jeesl.model.xml.jeesl.Container surveyQuestionUnits();
	
	@GET @Path("/survey/status") @Produces(MediaType.APPLICATION_XML)
	Aht exportSurveyStatus();
	
//	@GET @Path("/survey/analysis/type") @Produces(MediaType.APPLICATION_XML)
//	Container exportSurveyAnalysisType();
	
	@GET @Path("/survey/templates") @Produces(MediaType.APPLICATION_XML)
	Templates exportSurveyTemplates();
	
	@GET @Path("/survey/correlations") @Produces(MediaType.APPLICATION_XML)
	Correlation exportSurveyCorrelations();
	
	@GET @Path("/surveys") @Produces(MediaType.APPLICATION_XML)
	Surveys exportSurveys();
	

	
	@GET @Path("/survey/{id:[1-9][0-9]*}") @Produces(MediaType.APPLICATION_XML)
	Survey exportSurvey(@PathParam("id") long id);
}