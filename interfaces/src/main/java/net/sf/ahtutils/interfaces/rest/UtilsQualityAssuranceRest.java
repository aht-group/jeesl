package net.sf.ahtutils.interfaces.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.sf.ahtutils.xml.aht.Aht;
import net.sf.ahtutils.xml.qa.Qa;
import net.sf.ahtutils.xml.qa.Test;

@Path("/rest/qa")
public interface UtilsQualityAssuranceRest
{
	public static final String cfgRestUrl = "doc.qa.rest";
	
	@GET @Path("/roles")
	@Produces(MediaType.APPLICATION_XML)
	org.jeesl.model.xml.system.security.Category qaRoles();
	
	@GET @Path("/status/result")
	@Produces(MediaType.APPLICATION_XML)
	Aht qaStatusResult();
	
	@GET @Path("/status/test")
	@Produces(MediaType.APPLICATION_XML)
	Aht qaStatusTest();
	
	@GET @Path("/status/condition")
	@Produces(MediaType.APPLICATION_XML)
	Aht qaStatusCondition();
	
	@GET @Path("/team/{id:[0-9]*}")
	@Produces(MediaType.APPLICATION_XML)
	Qa qaTeam(@PathParam("id") long qaId);
	
	@GET @Path("/groups/{id:[0-9]*}")
	@Produces(MediaType.APPLICATION_XML)
	Qa qaGroups(@PathParam("id") long qaId);
	
	@GET @Path("/schedule/{id:[0-9]*}")
	@Produces(MediaType.APPLICATION_XML)
	Qa qaSchedule(@PathParam("id") long qaId);
	
	@GET @Path("/categories/{id:[0-9]*}")
	@Produces(MediaType.APPLICATION_XML)
	Qa qaCategories(@PathParam("id") long qaId);
	
	@GET @Path("/fr/durations/{id:[0-9]*}")
	@Produces(MediaType.APPLICATION_XML)
	Qa qaFrDurations(@PathParam("id") long qaId);
	
	@GET @Path("/survey/{id:[0-9]*}")
	@Produces(MediaType.APPLICATION_XML)
	Qa qaSurvey(@PathParam("id") long qaId);
	
	@GET @Path("/category/{id:[0-9]*}/tests")
	@Produces(MediaType.APPLICATION_XML)
	net.sf.ahtutils.xml.qa.Category qaCategory(@PathParam("id") long categoryId);
	
	@GET @Path("/test/{id:[0-9]*}")
	@Produces(MediaType.APPLICATION_XML)
	Test qaTest(@PathParam("id") long testId);
	
	@GET @Path("/checklist/{id:[0-9]*}")
	@Produces(MediaType.APPLICATION_XML)
	Qa qaChecklist(@PathParam("id") long testId);
}