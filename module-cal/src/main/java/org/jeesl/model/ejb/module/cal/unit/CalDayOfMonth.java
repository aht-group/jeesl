package org.jeesl.model.ejb.module.cal.unit;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarDayOfMonth;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("calendarDayOfMonth")
@EjbErNode(name="Kalendertag",category="mj",level=3,subset="mj")
public class CalDayOfMonth extends IoStatus implements JeeslCalendarDayOfMonth<IoLang,IoDescription,CalDayOfMonth,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<String>();
		for(int i=1;i<=31;i++){fixed.add(Integer.valueOf(i).toString());}
		return fixed;
	}
	
	@Override public boolean equals(Object object){return (object instanceof CalDayOfMonth) ? id == ((CalDayOfMonth) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(23,43).append(id).toHashCode();}
}