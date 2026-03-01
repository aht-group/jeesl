package org.jeesl.model.json.ssi.openmeteo;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMeteoHourlyData
{
//	@JsonProperty("time")
//    private List<String> time;
//	public List<String> getTime() {return time;}
//	public void setTime(List<String> time) {this.time = time;}
	
	@JsonProperty("time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
	@JsonDeserialize(contentUsing = LocalDateTimeDeserializer.class)
	@JsonSerialize(contentUsing = LocalDateTimeSerializer.class)
	private List<LocalDateTime> time;
    public List<LocalDateTime> getTime() {return time;}
	public void setTime(List<LocalDateTime> time) {this.time = time;}

	@JsonProperty("temperature_2m")
    private List<Double> temperature2m;
    public List<Double> getTemperature2m() {return temperature2m;}
	public void setTemperature2m(List<Double> temperature2m) {this.temperature2m = temperature2m;}

	@JsonProperty("terrestrial_radiation")
    private List<Double> terrestrialRadiation;
	public List<Double> getTerrestrialRadiation() {return terrestrialRadiation;}
	public void setTerrestrialRadiation(List<Double> terrestrialRadiation) {this.terrestrialRadiation = terrestrialRadiation;}
	
    @JsonProperty("shortwave_radiation")
    private List<Double> shortwaveRadiation;
    public List<Double> getShortwaveRadiation() {return shortwaveRadiation;}
    public void setShortwaveRadiation(List<Double> shortwaveRadiation) {this.shortwaveRadiation = shortwaveRadiation;}

    @JsonProperty("cloudcover")
    private List<Integer> cloudcover;
    public List<Integer> getCloudcover() {return cloudcover;}
    public void setCloudcover(List<Integer> cloudcover) {this.cloudcover = cloudcover;}

    @JsonProperty("direct_radiation")
    private List<Double> directRadiation;
    public List<Double>  getDirectRadiation() {return directRadiation;}
    public void setDirectRadiation(List<Double> directRadiation) {this.directRadiation = directRadiation;}

    @JsonProperty("diffuse_radiation")
    private List<Double> diffuseRadiation;
    public List<Double>  getDiffuseRadiation() {return diffuseRadiation;}
    public void setDiffuseRadiation(List<Double> diffuseRadiation) {this.diffuseRadiation = diffuseRadiation;}
}