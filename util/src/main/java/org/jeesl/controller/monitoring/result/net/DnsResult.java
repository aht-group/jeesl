package org.jeesl.controller.monitoring.result.net;

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
public class DnsResult implements Serializable,MonitoringResult,EjbWithId,EjbWithRecord
{
	public static final long serialVersionUID=1;
	public static enum Code {SUCCESSFUL,UNRECOVERABLE,TRY_AGAIN,HOST_NOT_FOUND,TYPE_NOT_FOUND}
	
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
	
	private Code code;	
	public Code getCode() {return code;}
	public void setCode(Code code) {this.code = code;}
	
	// ***********   Methods   **************
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		sb.append(" ").append(duration);
		sb.append(" ").append(record);
		sb.append(" ").append(code.toString());
		return sb.toString();
	}
}