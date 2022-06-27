package org.jeesl.controller.monitoring.result;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.jeesl.interfaces.model.with.date.ju.EjbWithRecord;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

import net.sf.ahtutils.interfaces.controller.monitoring.MonitoringResult;

@Entity
public class DebugResult implements Serializable,MonitoringResult,EjbWithId,EjbWithRecord
{
	public static final long serialVersionUID=1;
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	public long getId() {return id;}
	public void setId(long id){this.id = id;}
		
	private long duration;
	public long getDuration() {return duration;}
	public void setDuration(long duration) {this.duration = duration;}
	
	private Date record;
	public Date getRecord() {return record;}
	public void setRecord(Date record) {this.record = record;}
	
	private String text;
	public String getText() {return text;}
	public void setText(String text) {this.text = text;}
	
	// ***********   Methods   ************** //
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		sb.append(" ").append(duration);
		sb.append(" ").append(record);
		sb.append(" ").append(text);
		return sb.toString();
	}
}