package org.jeesl.model.ejb.module.cal.unit;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarDayOfWeekIcon;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("calendarDayOfWeekIcon")
@EjbErNode(name="Type",category="util",subset="calendar",level=4)
public class CalDayOfWeekIcon extends IoStatus implements JeeslCalendarDayOfWeekIcon<IoLang,IoDescription,CalDayOfWeekIcon,IoGraphic>
{
	public static final long serialVersionUID=1;
	public static enum Code{x}
	
	@Override public boolean equals(Object object) {return (object instanceof CalDayOfWeekIcon) ? id == ((CalDayOfWeekIcon) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,51).append(id).toHashCode();}
}