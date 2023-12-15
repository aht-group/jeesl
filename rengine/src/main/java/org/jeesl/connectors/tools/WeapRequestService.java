package org.jeesl.connectors.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.connectors.weap.WeapConnector;
import org.jeesl.connectors.weap.WeapResultValueRequest;
import org.jeesl.model.xml.io.locale.status.Status;
import org.jeesl.model.xml.xsd.Container;

@Path("/rest")
public class WeapRequestService
{
	@POST @Path("/request") @Consumes(MediaType.APPLICATION_XML) @Produces(MediaType.APPLICATION_XML)
	public WeapResultValueRequest getRequest(WeapResultValueRequest request)
	{
		System.out.println("Requesting Results for branches " +request.getBranches().get(0));
		Double result = 120.0;
		
		try {
			result = WeapConnector.getResultValue(request);
		} catch (IOException ex) {
			request.setErrorMessage(ex.getMessage());
			System.err.println(ex.getMessage());
		}
		
		request.setResult(result);
		return request;
	}
	
	@POST @Path("/scenario") @Consumes(MediaType.TEXT_PLAIN) @Produces(MediaType.TEXT_PLAIN)
	public String setScenario(String scenario)
	{
		System.out.println("Setting Scenario to " +scenario);
		try {
			return WeapConnector.setScenario(scenario);
		} catch (IOException ex) {
			return (ex.getMessage());
		}
	}
	
	@POST @Path("/area") @Consumes(MediaType.TEXT_PLAIN) @Produces(MediaType.TEXT_PLAIN)
	public String setArea(String area)
	{
		System.out.println("Setting Area to " +area);
		try {
			return WeapConnector.setArea(area);
		} catch (IOException ex) {
			return (ex.getMessage());
		}
	}
	
	@POST @Path("/calculate") @Consumes(MediaType.APPLICATION_XML)
	public void calculate()
	{
		System.out.println("Triggering calculation");
		
		try {
			WeapConnector.triggerCalculation();
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	@GET @Path("/baseYear") @Produces(MediaType.TEXT_PLAIN)
	public int getBaseYear()
	{
		System.out.println("Requesting Base Year ");
		Integer baseYear = 1900;
		try {
			baseYear = WeapConnector.getBaseYear();
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
		return baseYear;
	}
	
	@GET @Path("/endYear") @Produces(MediaType.TEXT_PLAIN)
	public int getEndYear()
	{
		System.out.println("Requesting End Year ");
		Integer endYear = 1900;
		try {
			endYear = WeapConnector.getEndYear();
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
		return endYear;
	}
	
	@GET @Path("/areas")@Produces("application/xml")
	public Container  getAreas()
	{
		System.out.println("Requesting Areas");
		List<String> areas = new ArrayList<String>();
		Container container = new Container();
		try {
                areas.addAll(WeapConnector.getAreas());
                for (String area : areas)
                {
                    Status status = new Status();
                    status.setLabel(area);
                    container.getStatus().add(status);
                }
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
		return container;
	}
	
	@GET @Path("/scenarios")
        @Produces("application/xml")
	public Container getScenarios()
	{
		System.out.println("Requesting Scenarios");
		List<String> scenarios = new ArrayList<String>();
                Container container = new Container();
		try {
                scenarios.addAll(WeapConnector.getScenarios());
                for (String scenario : scenarios)
                {
                    Status status = new Status();
                    status.setLabel(scenario);
                    container.getStatus().add(status);
                }
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
                return container;
	}
	
	@GET @Path("/version")
	public String getVersion()
	{
		System.out.println("Requesting WEAP version");
		String version = "NOT CONNECTED";
		try {
			version = WeapConnector.getWeapVersion();
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
		return version;
	}
}