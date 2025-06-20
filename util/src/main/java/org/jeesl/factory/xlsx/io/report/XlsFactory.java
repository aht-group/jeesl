package org.jeesl.factory.xlsx.io.report;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.exlp.util.io.StringUtil;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.factory.builder.io.IoReportFactoryBuilder;
import org.jeesl.factory.ejb.io.report.EjbIoReportColumnFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportColumnGroupFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportRowFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportSheetFactory;
import org.jeesl.factory.pojo.io.report.JeeslTreeFigureFactory;
import org.jeesl.factory.txt.system.io.report.TxtIoGroupFactory;
import org.jeesl.interfaces.controller.report.JeeslReport;
import org.jeesl.interfaces.controller.report.JeeslReportSelectorTransformation;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReportCategory;
import org.jeesl.interfaces.model.io.report.col.JeeslReportCellType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRowType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.setting.JeeslReportSetting;
import org.jeesl.interfaces.model.io.report.style.JeeslReportAlignment;
import org.jeesl.interfaces.model.io.report.style.JeeslReportColumnWidth;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportWorkbook;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLightScope;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.xml.module.finance.Figures;
import org.jeesl.model.xml.module.finance.Finance;
import org.jeesl.util.query.xpath.ReportXpath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;

public class XlsFactory <L extends JeeslLang,D extends JeeslDescription,
						CATEGORY extends JeeslIoReportCategory<L,D,CATEGORY,?>,
						REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
						IMPLEMENTATION extends JeeslStatus<L,D,IMPLEMENTATION>,
						WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
						SHEET extends JeeslReportSheet<L,D,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
						GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
						COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,TLS>,
						ROW extends JeeslReportRow<L,D,SHEET,TEMPLATE,CDT,RT>,
						TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
						CELL extends JeeslReportCell<L,D,TEMPLATE>,
						STYLE extends JeeslReportStyle<L,D,ALIGNMENT>,
						ALIGNMENT extends JeeslReportAlignment<L,D,ALIGNMENT,?>,
						CDT extends JeeslReportCellType<L,D,CDT,?>,
						CW extends JeeslReportColumnWidth<L,D,CW,?>,
						RT extends JeeslReportRowType<L,D,RT,?>,
						RCAT extends JeeslStatus<L,D,RCAT>,
						ENTITY extends EjbWithId,
						ATTRIBUTE extends EjbWithId,
						TL extends JeeslTrafficLight<L,D,TLS>,
						TLS extends JeeslTrafficLightScope<L,D,TLS,?>,
