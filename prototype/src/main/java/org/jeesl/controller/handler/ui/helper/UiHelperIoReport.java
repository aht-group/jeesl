package org.jeesl.controller.handler.ui.helper;

import java.io.Serializable;

import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.data.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportWorkbook;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UiHelperIoReport <L extends JeeslLang,D extends JeeslDescription,
										CATEGORY extends JeeslStatus<L,D,CATEGORY>,
										REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
										IMPLEMENTATION extends JeeslStatus<L,D,IMPLEMENTATION>,
										WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
										SHEET extends JeeslReportSheet<L,D,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
										GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
										COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,TLS>,
										ROW extends JeeslReportRow<L,D,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>,
										TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
										CELL extends JeeslReportCell<L,D,REPORT,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>,
										STYLE extends JeeslReportStyle<L,D>,
										CDT extends JeeslStatus<L,D,CDT>,
										CW extends JeeslStatus<L,D,CW>,
										RT extends JeeslStatus<L,D,RT>,
										ENTITY extends EjbWithId,
										ATTRIBUTE extends EjbWithId,
										TL extends JeeslTrafficLight<L,D,TLS>,
										TLS extends JeeslStatus<L,D,TLS>,
										FILLING extends JeeslStatus<L,D,FILLING>,
										TRANSFORMATION extends JeeslStatus<L,D,TRANSFORMATION>>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(UiHelperIoReport.class);
		
	private REPORT report;
	private SHEET sheet;
	private GROUP group;
	
	private boolean showPanelReport; public boolean isShowPanelReport() {return showPanelReport;}
	private boolean showPanelSheet; public boolean isShowPanelSheet() {return showPanelSheet;}
	private boolean showPanelGroup; public boolean isShowPanelGroup() {return showPanelGroup;}

	public UiHelperIoReport()
	{
		showPanelReport = false;
		showPanelSheet = false;
		showPanelGroup = false;
	}
	
	public void check(REPORT report)
	{
		if(report!=null && EjbIdFactory.isUnSaved(report)){showPanelReport=true;}
		else if(this.report==null){showPanelReport = false;}
		else if(this.report!=null && report!=null) {showPanelReport = this.report.equals(report);}
		else {showPanelReport = false;}
		this.report=report;
	}
	
	public void check(SHEET sheet)
	{
		if(sheet!=null && EjbIdFactory.isUnSaved(sheet)){showPanelSheet=true;}
		else if(this.sheet==null){showPanelSheet = false;}
		else if(this.sheet!=null && sheet!=null) {showPanelSheet = this.sheet.equals(sheet);}
		else {showPanelSheet = false;}
		this.sheet=sheet;
	}
	
	public void check(GROUP group)
	{
		if(group!=null && EjbIdFactory.isUnSaved(group)){showPanelGroup=true;}
		else if(this.group==null){showPanelGroup = false;}
		else if(this.group!=null && group!=null) {showPanelGroup = this.group.equals(group);}
		else {showPanelGroup = false;}
		this.group=group;
	}
}