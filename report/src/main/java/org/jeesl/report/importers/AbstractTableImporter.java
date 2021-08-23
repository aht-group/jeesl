package org.jeesl.report.importers;

import java.beans.Introspector;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.sf.ahtutils.xml.report.DataAssociation;
import net.sf.ahtutils.xml.report.ImportStructure;
import net.sf.ahtutils.xml.report.ImportType;
import net.sf.ahtutils.xml.report.XlsSheet;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import org.jeesl.api.controller.ImportStrategy;
import org.jeesl.api.controller.ValidationStrategy;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.util.ReflectionUtil;
import org.jeesl.util.SetterHelper;
import org.jeesl.util.query.xpath.ReportXpath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTableImporter <C extends Serializable, I extends ImportStrategy> {
	
    final static Logger logger = LoggerFactory.getLogger(AbstractTableImporter.class);

    // ************************************************************************************************************
    // Beans and CDI Injections
    // ************************************************************************************************************
    public  JeeslFacade                  facade;            // For database requests
    public void setFacade(JeeslFacade facade){this.facade = facade;}

    // ************************************************************************************************************
    // Properties, Getters and Setters
    // ************************************************************************************************************
    protected short                     startRow; public void setStartRow(short c) {startRow = c;}
    protected short                     endRow; public short getEndRow() {return endRow;} public void setEndRow(short endRow) {this.endRow = endRow;}
    protected short                     startColumn; public short getStartColumn() {return startColumn;} public void setStartColumn(short startColumn) {this.startColumn = startColumn;}
    protected short                     endColumn; public short getEndColumn() {return endColumn;} public void setEndColumn(short endColumn) {this.endColumn = endColumn;}
    protected String                    activeColumn;
    protected Map<String, String>       propertyRelations;
    protected Map<String, Class>        strategies;
    protected Map<String, Class>        validators;
    protected Map<String, Class>        targetClasses;
    protected Map<String, Boolean>      isList;
    protected short                     primaryKey;
    protected Hashtable<String, C>      entities          = new Hashtable<String, C>();
    protected Hashtable<String, Object> tempPropertyStore = new Hashtable<String, Object>(); public void setTempPropertyStore(Hashtable<String, Object> tempPropertyStore) {this.tempPropertyStore = tempPropertyStore;} public Hashtable<String, Object> getTempPropertyStore() {return tempPropertyStore;}
    protected Boolean                   hasPrimaryKey     = false;
    protected XlsSheet definition;
    protected ImportStructure structure;
    protected LinkedHashMap<C,ArrayList<String>> importedEntities = new LinkedHashMap<C,ArrayList<String>>();

    // ************************************************************************************************************
    // Constructor and Post Construct method
    // ************************************************************************************************************
    public AbstractTableImporter(XlsSheet definition) throws IOException, ClassNotFoundException, ExlpXpathNotFoundException, ExlpXpathNotUniqueException
    {
        // Read information to import taken from Resolver
        this.definition = definition;
        structure = ReportXpath.getImportStructure(definition.getContent());

        // Prepare the row import definitions
        // According to this post http://stackoverflow.com/questions/18231991/class-forname-caching
        // Caching is most probably not important for classes, but to minimize JXPath searches
        propertyRelations   = new HashMap<String, String>();
        strategies          = new HashMap<String, Class>();
        validators          = new HashMap<String, Class>();
        targetClasses       = new HashMap<String, Class>();
        isList              = new HashMap<String, Boolean>();
        for (DataAssociation association : structure.getDataAssociations().getDataAssociation())
        {
            String column = association.getColumn();
            propertyRelations.put(column, association.getProperty());
            if (association.isSetHandledBy())	
            {
                strategies.put(column, Class.forName(association.getHandledBy()));
                if (association.isSetType())
                {
                    if(association.getType().equals(ImportType.LIST))
                    {
                        isList.put(column, true);
                    }
                }
            }
            if (association.isSetValidatedBy()) {validators.put(column, Class.forName(association.getValidatedBy()));}
            if (association.isSetTargetClass()) {targetClasses.put(column, Class.forName(association.getTargetClass()));}
        }
    }

    /**
     * Execute the import process - iterate through the table, read and process values and setters
     * @param skipTitle Indicates that the header row should be skipped (as in many standard tables)
     * @return The LinkedHashMap contains the actual entities that were imported as keys (can be accessed as list using keySet()) and for every entity imported a list of properties for which the validation failed. That is important e.g. to mark them in a preview table as wrong.
     */
    public LinkedHashMap<C,ArrayList<String>> execute(Boolean skipTitle)
    {
        rangeCheck(skipTitle);
        for (short row = startRow; row < endRow; row++)
        {
            logger.info("Processing row " +row);
            if (rowExists(row))
            {
                try {
                    processImport(row);
                } catch (Exception ex) {
                    logger.warn("Could not import row " +row +" with the following Exception " +ex.getMessage());
                }
            }
            else
            {
                logger.warn("Row " +row +" does not exist.");
            }
        }
        return importedEntities;
    }

    /**
     * Iterate through the columns of the row, read the data and process it and call the respected setter on the created entity
     * @param row   The row number
     * @throws Exception
     */
    public void processImport(short row) throws Exception
    {
        // Create a new Entity
        C entity = (C) Class.forName(structure.getTargetClass()).newInstance();

        // Create a list of properties that falied the validation
        // This can be used for staging purposes later on
        ArrayList<String> failedValidations = new ArrayList<String>();

        // Handle creation of IDs if requested 
        if (entity instanceof EjbWithId && !(tempPropertyStore.containsKey("dontGenerateIds")))
        {
            Long currentId = new Long(1);
            if (tempPropertyStore.containsKey("currentId")) 
            {
                currentId = (Long) tempPropertyStore.get("currentId");
            }
            ((EjbWithId) entity).setId(currentId + 1);
            tempPropertyStore.put("currentId", currentId +1);
        }

        // Handle entities that have a primary key and check if there is a matching instance already
        if (hasPrimaryKey)
        {
            // See if there is already an instance created for this key, otherwise create a new one
            String entityKey = getCellValue(row, primaryKey).toString();
            if ( this.entities.containsKey(entityKey))
            {
                entity = this.entities.get(entityKey);
                entities.put(entityKey, entity);
            }
        }

        for (short column = startColumn; column < endColumn; column++)
        {
            // Assign the data to the entity using the setter
            if (propertyRelations.containsKey(column +""))
            {
                // Get the Cell Value as Object
                Object object = getCellValue(row, column);

                // Read the name of the property that should be filled with the data from this column
                String propertyName = propertyRelations.get(column +"");
                if (propertyName!=null && !object.getClass().getCanonicalName().endsWith("java.lang.Object"))
                {
                    if(logger.isTraceEnabled()){logger.trace("Cell " +column +"," +" should store " +propertyName +", value as String is " +object.toString() +" and is of class " +object.getClass().getCanonicalName());}

                    String property = propertyName;
                    if(logger.isTraceEnabled()){logger.trace("Setting " +property + " to " +object.toString() +" type: " +object.getClass().getCanonicalName() +")");}
                    tempPropertyStore.put(property, object.toString());
                    Class handler = strategies.get(column +"");
                    activeColumn = column +"";
                    Boolean validated = false;
                    try 
                    {
                        validated = invokeSetter(property,
                                    new Object[] { object },
                                    entity.getClass(),
                                    entity,
                                    handler);
                    }
                    catch (Exception e)
                    {
                        if (logger.isTraceEnabled()) {logger.warn("Could not read " +row +"." +column +": " +property +" because of " +e.getMessage());}
                    }
                    if (!validated)
                    {
                        if (logger.isTraceEnabled()) {logger.warn("Could not read " +row +"." +column +": " +property);}
                        failedValidations.add(row +"." +column +": " +property);
                    }
                }
            }
        }
        importedEntities.put(entity, failedValidations);
    }
	
    /**
     * Prepare the data to be set, validate it and add it including the result to the result Map
     * @param property      The name of the property (e.g. Name or Country)
     * @param parameters    The value to be set as first element of the parameters Array
     * @param targetClass   The class of the target object
     * @param target        The target object itself
     * @param handler       The handler to process the value and set the property, if given (can be null). Must implement the ImportStrategy interface (e.g. LoadByCodeStrategy)
     * @return Is the imported data valid?
     * @throws Exception
     */
    protected Boolean invokeSetter(String   property, 
                                   Object[] parameters,
                                   Class    targetClass,
                                   Object   target,
                                   Class    handler)        throws Exception
    {
        if (isList.containsKey(activeColumn))
        {
            List list = (List) MethodUtils.invokeMethod(target, "get" +property, null);

            // Instantiate new strategy to handle import
            ImportStrategy strategy = (ImportStrategy) handler.newInstance();

            // Pass database connection and current set of temporary properties
            strategy.setFacade(facade);
            strategy.setTempPropertyStore(tempPropertyStore);

            // Process import step - Parameterclass is not requrired here
            Object value  = strategy.handleObject(parameters[0], "", property);
            parameters[0] =  value;

            list.add(value);
            return true;
        }
        else
        {
            Boolean validated		= false;
            String methodName		= "set" +property;
            String valueFromCell	= parameters[0].toString();
            logger.trace("Invoking " +methodName);

            // Now find the correct method and read information from it
            Method m         = SetterHelper.findSetMethod(target, property);
            Class parameter         = m.getParameterTypes()[0];
            String parameterClass   = parameter.getCanonicalName();
            
            // Lets see if the setter is accepting a data type that is available in Excel (String, Double, Date)
            // Otherwise assume that it is used with a lookup table
            Boolean isHandled = (handler != null);
            if (ReflectionUtil.hasMethod(target, methodName))
            {
                if (isHandled)
                {
                    if (targetClasses.containsKey(activeColumn)) {parameterClass = targetClasses.get(activeColumn).getCanonicalName();}
                    if (logger.isTraceEnabled()) {logger.trace("Loading import strategy for " +parameterClass +": " +handler.getCanonicalName() +".");}
                    // Instantiate new strategy to handle import
                    ImportStrategy strategy = (ImportStrategy) handler.newInstance();

                    // Pass database connection and current set of temporary properties
                    strategy.setFacade(facade);
                    strategy.setTempPropertyStore(tempPropertyStore);

                    // Process import step
                    Object value  = strategy.handleObject(parameters[0], parameterClass, property);
                    parameters[0] =  value;

                    // Sync new temporary properties if any added
                    tempPropertyStore = strategy.getTempPropertyStore();

                    // Add the current property/value pair, can be useful when inspecting IDs (overwritten for new lines for examples)
                    if(logger.isTraceEnabled()){logger.trace("Set " +property + " to " + value.toString());}
                    if (value!=null) {tempPropertyStore.put(property, value);}

                    try {
                        BeanUtils.setProperty(target,Introspector.decapitalize(property),parameters[0]);
                    } catch (IllegalAccessException ex) {
                        logger.error("Could not set " +m.getName() +" with " +parameters[0] +": " +ex.getMessage());
                        return false;
                    } catch (InvocationTargetException ex) {
                        logger.error("Could not set " +m.getName() +" with " +parameters[0] +": " +ex.getMessage());
                        return false;
                    }
                } 

                // The Setter Helper will do the necessary conversions (e.g. that a code is printed as String correctly without artifacts like 1E3 instead of 1000
                else 
                {
                    SetterHelper.set(property, target, parameters[0]);
                }
            }
            else
            {
                logger.trace("Entity does not have the method " +methodName +". Initiating special treatment.");
            }
            
            if (validators.containsKey(activeColumn))
            {
                // Instantiate new strategy to handle import
                ValidationStrategy validator = (ValidationStrategy) validators.get(activeColumn).newInstance();
                validator.setFacade(facade);
                if (logger.isTraceEnabled()) {logger.info("Using " +validator.getClass().getCanonicalName());}
                // Validate the loaded value
                if (targetClasses.containsKey(activeColumn)) 
                {
                    validated = validator.validate(valueFromCell, targetClasses.get(activeColumn).getCanonicalName(), property);
                }
                else
                {
                    validated = validator.validate(valueFromCell, "", property);
                }

                if (logger.isTraceEnabled()) {logger.trace("Validation result: " +validated);}
            }
            else
            {
                validated = true;
            }
            return validated;
        }
    }
    
    public void setPrimaryKey(Integer columnNumber)
    {
        if(columnNumber == null) 
        {
            this.hasPrimaryKey = false;
        }
        else
        {
            this.primaryKey    = columnNumber.shortValue();
            this.hasPrimaryKey = true;
        }
    }
    
    
    // ************************************************************************************************************
    // SPECIFIC IMPLEMENTATIONs TO BE OVERWRITTEN IN CONCRETE CLASSES
    // ************************************************************************************************************

    /**
     * The custom methods to read the value from the cell at the given row and column needs to be overwritten in concrete class
     * This could be Apache POI based like for Excel worksheets for example
     * @param row    The row number
     * @param column The column number
     * @return The value found in the given cell
     */
    public Object getCellValue(short row, short column)
    {
        logger.error("Override this method in the concrete implentation (e.g. Excel or Shape File import) or leave as is for debugging");
        return "Debugging mode - override method in concrete class";
    }
    
    /**
     * Tests if a row is empty - if so it will be skipped when processing the table
     * @param row   The row number
     * @return True if the row exists and has content in it
     */
    public boolean rowExists(short row)
    {
        logger.error("Override this method in the concrete implentation (e.g. Excel or Shape File import) or leave as is for debugging");
        return true;
    }
    
    /**
     * The boundaries of the iteration of rows and columns shall be set here.
     * @param skipTitle If true, it indicates that the first row should be skipped. It can be useful for many tables to ignore their header row
     */
    public void rangeCheck(Boolean skipTitle)
    {
        logger.error("Override this method in the concrete implentation (e.g. Excel or Shape File import) or leave as is for debugging");
    }
    
    /**
     * Get the headers of the columns (e.g. first row in Excel or property names in Shape files)
     */
    public Map<Short, String> getColumnTitles()
    {
        logger.error("Override this method in the concrete implentation (e.g. Excel or Shape File import) or leave as is for debugging");
	return new HashMap<Short, String>();
    }
    
    /**
     * Get a preview of the contained data
     */
    public Map<Short, String> getPreview()
    {
        logger.error("Override this method in the concrete implentation (e.g. Excel or Shape File import) or leave as is for debugging");
	return new HashMap<Short, String>();
    }
}
