package org.jeesl.model.ejb.module.cal.common;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.calendar.common.JeeslCalendarHour;
import org.jeesl.model.ejb.module.cal.unit.CalHourOfDay;

@Entity
@Table(name="CalHour")
public class CalHour implements JeeslCalendarHour<CalDay,CalHourOfDay>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_CalHour_day"))
	private CalDay day;
	@Override public CalDay getDay() {return day;}
	@Override public void setDay(CalDay day) {this.day = day;}

	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_CalHour_hour"))
	private CalHourOfDay hour;
	@Override public CalHourOfDay getHour() {return hour;}
	@Override public void setHour(CalHourOfDay hour) {this.hour = hour;}

	@NotNull
	private LocalTime time;
	public LocalTime getTime() {return time;}
	public void setTime(LocalTime time) {this.time = time;}


	@Override public boolean equals(Object object){return (object instanceof CalHour) ? id == ((CalHour) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		sb.append(" ").append(hour.getCode());
		sb.append(" ").append(time.toString());
		return sb.toString();
	}
}