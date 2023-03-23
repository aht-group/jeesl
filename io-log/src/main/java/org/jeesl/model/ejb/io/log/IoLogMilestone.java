package org.jeesl.model.ejb.io.log;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.logging.JeeslIoLogMilestone;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Entity
@Table(name="IoLogMilestone")
@EjbErNode(name="Log",category="systemIo",subset="ioLog")
public class IoLogMilestone implements JeeslIoLogMilestone<IoLog>
{
	public static final long serialVersionUID=1;	


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@NotNull @ManyToOne
	private IoLog log;
	@Override public IoLog getLog() {return log;}
	@Override public void setLog(IoLog log) {this.log = log;}

	private Date record;
	@Override public Date getRecord() {return record;}
	@Override public void setRecord(Date record) {this.record = record;}

	private long milliTotal;
	@Override public long getMilliTotal() {return milliTotal;}
	@Override public void setMilliTotal(long milliTotal) {this.milliTotal = milliTotal;}

	private long milliStep;
	@Override public long getMilliStep() {return milliStep;}
	@Override public void setMilliStep(long milliStep) {this.milliStep = milliStep;}

	private long milliRelative;
	@Override public long getMilliRelative() {return milliRelative;}
	@Override public void setMilliRelative(long milliRelative) {this.milliRelative = milliRelative;}

	private String name;
	@Override public String getName() {return name;}
	@Override public void setName(String name) {this.name = name;}

	private String message;
	@Override public String getMessage() {return message;}
	@Override public void setMessage(String message) {this.message = message;}

	private Integer elements;
	@Override public Integer getElements() {return elements;}
	@Override public void setElements(Integer elements) {this.elements = elements;}


	@Override public boolean equals(Object object){return (object instanceof IoLogMilestone) ? id == ((IoLogMilestone) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}