package org.jeesl.factory.xml.system.io.report;

import org.jeesl.factory.xml.system.status.XmlTypeFactory;
import org.jeesl.factory.xml.system.symbol.XmlColorFactory;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReportCategory;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.row.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.style.JeeslReportLayout;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportWorkbook;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.xml.io.report.Layout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlLayoutFactory<L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslIoReportCategory<L,D,CATEGORY,?>,
								REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
								IMPLEMENTATION extends JeeslStatus<L,D,IMPLEMENTATION>,
								WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
								SHEET extends JeeslReportSheet<L,D,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
								GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
								COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,?>,
								ROW extends JeeslReportRow<L,D,SHEET,TEMPLATE,CDT,?>,
								TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
								CELL extends JeeslReportCell<L,D,TEMPLATE>,
								STYLE extends JeeslReportStyle<L,D>,CDT extends JeeslStatus<L,D,CDT>,
								CW extends JeeslStatus<L,D,CW>,
								
								ENTITY extends EjbWithId,
								ATTRIBUTE extends EjbWithId>
{
	final static Logger logger = LoggerFactory.getLogger(XmlLayoutFactory.class);
	
	private Layout q;
	
	private XmlTypeFactory<L,D,CW> xfType;
	private XmlStylesFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,ENTITY,ATTRIBUTE> xfStyles;
	private XmlFontFactory<STYLE> xfFont;
	
	public XmlLayoutFactory(String localeCode, Layout q)
	{
		this.q=q;
		if(q.isSetSize()){xfType = new XmlTypeFactory<>(localeCode,q.getSize().get(0).getType());}
		if(q.isSetStyles()){xfStyles = new XmlStylesFactory<>(localeCode,q.getStyles());}
		if(q.isSetFont()){xfFont = new XmlFontFactory<>(q.getFont());}
	}
	
	public Layout build(ROW row)
	{
		Layout xml = build();
		if(q.isSetOffset()){xml.setOffset(XmlOffsetFactory.build(row.getOffsetRows(), row.getOffsetColumns()));}
		return xml;
	}
	
	public Layout build(COLUMN column)
	{
		Layout xml = build();
		if(q.isSetSize())
		{
			if(column.getColumWidth()!=null)
			{
				xml.getSize().add(XmlSizeFactory.build(JeeslReportLayout.Code.columnWidth,
						xfType.build(column.getColumWidth()),
						column.getColumSize()));
			}
		}
		if(q.isSetStyles()){xml.setStyles(xfStyles.build(column));}
		return xml;
	}
	
	public Layout build(GROUP group)
	{
		Layout xml = build();
		if(q.isSetStyles()){xml.setStyles(xfStyles.build(group));}
		return xml;
	}
	
	public Layout layout(STYLE style)
	{
		Layout xml = XmlLayoutFactory.build();
		
		xml.getColor().add(XmlColorFactory.build("background", style.getColorBackground()));
		if(q.isSetFont()){xml.setFont(xfFont.build(style));}
		
		return xml;
	}
	
	public static Layout build()
	{
		Layout xml = new Layout();						
		return xml;
	}
}