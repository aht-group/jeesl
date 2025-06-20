package org.jeesl.web.mbean.prototype.io.report;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoReportFacade;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.IoReportFactoryBuilder;
import org.jeesl.factory.ejb.io.report.EjbIoReportStyleFactory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
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
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLightScope;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.web.JeeslJsfSecurityHandler;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.util.comparator.ejb.system.io.report.IoReportStyleComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractAdminIoReportStyleBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
										STYLE extends JeeslReportStyle<L,D,ALIGNMENT>,
										ALIGNMENT extends JeeslReportAlignment<L,D,ALIGNMENT,?>,
										CDT extends JeeslReportCellType<L,D,CDT,?>,
										CW extends JeeslReportColumnWidth<L,D,CW,?>,
										RT extends JeeslReportRowType<L,D,RT,?>,
										ENTITY extends EjbWithId,
										ATTRIBUTE extends EjbWithId,
										TL extends JeeslTrafficLight<L,D,TLS>,
										TLS extends JeeslTrafficLightScope<L,D,TLS,?>,
										FILLING extends JeeslStatus<L,D,FILLING>,
										TRANSFORMATION extends JeeslStatus<L,D,TRANSFORMATION>,
										RCAT extends JeeslRevisionCategory<L,D,RCAT,?>,				
										RE extends JeeslRevisionEntity<L,D,RCAT,?,RA,?>,
										RA extends JeeslRevisionAttribute<L,D,RE,?,?>
										>
	extends AbstractIoReportBean<L,D,LOC,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION,RCAT,RE,RA>
	implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminIoReportStyleBean.class);

	private List<STYLE> styles; public List<STYLE> getStyles() {return styles;}
	
	private STYLE style; public STYLE getStyle() {return style;} public void setStyle(STYLE style) {this.style = style;}

	private final Comparator<STYLE> comparatorStyle;
	
	private final EjbIoReportStyleFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT> efStyle;
	
	public AbstractAdminIoReportStyleBean(final IoReportFactoryBuilder<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> fbReport)
	{
		super(fbReport);
		efStyle = fbReport.style();
		comparatorStyle = new IoReportStyleComparator<STYLE>().factory(IoReportStyleComparator.Type.position);
	}
	
	protected void postConstructReportStyle(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage, JeeslIoReportFacade<REPORT,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL> fReport)
	{
		super.initSuperReport(bTranslation,bMessage,fReport);
		reloadStyles();
	}
	
	private void reset(boolean rStyle)
	{
		if(rStyle){style=null;}
	}
	
	//*************************************************************************************
	private void reloadStyles()
	{
		styles = fReport.all(fbReport.getClassStyle());
		if(debugOnInfo){logger.info(AbstractLogMessage.reloaded(fbReport.getClassStyle(),styles));}
//		Collections.sort(templates,comparatorTemplate);
	}
	
	public void addStyle()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbReport.getClassStyle()));}
		style = efStyle.build();
		style.setName(efLang.createEmpty(localeCodes));
		style.setDescription(efDescription.createEmpty(localeCodes));
		reset(false);
	}
	
	private void reloadStyle()
	{
		style = fReport.find(fbReport.getClassStyle(),style);
	}
	
	public void selectStyle() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(style));}
		style = fReport.find(fbReport.getClassStyle(), style);
		style = efLang.persistMissingLangs(fReport,localeCodes,style);
		style = efDescription.persistMissingLangs(fReport,localeCodes,style);
		
		reloadStyle();
		reset(false);
	}
	
	public void saveStyle() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(style));}
		style = fReport.save(style);
		reloadStyles();
		reloadStyle();
		bMessage.growlSaved(style);
		updatePerformed();
	}
	
	public void cancelStyles() {reset(true);}
	 
	//*************************************************************************************
	protected void reorderStyles() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fReport,styles);Collections.sort(styles,comparatorStyle);}
	
	protected void updatePerformed(){}	
	
	@SuppressWarnings("rawtypes")
	@Override protected void updateSecurity2(JeeslJsfSecurityHandler jsfSecurityHandler, String actionDeveloper)
	{
		uiAllowSave = jsfSecurityHandler.allow(actionDeveloper);

		if(logger.isTraceEnabled())
		{
			logger.info(uiAllowSave+" allowSave ("+actionDeveloper+")");
		}
	}
}