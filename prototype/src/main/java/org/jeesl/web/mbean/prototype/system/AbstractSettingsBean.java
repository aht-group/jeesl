package org.jeesl.web.mbean.prototype.system;

import java.io.Serializable;
import java.time.Instant;

import org.jeesl.api.bean.JeeslSettingsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractSettingsBean implements Serializable,JeeslSettingsBean
{
	final static Logger logger = LoggerFactory.getLogger(AbstractSettingsBean.class);
	private static final long serialVersionUID = 1L;
	
	protected String positionSideMenu;public String getPositionSideMenu() {return positionSideMenu;}
	protected String positionMenu2;public String getPositionMenu2() {return positionMenu2;}
	protected String calendarFormat; public String getCalendarFormat(){return calendarFormat;}
	protected String timeFormat; public String getTimeFormat(){return timeFormat;}
	
	protected String patternDate; public String getPatternDate() {return patternDate;}
	protected String patternDateHour; public String getPatternDateHour() {return patternDateHour;}
	protected String patternMinute; public String getPatternMinute() {return patternMinute;}
	protected String patternHour; public String getPatternHour() {return patternHour;}
	protected String patternTimeMinute; public String getPatternTimeMinute() {return patternTimeMinute;}

	protected String allowUploadSvg; public String getAllowUploadSvg() {return allowUploadSvg;}
	protected String allowUploadJesslGraphicType; public String getAllowUploadJesslGraphicType() {return allowUploadJesslGraphicType;}
	
	protected String paginatorPosition; public String getPaginatorPosition() {return paginatorPosition;}
	protected String paginatorTemplate; @Override public String getPaginatorTemplate() {return paginatorTemplate;}
	protected String rowsPerPageTemplate; @Override public String getRowsPerPageTemplate(){return rowsPerPageTemplate;}
	
	protected String filterStyle; public String getFilterStyle() {return filterStyle;}
	protected String title; public String getTitle() {return title;}
	protected String titlePrefix; public String getTitlePrefix() {return titlePrefix;}
	
	private final String cssTimestamp; @Override public String getCssTimestamp() {return cssTimestamp;}

	public AbstractSettingsBean()
	{
		positionMenu2="right";
		positionSideMenu="right";
		calendarFormat = "dd.MM.yyyy";
		timeFormat = "dd.MM.yyyy HH:mm";
		initCalendarPattern();
		paginatorTemplate = "{RowsPerPageDropdown} {FirstPageLink} {PreviousPageLink} {CurrentPageReport} {NextPageLink} {LastPageLink}";
		paginatorPosition = "bottom";
		rowsPerPageTemplate = "5,20,50,100";
		filterStyle = "width: 75px;";
		
		allowUploadSvg = "/(\\.|\\/)(svg)$/";
		allowUploadJesslGraphicType = "/(\\.|\\/)(svg|png)$/";
		
		cssTimestamp = Long.valueOf(Instant.now().getEpochSecond()).toString();
	}
	
	protected String datePattern; @Override public String getDatePattern(){return datePattern;}
	protected String dateTimePattern; @Override public String getDateTimePattern(){return dateTimePattern;}
	
	private void initCalendarPattern()
	{
		patternDate = "dd.MM.yyyy";
		patternDateHour = "dd.MM.yyyy HH:mm";
		patternMinute = "dd.MM.yyyy HH:mm";
		patternHour = "HH:mm";
		patternTimeMinute = "HH:mm:ss";
		
//		Deprecated below
		datePattern = "dd.MM.yyyy";
		dateTimePattern = "dd.MM.yyyy HH:mm:ss";
	}
}