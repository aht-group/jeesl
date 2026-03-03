package org.jeesl.model.json.ssi.pvgis.input;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Inputs
{
    @JsonProperty("location")
    private Location location;
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    @JsonProperty("meteo_data")
    private MeteoData meteoData;
    public MeteoData getMeteoData() { return meteoData; }
    public void setMeteoData(MeteoData meteoData) { this.meteoData = meteoData; }

    @JsonProperty("mounting_system")
    private MountingSystem mountingSystem;
    public MountingSystem getMountingSystem() { return mountingSystem; }
    public void setMountingSystem(MountingSystem mountingSystem) { this.mountingSystem = mountingSystem; }

    @JsonProperty("pv_module")
    private PvModule pvModule;
    public PvModule getPvModule() { return pvModule; }
    public void setPvModule(PvModule pvModule) { this.pvModule = pvModule; }
}