package org.jeesl.model.ejb.io.mail.core;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.mail.core.JeeslIoMail;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.fr.IoFileContainer;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;

@Entity
@Table(name="IoMail")
@EjbErNode(name="Mail",category="system",subset="mail")
public class IoMail implements JeeslIoMail<IoLang,IoDescription,IoMailCategory,IoMailStatus,IoMailRetention,IoFileContainer>
{
	public static final long serialVersionUID=1;	
	
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@Version
	private Long versionLock;
	@Override public Long getVersionLock() {return versionLock;}
	
	@ManyToOne
	private IoMailCategory category;
	@Override public IoMailCategory getCategory() {return category;}
	@Override public void setCategory(IoMailCategory category) {this.category = category;}
	
	@ManyToOne
	private IoMailStatus status;
	@Override public IoMailStatus getStatus() {return status;}
	@Override public void setStatus(IoMailStatus status) {this.status = status;}
	
	@ManyToOne
	private IoMailRetention retention;
	@Override public IoMailRetention getRetention() {return retention;}
	@Override public void setRetention(IoMailRetention retention) {this.retention = retention;}
	
	private Date recordCreation;
	@Override public Date getRecordCreation() {return recordCreation;}
	@Override public void setRecordCreation(Date recordCreation) {this.recordCreation = recordCreation;}

	private Date recordSpool;
	@Override public Date getRecordSpool() {return recordSpool;}
	@Override public void setRecordSpool(Date recordSpool) {this.recordSpool = recordSpool;}
	
	private Date recordSent;
	@Override public Date getRecordSent() {return recordSent;}
	@Override public void setRecordSent(Date recordSent) {this.recordSent = recordSent;}
	
	private int counter;
	@Override public int getCounter() {return counter;}
	@Override public void setCounter(int counter) {this.counter = counter;}

	private Integer retentionDays;
	public Integer getRetentionDays() {return retentionDays;}
	public void setRetentionDays(Integer retentionDays) {this.retentionDays = retentionDays;}
	
	@NotNull
	private String recipient;
	@Override public String getRecipient() {return recipient;}
	@Override public void setRecipient(String recipient) {this.recipient = recipient;}

	@Basic @Column(columnDefinition = "text")
	private String xml;
	@Override public String getXml() {return xml;}
	@Override public void setXml(String xml) {this.xml = xml;}
	
	
	@Override public boolean equals(Object object){return (object instanceof IoMail) ? id == ((IoMail) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}