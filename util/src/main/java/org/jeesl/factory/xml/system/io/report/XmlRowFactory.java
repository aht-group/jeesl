package org.jeesl.factory.xml.system.io.report;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.io.locale.status.XmlDataTypeFactory;
import org.jeesl.factory.xml.io.locale.status.XmlTypeFactory;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.io.report.col.JeeslReportCellType;
import org.jeesl.interfaces.model.io.report.data.JeeslReportQueryType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRowType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.style.JeeslReportAlignment;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.report.Queries;
import org.jeesl.model.xml.io.report.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlRowFactory <L extends JeeslLang,D extends JeeslDescription,
								GROUP extends JeeslReportColumnGroup<L,D,?,COLUMN,STYLE>,
								COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,?>,
								ROW extends JeeslReportRow<L,D,?,TEMPLATE,CDT,RT>,
								TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
								CELL extends JeeslReportCell<L,D,TEMPLATE>,
								STYLE extends JeeslReportStyle<L,D,ALIGNMENT>,
								ALIGNMENT extends JeeslReportAlignment<L,D,ALIGNMENT,?>,
								CDT extends JeeslReportCellType<L,D,CDT,?>,
								CW extends JeeslStatus<L,D,CW>,
								RT extends JeeslReportRowType<L,D,RT,?>
								>
{
	final static Logger logger = LoggerFactory.getLogger(XmlRowFactory.class);
	
	private Row q;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescriptions;
	private XmlTypeFactory<L,D,RT> xfType;
	private XmlDataTypeFactory<L,D,CDT> xfDataType;
	private XmlLayoutFactory<L,D,GROUP,COLUMN,ROW,STYLE,ALIGNMENT,CDT,CW> xfLayout;
	private XmlTemplateFactory<L,D,TEMPLATE,CELL> xfTemplate;
	
	public XmlRowFactory(String localeCode, Row q)
	{
		this.q=q;
		if(Objects.nonNull(q.getLangs())){xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(Objects.nonNull(q.getDescriptions())){xfDescriptions = new XmlDescriptionsFactory<D>(q.getDescriptions());}
		if(Objects.nonNull(q.getType())) {xfType = new XmlTypeFactory<>(localeCode,q.getType());}
		if(Objects.nonNull(q.getDataType())) {xfDataType = new XmlDataTypeFactory<>(localeCode,q.getDataType());}
		if(Objects.nonNull(q.getLayout())) {xfLayout = new XmlLayoutFactory<>(localeCode,q.getLayout());}
		if(Objects.nonNull(q.getTemplate())) {xfTemplate = new XmlTemplateFactory<>(q.getTemplate());}
	}
	
	public Row build(ROW row)
	{
		Row xml = new Row();
		
		if(Objects.nonNull(q.getCode())) {xml.setCode(row.getCode());}
		if(Objects.nonNull(q.isVisible())) {xml.setVisible(row.isVisible());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(row.getPosition());}
		
		if(Objects.nonNull(q.getLangs())){xml.setLangs(xfLangs.getUtilsLangs(row.getName()));}
		if(Objects.nonNull(q.getDescriptions())){xml.setDescriptions(xfDescriptions.create(row.getDescription()));}
		if(Objects.nonNull(q.getType())) {xml.setType(xfType.build(row.getType()));} 
		if(ObjectUtils.allNotNull(q.getDataType(),row.getDataType())) {xml.setDataType(xfDataType.build(row.getDataType()));}
		if(ObjectUtils.allNotNull(q.getTemplate(),row.getTemplate())) {xml.setTemplate(xfTemplate.build(row.getTemplate()));}
		
		if(Objects.nonNull(q.getQueries())) {xml.setQueries(queries(row));}
		if(Objects.nonNull(q.getLayout())) {xml.setLayout(xfLayout.build(row));}
						
		return xml;
	}
	
	private Queries queries(ROW row)
	{
		Queries xml = XmlQueriesFactory.build();
		if(row.getQueryCell()!=null){xml.getQuery().add(XmlQueryFactory.build(JeeslReportQueryType.Row.cell, row.getQueryCell()));}
		return xml;
	}
}