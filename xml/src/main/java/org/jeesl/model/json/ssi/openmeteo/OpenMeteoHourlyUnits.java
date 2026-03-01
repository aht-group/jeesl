package org.jeesl.model.json.ssi.openmeteo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMeteoHourlyUnits
{
	@JsonProperty("time")
    private String time;
    public String getTime() {return time;}
    public void setTime(String time) {this.time = time;}
    
    @JsonProperty("temperature_2m")
    private String temperature2m;
    public String getTemperature2m() {return temperature2m;}
	public void setTemperature2m(String temperature2m) {this.temperature2m = temperature2m;}

		@JsonProperty("shortwave_radiation")
        private String shortwaveRadiation;
        public String getShortwaveRadiation()  { return shortwaveRadiation; }
        public void setShortwaveRadiation(String shortwaveRadiation)     { this.shortwaveRadiation = shortwaveRadiation; }

        @JsonProperty("terrestrial_radiation")
        private String terrestrialRadiation;
        public String getTerrestrialRadiation(){ return terrestrialRadiation; }
        public void setTerrestrialRadiation(String terrestrialRadiation) { this.terrestrialRadiation = terrestrialRadiation; }

        @JsonProperty("cloudcover")
        private String cloudcover;
        public String getCloudcover()          { return cloudcover; }
        public void setCloudcover(String cloudcover)                     { this.cloudcover = cloudcover; }

        @JsonProperty("direct_radiation")
        private String directRadiation;
        public String getDirectRadiation()     { return directRadiation; }
        public void setDirectRadiation(String directRadiation)           { this.directRadiation = directRadiation; }

        @JsonProperty("diffuse_radiation")
        private String diffuseRadiation;
        public String getDiffuseRadiation()    { return diffuseRadiation; }
        public void setDiffuseRadiation(String diffuseRadiation)         { this.diffuseRadiation = diffuseRadiation; }     
}
