package org.jeesl.report.importers.model.shp;

import java.util.Map;

/**
 * Represents Features in ESRI ArcGIS Shape-Files
 */
public class ShapeFeature {
    
    private Map<String, Object> properties;

    /**
     * @return The actual data within the Shape file, storing e.g. NAME = Germany
    */
    public Map<String, Object> getProperties() {return properties;}
    public void setProperties(Map<String, Object> properties) {this.properties = properties;}
}
