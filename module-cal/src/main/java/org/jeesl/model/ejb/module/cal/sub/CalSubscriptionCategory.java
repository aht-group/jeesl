package org.jeesl.model.ejb.module.cal.sub;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.calendar.sub.JeeslCalendarSubscriptionCategory;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("calendarSubscriptionCategory")
@EjbErNode(name="Wochentag",category="mj",level=3,subset="mj")
public class CalSubscriptionCategory extends IoStatus implements JeeslCalendarSubscriptionCategory<IoLang,IoDescription,CalSubscriptionCategory,IoGraphic>
{
	public static final long serialVersionUID=1;

	
	@Override public boolean equals(Object object){return (object instanceof CalSubscriptionCategory) ? id == ((CalSubscriptionCategory) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(23,43).append(id).toHashCode();}
}