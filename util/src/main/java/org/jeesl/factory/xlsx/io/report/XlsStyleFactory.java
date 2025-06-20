package org.jeesl.factory.xlsx.io.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeesl.factory.builder.io.IoReportFactoryBuilder;
import org.jeesl.factory.ejb.io.report.EjbIoReportColumnFactory;
import org.jeesl.factory.txt.system.io.report.TxtIoColumnFactory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.report.col.JeeslReportCellType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.style.JeeslReportAlignment;
import org.jeesl.interfaces.model.io.report.style.JeeslReportLayout;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XlsStyleFactory<GROUP extends JeeslReportColumnGroup<?,?,?,COLUMN,STYLE>,
								COLUMN extends JeeslReportColumn<?,?,GROUP,STYLE,CDT,?,?>,
								ROW extends JeeslReportRow<?,?,?,?,CDT,?>,
								STYLE extends JeeslReportStyle<?,?,ALIGNMENT>,
								ALIGNMENT extends JeeslReportAlignment<?,?,ALIGNMENT,?>,
								CDT extends JeeslReportCellType<?,?,CDT,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XlsStyleFactory.class);
	
	private Map<STYLE,CellStyle>	mapHeader;
	private Map<COLUMN,CellStyle>	mapHeaderStyles;
	private Map<COLUMN,CellStyle>	mapCell;
	private Map<ROW,CellStyle>		mapRow;
	
	private Map<COLUMN,JeeslReportLayout.Data>	mapCellDataType;
	private Map<ROW,JeeslReportLayout.Data>		mapRowDataType;
	
	// Default Styles Properties
	private CellStyle styleFallback;	public CellStyle getStyleFallback()		{return styleFallback;}
	private CellStyle styleLabelCenter; public CellStyle getStyleLabelCenter()	{return styleLabelCenter;}
	private CellStyle styleLabelLeft;	public CellStyle getStyleLabelLeft()	{return styleLabelLeft;}
	private Font	  font;
	
	// Factories
	private TxtIoColumnFactory<COLUMN> tfColumn;
	private final EjbIoReportColumnFactory<?,?,?,GROUP,COLUMN,ROW,?,STYLE,CDT,?,?> efColumn;
	
	public XlsStyleFactory(final IoReportFactoryBuilder<?,?,?,?,?,?,?,GROUP,COLUMN,ROW,?,?,STYLE,ALIGNMENT,CDT,?,?,?,?,?,?,?,?,?> fbReport,
							Workbook xlsWorkbook, List<GROUP> ioGroups, List<COLUMN> ioColumns, List<ROW> ioRows)
	{
		efColumn			= fbReport.column();
		tfColumn			= new TxtIoColumnFactory<COLUMN>("en");
		
		// Prepare the maps for caching the styles
		mapHeader		= new HashMap<>();
		mapHeaderStyles	= new HashMap<>();
		mapCell			= new HashMap<>();
		mapRow			= new HashMap<>();
		mapCellDataType = new HashMap<>();
		mapRowDataType	= new HashMap<>();
		
		// Prepare the default styles and cache Maps
        this.setupFallbackStyles(xlsWorkbook);
		
		for(ROW r : ioRows)
		{
			mapRow.put(r, createCellStyleFromDefinition(xlsWorkbook, null, r.getDataType()));
			mapRowDataType.put(r,toDataTypeEnum(efColumn.toRowDataType(r)));
		}
		for(GROUP g : ioGroups)
		{
			if(!mapHeader.containsKey(g.getStyleHeader())){mapHeader.put(g.getStyleHeader(), createCellStyleFromDefinition(xlsWorkbook,g.getStyleHeader(),null));}
		}
		for(COLUMN c : ioColumns)
		{
			CDT cdt = efColumn.toCellDataType(c);
			mapCell.put(c, createCellStyleFromDefinition(xlsWorkbook, c.getStyleCell(), cdt));
			if(!mapHeaderStyles.containsKey(c)){mapHeaderStyles.put(c, createCellStyleFromDefinition(xlsWorkbook,c.getStyleHeader(),null));}
			mapCellDataType.put(c,toDataTypeEnum(cdt));
			logger.trace(tfColumn.position(c));
		}
	}
	
	private JeeslReportLayout.Data toDataTypeEnum(CDT cdt)
	{
		if(cdt==null){return JeeslReportLayout.Data.string;}
		
		if(cdt.getCode().startsWith("text")){return JeeslReportLayout.Data.string;}
		else if(cdt.getCode().startsWith("numberDouble")){return JeeslReportLayout.Data.dble;}
		else if(cdt.getCode().startsWith("numberInteger")){return JeeslReportLayout.Data.intgr;}
		else if(cdt.getCode().startsWith("numberLong")){return JeeslReportLayout.Data.lng;}
		else if(cdt.getCode().startsWith("date")){return JeeslReportLayout.Data.dte;}
		else if(cdt.getCode().startsWith("bool")){return JeeslReportLayout.Data.bool;}
		else
		{
			return JeeslReportLayout.Data.string;
		}	
	}
	
	public CellStyle get(JeeslReportLayout.Style type, GROUP group)
	{
		switch(type)
		{
			case header	: return mapHeader.get(group.getStyleHeader());
			case cell	: return styleFallback;
			default		: return styleFallback;
		}
	}
	
	public JeeslReportLayout.Data getDataType(COLUMN column){return mapCellDataType.get(column);}
	public JeeslReportLayout.Data getDataType(ROW row)		{return mapRowDataType.get(row);}
	
	/**
	* Request the Apache POI CellStyle for the given JEESL Column's and Type (e.g. header, cell, footer)
	* @param type The JEESL Style Definition type e.g. header, cell, footer
	* @param column The JEESL Column definition
	* @return The cached CellStyle or if not configured the default Style (Arial, 11pt) 
	*/
	public CellStyle get(JeeslReportLayout.Style type, COLUMN column)
	{
		switch(type)
		{
			case header:	return mapHeaderStyles.containsKey(column)	? mapHeaderStyles.get(column)			: styleLabelCenter;
			case cell:		return mapCell.containsKey(column)			? mapCell.get(column)					: styleFallback;
			default:		return styleFallback;
		}
	}
	
	/**
	* Request the Apache POI CellStyle for the given JEESL Column's Cell Style
	* @param column The JEESL Column definition
	* @return The cached CellStyle or if not configured the default Style (Arial, 11pt) 
	*/
	public CellStyle get(COLUMN column)	{return mapCell.containsKey(column)	? mapCell.get(column)	: styleFallback;}
	
	/**
	* Request the Apache POI CellStyle for the given JEESL Row
	* @param row The JEESL Row definition
	* @return The cached CellStyle or if not configured the default Style (Arial, 11pt) 
	*/
	public CellStyle get(ROW row)		{return mapRow.containsKey(row)		? mapRow.get(row)		: styleFallback;}
	
	
	public static CellStyle unusedlabel(Workbook workbook, Font font)
	{
		CellStyle style = workbook.createCellStyle();
//		style.setDataFormat(createHelper.createDataFormat().getFormat("yyyy.MM"));
		style.setAlignment(HorizontalAlignment.LEFT);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setFont(font);
		return style;
	}
	
	public static CellStyle unusedfallback(Workbook workbook)
	{
		CellStyle style = workbook.createCellStyle();
//		style.setDataFormat(createHelper.createDataFormat().getFormat("yyyy.MM"));
		style.setAlignment(HorizontalAlignment.LEFT);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		return style;
	}
	
	public static String transformJavaToPoiPattern(String pattern)
	{
		return pattern.replaceAll("#","0");
	}
	
	/**
	* Creates Apache POI CellStyle for the given JEESL definition of Style and DataType
	* This includes the data type (e.g. text or date incl. format) and fonts
	* @param xlsWorkbook The Apache POI workbook
	* @param column The JEESL Style definition
	*/
	private CellStyle createCellStyleFromDefinition(Workbook workbook, STYLE styleDefinition, CDT cellDataFormatDefinition)
	{
		// Create the CellStyle Excel POI object
		CellStyle style = workbook.createCellStyle();
		
		// Identify the data type configured in the JEESL Column (text, number, ..) and set the POI format accordingly
		// StartsWith is used for being open for future use of IO Module with e.g. textLang types
		if(cellDataFormatDefinition!=null)
        {
        	if(cellDataFormatDefinition.getCode().startsWith(JeeslRevisionAttribute.Type.text.toString()))
        	{
        		style.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("text"));
        	}
        	else if(cellDataFormatDefinition.getCode().startsWith(JeeslRevisionAttribute.Type.number.toString()))
        	{
        		logger.trace("Creating Number "+cellDataFormatDefinition.getSymbol());
        		style.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat(transformJavaToPoiPattern(cellDataFormatDefinition.getSymbol())));
        	}
        	else if(cellDataFormatDefinition.getCode().startsWith(JeeslRevisionAttribute.Type.date.toString()))
        	{
        		logger.trace("Creating Date "+cellDataFormatDefinition.getSymbol());
        		style.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat(cellDataFormatDefinition.getSymbol()));
        	}
        }
		
		// Check, if the Style is given and has Font information configured and set them accordingly
		// If only a size is given, Arial is taken as standard font
		if (styleDefinition!=null)
		{
			Font font = workbook.createFont();
			
			// Set standard name and size in case not configured, otherwise Style in Excel is corrupt
			font.setFontName("Arial");
			font.setFontHeightInPoints((short)11);
			
			boolean configured = false;
			if(styleDefinition.isFontItalic())		{font.setItalic(true);}
			if(styleDefinition.isFontBold())		{font.setBold(true);}
			if (styleDefinition.getFont()!=null)	{font.setFontName(styleDefinition.getFont()); configured=true;}
			if (styleDefinition.getSizeFont()>0)	{font.setFontHeightInPoints((short)styleDefinition.getSizeFont()); configured=true;}
			if (configured)
			{	
				style.setFont(font);
				logger.trace("Added font as configured (" +font.getFontName() +" - " +font.getFontHeightInPoints() +" pt)");
			}
		}
		
		return style;
	}
	
	// *************************************************
	// **************** Fallback Styles ****************
	// *************************************************
	private void setupFallbackStyles(Workbook xlsWorkbook) 
	{
		font = xlsWorkbook.createFont();
        font.setFontName("Arial");
		font.setFontHeightInPoints((short)(11));
		
        Font fontItalicBold = xlsWorkbook.createFont();
        fontItalicBold.setItalic(true);
        fontItalicBold.setBold(true);
		fontItalicBold.setFontName(font.getFontName());
		fontItalicBold.setFontHeightInPoints(font.getFontHeightInPoints());
		
		styleFallback = xlsWorkbook.createCellStyle();
        styleFallback.setAlignment(HorizontalAlignment.LEFT);
        styleFallback.setVerticalAlignment(VerticalAlignment.CENTER);
		styleFallback.setFont(font);
		
        styleLabelCenter = xlsWorkbook.createCellStyle();
        styleLabelCenter.setAlignment(HorizontalAlignment.CENTER);
        styleLabelCenter.setVerticalAlignment(VerticalAlignment.CENTER);
        styleLabelCenter.setFont(fontItalicBold);
        
        styleLabelLeft = xlsWorkbook.createCellStyle();
        styleLabelLeft.setAlignment(HorizontalAlignment.LEFT);
        styleLabelLeft.setVerticalAlignment(VerticalAlignment.CENTER);
        styleLabelLeft.setFont(fontItalicBold);
	}
}