package net.sf.ahtutils.report.revert.excel;

import java.beans.Introspector;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.sf.ahtutils.interfaces.controller.report.UtilsXlsDefinitionResolver;
import net.sf.ahtutils.report.util.DataUtil;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jeesl.api.controller.ImportStrategy;
import org.jeesl.api.controller.ValidationStrategy;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.xml.io.report.DataAssociation;
import org.jeesl.model.xml.io.report.ImportStructure;
import org.jeesl.model.xml.io.report.ImportType;
import org.jeesl.model.xml.io.report.XlsSheet;
import org.jeesl.util.ReflectionUtil;
import org.jeesl.util.query.xpath.ReportXpath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractExcelImporter <C extends Serializable, I extends ImportStrategy> {
	
	final static Logger logger = LoggerFactory.getLogger(AbstractExcelImporter.class);
	
	protected File                       excelFile;
        protected Integer                    startRow;
	protected XSSFWorkbook               workbook;
	protected Sheet                      activeSheet;
	protected String                     activeColumn;
	public  JeeslFacade                  facade;
	protected Map<String, String>        propertyRelations;
	protected Map<String, Class>         strategies;
	protected Map<String, Class>		 validators;
	protected Map<String, Class>		 targetClasses;
	protected Map<String, Boolean>		 isList;
	protected short                      primaryKey;
	protected Hashtable<String, C>       entities          = new Hashtable<String, C>();
	protected Hashtable<String, Object>  tempPropertyStore = new Hashtable<String, Object>();
	protected Boolean                    hasPrimaryKey     = false;
	protected XlsSheet definition;
	protected ImportStructure structure;
	
	public AbstractExcelImporter(UtilsXlsDefinitionResolver resolver, String reportCode, InputStream is) throws IOException, ClassNotFoundException, ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		this(resolver.definition(reportCode).getXlsSheet().get(0),is);
	}
	
	public AbstractExcelImporter(UtilsXlsDefinitionResolver resolver, String reportCode, String filename) throws IOException, ClassNotFoundException, ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		this(resolver.definition(reportCode).getXlsSheet().get(0),filename);
	}
	
	public AbstractExcelImporter(XlsSheet definition, InputStream is) throws IOException, ClassNotFoundException, ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		// Read Excel workbook from given file(name)
		this.workbook       = new XSSFWorkbook(is);
		
		// Read information to import taken from Resolver
		this.definition = definition;
		structure = ReportXpath.getImportStructure(definition.getContent());
		
		// Prepare the row import definitions
		// According to this post http://stackoverflow.com/questions/18231991/class-forname-caching
		// Caching is most probably not important for classes, but to minimize JXPath searches
		propertyRelations = new HashMap<String, String>();
		strategies		  = new HashMap<String, Class>();
		validators		  = new HashMap<String, Class>();
		targetClasses	  = new HashMap<String, Class>();
		isList			  = new HashMap<String, Boolean>();
		for (DataAssociation association : structure.getDataAssociations().getDataAssociation())
		{
			String column = association.getColumn();
			propertyRelations.put(column, association.getProperty());
			if(Objects.nonNull(association.getHandledBy()))
			{
				strategies.put(column, Class.forName(association.getHandledBy()));
				if(Objects.nonNull(association.getType()))
				{
					if(association.getType().equals(ImportType.LIST))
					{
						isList.put(column, true);
					}
				}
			}
			if(Objects.nonNull(association.getValidatedBy())) {validators.put(column, Class.forName(association.getValidatedBy()));}
			if(Objects.nonNull(association.getTargetClass())) {targetClasses.put(column, Class.forName(association.getTargetClass()));}
		}
	}
	
	public AbstractExcelImporter(XlsSheet definition, String filename) throws IOException, ClassNotFoundException, ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		// Prepare file to be read
		this.excelFile      = new File(filename);
		FileInputStream fis = new FileInputStream(excelFile);
		
		// Read Excel workbook from given file(name)
		this.workbook       = new XSSFWorkbook(fis);
		
		// Read information to import taken from Resolver
		this.definition = definition;
		structure = ReportXpath.getImportStructure(definition.getContent());
		
		// Prepare the row import definitions
		// According to this post http://stackoverflow.com/questions/18231991/class-forname-caching
		// Caching is most probably not important for classes, but to minimize JXPath searches
		propertyRelations = new HashMap<String, String>();
		strategies		  = new HashMap<String, Class>();
		validators		  = new HashMap<String, Class>();
		targetClasses	  = new HashMap<String, Class>();
		isList			  = new HashMap<String, Boolean>();
		for (DataAssociation association : structure.getDataAssociations().getDataAssociation())
		{
			String column = association.getColumn();
			propertyRelations.put(column, association.getProperty());
			if(Objects.nonNull(association.getHandledBy()))
			{
				strategies.put(column, Class.forName(association.getHandledBy()));
				if(Objects.nonNull(association.getType()))
													{
														if(association.getType().equals(ImportType.LIST))
														{
															isList.put(column, true);
														}
													}
												}
			if(Objects.nonNull(association.getValidatedBy())) {validators.put(column, Class.forName(association.getValidatedBy()));}
			if(Objects.nonNull(association.getTargetClass())) {targetClasses.put(column, Class.forName(association.getTargetClass()));}
		}
	}
	
	public void setFacade(JeeslFacade facade){this.facade = facade;}
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
	
	// Select a sheet from the Excel workbook by name
	public void selectSheetByName(String name)	{activeSheet         = workbook.getSheet(name);}
	
	// Select a sheet from the Excel workbook by name
	public void selectFirstSheet()				{activeSheet         = workbook.getSheetAt(0);}
	
	// Debugging of sheet data
	public void debugHeader()       {DataUtil.debugRow(activeSheet, 0);}
	public void debugFirstRow()     {DataUtil.debugRow(activeSheet, 1);}
		
	public Map<C,ArrayList<String>> execute(Boolean skipTitle) throws Exception
	{
	// Create a list to hold the Entity classes to be created
	LinkedHashMap<C,ArrayList<String>> importedEntities = new LinkedHashMap<C,ArrayList<String>>();
                
        // Define the rows to begin with and to end with, whether with or without first row
	Integer end   = activeSheet.getLastRowNum();
	Integer start = activeSheet.getFirstRowNum();

	if (logger.isDebugEnabled()){logger.debug("Sheet goes from " +start +" to " +end);}
	if (skipTitle) {start++;}
	if (startRow!=null) {start = startRow;}
	if (logger.isDebugEnabled()){logger.debug("Starting at " +start);}
	// Iterate through all given rows
	for (int i = start; i < end+1; i++)
	{
	    // Get the next row
	    Row row = activeSheet.getRow(i);
	    if (row==null || isEmptyRow(row)) {continue;}
			// Create a new Entity
			C entity = (C) Class.forName(structure.getTargetClass()).newInstance();
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

	    // Create a list of properties that falied the validation
	    // This can be used for staging purposes later on
	    ArrayList<String> failedValidations = new ArrayList<String>();

	    if (hasPrimaryKey)
	    {
		    // See if there is already an instance created for this key, otherwise create a new one
		    String entityKey = DataUtil.getStringValue(DataUtil.getCellValue(row.getCell(primaryKey)));
		    if ( this.entities.containsKey(entityKey))
		    {
			entity = this.entities.get(entityKey);
		    }
	    }

	    // Iterate through the columns and assign data as given in the association table
	    for (short j = row.getFirstCellNum() ; j < row.getLastCellNum() ; j++)
	    {
		Cell cell = row.getCell(j);
		activeColumn = j +"";

		// Assign the data to the entity using the setter
		if (propertyRelations.containsKey(j +""))
		{
		    // Get the Cell Value as Object
		    Object object = DataUtil.getCellValue(cell);
			
		    // Read the name of the property that should be filled with the data from this column
		    String propertyName = propertyRelations.get(j +"");
		    if (propertyName!=null && !object.getClass().getCanonicalName().endsWith("java.lang.Object"))
		    {
			if(logger.isTraceEnabled()){logger.trace("Cell " +row.getRowNum() +"," +j +" should store " +propertyName +", value as String is " +object.toString() +" and is of class " +object.getClass().getCanonicalName());}

			String property = propertyName;
			if(logger.isTraceEnabled()){logger.trace("Setting " +property + " to " +object.toString() +" type: " +object.getClass().getCanonicalName() +")");}
			tempPropertyStore.put(property, object.toString());
			Class handler = strategies.get(activeColumn);
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
			    if (logger.isTraceEnabled()) {logger.warn("Could not read " +CellReference.convertNumToColString(j) +"." +(row.getRowNum()+1) +": " +property +" because of " +e.getMessage());}
			}
			if (!validated)
			{
			    if (logger.isTraceEnabled()) {logger.warn("Could not read " +CellReference.convertNumToColString(j) +"." +(row.getRowNum()+1) +": " +property);}
			    failedValidations.add(CellReference.convertNumToColString(j) +"." +(row.getRowNum()+1) +": " +property);
			}
		    }
		}
	    }

	    //facade.save(entity);
	    importedEntities.put(entity, failedValidations);
	    if (hasPrimaryKey)
	    {
		String entityKey = DataUtil.getStringValue(DataUtil.getCellValue(row.getCell(primaryKey)));
		entities.put(entityKey, entity);
	    }
	}
	return importedEntities;
    }
	
	
		
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

			// Now find the correct method
			Method[] methods = targetClass.getDeclaredMethods();
			Class parameter  = null;
			Method m         = null;
			for (Method method : methods)
			{
			    if (method.getName().equals(methodName))
			    {
				parameter = method.getParameterTypes()[0];
				if (Modifier.isPrivate(method.getModifiers()))
				{
				    method.setAccessible(true);
				}
				m = method;
			    }
			}
			
			if (m== null)
			{
				// Using superclass
				Method[] methodsOfParent = targetClass.getSuperclass().getDeclaredMethods();
				for (Method method : methodsOfParent)
				{
					if (method.getName().equals(methodName))
					{
						parameter = method.getParameterTypes()[0];
						if (Modifier.isPrivate(method.getModifiers()))
						{
							method.setAccessible(true);
						}
						m = method;
					}
				}
			}
			// Determine parameter type of setter
			// Type t = m.getGenericParameterTypes()[0];
			// String parameterClass = t.getTypeName();

			String parameterClass = parameter.getCanonicalName();
		 //   logger.info(parameterClass);
		 //   logger.info(parameters[0].getClass().getCanonicalName());

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
				} 

				// Needed to correct the Class of the general number
				else if (parameterClass.equals("long"))
				{
				    Number number = (Number) parameters[0];
				    parameters[0] = number.longValue();
				}
				
				// Needed to correct the Class of the general number
				else if (parameterClass.equals("int"))
				{
				    Number number = (Number) parameters[0];
				    parameters[0] = number.intValue();
				}
				
				// Needed to correct the Class of the general number
				else if (parameterClass.equals("java.lang.Integer"))
				{
				    Number number = (Number) parameters[0];
				    parameters[0] = new Integer(number.intValue());
				}
                                
                                // Needed to correct the Class of the general number
				else if (parameterClass.equals("java.lang.Double"))
				{
				    Number number = (Number) parameters[0];
				    parameters[0] = new Double(number.doubleValue());
				}

				// This is important if the String is a Number, Excel will format the cell to be a "general number"
				else if (parameterClass.equals("java.lang.String"))
				{
					if (parameters[0].getClass().getName().equals("java.lang.Double"))
				    {
						Double n			= (Double) parameters[0];
						// This version is not working! It produces results such as this:
						// Result 2147483647 taken from 3.57305101E9 (toString of n)
						// parameters[0]	= "" +n.intValue();
						BigDecimal bd = new BigDecimal(n);
						parameters[0]	= bd.toPlainString();
						
						/* See above
						if (n % 1 == 0)
						{
							parameters[0]	= "" +n.intValue();
						}
						else
						{
							parameters[0]	= "" +n;
						}
						*/
				    }
				    else
				    {
						parameters[0] = parameters[0] +"";
				    }
				}
				
				// This is important if the String is a Number, Excel will format the cell to be a "general number"
				else if (parameterClass.equals("java.lang.Boolean") || parameterClass.equals("boolean"))
				{
				    Number number = (Number) parameters[0];
				    Boolean b     = true;
				    if (number.intValue() == 0)
				    {
					    b = false;
				    }
				    parameters[0] = b;
				}

				// Now invoke the method with the parameter from the Excel sheet
                                if (logger.isTraceEnabled())
                                {
                                    logger.trace("Parameter Class is " +parameters[0].getClass().getSimpleName() + " and original entity property is " +parameterClass);
                                }
                            try {
                               //m.invoke(target, parameters[0]);
							   BeanUtils.setProperty(target,Introspector.decapitalize(property),parameters[0]);
                            } catch (IllegalAccessException ex) {
                                logger.error("Could not set " +m.getName() +" with " +parameters[0] +": " +ex.getMessage());
				return false;
                            } catch (InvocationTargetException ex) {
								logger.error("Could not set " +m.getName() +" with " +parameters[0] +": " +ex.getMessage());
								return false;
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
	 
	private boolean isEmptyRow(Row row)
	{
		boolean isEmptyRow = true;
			for(int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++)
			{
			   Cell cell = row.getCell(cellNum);
			   if(cell != null && cell.getCellType() != CellType.BLANK && StringUtils.isNotBlank(cell.toString()))
			   {
				   isEmptyRow = false;
			   }    
			}
		if (isEmptyRow) 
		{
			logger.warn("Row " +row.getRowNum() + " seems to be empty and will be ignored!");
		}
		return isEmptyRow;
   }
	 
	public Hashtable<String, Object> getTempPropertyStore() {return tempPropertyStore;}
	public void setTempPropertyStore(Hashtable<String, Object> tempPropertyStore) {this.tempPropertyStore = tempPropertyStore;}
        
        public void setStartRow(Integer c) {startRow = c;}
}
