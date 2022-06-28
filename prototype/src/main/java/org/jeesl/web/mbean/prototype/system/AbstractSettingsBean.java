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
	
	protected String allowUploadSvg; public String getAllowUploadSvg() {return allowUploadSvg;}
	protected String allowUploadJesslGraphicType; public String getAllowUploadJesslGraphicType() {return allowUploadJesslGraphicType;}
	
	protected String paginatorPosition; public String getPaginatorPosition() {return paginatorPosition;}
	protected String paginatorTemplate; @Override public String getPaginatorTemplate() {return paginatorTemplate;}
	protected String rowsPerPageTemplate; @Override public String getRowsPerPageTemplate(){return rowsPerPageTemplate;}
	
	protected String filterStyle; public String getFilterStyle() {return filterStyle;}
	protected String filterStyleCode; public String getFilterStyleCode() {return filterStyleCode;}
	
	protected String title; public String getTitle() {return title;}
	protected String titlePrefix; public String getTitlePrefix() {return titlePrefix;}
	
	protected boolean indexWithSubmenuDescription; public boolean isIndexWithSubmenuDescription() {return indexWithSubmenuDescription;}
	
	private final String cssTimestamp; @Override public String getCssTimestamp() {return cssTimestamp;}

	public AbstractSettingsBean()
	{
		positionMenu2="right";
		positionSideMenu="right";
		initCalendarPattern();
		paginatorTemplate = "{RowsPerPageDropdown} {FirstPageLink} {PreviousPageLink} {CurrentPageReport} {NextPageLink} {LastPageLink}";
		paginatorPosition = "bottom";
		rowsPerPageTemplate = "5,20,50,100";
		
		filterStyle = "width: 75px;";
		filterStyleCode = "width: 50px;";
		
		allowUploadSvg = "/(\\.|\\/)(svg)$/";
		allowUploadJesslGraphicType = "/(\\.|\\/)(svg|png)$/";
		
		cssTimestamp = Long.valueOf(Instant.now().getEpochSecond()).toString();
		
		indexWithSubmenuDescription = true;
	}

//jeesl.highlight:datetime
	protected String patternDate; @Override public String getPatternDate() {return patternDate;}
	protected String patternDateMinute; @Override public String getPatternDateMinute() {return patternDateMinute;}
	protected String patternDateSecond; @Override public String getPatternDateSecond() {return patternDateSecond;}	
	
	protected String patternTimeMinute; public String getPatternTimeMinute() {return patternTimeMinute;}
	protected String patternTimeSecond; public String getPatternTimeSecond() {return patternTimeSecond;}
	
	private void initCalendarPattern()
	{
		patternDate = "dd.MM.yyyy";
		patternDateMinute = "dd.MM.yyyy HH:mm";
		patternDateSecond = "dd.MM.yyyy HH:mm:ss";

		patternTimeMinute = "HH:mm";
		patternTimeSecond = "HH:mm:ss";
	}
//jeesl.highlight:datetime
}