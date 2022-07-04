package org.jeesl.controller.handler.module.calendar;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleRenderingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslCalendarEvent implements Serializable,ScheduleEvent<EjbWithId>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslCalendarEvent.class);
	private static final long serialVersionUID = 1L;

	private String id;
	@Override public String getId() {return id;}
	@Override public void setId(String id) {this.id=id;}

	private String title; @Override public String getTitle() {return title;} public void setTitle(String title) {this.title = title;}
	@Override public String getDescription() {return title;}
	
	private String styleClass; @Override public String getStyleClass() {return styleClass;} public void setStyleClass(String styleClass) {this.styleClass = styleClass;}

	private LocalDateTime startDate; @Override public LocalDateTime getStartDate() {return startDate;} @Override public void setStartDate(LocalDateTime startDate) {this.startDate = startDate;}
	private LocalDateTime endDate; @Override public LocalDateTime getEndDate() {return endDate;} @Override public void setEndDate(LocalDateTime endDate) {this.endDate = endDate;}

	private boolean editable; @Override public boolean isEditable() {return editable;} public void setEditable(boolean editable) {this.editable = editable;}
	private boolean allDay; @Override public boolean isAllDay() {return allDay;} public void setAllDay(boolean allDay) {this.allDay = allDay;}

	
	public JeeslCalendarEvent()
	{
		editable = true;
		allDay = true;
	}

	public JeeslCalendarEvent(String title, LocalDateTime startDate, LocalDateTime endDate)
	{
		this();
		this.title=title;
		this.startDate=startDate;
		this.endDate=endDate;
		editable = false;
	}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("] ");
		
		sb.append(" ").append(startDate.toString());
		return sb.toString();
	}

	public static String debug(ScheduleEntryMoveEvent move)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(ScheduleEntryMoveEvent.class.getSimpleName());
		sb.append(" dDay:").append(move.getDayDelta());
		sb.append(" dMinute:").append(move.getMinuteDelta());
		sb.append(" ").append(((JeeslCalendarEvent)move.getScheduleEvent()).toString());
		return sb.toString();
	}

	@Override public EjbWithId getData() {return null;}
	@Override public String getUrl() {return null;}
	@Override public ScheduleRenderingMode getRenderingMode() {return null;}
	@Override public Map<String, Object> getDynamicProperties() {return null;}
	@Override public String getGroupId() {return null;}
	@Override public boolean isOverlapAllowed() {return false;}
}