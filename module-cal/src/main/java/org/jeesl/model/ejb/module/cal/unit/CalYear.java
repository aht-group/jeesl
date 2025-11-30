package org.jeesl.model.ejb.module.cal.unit;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarYear;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("calendarYear")
@EjbErNode(name="Jahr",category="lcf",level=3,subset="hrMailings")
public class CalYear extends IoStatus
								implements EjbPersistable,EjbWithCode,EjbWithPositionVisible,
											JeeslCalendarYear<IoLang,IoDescription,CalYear,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	@Override public boolean equals(Object object){return (object instanceof CalYear) ? id == ((CalYear) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(23,43).append(id).toHashCode();}
}