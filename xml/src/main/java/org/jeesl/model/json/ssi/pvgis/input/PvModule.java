package org.jeesl.model.json.ssi.pvgis.input;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PvModule
{
	@JsonProperty("technology")
	private String technology;
    public String getTechnology() {return technology;}
	public void setTechnology(String technology) {this.technology = technology;}
	
	@JsonProperty("peak_power")
	private double peakPower;
	public double getPeakPower() {return peakPower;}
	public void setPeakPower(double peakPower) {this.peakPower = peakPower;}
	
	@JsonProperty("system_loss")
	private double systemLoss;
	public double getSystemLoss() {return systemLoss;}
	public void setSystemLoss(double systemLoss) {this.systemLoss = systemLoss;}
}