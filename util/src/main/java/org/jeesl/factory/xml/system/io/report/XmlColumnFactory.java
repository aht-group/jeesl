package org.jeesl.factory.xml.system.io.report;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.io.locale.status.XmlDataTypeFactory;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.io.report.col.JeeslReportCellType;
import org.jeesl.interfaces.model.io.report.data.JeeslReportQueryType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.style.JeeslReportAlignment;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.report.Queries;
import org.jeesl.model.xml.io.report.XlsColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlColumnFactory <L extends JeeslLang,D extends JeeslDescription,

								GROUP extends JeeslReportColumnGroup<L,D,?,COLUMN,STYLE>,
								COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,?>,
								ROW extends JeeslReportRow<L,D,?,?,CDT,?>,
								
								CELL extends JeeslReportCell<L,D,?>,
								
								STYLE extends JeeslReportStyle<L,D,ALIGNMENT>,
								ALIGNMENT extends JeeslReportAlignment<L,D,ALIGNMENT,?>,
								CDT extends JeeslReportCellType<L,D,CDT,?>,
								CW extends JeeslStatus<L,D,CW>
								>
{
	final static Logger logger = LoggerFactory.getLogger(XmlColumnFactory.class);
	
	private XlsColumn q;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescriptions;
	private XmlDataTypeFactory<L,D,CDT> xfDataType;
	private XmlLayoutFactory<L,D,GROUP,COLUMN,ROW,STYLE,ALIGNMENT,CDT,CW> xfLayout;
	
	public XmlColumnFactory(String localeCode, XlsColumn q)
	{
		this.q=q;
		if(Objects.nonNull(q.getLangs())){xfLangs = new XmlLangsFactory<>(q.getLangs());}
		if(Objects.nonNull(q.getDescriptions())){xfDescriptions = new XmlDescriptionsFactory<>(q.getDescriptions());}
		if(Objects.nonNull(q.getDataType())) {xfDataType = new XmlDataTypeFactory<>(localeCode,q.getDataType());}
		if(Objects.nonNull(q.getLayout())) {xfLayout = new XmlLayoutFactory<>(localeCode,q.getLayout());}
	}
	
	public XlsColumn build(COLUMN column)
	{
		XlsColumn xml = new XlsColumn();
		
		if(Objects.nonNull(q.getCode())) {xml.setCode(column.getCode());}
		if(Objects.nonNull(q.isVisible())) {xml.setVisible(column.isVisible());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(column.getPosition());}
		
		if(Objects.nonNull(q.isShowLabel())) {xml.setShowLabel(column.getShowLabel());}
		if(Objects.nonNull(q.isShowWeb())) {xml.setShowWeb(column.getShowWeb());}
		
		if(ObjectUtils.allNotNull(q.getDataType(),column.getDataType())) {xml.setDataType(xfDataType.build(column.getDataType()));}
		
		if(Objects.nonNull(q.getLangs())){xml.setLangs(xfLangs.getUtilsLangs(column.getName()));}
		if(Objects.nonNull(q.getDescriptions())){xml.setDescriptions(xfDescriptions.create(column.getDescription()));}
		
		if(Objects.nonNull(q.getQueries())) {xml.setQueries(queries(column));}
		if(Objects.nonNull(q.getLayout())) {xml.setLayout(xfLayout.build(column));}
						
		return xml;
	}
	
	private Queries queries(COLUMN column)
	{
		Queries xml = XmlQueriesFactory.build();
		if(column.getQueryHeader()!=null){xml.getQuery().add(XmlQueryFactory.build(JeeslReportQueryType.Column.header, column.getQueryHeader()));}
		if(column.getQueryCell()!=null){xml.getQuery().add(XmlQueryFactory.build(JeeslReportQueryType.Column.cell, column.getQueryCell()));}
		if(column.getQueryFooter()!=null){xml.getQuery().add(XmlQueryFactory.build(JeeslReportQueryType.Column.footer, column.getQueryFooter()));}
		return xml;
	}
}