package org.jeesl.api.rest.extern;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.jeesl.model.json.ssi.pvgis.PvgisResponse;

@Path("/api/v5_3")
public interface PvgisRest
{
    @GET
    @Path("/seriescalc")
    PvgisResponse seriesCalc(
        @QueryParam("lat")          double latitude,
        @QueryParam("lon")          double longitude,
        @QueryParam("pvcalculation") int pvCalculation,
        @QueryParam("peakpower")    double peakPower,
        @QueryParam("loss")         double loss,
        @QueryParam("angle")        double angle,
        @QueryParam("aspect")       double azimuth,
        @QueryParam("outputformat") String outputFormat,
        @QueryParam("raddatabase")  String radDatabase,
        @QueryParam("startyear")    Integer startYear,
        @QueryParam("endyear")      Integer endYear
    );
}