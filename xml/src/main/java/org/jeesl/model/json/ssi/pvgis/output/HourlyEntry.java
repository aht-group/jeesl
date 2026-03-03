package org.jeesl.model.json.ssi.pvgis.output;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class HourlyEntry
{
	
//	@JsonProperty("time")
//	private String time;
//	public String getTime() { return time; }
//	public void setTime(String time) { this.time = time; }
	
	@JsonProperty("time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd:HHmm")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime time;
	public LocalDateTime getTime() {return time;}
	public void setTime(LocalDateTime time) {this.time = time;}

	@JsonProperty("P")
	private double power;
	public double getPower() { return power; }
	public void setPower(double power) { this.power = power; }

	@JsonProperty("G(i)")
	private double irradiance;
    public double getIrradiance() {return irradiance;}
    public void setIrradiance(double irradiance) {this.irradiance = irradiance;}

	@JsonProperty("H_sun")
	private double sunHeight;
    public double getSunHeight() { return sunHeight; }
    public void setSunHeight(double sunHeight) { this.sunHeight = sunHeight; }

	@JsonProperty("T2m")
	private double temperature;
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

	@JsonProperty("WS10m")
	private double windSpeed;
    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }

	@JsonProperty("Int")
	private double interpolationFlag;
    public double getInterpolationFlag() { return interpolationFlag; }
    public void setInterpolationFlag(double interpolationFlag) { this.interpolationFlag = interpolationFlag; }
}