//						FILLING extends JeeslStatus<L,D,FILLING>,
						TRANSFORMATION extends JeeslStatus<L,D,TRANSFORMATION>>
{
	private final static Logger logger = LoggerFactory.getLogger(XlsFactory.class);
		
	private WORKBOOK ioWorkbook;
	
	private final IoReportFactoryBuilder<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS,?,TRANSFORMATION> fbReport;
	private final EjbIoReportColumnFactory<L,D,SHEET,GROUP,COLUMN,ROW,CELL,STYLE,CDT,CW,RT> efColumn;
	private final EjbIoReportRowFactory<L,D,SHEET,GROUP,ROW,TEMPLATE,STYLE,CDT,RT> efRow;
	private final EjbIoReportSheetFactory<L,D,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,STYLE> efSheet;
	
	private final XlsColumnFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS> xfColumn;
	
//	private final TxtIoColumnFactory<COLUMN> tfColumn;
	private final TxtIoGroupFactory<SHEET,GROUP> tfGroup;
	
    // Excel related objects
    public Font             headerFont;
    public CellStyle        dateHeaderStyle;
    public CellStyle        numberStyle;
    public CreationHelper   createHelper;
    
    // How many results are there for the given query
    public Integer   counter;
	
	private String localeCode;
        
    public Hashtable<String,CellStyle> cellStyles = new Hashtable<String, CellStyle>();
    public Hashtable<String,Integer> errors = new Hashtable<String, Integer>();
    
	
	public XlsFactory(String localeCode,
			final IoReportFactoryBuilder<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS,?,TRANSFORMATION> fbReport,
			WORKBOOK ioWorkbook)
    {
        this.localeCode = localeCode;
        this.ioWorkbook=ioWorkbook;
        this.fbReport=fbReport;
        efColumn = fbReport.column();
        efSheet = fbReport.sheet();
        efRow = fbReport.row();
//        tfColumn = fbReport.tfColumn(localeCode);
        tfGroup = fbReport.tfGroup(localeCode);
        
        xfColumn = fbReport.xlsColumn();
    }
	
	private void init(Workbook wb)
	{
		createHelper = wb.getCreationHelper();

        // Create a new font and alter it.
        Font font = wb.createFont();
        font.setItalic(true);
        font.setBold(true);
		font.setFontName("Arial");
		font.setFontHeight((short)(9*20));

        // Create styles
        dateHeaderStyle = wb.createCellStyle();
        dateHeaderStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy.MM"));
        dateHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
        dateHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dateHeaderStyle.setFont(font);

        numberStyle = wb.createCellStyle();
        numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("#.00\\ RWF"));
	}
	
	public void write(Object report, OutputStream os) throws IOException {write(null,report,os);}
	public void write(JeeslReport<REPORT> ioReport, Object report, OutputStream os) throws IOException
	{
		Map<SHEET,Boolean> mapSheetVisibilityToggle = null; 
		
	    SXSSFWorkbook wb = new SXSSFWorkbook(100);
	    this.init(wb);
	    
	    JXPathContext context = JXPathContext.newContext(report);
	    
		for(SHEET ioSheet : EjbIoReportSheetFactory.toListVisibleShets(ioWorkbook,mapSheetVisibilityToggle))
		{
			logger.info("Processing sheet: "+ioSheet.getCode());
			List<GROUP> groups = EjbIoReportColumnGroupFactory.toListVisibleGroups(ioSheet);
			List<COLUMN> columns = efColumn.toListVisibleColumns(ioSheet);
			List<ROW> rows = efRow.toListVisibleRows(ioSheet);
			
			Map<GROUP,List<String>> mapDynamicGroups = tfGroup.toContextKeys(ioSheet, context);
			
			XlsStyleFactory<GROUP,COLUMN,ROW,STYLE,ALIGNMENT,CDT> xfStyle = fbReport.xlsStyle(wb,groups,columns,rows);
			XlsCellFactory<GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW> xfCell = fbReport.xlsCell(localeCode,xfStyle);
			XlsRowFactory<WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT> xfRow = fbReport.xlsRow(localeCode,xfCell);
			
			MutableInt rowNr = new MutableInt(0);
			MutableInt sheetNr = new MutableInt(1);
			Sheet sheet = XlsSheetFactory.getSheet(wb,ioSheet.getName().get(localeCode).getLang());
			xfColumn.trackWidth(sheet, columns);
			
			for(ROW ioRow : rows)
			{
				logger.trace(ioRow.getPosition()+" "+ioRow.getName().get(localeCode).getLang());
				switch(JeeslReportRowType.Code.valueOf(ioRow.getType().getCode()))
				{
					case label: xfRow.label(sheet, rowNr, ioRow); break;
					case labelValue: xfRow.labelValue(sheet, rowNr, ioRow, context); break;
					case table: this.applyTable(ioReport,context,wb,ioSheet,sheet,sheetNr,ioRow,rowNr,columns,mapDynamicGroups,xfRow,xfCell); break;
					case template: applyTemplate(sheet,rowNr,ioSheet,ioRow,xfCell); break;
					default: break;
				}	
			}
			xfColumn.adjustWidth(sheet, columns);
			
			logger.trace(rowNr.getValue().toString());
			
//			applyHeader(sheet,ioSheet,report);
//			exportSheet(sheet,report,ioSheet,null);
//			applyFooter(sheet,report);
			rowNr.add(3);
		}
		wb.write(os);
		wb.dispose();
	}

	@SuppressWarnings("unchecked")
	private void applyTable(JeeslReport<REPORT> jeeslReport, JXPathContext context, SXSSFWorkbook wb,
							SHEET ioSheet, Sheet poiSheet, MutableInt sheetNr,
							ROW ioRow, MutableInt rowNr, List<COLUMN> columns,
							Map<GROUP,List<String>> mapDynamicGroups,
							XlsRowFactory<WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT> xfRow,
							XlsCellFactory<GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW> xfCell)
	{
		rowNr.add(ioRow.getOffsetRows());
		JeeslReportSetting.Implementation implementation = JeeslReportSetting.Implementation.valueOf(ioSheet.getImplementation().getCode());
		JeeslReportSetting.Transformation transformation = JeeslReportSetting.Transformation.none;
		
		if(jeeslReport instanceof JeeslReportSelectorTransformation)
		{
			JeeslReportSelectorTransformation<L,D,REPORT,TRANSFORMATION> jr = ((JeeslReportSelectorTransformation<L,D,REPORT,TRANSFORMATION>)jeeslReport);
			transformation = JeeslReportSetting.Transformation.valueOf(jr.getReportSettingTransformation().getCode());
		}
		logger.debug("Tranformation:"+transformation+" instanceof:"+(jeeslReport instanceof JeeslReportSelectorTransformation));
		
		Figures tree = null;
		Figures treeHeader = null;
		Figures transformationHeader = null;
		try
		{
			switch(implementation)
			{
				case tree: tree = (Figures)context.getValue(ioSheet.getQueryTable());
						   treeHeader = ReportXpath.getFigures(JeeslTreeFigureFactory.Type.tree, tree.getFigures());
						   if(transformation.equals(JeeslReportSetting.Transformation.last)){transformationHeader = ReportXpath.getFigures(JeeslTreeFigureFactory.Type.transformation, tree.getFigures());}
						   break;
				default: break;
			}
		}
		catch (ExlpXpathNotFoundException e) {e.printStackTrace();}

		switch(implementation)
		{
			case model: xfRow.header(poiSheet,rowNr,ioSheet,mapDynamicGroups,context);break;
			case flat: xfRow.header(poiSheet,rowNr,ioSheet,mapDynamicGroups,context);break;
			case tree: xfRow.headerTree(poiSheet,rowNr,ioSheet,treeHeader,transformation,transformationHeader);break;
		}
		
		switch(implementation)
		{
			case model: applyDomainTable(wb,context,ioSheet,poiSheet,sheetNr,rowNr,columns,xfCell); break;
			case flat: applyDomainTable(wb,context,ioSheet,poiSheet,sheetNr,rowNr,columns,xfCell); break;
			case tree: applyTreeTable(tree,treeHeader,poiSheet,rowNr,ioSheet,columns,xfCell,transformation);break;
		}

        if(efSheet.hasFooters(ioSheet))
        {
        	Row xlsFooter = poiSheet.createRow(rowNr.intValue());
        	logger.info(StringUtil.stars());
        	logger.info("Handling Footer");
        	MutableInt columnNr = new MutableInt(0);
        	for(COLUMN ioColumn : efColumn.toListVisibleColumns(ioSheet))
			{
				if(efColumn.hasFooter(ioColumn))
				{
					xfCell.footer(ioColumn,xlsFooter,columnNr,context);
				}
				else
				{
					columnNr.add(1);
				}
			}
        	rowNr.add(1);
        }
	}
	
	private void applyDomainTable(SXSSFWorkbook poiWb, JXPathContext context, 
			SHEET ioSheet, Sheet poiSheet, MutableInt sheetNr,
			MutableInt rowNr,
			List<COLUMN> ioColumns, XlsCellFactory<GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW> xfCell)
	{
		logger.info("Applying Domain Table for "+ioSheet.getQueryTable());
		
		@SuppressWarnings("unchecked")
		Iterator<Pointer> iterator = context.iteratePointers(ioSheet.getQueryTable());
		logger.debug("Beginning iteration");
        while (iterator.hasNext())
        {
        	Row xlsRow = poiSheet.createRow(rowNr.intValue());
        	
            Pointer pointer = iterator.next();
			if (logger.isTraceEnabled()) {logger.info("Got pointer: " +pointer.getValue().getClass());}
			JXPathContext relativeContext = context.getRelativeContext(pointer);
			
			MutableInt columnNr = new MutableInt(0);
			for(COLUMN ioColumn : ioColumns)
			{
//				logger.info(rowNr.getValue()+" "+tfColumn.position(ioColumn)+" "+columnNr.intValue()+" "+ioColumn.getGroup().getQueryColumns());
				
				if(ioColumn.getGroup().getQueryColumns()==null || ioColumn.getGroup().getQueryColumns().trim().isEmpty())
				{
					xfCell.cell(ioColumn,xlsRow,columnNr,relativeContext);
				}
				else
				{
					@SuppressWarnings({"unchecked"})
					Iterator<Object> dynamicIterator = context.iterate(ioColumn.getGroup().getQueryColumns().trim());
					while (dynamicIterator.hasNext())
			        {
			        	String s = dynamicIterator.next().toString();
			        	xfCell.cell(ioColumn,xlsRow,columnNr,relativeContext,s);
			        }					
				}
			}
			rowNr.add(1);
			
			if(rowNr.getValue()>1048500)
			{
				xfColumn.adjustWidth(poiSheet,ioColumns);
				poiSheet = XlsSheetFactory.getSheet(poiWb,ioSheet.getName().get(localeCode).getLang()+"."+sheetNr.incrementAndGet());
				xfColumn.trackWidth(poiSheet, ioColumns);
				rowNr.setValue(5);
			}
			
        }
        xfColumn.adjustWidth(poiSheet,ioColumns);
        logger.debug("Completed iteration");
	}
	
	private void applyTreeTable(Figures tree, Figures treeHeader, Sheet sheet, MutableInt rowNr, SHEET ioSheet, List<COLUMN> columns, XlsCellFactory<GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW> xfCell, JeeslReportSetting.Transformation transformation)
	{
		try
		{
			Figures data = ReportXpath.getFigures(JeeslTreeFigureFactory.Type.data, tree.getFigures());
			for(Figures f : data.getFigures())
			{
				applyTreeRow(0,treeHeader,sheet,rowNr,columns,xfCell,new ArrayList<String>(),f,transformation);
			}
			JaxbUtil.trace(data);
		}
		catch (ExlpXpathNotFoundException e) {e.printStackTrace();}
	}
	
	private void applyTreeRow(int level, Figures treeHeader, Sheet sheet, MutableInt rowNr, List<COLUMN> columns, XlsCellFactory<GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW> xfCell,
								List<String> parents, Figures f, JeeslReportSetting.Transformation transformation)
	{
		MutableInt columnNr = new MutableInt(0);
		columnNr.add(level);
		Row xlsRow = sheet.createRow(rowNr.intValue());
		boolean treeColumn=true;
		for(COLUMN ioColumn : columns)
		{
			if(treeColumn)
			{
				for(String p : parents)
				{
					xfCell.cell(ioColumn,xlsRow,columnNr,p);
				}
				xfCell.cell(ioColumn,xlsRow,columnNr.intValue(),f.getLabel());
				
				treeColumn=false;
				List<String> path = new ArrayList<String>(parents);
				path.add(f.getLabel());
				rowNr.add(1);
				for(Figures childs : f.getFigures())
				{
					applyTreeRow(level,treeHeader,sheet,rowNr,columns,xfCell,path,childs,transformation);
				}
				columnNr.add(treeHeader.getFigures().size()-level-parents.size());
			}
			else
			{
				if(ioColumn.getQueryCell().startsWith("d"))
				{
					int nr = Integer.valueOf(ioColumn.getQueryCell().substring(1));
					switch(transformation)
					{
						case none: xfCell.cell(ioColumn,xlsRow,columnNr,Double.valueOf(f.getFinance().get(nr-1).getValue()));break;
						case last: for(Finance f2 : f.getFinance().get(nr-1).getFinance())
								   {
										if(Objects.nonNull(f2.getValue())) {xfCell.cell(ioColumn,xlsRow,columnNr,f2.getValue());}
										else{columnNr.add(1);}
								   }break;
						
					}
					
				}
				else{logger.warn("NYI: "+ioColumn.getQueryCell());}
			}
		}
	}
	
	private void applyTemplate(Sheet sheet, MutableInt rowNr, SHEET ioSheet, ROW ioRow, XlsCellFactory<GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW> xfCell)
	{
		if(ioRow.getTemplate()!=null)
		{
			rowNr.add(ioRow.getOffsetRows());
			xfCell.build(sheet, rowNr, ioRow);
		}
	}
}