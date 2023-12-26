package net.sf.ahtutils.report.excel;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jeesl.factory.xls.system.io.report.XlsRowFactory;
import org.jeesl.factory.xls.system.io.report.XlsSheetFactory;
import org.jeesl.model.xml.io.locale.status.Lang;
import org.jeesl.model.xml.io.locale.status.Langs;
import org.jeesl.model.xml.io.report.Label;
import org.jeesl.model.xml.io.report.XlsColumn;
import org.jeesl.model.xml.io.report.XlsMultiColumn;
import org.jeesl.model.xml.io.report.XlsSheet;
import org.jeesl.model.xml.io.report.XlsTransformation;
import org.jeesl.model.xml.io.report.XlsWorkbook;
import org.jeesl.util.query.xpath.ReportXpath;
import org.jeesl.util.query.xpath.StatusXpath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.controller.report.UtilsXlsDefinitionResolver;
import net.sf.exlp.util.io.StringUtil;

@Deprecated // Use JeeslExcelDomainExporter
public class ExcelExporter
{
	
    // Excel related objects
    public Workbook         wb;
    public Font             headerFont;
    public CellStyle        dateHeaderStyle;
    public CellStyle        numberStyle; 
    public CreationHelper   createHelper;
    public JXPathContext	context;
    
    // The data
    public Object    report;
    
    // How many results are there for the given query
    public Integer   counter;
	
	// Current line while exporting
	private int      rowNr = 1;
	
	// Languge
	private String   languageKey;
        
    public Hashtable<String, CellStyle> cellStyles = new Hashtable<String, CellStyle>();
    public Hashtable<String, Integer> errors = new Hashtable<String, Integer>();
	
    final static Logger logger = LoggerFactory.getLogger(ExcelExporter.class);
	private int MIN_WIDTH = 5000;
	
	public ExcelExporter(UtilsXlsDefinitionResolver resolver, String id, Object report, String languageKey)
    {
		// Get all info
        this.report     = report;
        this.languageKey= languageKey;
		
        // Create a new Excel Workbook and a POI Helper Object
        wb = new XSSFWorkbook();
        createHelper = wb.getCreationHelper();

        // Create a new font and alter it.
        Font font = wb.createFont();
        font.setItalic(true);
        font.setBold(true);

        // Create styles
        dateHeaderStyle = wb.createCellStyle();
        dateHeaderStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy.MM"));
        dateHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
        dateHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dateHeaderStyle.setFont(font);

        numberStyle = wb.createCellStyle();
        numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("#.00\\ RWF"));

        // Search for the right Workbook definition in the XlsDefinition
        XlsWorkbook workbook = resolver.definition(id);
        
//        JaxbUtil.info(workbook);
        
        if (workbook == null)
        {
        	logger.error("Xml Workbook not found in Definition.");
        	workbook = new XlsWorkbook();
        }

