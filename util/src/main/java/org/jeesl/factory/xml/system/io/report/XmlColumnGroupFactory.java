package org.jeesl.factory.xml.system.io.report;

import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReportCategory;
import org.jeesl.interfaces.model.io.report.col.JeeslReportCellType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.style.JeeslReportAlignment;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportWorkbook;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.report.ColumnGroup;
import org.jeesl.util.comparator.ejb.system.io.report.IoReportColumnComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlColumnGroupFactory <L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslIoReportCategory<L,D,CATEGORY,?>,
								REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
								
								WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
								SHEET extends JeeslReportSheet<L,D,?,WORKBOOK,GROUP,ROW>,
								GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
								COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,?>,
								ROW extends JeeslReportRow<L,D,SHEET,?,CDT,?>,
								
								CELL extends JeeslReportCell<L,D,?>,
								STYLE extends JeeslReportStyle<L,D,ALIGNMENT>,
								ALIGNMENT extends JeeslReportAlignment<L,D,ALIGNMENT,?>,
								CDT extends JeeslReportCellType<L,D,CDT,?>,
								CW extends JeeslStatus<L,D,CW>
								>
{
	final static Logger logger = LoggerFactory.getLogger(XmlColumnGroupFactory.class);
	
	private ColumnGroup q;
	
	private Comparator<COLUMN> cColumn;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescriptions;
	private XmlColumnFactory<L,D,GROUP,COLUMN,ROW,CELL,STYLE,ALIGNMENT,CDT,CW> xfColumn;
	private XmlLayoutFactory<L,D,GROUP,COLUMN,ROW,STYLE,ALIGNMENT,CDT,CW> xfLayout;
	
	public XmlColumnGroupFactory(String localeCode, ColumnGroup q)
	{
		this.q=q;
		cColumn = new IoReportColumnComparator<L,D,CATEGORY,REPORT,WORKBOOK,SHEET,GROUP,COLUMN>().factory(IoReportColumnComparator.Type.position);
		if(Objects.nonNull(q.getLangs())){xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(Objects.nonNull(q.getDescriptions())){xfDescriptions = new XmlDescriptionsFactory<D>(q.getDescriptions());}
		if(Objects.nonNull(q.getLayout())) {xfLayout = new XmlLayoutFactory<>(localeCode,q.getLayout());}
		if(ObjectUtils.isNotEmpty(q.getXlsColumn())) {xfColumn = new XmlColumnFactory<>(localeCode,q.getXlsColumn().get(0));}
	}
	
	public ColumnGroup build(GROUP group)
	{
		ColumnGroup xml = new ColumnGroup();
		
		if(Objects.nonNull(q.getCode())) {xml.setCode(group.getCode());}
		if(Objects.nonNull(q.isVisible())) {xml.setVisible(group.isVisible());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(group.getPosition());}
		
		if(Objects.nonNull(q.isShowLabel())) {xml.setShowLabel(group.getShowLabel());}
		if(Objects.nonNull(q.isShowWeb())) {xml.setShowWeb(group.getShowWeb());}
		
		if(Objects.nonNull(q.getQuery())) {xml.setQuery(group.getQueryColumns());}
		
		if(Objects.nonNull(q.getLangs())){xml.setLangs(xfLangs.getUtilsLangs(group.getName()));}
		if(Objects.nonNull(q.getDescriptions())){xml.setDescriptions(xfDescriptions.create(group.getDescription()));}
		if(Objects.nonNull(q.getLayout())) {xml.setLayout(xfLayout.build(group));}
		
		if(ObjectUtils.isNotEmpty(q.getXlsColumn()))
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