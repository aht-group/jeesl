package org.jeesl.factory.xml.system.io.report;

import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReportCategory;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRowType;
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
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLightScope;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.xml.io.report.ColumnGroup;
import org.jeesl.util.comparator.ejb.system.io.report.IoReportColumnComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlColumnGroupFactory <L extends JeeslLang,D extends JeeslDescription,
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
								STYLE extends JeeslReportStyle<L,D>,CDT extends JeeslStatus<L,D,CDT>,
								CW extends JeeslStatus<L,D,CW>,
								RT extends JeeslReportRowType<L,D,RT,?>,
								ENTITY extends EjbWithId,
								ATTRIBUTE extends EjbWithId,
								TL extends JeeslTrafficLight<L,D,TLS>,
								TLS extends JeeslTrafficLightScope<L,D,TLS,?>,
								FILLING extends JeeslStatus<L,D,FILLING>,
								TRANSFORMATION extends JeeslStatus<L,D,TRANSFORMATION>
								>
{
	final static Logger logger = LoggerFactory.getLogger(XmlColumnGroupFactory.class);
	
	private ColumnGroup q;
	
	private Comparator<COLUMN> cColumn;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescriptions;
	private XmlColumnFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> xfColumn;
	private XmlLayoutFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,ENTITY,ATTRIBUTE> xfLayout;
	
	public XmlColumnGroupFactory(String localeCode, ColumnGroup q)
	{
		this.q=q;
		cColumn = new IoReportColumnComparator<L,D,CATEGORY,REPORT,WORKBOOK,SHEET,GROUP,COLUMN>().factory(IoReportColumnComparator.Type.position);
		if(Objects.nonNull(q.getLangs())){xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(Objects.nonNull(q.getDescriptions())){xfDescriptions = new XmlDescriptionsFactory<D>(q.getDescriptions());}
		if(q.isSetXlsColumn()){xfColumn = new XmlColumnFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION>(localeCode,q.getXlsColumn().get(0));}
		if(Objects.nonNull(q.getLayout())) {xfLayout = new XmlLayoutFactory<>(localeCode,q.getLayout());}
	}
	
	public ColumnGroup build(GROUP group)
	{
		ColumnGroup xml = new ColumnGroup();
		
		if(Objects.nonNull(q.getCode())) {xml.setCode(group.getCode());}
		if(Objects.nonNull(q.isVisible())) {xml.setVisible(group.isVisible());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(group.getPosition());}
		
		if(q.isSetShowLabel()){xml.setShowLabel(group.getShowLabel());}
		if(q.isSetShowWeb()){xml.setShowWeb(group.getShowWeb());}
		
		if(q.isSetQuery()){xml.setQuery(group.getQueryColumns());}
		
		if(Objects.nonNull(q.getLangs())){xml.setLangs(xfLangs.getUtilsLangs(group.getName()));}
		if(Objects.nonNull(q.getDescriptions())){xml.setDescriptions(xfDescriptions.create(group.getDescription()));}
		if(Objects.nonNull(q.getLayout())) {xml.setLayout(xfLayout.build(group));}
		
		if(q.isSetXlsColumn())
		{
			Collections.sort(group.getColumns(),cColumn);
			for(COLUMN column : group.getColumns())
			{
				xml.getXlsColumn().add(xfColumn.build(column));
			}
		}
		
		return xml;
	}
}