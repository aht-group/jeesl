package org.jeesl.controller.handler.ui.helper;

import java.io.Serializable;

import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UiHelperIoReport <REPORT extends JeeslIoReport<?,?,?,?>,
								SHEET extends JeeslReportSheet<?,?,?,?,GROUP,?>,
								GROUP extends JeeslReportColumnGroup<?,?,SHEET,?,?>>
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