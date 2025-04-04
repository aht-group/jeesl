package org.jeesl.model.ejb.module.cal.unit;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarWeekOfMonth;
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarWeekOfYear;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("calendarWeekOfYear")
@EjbErNode(name="Week of Year",category="sys",level=3,subset="sysTime,mTimeIntern")
public class CalWeekOfYear extends IoStatus implements JeeslCalendarWeekOfYear<IoLang,IoDescription,CalWeekOfYear,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	@Override public List<String> getFixedCodes() {return JeeslCalendarWeekOfMonth.toFixedCodes();}
	
	@Override public boolean equals(Object object){return (object instanceof CalWeekOfYear) ? id == ((CalWeekOfYear) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(23,43).append(id).toHashCode();}
}