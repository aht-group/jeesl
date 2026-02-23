package org.jeesl.model.ejb.module.cal.sun;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.calendar.sun.JeeslCalendarSunHorizon;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("calendarSunHorizon")
@EjbErNode(name="Wochentag",category="mj",level=3,subset="mj")
public class CalSunHorizon extends IoStatus implements JeeslCalendarSunHorizon<IoLang,IoDescription,CalSunHorizon,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<String>();
//		for(JeeslCalendarDayOfWeek.Code c : JeeslCalendarDayOfWeek.Code.values()){fixed.add(c.toString());}
		return fixed;
	}
	
	@Override public boolean equals(Object object){return (object instanceof CalSunHorizon) ? id == ((CalSunHorizon) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(23,43).append(id).toHashCode();}
}