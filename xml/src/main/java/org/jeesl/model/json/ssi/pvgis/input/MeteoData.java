package org.jeesl.model.json.ssi.pvgis.input;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MeteoData
{
    @JsonProperty("radiation_db")
    private String radiationDb;
    public String getRadiationDb() { return radiationDb; }
    public void setRadiationDb(String radiationDb) { this.radiationDb = radiationDb; }

    @JsonProperty("meteo_db")
    private String meteoDb;
    public String getMeteoDb() { return meteoDb; }
    public void setMeteoDb(String meteoDb) { this.meteoDb = meteoDb; }

    @JsonProperty("year_min")
    private int yearMin;
    public int getYearMin() { return yearMin; }
    public void setYearMin(int yearMin) { this.yearMin = yearMin; }

    @JsonProperty("year_max")
    private int yearMax;
    public int getYearMax() { return yearMax; }
    public void setYearMax(int yearMax) { this.yearMax = yearMax; }

    @JsonProperty("use_horizon")
    private boolean useHorizon;
    public boolean isUseHorizon() { return useHorizon; }
    public void setUseHorizon(boolean useHorizon) { this.useHorizon = useHorizon; }

    @JsonProperty("horizon_db")
    private String horizonDb;
    public String getHorizonDb() { return horizonDb; }
    public void setHorizonDb(String horizonDb) { this.horizonDb = horizonDb; }

    @JsonProperty("horizon_data")
    private String horizonData;
    public String getHorizonData() {return horizonData;}
    public void setHorizonData(String horizonData) { this.horizonData = horizonData;}
}