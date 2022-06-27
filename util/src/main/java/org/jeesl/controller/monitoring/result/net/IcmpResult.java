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
public class IcmpResult implements Serializable,MonitoringResult,EjbWithId,EjbWithRecord
{
	public static final long serialVersionUID=1;
	public static enum Code {REACHABLE,TIMEOUT,UNKNOWN_HOST,ERROR}
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;	
	
	private Date record;
	
	private long duration;
	
	private Code code;
			
	//***********************************//
	public long getId() {return id;}
	public void setId(long id){this.id = id;}
	
	public long getDuration() {return duration;}
	public void setDuration(long duration) {this.duration = duration;}
		
	public Date getRecord() {return record;}
	public void setRecord(Date record) {this.record = record;}
		
	public Code getCode() {return code;}
	public void setCode(Code code) {this.code = code;}
	
	//***********************************//
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