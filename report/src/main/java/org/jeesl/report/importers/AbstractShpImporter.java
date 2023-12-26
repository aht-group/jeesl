package org.jeesl.report.importers;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.jeesl.api.controller.ImportStrategy;
import org.jeesl.model.xml.io.report.XlsSheet;
import org.jeesl.report.importers.model.shp.ShapeFeature;
import org.jeesl.report.importers.model.shp.ShapeFile;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public abstract class AbstractShpImporter <C extends Serializable, I extends ImportStrategy> extends AbstractTableImporter<C, I>{
	
    final static Logger logger = LoggerFactory.getLogger(AbstractShpImporter.class);

    protected Integer                    startRow;
    protected File                       shpDirectory;
    protected FeatureCollection          collection;
    protected Map<Short, String>         propertyNameForColumn;
    private ShapeFile importDefinition; public ShapeFile getImportDefinition() {return importDefinition;} public void setImportDefinition(ShapeFile importDefinition) {this.importDefinition = importDefinition;}

    public AbstractShpImporter(XlsSheet definition, File shpDirectory) throws IOException, ClassNotFoundException, ExlpXpathNotFoundException, ExlpXpathNotUniqueException
    {
        super(definition);
        
        Map connect = new HashMap();
        connect.put("url", shpDirectory.toURI().toURL());
        
        DataStore dataStore = DataStoreFinder.getDataStore(connect);
        String[] typeNames = dataStore.getTypeNames();
        String typeName = typeNames[0];

	/* Deprecated with new GeoTools version
        FeatureSource featureSource = dataStore.getFeatureSource(typeName);
	*/
	
	SimpleFeatureSource featureSource = dataStore.getFeatureSource( typeName );

        collection                  = featureSource.getFeatures();
        FeatureIterator iterator        = collection.features();
                
        // Prepare the import definition reflecting the Shape Files data and meta data
        importDefinition = new ShapeFile();
        importDefinition.setPropertyNames(new ArrayList<>());
        importDefinition.setData(new ArrayList<>());
        importDefinition.setCorrelationMap(new HashMap<>());
        while (iterator.hasNext())
        {
            Feature feature         = iterator.next();
            ShapeFeature sFeature   = new ShapeFeature();
            sFeature.setProperties(new HashMap<>());
            for (Property property : feature.getProperties())
            {
                if (!importDefinition.getPropertyNames().contains(property.getName().toString()) && !property.getName().toString().equals("the_geom"))
                {
                    importDefinition.getPropertyNames().add(property.getName().toString());
                }
                sFeature.getProperties().put(property.getName().toString(), property.getValue());
            }
            importDefinition.getData().add(sFeature);
        }
        logger.info("Using feature source: " +featureSource.getName());
        
    }

    @Override
    public void rangeCheck(Boolean skipTitle)
    {
        logger.info("Performing Range check");
        // Define the rows to begin with and to end with, whether with or without first row
        endRow   = (short) collection.size();
        startRow = 0;
        
        
        startColumn = 0;
        endColumn   = (short) importDefinition.getPropertyNames().size();
        logger.info("Table columns are from " +startColumn  + " to " +endColumn);
        logger.info("Table rows    are from " +startRow     + " to " +endRow);
        
        // This is an appropriate place to construc the relaton between column nr and property name
        propertyNameForColumn = new HashMap<>();
        Short counter = 0;
        for (String propertyName : importDefinition.getPropertyNames())
        {
            propertyNameForColumn.put(counter, propertyName);
            counter++;
        }
        logger.info(propertyNameForColumn.toString());
    }
    
    @Override
    public Object getCellValue(short row, short column)
    {
        ShapeFeature feature = importDefinition.getData().get(row);
        return feature.getProperties().get(propertyNameForColumn.get(column));
    }
	 
    @Override
    public boolean rowExists(short row)
    {
        // Since the data is preprocessed, every request will return a valid row construct
        return true;
    }
    
    @Override
    public Map<Short, String> getColumnTitles()
    {
        logger.info("Properties and Columns");
	logger.info("======================");
	for (Short column : propertyNameForColumn.keySet())
	{
	    logger.info("Column " +column + " stores " +propertyNameForColumn.get(column));
	}
	return propertyNameForColumn;
    }
    
    @Override
    public Map<Short, String> getPreview()
    {
        logger.info("Data preview");
	logger.info("======================");
	Short row = 2;
	Map<Short, String> preview = new HashMap<>();
	for (Short column : propertyNameForColumn.keySet())
	{
	    logger.info("Column " +column + " stores " +getCellValue(row, column) +" in second row");
	    preview.put(column, getCellValue(row, column).toString());
	}
	return preview;
    }
}
