package org.jeesl.factory.xml.system.io.report;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
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
import org.jeesl.model.xml.io.report.Style;
import org.jeesl.model.xml.io.report.Styles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlStylesFactory <L extends JeeslLang,D extends JeeslDescription,
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
								ATTRIBUTE extends EjbWithId
								>
{
	final static Logger logger = LoggerFactory.getLogger(XmlStylesFactory.class);
	
	private Styles q;
	
	private XmlStyleFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,ENTITY,ATTRIBUTE> xfStyle;
	
	public XmlStylesFactory(String localeCode, Styles q)
	{
		this.q = q;
		if(ObjectUtils.isNotEmpty(q.getStyle())) {xfStyle = new XmlStyleFactory<>(localeCode,q.getStyle().get(0));}
	}
	
	public Styles build(GROUP group)
	{
		Styles xml = build();
		
		if(ObjectUtils.isNotEmpty(q.getStyle()))
		{
			if(group.getStyleHeader()!=null){xml.getStyle().add(xfStyle.build(JeeslReportLayout.Style.header, group.getStyleHeader()));}
		}
		
		return xml;
	}
	
	public Styles build(COLUMN column)
	{
		Styles xml = build();
		
		if(ObjectUtils.isNotEmpty(q.getStyle()))
		{
			if(column.getStyleHeader()!=null){xml.getStyle().add(xfStyle.build(JeeslReportLayout.Style.header, column.getStyleHeader()));}
			if(column.getStyleCell()!=null){xml.getStyle().add(xfStyle.build(JeeslReportLayout.Style.cell, column.getStyleCell()));}
			if(column.getStyleFooter()!=null){xml.getStyle().add(xfStyle.build(JeeslReportLayout.Style.footer, column.getStyleFooter()));}
		}
		
		return xml;
	}
	
	public static Styles build()
	{
		Styles xml = new Styles();
		return xml;
	}
	
	public static Styles build(Style style)
	{
		Styles xml = build();
		xml.getStyle().add(style);
		return xml;
	}
}