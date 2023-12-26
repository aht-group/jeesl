package org.jeesl.factory.xml.system.io.report;

import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReportCategory;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.row.JeeslReportTemplate;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlStyleFactory <L extends JeeslLang,D extends JeeslDescription,
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
								STYLE extends JeeslReportStyle<L,D>,
								CDT extends JeeslStatus<L,D,CDT>,
								CW extends JeeslStatus<L,D,CW>,
								ENTITY extends EjbWithId,
								ATTRIBUTE extends EjbWithId
								>
{
	final static Logger logger = LoggerFactory.getLogger(XmlStyleFactory.class);
	
	private Style q;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescriptions;
	private XmlLayoutFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,ENTITY,ATTRIBUTE> xfLayout;
	
	public XmlStyleFactory(Style q){this(null,q);}
	public XmlStyleFactory(String localeCode, Style q)
	{
		this.q=q;
		if(Objects.nonNull(q.getLangs())){xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(Objects.nonNull(q.getDescriptions())){xfDescriptions = new XmlDescriptionsFactory<D>(q.getDescriptions());}
		if(Objects.nonNull(q.getLayout())) {xfLayout = new XmlLayoutFactory<>(localeCode,q.getLayout());}
	}
	
	
	public Style build(STYLE style){return build(null,style);}
	public <E extends Enum<E>> Style build(E type, STYLE style)
	{
		Style xml = XmlStyleFactory.build();
		
		if(type!=null){xml.setType(type.toString());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(style.getCode());}
		if(Objects.nonNull(q.isVisible())) {xml.setVisible(style.isVisible());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(style.getPosition());}
		
		if(Objects.nonNull(q.getLangs())){xml.setLangs(xfLangs.getUtilsLangs(style.getName()));}
		if(Objects.nonNull(q.getDescriptions())){xml.setDescriptions(xfDescriptions.create(style.getDescription()));}
		
		if(Objects.nonNull(q.getLayout())) {xml.setLayout(xfLayout.layout(style));}
		
		return xml;
	}
	
	public static Style build(String code)
	{
		Style xml = build();
		xml.setCode(code);
		return xml;
	}
	public static Style build()
	{
		Style xml = new Style();
		return xml;
	}
}