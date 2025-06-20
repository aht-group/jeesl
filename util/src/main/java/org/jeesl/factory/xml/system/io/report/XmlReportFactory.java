package org.jeesl.factory.xml.system.io.report;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.io.locale.status.XmlCategoryFactory;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReportCategory;
import org.jeesl.interfaces.model.io.report.col.JeeslReportCellType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRowType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportTemplate;
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
import org.jeesl.model.xml.io.report.Report;
import org.jeesl.model.xml.module.ts.Ts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.aht.Query;

public class XmlReportFactory <L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslIoReportCategory<L,D,CATEGORY,?>,
								REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
								IMPLEMENTATION extends JeeslStatus<L,D,IMPLEMENTATION>,
								WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
								SHEET extends JeeslReportSheet<L,D,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
								GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
								COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,?>,
								ROW extends JeeslReportRow<L,D,SHEET,TEMPLATE,CDT,RT>,
								TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
								CELL extends JeeslReportCell<L,D,TEMPLATE>,
								STYLE extends JeeslReportStyle<L,D,ALIGNMENT>,
								ALIGNMENT extends JeeslReportAlignment<L,D,ALIGNMENT,?>,
								CDT extends JeeslReportCellType<L,D,CDT,?>,
								CW extends JeeslReportColumnWidth<L,D,CW,?>,
								RT extends JeeslReportRowType<L,D,RT,?>
								>
{
	final static Logger logger = LoggerFactory.getLogger(XmlReportFactory.class);
	
	private Report q;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescriptions;
	private XmlCategoryFactory<L,D,CATEGORY> xfCategory;
	private XmlWorkbookFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT> xfWorkbook;

	public XmlReportFactory(Query q){this(q.getLang(), q.getReport());}
	public XmlReportFactory(String localeCode, Report q)
	{
		this.q=q;
		if(Objects.nonNull(q.getLangs())){xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(Objects.nonNull(q.getDescriptions())){xfDescriptions = new XmlDescriptionsFactory<D>(q.getDescriptions());}
		if(Objects.nonNull(q.getCategory())) {xfCategory = new XmlCategoryFactory<>(localeCode,q.getCategory());}
		if(Objects.nonNull(q.getXlsWorkbook())) {xfWorkbook = new XmlWorkbookFactory<>(localeCode,q.getXlsWorkbook());}
		
	}
	
	public Report build(REPORT report)
	{
		Report xml = build();
		
//		if(Objects.nonNull(q.getId())) {xml.setId(ejb.getId());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(report.getCode());}
		if(Objects.nonNull(q.isVisible())) {xml.setVisible(report.isVisible());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(report.getPosition());}
		if(Objects.nonNull(q.getXmlExample())) {xml.setXmlExample(report.getXmlExample());}
		
		if(Objects.nonNull(q.getCategory())) {xml.setCategory(xfCategory.build(report.getCategory()));}
		
		if(Objects.nonNull(q.getLangs())){xml.setLangs(xfLangs.getUtilsLangs(report.getName()));}
		if(Objects.nonNull(q.getDescriptions())){xml.setDescriptions(xfDescriptions.create(report.getDescription()));}
		
		if(ObjectUtils.allNotNull(q.getXlsWorkbook(),report.getWorkbook())){xml.setXlsWorkbook(xfWorkbook.build(report.getWorkbook()));}
		
		return xml;
	}
	
	public static Report build(){return new Report();}
	public static Report build(Ts ts)
	{
		Report xml = build();
		xml.setTs(ts);
		return xml;
	}
}