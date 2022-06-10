package org.jeesl.web.mbean.prototype.io.report;

import java.io.Serializable;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoReportFacade;
import org.jeesl.factory.builder.system.ReportFactoryBuilder;
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
import org.jeesl.interfaces.model.io.revision.core.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLightScope;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractIoReportBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
							STYLE extends JeeslReportStyle<L,D>,
							CDT extends JeeslStatus<L,D,CDT>,
							CW extends JeeslStatus<L,D,CW>,
							RT extends JeeslReportRowType<L,D,RT,?>,
							ENTITY extends EjbWithId,
							ATTRIBUTE extends EjbWithId,
							TL extends JeeslTrafficLight<L,D,TLS>,
							TLS extends JeeslTrafficLightScope<L,D,TLS,?>,
							FILLING extends JeeslStatus<L,D,FILLING>,
							TRANSFORMATION extends JeeslStatus<L,D,TRANSFORMATION>,
							RCAT extends JeeslRevisionCategory<L,D,RCAT,?>,
							RE extends JeeslRevisionEntity<L,D,RCAT,?,RA,?>,
							RA extends JeeslRevisionAttribute<L,D,RE,?,CDT>
							>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractIoReportBean.class);
	
	protected JeeslIoReportFacade<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> fReport;
	protected final ReportFactoryBuilder<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> fbReport;
	
	protected AbstractIoReportBean(ReportFactoryBuilder<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> fbReport)
	{
		super(fbReport.getClassL(),fbReport.getClassD());
		this.fbReport=fbReport;
	}
	
	protected void initSuperReport(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage, JeeslIoReportFacade<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> fReport)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fReport=fReport;
	}
}