package org.jeesl.api.rest.rs.io.report.traffic;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.xml.io.ssi.sync.DataUpdate;
import org.jeesl.model.xml.system.util.TrafficLights;

public interface JeeslTrafficLightRestImport
{
	@POST @Path("/utils/trafficLight") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importTrafficLights(TrafficLights lights);
}
