package org.jeesl.api.rest.system.traffic;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.sf.ahtutils.xml.sync.DataUpdate;
import net.sf.ahtutils.xml.utils.TrafficLights;

public interface JeeslTrafficLightRestImport
{
	@POST @Path("/utils/trafficLight") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importTrafficLights(TrafficLights lights);
}
