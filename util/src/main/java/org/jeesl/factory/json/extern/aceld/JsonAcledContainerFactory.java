package org.jeesl.factory.json.extern.aceld;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.exlp.util.io.JsonUtil;
import org.jeesl.api.rest.extern.AcledExternRest;
import org.jeesl.model.json.ssi.acled.JsonAcledActor;
import org.jeesl.model.json.ssi.acled.JsonAcledAdmin1;
import org.jeesl.model.json.ssi.acled.JsonAcledContainer;
import org.jeesl.model.json.ssi.acled.JsonAcledCountriesResponse;
import org.jeesl.model.json.ssi.acled.JsonAcledCountry;
import org.jeesl.model.json.ssi.acled.JsonAcledIncident;
import org.jeesl.model.json.ssi.acled.JsonAcledResponse;
import org.jeesl.model.json.ssi.acled.JsonAcledSource;
import org.jeesl.model.json.ssi.acled.JsonAcledTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonAcledContainerFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonAcledContainerFactory.class);

	private final AcledExternRest rest;
	private final String oAuthToken;

	private JsonAcledCountry jsonAcledCountry;
	public JsonAcledCountry getJsonAcledCountry() {return jsonAcledCountry;}
	public void setJsonAcledCountry(JsonAcledCountry jsonAcledCountry) {this.jsonAcledCountry = jsonAcledCountry;}

	public JsonAcledContainerFactory(AcledExternRest rest, JsonAcledTokenResponse tokenResponse)
	{
		this.rest=rest;

		StringBuffer sb =  new StringBuffer(tokenResponse.getToken_type());
		sb.append(" ");
		sb.append(tokenResponse.getAccess_token());
		this.oAuthToken = sb.toString();
	}

	public static JsonAcledContainer build() {return new JsonAcledContainer();}

	public JsonAcledContainer restCountries() {return restCountriesByIso3(null);}
	public JsonAcledContainer restCountriesByIso3(String filter)
    {
    	JsonAcledContainer container = build();
    	container.setCountries(new ArrayList<JsonAcledCountry>());
    	JsonAcledCountriesResponse acledCountriesResponse = null;
    	if(Objects.isNull(filter)) {acledCountriesResponse = rest.countries(this.oAuthToken);}
    	else {acledCountriesResponse = rest.countriesByIso3(this.oAuthToken,filter);}

    	for(JsonAcledCountry data : acledCountriesResponse.getData())
    	{
    		container.getCountries().add(JsonCountryFactory.build(data));
    	}
    	return container;
    }

	public JsonAcledContainer restRegions()
    {
    	JsonAcledContainer container = build();
//    	container.setCountries(new ArrayList<JsonAcledCountry>());
    	JsonAcledResponse acled = rest.regions(this.oAuthToken);
    	logger.warn("NYI, until now not required");
    	JsonUtil.info(acled);

//    	for(JsonAcledData data : acled.getData())
//    	{
//    		container.getCountries().add(JsonCountryFactory.build(data));
//    	}
    	return container;
    }

	public JsonAcledResponse restIncidents(String country, LocalDate ldFrom, LocalDate ldTo)
	{
		String dates = ldFrom.toString()+"|"+ldTo.toString();

		JsonAcledResponse acled = rest.incidentsByCountryBetween(this.oAuthToken,country,dates,"BETWEEN");
		//JsonAcledResponse acled = rest.incidentsByCountry(this.oAuthToken,country);
		return acled;
	}

	public JsonAcledResponse restIncidentsByYear(String country, Integer year)
	{
		//String dates = ldFrom.toString()+"|"+ldTo.toString();

		JsonAcledResponse acled = rest.incidentsByCountryYear(this.oAuthToken,country,year);
		//JsonAcledResponse acled = rest.incidentsByCountry(this.oAuthToken,country);
		return acled;
	}

	public static JsonAcledContainer buildAdmin1(List<JsonAcledAdmin1> admin1) {JsonAcledContainer json = build();json.setAdmin1(admin1);return json;}
	public static JsonAcledContainer buildSources(List<JsonAcledSource> sources) {JsonAcledContainer json = build();json.setSources(sources);return json;}
	public static JsonAcledContainer buildActors(List<JsonAcledActor> actors) {JsonAcledContainer json = build();json.setActors(actors);return json;}
	public static JsonAcledContainer buildIncidents(List<JsonAcledIncident> incidents) {JsonAcledContainer json = build();json.setIncidents(incidents);return json;}

//    public JsonAcledContainer incidents(JsonAcledCountry country) {return incidents(null,country,null);}
//    public JsonAcledContainer incidents(JsonAcledCountry country, String admin1) {return incidents(null,country,admin1);}
//    public JsonAcledContainer incidents(Integer limit, JsonAcledCountry country, String admin1)
//    {
//    	Map<String,JsonAcledActor> mapActors = new HashMap<>();
//    	Map<String,JsonAcledSource> mapSources = new HashMap<>();
//    	Map<String,JsonAcledAdmin1> mapAdmin1 = new HashMap<>();
//
//    	JsonAcledContainer container = build();
//    	container.setIncidents(new ArrayList<JsonAcledIncident>());
//
//    	boolean hasResults = true;
//    	int page=1;
//
//    	while(hasResults)
//    	{
//    		logger.info("Country:"+country.getName()+" Page "+page);
//
//    		JsonAcledResponse response;
////    		if(admin1==null) {response = rest.incidents("accept",page,country.getId());}
////    		else {response = rest.incidents("accept",page,country.getId(),admin1);}
//        	for(JsonAcledData data : response.getData())
//        	{
//        		JsonAcledIncident incident = JsonIncidentFactory.build(data);
//        		incident.setCountry(country);
//        		incident.setAdmin1(JsonAdmin1Factory.build(country,data));
//
//        		if(incident.getActor1()!=null && !mapActors.containsKey(incident.getActor1().getName())) {mapActors.put(incident.getActor1().getName(), JsonActorFactory.build(incident.getActor1().getName()));}
//        		if(incident.getActor2()!=null && !mapActors.containsKey(incident.getActor2().getName())) {mapActors.put(incident.getActor2().getName(), JsonActorFactory.build(incident.getActor2().getName()));}
//
//        		if(!mapSources.containsKey(incident.getSource().getName())) {mapSources.put(incident.getSource().getName(), JsonSourceFactory.build(incident.getSource().getName()));}
//        		mapAdmin1.put(JsonAdmin1Factory.toSsiCode(incident.getAdmin1()),incident.getAdmin1());
//
//        		container.getIncidents().add(incident);
//        		if(limit!=null && container.getIncidents().size()==limit) {hasResults=false; break;}
//        	}
//        	hasResults = !response.getData().isEmpty();
//        	if(limit!=null && container.getIncidents().size()==limit) {hasResults=false;}
//        	page++;
//    	}
//
//    	container.setActors(new ArrayList<>(mapActors.values()));
//    	container.setSources(new ArrayList<>(mapSources.values()));
//    	container.setAdmin1(new ArrayList<>(mapAdmin1.values()));
//    	return container;
//    }
}