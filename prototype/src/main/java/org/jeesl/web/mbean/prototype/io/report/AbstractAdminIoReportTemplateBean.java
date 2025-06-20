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
import org.jeesl.factory.ejb.io.report.EjbIoReportCellFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportTemplateFactory;
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
import org.jeesl.util.comparator.ejb.system.io.report.IoReportCellComparator;
import org.jeesl.util.comparator.ejb.system.io.report.IoReportTemplateComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractAdminIoReportTemplateBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
										RC extends JeeslRevisionCategory<L,D,RC,?>,
										RE extends JeeslRevisionEntity<L,D,RC,?,RA,?>,
										RA extends JeeslRevisionAttribute<L,D,RE,?,?>
										>
	extends AbstractIoReportBean<L,D,LOC,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION,RC,RE,RA>
	implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminIoReportTemplateBean.class);
	
	private List<TEMPLATE> templates; public List<TEMPLATE> getTemplates() {return templates;}
	private List<CELL> cells; public List<CELL> getCells() {return cells;}
	
	private TEMPLATE template; public TEMPLATE getTemplate() {return template;} public void setTemplate(TEMPLATE template) {this.template = template;}
	private CELL cell; public CELL getCell() {return cell;} public void setCell(CELL cell) {this.cell = cell;}

	private Comparator<TEMPLATE> comparatorTemplate;
	private Comparator<CELL> comparatorCell;
	
	private EjbIoReportTemplateFactory<L,D,TEMPLATE,CELL,STYLE,CDT,CW> efTemplate;
	private EjbIoReportCellFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT> efCell;
		
	public AbstractAdminIoReportTemplateBean(final IoReportFactoryBuilder<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT,RC,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> fbReport)
	{
		super(fbReport);
	}
	
	protected void postConstructReportTemplate(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
			JeeslIoReportFacade<REPORT,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL> fReport)
	{
		super.initSuperReport(bTranslation,bMessage,fReport);
		
		efTemplate = fbReport.template();
		efCell = fbReport.cell();
				
		comparatorTemplate = new IoReportTemplateComparator<TEMPLATE>().factory(IoReportTemplateComparator.Type.position);
		comparatorCell = new IoReportCellComparator<TEMPLATE,CELL>().factory(IoReportCellComparator.Type.position);

		reloadTemplates();
	}
	
	private void reset(boolean rTemplate, boolean rCell)
	{
		if(rTemplate){template=null;}
		if(rCell){cell=null;}
	}
	
	//*************************************************************************************
	private void reloadTemplates()
	{
		templates = fReport.all(fbReport.getClassTemplate());
		if(debugOnInfo){logger.info(AbstractLogMessage.reloaded(fbReport.getClassTemplate(),templates));}
//		Collections.sort(templates,comparatorTemplate);
	}
	
	public void addTemplate()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbReport.getClassTemplate()));}
		template = efTemplate.build();
		template.setName(efLang.createEmpty(localeCodes));
		template.setDescription(efDescription.createEmpty(localeCodes));
		reset(false,true);
	}
	
	private void reloadTemplate()
	{
		template = fReport.load(template);
		cells = template.getCells();
		
		Collections.sort(cells, comparatorCell);
	}
	
	public void selectTemplate() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(template));}
		template = fReport.find(fbReport.getClassTemplate(), template);
		template = efLang.persistMissingLangs(fReport,localeCodes,template);
		template = efDescription.persistMissingLangs(fReport,localeCodes,template);
		
		reloadTemplate();
		reset(false,true);
	}
	
	public void saveTemplate() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(template));}
		template = fReport.save(template);
		reloadTemplates();
		reloadTemplate();
		bMessage.growlSaved(template);
		updatePerformed();
	}
	
	public void cancelTemplate() {reset(true,true);}
	
	//*************************************************************************************

	public void addCell()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbReport.getClassCell()));}
		cell = efCell.build(template);
		cell.setName(efLang.createEmpty(localeCodes));
		cell.setDescription(efDescription.createEmpty(localeCodes));
		reset(false,false);
	}
	
	public void selectCell()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(cell));}
		cell = fReport.find(fbReport.getClassCell(), cell);
		reset(false,false);
	}
		
	public void saveCell() throws JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(cell));}
		try
		{
			cell = fReport.save(cell);
			reloadTemplate();
			
			bMessage.growlSaved(cell);
			updatePerformed();
		}
		catch (JeeslConstraintViolationException e) {bMessage.constraintDuplicate(null);}
	}

	public void cancelCell() {reset(false,true);}
    
	//*************************************************************************************
	protected void reorderTemplates() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fReport,templates);Collections.sort(templates,comparatorTemplate);}
	
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