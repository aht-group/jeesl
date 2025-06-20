package org.jeesl.factory.xml.system.io.report;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.io.locale.status.XmlTypeFactory;
import org.jeesl.factory.xml.system.symbol.XmlColorFactory;
import org.jeesl.interfaces.model.io.report.col.JeeslReportCellType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.style.JeeslReportAlignment;
import org.jeesl.interfaces.model.io.report.style.JeeslReportLayout;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.report.Layout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlLayoutFactory<L extends JeeslLang,D extends JeeslDescription,
								GROUP extends JeeslReportColumnGroup<L,D,?,COLUMN,STYLE>,
								COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,?>,
								ROW extends JeeslReportRow<L,D,?,?,CDT,?>,
								
								
								STYLE extends JeeslReportStyle<L,D,ALIGNMENT>,
								ALIGNMENT extends JeeslReportAlignment<L,D,ALIGNMENT,?>,
								CDT extends JeeslReportCellType<L,D,CDT,?>,
								CW extends JeeslStatus<L,D,CW>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlLayoutFactory.class);
	
	private Layout q;
	
	private XmlTypeFactory<L,D,CW> xfType;
	private XmlStylesFactory<L,D,GROUP,COLUMN,ROW,STYLE,ALIGNMENT,CDT,CW> xfStyles;
	private XmlFontFactory<STYLE> xfFont;
	
	public XmlLayoutFactory(String localeCode, Layout q)
	{
		this.q=q;
		if(Objects.nonNull(q.getStyles())) {xfStyles = new XmlStylesFactory<>(localeCode,q.getStyles());}
		if(Objects.nonNull(q.getFont())) {xfFont = new XmlFontFactory<>(q.getFont());}
		if(ObjectUtils.isNotEmpty(q.getSize())) {xfType = new XmlTypeFactory<>(localeCode,q.getSize().get(0).getType());}
	}
	
	public Layout build(ROW row)
	{
		Layout xml = build();
		if(Objects.nonNull(q.getOffset())) {xml.setOffset(XmlOffsetFactory.build(row.getOffsetRows(), row.getOffsetColumns()));}
		return xml;
	}
	
	public Layout build(COLUMN column)
	{
		Layout xml = build();
		if(ObjectUtils.isNotEmpty(q.getSize()))
		{
			if(column.getColumWidth()!=null)
			{
				xml.getSize().add(XmlSizeFactory.build(JeeslReportLayout.Code.columnWidth,
						xfType.build(column.getColumWidth()),
						column.getColumSize()));
			}
		}
		if(Objects.nonNull(q.getStyles())) {xml.setStyles(xfStyles.build(column));}
		return xml;
	}
	
	public Layout build(GROUP group)
	{
		Layout xml = build();
		if(Objects.nonNull(q.getStyles())) {xml.setStyles(xfStyles.build(group));}
		return xml;
	}
	
	public Layout layout(STYLE style)
	{
		Layout xml = XmlLayoutFactory.build();
		
		xml.getColor().add(XmlColorFactory.build("background", style.getColorBackground()));
		if(Objects.nonNull(q.getFont())) {xml.setFont(xfFont.build(style));}
		
		return xml;
	}
	
	public static Layout build()
	{
		Layout xml = new Layout();						
		return xml;
	}
}