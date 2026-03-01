package org.jeesl.model.ejb.module.cal.unit;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarHourOfDay;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("calendarHourOfDay")
@EjbErNode(name="Wochentag",category="mj",level=3,subset="mj")
public class CalHourOfDay extends IoStatus implements JeeslCalendarHourOfDay<IoLang,IoDescription,CalHourOfDay,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<String>();
//		for(JeeslCalendarDayOfWeek.Code c : JeeslCalendarDayOfWeek.Code.values()){fixed.add(c.toString());}
		return fixed;
	}
	
	@Override public boolean equals(Object object){return (object instanceof CalHourOfDay) ? id == ((CalHourOfDay) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(23,43).append(id).toHashCode();}
}