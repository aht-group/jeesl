package org.jeesl.model.json.ssi.pvgis.meta;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MetaInputs
{
    @JsonProperty("location")
    private MetaSection location;
    public MetaSection getLocation() { return location; }
    public void setLocation(MetaSection location) { this.location = location; }

    @JsonProperty("meteo_data")
    private MetaSection meteoData;
    public MetaSection getMeteoData() { return meteoData; }
    public void setMeteoData(MetaSection meteoData) { this.meteoData = meteoData; }

    @JsonProperty("mounting_system")
    private MetaSection mountingSystem;
    public MetaSection getMountingSystem() { return mountingSystem; }
    public void setMountingSystem(MetaSection mountingSystem) { this.mountingSystem = mountingSystem; }

    @JsonProperty("pv_module")
    private MetaSection pvModule;
    public MetaSection getPvModule() { return pvModule; }
    public void setPvModule(MetaSection pvModule) { this.pvModule = pvModule; }
}