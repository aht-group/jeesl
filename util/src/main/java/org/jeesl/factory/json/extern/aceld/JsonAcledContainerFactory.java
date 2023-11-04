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
import org.jeesl.model.json.ssi.acled.JsonAcledCountry;
import org.jeesl.model.json.ssi.acled.JsonAcledData;
import org.jeesl.model.json.ssi.acled.JsonAcledIncident;
import org.jeesl.model.json.ssi.acled.JsonAcledResponse;
import org.jeesl.model.json.ssi.acled.JsonAcledSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonAcledContainerFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonAcledContainerFactory.class);
	
	private final AcledExternRest rest;
	private final String apiKey, apiEmail;
	
	public JsonAcledContainerFactory(AcledExternRest rest, String apiKey, String apiEmail)
	{
		this.rest=rest;
		this.apiKey=apiKey;
		this.apiEmail=apiEmail;
	}
	
	public static JsonAcledContainer build() {return new JsonAcledContainer();}
	
	public JsonAcledContainer restCountries() {return restCountriesByIso3(null);}
	public JsonAcledContainer restCountriesByIso3(String filter)
    {
    	JsonAcledContainer container = build();
    	container.setCountries(new ArrayList<JsonAcledCountry>());
    	JsonAcledResponse acled = null;
    	if(Objects.isNull(filter)) {acled = rest.countries(apiKey,apiEmail);}
    	else {acled = rest.countriesByIso3(apiKey,apiEmail,filter);}
    	
    	for(JsonAcledData data : acled.getData())
    	{
    		container.getCountries().add(JsonCountryFactory.build(data));	
    	}
    	return container;
    }
	
	public JsonAcledContainer restRegions()
    {
    	JsonAcledContainer container = build();
//    	container.setCountries(new ArrayList<JsonAcledCountry>());
    	JsonAcledResponse acled = rest.regions(apiKey,apiEmail);
    	logger.warn("NYI, until now not required");
    	JsonUtil.info(acled);
    	
//    	for(JsonAcledData data : acled.getData())
//    	{
//    		container.getCountries().add(JsonCountryFactory.build(data));
//    	}
    	return container;
    }
	
	public JsonAcledResponse restIncidents(String countryIso, LocalDate ldFrom, LocalDate ldTo)
	{
		String dates = ldFrom.toString()+"|"+ldTo.toString();
		
		JsonAcledResponse acled = rest.incidentsByIsoBetween(apiKey,apiEmail,countryIso,dates,"BETWEEN");
		
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