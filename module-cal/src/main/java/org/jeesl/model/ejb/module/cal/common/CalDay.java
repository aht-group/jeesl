package org.jeesl.model.ejb.module.cal.common;

import java.io.Serializable;
import java.time.LocalDate;

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
import org.jeesl.interfaces.model.module.calendar.common.JeeslCalendarDay;
import org.jeesl.interfaces.model.with.date.jt.JeeslWithRecordDate;
import org.jeesl.model.ejb.module.cal.unit.CalDayOfMonth;
import org.jeesl.model.ejb.module.cal.unit.CalMonth;
import org.jeesl.model.ejb.module.cal.unit.CalYear;

@Entity
@Table(name="CalDay")
public class CalDay implements Serializable,JeeslWithRecordDate,
									JeeslCalendarDay<CalYear,CalMonth,CalDayOfMonth>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_CalDay_year"))
	private CalYear year;
	@Override public CalYear getYear() {return year;}
	@Override public void setYear(CalYear year) {this.year = year;}
	
	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_CalDay_month"))
	private CalMonth month;
	@Override public CalMonth getMonth() {return month;}
	@Override public void setMonth(CalMonth month) {this.month = month;}
	
	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_CalDay_day"))
	private CalDayOfMonth day;
	@Override public CalDayOfMonth getDay() {return day;}
	@Override public void setDay(CalDayOfMonth day) {this.day = day;}
	
	@NotNull
	private LocalDate record;
	@Override public LocalDate getRecord() {return record;}
	@Override public void setRecord(LocalDate record) {this.record = record;}


	@Override public boolean equals(Object object){return (object instanceof CalDay) ? id == ((CalDay) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}