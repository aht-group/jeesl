package net.sf.ahtutils.report.excel;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.List;


import org.apache.commons.jxpath.JXPathContext;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ExcelTemplateFiller
{
	
    // Excel related objects
    public XSSFWorkbook         wb;
    public Font             headerFont;
    public CellStyle        dateHeaderStyle;
    public CellStyle        numberStyle; 
    public CreationHelper   createHelper;
    public JXPathContext	context;
    
    // The data
    public Object    report;
    
    // The query which entities are the subject of this report
    public String    query;
    
    // How many results are there for the given query
    public Integer   counter;
	
	// Current line while exporting
	private int      rowNr = 1;
	
	// Languge
	private String   languageKey;
        
    public Hashtable<String, CellStyle> cellStyles = new Hashtable<String, CellStyle>();
    public Hashtable<String, Integer> errors = new Hashtable<String, Integer>();
	
    final static Logger logger = LoggerFactory.getLogger(ExcelTemplateFiller.class);
	private int MIN_WIDTH = 5000;
	
    public ExcelTemplateFiller(InputStream templateFile) throws Exception
    {
		// Create a new Excel Workbook and a POI Helper Object
        wb = new XSSFWorkbook(templateFile);
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

       
    }
    
    public void fill( List<FillingInstruction> instructions)
    {
         // Now lets apply the instructions on the template
        for (FillingInstruction instruction : instructions)
        {
                wb = instruction.fill(wb);
        }
    }

    public XSSFWorkbook getWb() {return wb;}
    public void setWb(XSSFWorkbook wb) {this.wb = wb;}
}