        // Now lets create the sheets and export the data
		int i = 1;
        for (XlsSheet sheet : workbook.getXlsSheet())
        {
			// Reset the rowCounter
			rowNr = 1;
			
			// First check for an existing name given in the definition
			String sheetName = "sheet" +i;
			
			try
			{
			Langs langs = ReportXpath.getFirstLangs(sheet.getContent());
			Lang  lang  = StatusXpath.getLang(langs, languageKey);
			if (lang != null)
			{
				logger.info("Setting Sheetname to " +lang.getTranslation());
				sheetName = lang.getTranslation();
			}}
			catch (Exception e)
			{
				logger.warn(e.getMessage());
				logger.warn("Could not retrieve sheet name from definition, falling back to standard name.");
			}
			
			// Write signiture fields
			applyHeader(sheetName);
			
			// Export data
			exportSheet(sheet, sheetName);
			
			// Begin the footer with three lines of free space
			rowNr = rowNr + 3;
			
			// Write signiture fields
			applyFooter(sheetName);
			
			// Continue with next sheet if existing
			i++;
		}
    }
		
	public void applyFooter(String sheetName)
	{
		// Reset the context back to the complete report XML, because it might have been changed to a local one
		context = JXPathContext.newContext(report);
		
		// Get the Excel Sheet
		Sheet sheet = XlsSheetFactory.getSheet(wb,sheetName);

		// Create the standard text style
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontName("Arial");
		style.setFont(font);

		// Ask for all labels and add the ones starting with signature to a list
		Iterator iterator     = context.iteratePointers("/info/labels/label[@scope='signatures']");
		ArrayList<Label> signatureLabels = new ArrayList<Label>();
		while (iterator.hasNext())
		{
			Pointer pointerToItem = (Pointer)iterator.next();
			Object o = pointerToItem.getValue();
			if ((o!=null))
			{
				//TODO Finds also label='xy' attributes, must be restricted in XPath if possible for performance improvement
				if (logger.isTraceEnabled()) {logger.trace("Got pointer: " +o);}
				if (o.getClass() == Label.class)
				{
					Label l = (Label) pointerToItem.getValue();
					signatureLabels.add(l);
				}
			}
		}
		logger.info("Footer will be printed with " +signatureLabels.size() +" labels.");
		
		// Write the groups of label (e.g. approved by, prepared by) and the line to write the associated person
		Integer columnNr = 0;
		for (int i=0; i<signatureLabels.size();i++)
		{
			String responsible = "___________________";
			Label label = signatureLabels.get(i);
			if (label.isSetValue()) {responsible = label.getValue();}
			createCell(sheet, rowNr,   columnNr, label.getKey(), "String", style);
			createCell(sheet, rowNr+1, columnNr, responsible, "String", style);
			createCell(sheet, rowNr+2, columnNr, "Date: ___/___/_____", "String", style);
			columnNr = columnNr + 2;
		}
	}
	
	public void applyHeader(String sheetName)
	{
		// Reset the context back to the complete report XML, because it might have been changed to a local one
		context = JXPathContext.newContext(report);
		
		// Get the Excel Sheet
		Sheet sheet = XlsSheetFactory.getSheet(wb,sheetName);
		logger.info(sheetName + " " +sheet.getSheetName());
		// Create the standard text style
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontName("Arial");
		style.setFont(font);

		// Build the Title, subtitle
		// DEPRECATED since PDF and Excel reports often use different information!
		/*
		Iterator iterator     = context.iteratePointers("/info/title");
		Pointer pointerToItem = (Pointer)iterator.next();
		Object o = pointerToItem.getValue();
		if ((o!=null))
		{
			if (logger.isTraceEnabled()) {logger.trace("Got pointer: " +o);}
			Title t = (Title) pointerToItem.getValue();
			createCell(sheet, rowNr, 0, t.getValue(), "String", style);
			logger.info("Title: " +t.getValue());
			rowNr++;
		}
		
		// Reset object
		o = null;
		iterator     = context.iteratePointers("//info/subtitle");
		if (iterator.hasNext()) 
		{
			pointerToItem = (Pointer)iterator.next();
			o = pointerToItem.getValue();
		}
		
		if ((o!=null))
		{
			if (logger.isTraceEnabled()) {logger.trace("Got pointer: " +o);}
			Subtitle s = (Subtitle) pointerToItem.getValue();
			logger.info("Subtitle: " +s.getValue());
			createCell(sheet, rowNr, 0, s.getValue(), "String", style);
			rowNr++;
		}
		*/
		
		// Ask for all labels and add the ones starting with header to a list
		Iterator iterator     = context.iteratePointers("/info/labels/label[@scope='header']");
		Pointer pointerToItem;
		Object o;
		ArrayList<Label> headerLabels = new ArrayList<Label>();
		while (iterator.hasNext())
		{
			pointerToItem = (Pointer)iterator.next();
			o = pointerToItem.getValue();
			if ((o!=null))
			{
				//TODO Finds also label='xy' attributes, must be restricted in XPath if possible for performance improvement
				if (logger.isTraceEnabled()) {logger.trace("Got pointer: " +o);}
				if (o.getClass() == Label.class)
				{
					Label l = (Label) pointerToItem.getValue();
					headerLabels.add(l);
				}
			}
		}

		for(Label header : headerLabels)
		{
			String value = "";
			if(header.isSetKey()) {value = header.getKey() + ": ";}
			value = value + header.getValue();
			createCell(sheet, rowNr, 0, value, "String", style);
			rowNr++;
		}
		
		rowNr++;
		rowNr++;
	}
	
	// Maybe add a bunch of sheets here for grouped report
	// Introduce Offsets for iteration of columns
	// Possible data structure:
	// 
    public void exportSheet(XlsSheet sheetDefinition, String id) /*throws Exception*/
    {
		logger.debug("Creating Sheet " +id);
        // Create JXPath context for working with the report data
        context = JXPathContext.newContext(report);
        
        // Create Excel Sheet named as given in constructor
        Sheet sheet = XlsSheetFactory.getSheet(wb,id);
        
		
        // PreProcess columns to create Styles and count the number of results for the given report query
        ArrayList<XlsColumn> sortedColumns = preProcessColumns(sheetDefinition, sheet);
		logger.debug("PreProcess complete. Got " +sortedColumns.size() +" columns.");
        // Create Headers
        ArrayList<String> headers = new ArrayList<String>();
		for (XlsColumn column : sortedColumns)
        {
            headers.add(column.getLangs().getLang().get(0).getTranslation());
			logger.info(column.getLangs().getLang().get(0).getTranslation());
            errors.put(column.getLangs().getLang().get(0).getTranslation(), 0);
        }
        String[] headerArray = new String[headers.size()];
        logger.info(StringUtil.stars());
        XlsRowFactory.header(sheet, rowNr, dateHeaderStyle, headers.toArray(headerArray));rowNr++;
        
        // Create Content Rows
		String queryExpression = sheetDefinition.getQuery();
		if (logger.isTraceEnabled()) {logger.trace("Iterating to find " +queryExpression);}
		Iterator iterator = context.iteratePointers(queryExpression);
		logger.debug("Beginning iteration");
		int iterationCounter=0;
        while (iterator.hasNext())
        {
        	logger.info("Iteration: "+iterationCounter++);
        	
            Pointer pointerToItem = (Pointer)iterator.next();
			if (logger.isTraceEnabled()) {logger.trace("Got pointer: " +pointerToItem.getValue().getClass());}
			JXPathContext relativeContext = context.getRelativeContext(pointerToItem);
			
			for (int i = 0; i<sortedColumns.size(); i++)
            {
                XlsColumn columnDefinition = sortedColumns.get(i);
                CellStyle   style = cellStyles.get(columnDefinition.getColumn());
                String      type  = columnDefinition.getXlsTransformation().getDataClass();
				String columnId   = columnDefinition.getLangs().getLang().get(0).getTranslation();
				
				String expression = "";
				
				
				Boolean relative  = true;
				Boolean isJoin    = false;
				if (columnDefinition.getXlsTransformation().isSetBeanProperty())
				{
					expression = columnDefinition.getXlsTransformation().getBeanProperty();
					if (expression.startsWith("string-join")) {logger.info("Polyfill for XPath 2 string-join activated for " +expression);isJoin = true;}
				}
				else if (columnDefinition.getXlsTransformation().isSetXPath())
				{
					expression = columnDefinition.getXlsTransformation().getXPath();
					relative   = false;
				}
				
				if (sheetDefinition.isSetPrimaryKey())
				{
					String primaryKey = relativeContext.getValue(sheetDefinition.getPrimaryKey()).toString();
					expression = expression.replace("@@@primaryKey@@@", primaryKey);
				}
				
                
                try {
                    Object  value;
                    //String xpath  = query +"[" +row +"]/" + expression;
                    if (logger.isTraceEnabled()) {logger.trace("Using XPath expression: " +expression);}
					if (relative && !isJoin)
					{
						value = relativeContext.getValue(expression);
						if (logger.isTraceEnabled()) {logger.trace("... in relative context.");}
					}
					else if (!relative && !isJoin)
					{
						value = context.getValue(expression);
						Iterator completeIterator = context.iterate(expression);
						if (completeIterator.hasNext()) { 
							value = completeIterator.next();
							if(logger.isTraceEnabled()) {logger.trace("Found " +value.toString());
						}}
						else {if(logger.isTraceEnabled()) {logger.trace("Could not find " +expression);}}
						if (logger.isTraceEnabled()) {logger.trace("... in complete context.");}
					}
					else
					{
						List<String> subSet = new ArrayList<String>();
						String joinExpression = expression.substring(expression.indexOf("(")+1, expression.indexOf(","));
						Iterator joinIterator = relativeContext.iteratePointers(joinExpression);
						while (joinIterator.hasNext())
						{
							Pointer pointerToSubItem	= (Pointer)joinIterator.next();
							String subValue				= pointerToSubItem.getValue().toString();
							subValue					= StringUtils.trim(subValue);
							subSet.add(subValue);
						}
						value = StringUtils.join(subSet, ", ");
					}
                    if (logger.isTraceEnabled()) {logger.trace("Got Value " +value.toString());}
                    createCell(sheet, rowNr, i, value, type, style);

                } catch (Exception e)
                {
                //    Integer counter = errors.get(columnId);
                //   counter++;
                //    errors.put(columnId, counter);
					if (logger.isTraceEnabled()) {logger.trace("ERROR occured: " +e.getMessage());}
                }
            }
			
			rowNr++;
        }
        logger.info("Processed " +rowNr +" entries.");
        
        fixWidth(sortedColumns, sheet);
        for (String key : errors.keySet())
        {
            if (errors.get(key)>0)
            {
                logger.error(errors.get(key) +" cell creation errors in " +key);
            }
        }
    }

    public CellStyle getCellStyle(XlsColumn columnDefinition)
    {
		// Refer to POI standard styles at https://poi.apache.org/apidocs/org/apache/poi/ss/usermodel/BuiltinFormats.html
        CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontName("Arial");
		style.setFont(font);
		String dataClass = columnDefinition.getXlsTransformation().getDataClass();
		if (dataClass.equalsIgnoreCase("String"))
		{
			// BuiltIn Style:  0x31 "text" - Alias for "@"
			style.setDataFormat(createHelper.createDataFormat().getFormat("text"));
		} 
		if (dataClass.equalsIgnoreCase("Date"))
		{
			// Create styles
			style.setDataFormat(createHelper.createDataFormat().getFormat("dd.MM.yyyy"));
			if (columnDefinition.getXlsTransformation().isSetFormatPattern())
			{
				style.setDataFormat(createHelper.createDataFormat().getFormat(columnDefinition.getXlsTransformation().getFormatPattern()));
			}
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
		} 
		else if (dataClass.equalsIgnoreCase("Integer"))
		{
			// BuiltIn Style: 1, "0"
			style.setDataFormat((short) 1);
		} else if (columnDefinition.getXlsTransformation().isSetFormatPattern())
		{
			// Use custom style
			style.setDataFormat(createHelper.createDataFormat().getFormat(columnDefinition.getXlsTransformation().getFormatPattern()));
		}
        return style;
    }

    public Sheet createCell(Sheet sheet, Integer rowNr, Integer cellNr, Object value, String type, CellStyle style)
    {
		if (logger.isTraceEnabled()) {logger.trace("Cell at " +rowNr +"/" +cellNr +" Value: " +value.toString());}
        Row     row = sheet.getRow(rowNr) != null ? sheet.getRow(rowNr) : sheet.createRow(rowNr);
        Cell   cell = row.createCell(cellNr);
        if      (type.equals("String"))                 {cell.setCellValue(value.toString());}
        else if (type.equals("Date"))   {
                                                    XMLGregorianCalendar calendar = (XMLGregorianCalendar) value;
                                                    cell.setCellValue(new Date(calendar.toGregorianCalendar().getTimeInMillis()));
                                                    cell.setCellStyle(style);
                                                }
        else if (type.equals("Number"))   {
                                                    Double number = (Double) value;
                                                    cell.setCellValue(number);
                                                    cell.setCellStyle(style);
                                                }
        else if (type.equals("Integer"))   {
		//	logger.info("Class " +value.toString());
												if (value.getClass().getSimpleName().equals("Long"))
												{
													Long l = (Long) value;
													cell.setCellValue(l.intValue());
												//	logger.info("Setting to Long " +l.intValue());
												}
												else if (value.getClass().getSimpleName().equals("int") || value.getClass().getSimpleName().equals("Integer"))
												{
													Integer i = (Integer) value;
													cell.setCellValue(i);
												//	logger.info("Setting to Integer " +i);
												}
												else
												{
                                                    cell.setCellValue(Integer.parseInt("" +value));
                                                }
		}
        else if (type.equals("Boolean"))   {
                                                    Boolean bool = (Boolean) value;
                                                    cell.setCellValue(bool);
                                                }
        return sheet;
    }

    public Sheet fixWidth(List<XlsColumn> columns, Sheet sheet)
    {
		
		for (int i = 0; i<columns.size(); i++)
            {
				String formatPattern = "";
				if (columns.get(i).getXlsTransformation().isSetFormatPattern())
				{
					formatPattern = columns.get(i).getXlsTransformation().getFormatPattern();
					logger.trace("Format Pattern " +formatPattern);
				}
				if (formatPattern.contains(" "))
				{
					sheet.setColumnWidth(i, MIN_WIDTH);
					logger.trace("Column " +i +" is to MID_WIDTH which is " +sheet.getColumnWidth(i));
				}
				else
				{
					sheet.autoSizeColumn(i);
					logger.trace("Column " +i +" is set of " +sheet.getColumnWidth(i));
				}
				
            }
        return sheet;
    }

    public ArrayList<XlsColumn> preProcessColumns(XlsSheet sheet, Sheet xlsSheet)
    {
        ArrayList<XlsColumn> columns = new ArrayList<XlsColumn>();
		Integer columnNr = 0;
		boolean hasExtraHeader = false;
		Row     headerRow = null;
        for (Object o : sheet.getContent())
        {
			if (o instanceof XlsColumn)
			{
				XlsColumn c = (XlsColumn) o;
				c.setColumn("" +columnNr);
				columns.add(c);
				cellStyles.put("" +columnNr, getCellStyle(c));
				columnNr++;
			}
			if (o instanceof XlsMultiColumn)
			{
				if (xlsSheet.getRow(rowNr) == null)
				{
					headerRow = xlsSheet.createRow(rowNr);
				}
				else
				{
					headerRow = xlsSheet.getRow(rowNr);
				}
				XlsMultiColumn c = (XlsMultiColumn) o;
				List<XlsColumn> list = createColumns(c, columnNr);
				columns.addAll(list);
				hasExtraHeader = true;
				
				Cell cell = headerRow.createCell(columnNr);
				cell.setCellStyle(dateHeaderStyle);
				cell.setCellValue(c.getLangs().getLang().get(0).getTranslation());
				for (int s = columnNr+1 ; s < columnNr + list.size() ; s++)
				{
					headerRow.createCell(s);
				}
				short columnMergeEnd	= (short) (columnNr+list.size()-1);
				logger.trace("Trying to create merged cells in row " +rowNr +" ranging from " +columnNr +"To " +columnMergeEnd);
				if (columnMergeEnd<=columnNr)
				{
				    logger.trace("No merging necessary or possible.");
				}
				else
				{
				    xlsSheet.addMergedRegion(new CellRangeAddress(rowNr, rowNr, columnNr, columnMergeEnd));
				}
				columnNr = columnNr + list.size();
			}
        }
		if (hasExtraHeader) {rowNr++;}
        return columns;
    }
	
	public List<XlsColumn> createColumns(XlsMultiColumn multiColumn, Integer startAt)
	{
		ArrayList<XlsColumn> list = new ArrayList<XlsColumn>();
		
		// Create Content Rows
		String queryExpression = multiColumn.getQuery();
		logger.info("Iterating to find the elements for the multi column " +queryExpression);
		
		Iterator iterator     = context.iteratePointers(queryExpression);
		logger.debug("Beginning iteration");
        
		// The XPath template to be filled with the ID from the multi column definition
		XlsColumn columnTemplate = multiColumn.getXlsColumn().get(0);
		String template = columnTemplate.getXlsTransformation().getBeanProperty();
		
		while (iterator.hasNext())
        {
			Pointer pointerToItem = (Pointer)iterator.next();
			logger.trace("Got pointer: " +pointerToItem.getValue().getClass());
			JXPathContext relativeContext = context.getRelativeContext(pointerToItem);
			
			Object  value = null;
			//String xpath  = query +"[" +row +"]/" + expression;

			String expressionId    = multiColumn.getId();
			String expressionLabel = multiColumn.getColumnLabel();

			logger.trace("Using XPath expression to evaluate the : " +expressionId);
			String id    = relativeContext.getValue(expressionId).toString();
			String label = relativeContext.getValue(expressionLabel).toString();
			logger.trace("Got Value " +id);

			String xPath = template.replace("@@@columnId@@@", id);
			logger.info(xPath);
			XlsColumn c = createColumn(startAt, xPath, label, columnTemplate);
			list.add(c);
			startAt++;
		}
		return list;
		
	}
		
	public XlsColumn createColumn(Integer columnNr, String xPath, String label, XlsColumn columnTemplate)
	{
		logger.trace("Creating Column at position " +columnNr + " with expression " +xPath + " and label " +label + " for language " +languageKey);
		XlsColumn c = new XlsColumn();

		XlsTransformation t = new XlsTransformation();
		t.setXPath(xPath);
		t.setFormatPattern(columnTemplate.getXlsTransformation().getFormatPattern());
		t.setDataClass(columnTemplate.getXlsTransformation().getDataClass());
		
		Langs langs = new Langs();
		Lang  lang  = new Lang();
		lang.setKey(languageKey);
		lang.setTranslation(label);
		langs.getLang().add(lang);
		
		c.setLangs(langs);
		c.setXlsTransformation(t);
		c.setColumn(columnNr +"");
		
		cellStyles.put("" +columnNr, getCellStyle(c));
		return c;
	}
	
    public Workbook getWb() {return wb;}
    public void setWb(Workbook wb) {this.wb = wb;}
}