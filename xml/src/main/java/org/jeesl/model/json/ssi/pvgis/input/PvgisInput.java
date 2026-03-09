package org.jeesl.model.json.ssi.pvgis.input;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PvgisInput
{
	 @JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
	
    @JsonProperty("location")
    private PvgisLocation location;
    public PvgisLocation getLocation() { return location; }
    public void setLocation(PvgisLocation location) { this.location = location; }

    @JsonProperty("meteo_data")
    private MeteoData meteoData;
    public MeteoData getMeteoData() { return meteoData; }
    public void setMeteoData(MeteoData meteoData) { this.meteoData = meteoData; }

    @JsonProperty("mounting_system")
    private MountingSystem mountingSystem;
    public MountingSystem getMountingSystem() { return mountingSystem; }
    public void setMountingSystem(MountingSystem mountingSystem) { this.mountingSystem = mountingSystem; }

    @JsonProperty("pv_module")
    private PvgisModule pvModule;
    public PvgisModule getPvModule() { return pvModule; }
    public void setPvModule(PvgisModule pvModule) { this.pvModule = pvModule; }
}