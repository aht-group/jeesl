package org.jeesl.model.ejb.io.log;

import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.logging.JeeslIoLog;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.system.security.user.SecurityUser;

@Entity
@Table(name="IoLog")
@EjbErNode(name="Log",category="systemIoLog",subset="ioLog")
public class IoLog implements JeeslIoLog<IoLogStatus,IoLogRetention,SecurityUser>
{
	public static final long serialVersionUID=1;	
	
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@NotNull @ManyToOne
	private IoLogStatus status;
	@Override public IoLogStatus getStatus() {return status;}
	@Override public void setStatus(IoLogStatus status) {this.status = status;}
	
	@NotNull @ManyToOne
	private IoLogRetention retention;
	@Override public IoLogRetention getRetention() {return retention;}
	@Override public void setRetention(IoLogRetention retention) {this.retention = retention;}
	
	@NotNull @ManyToOne
	private SecurityUser user;
	@Override public SecurityUser getUser() {return user;}
	@Override public void setUser(SecurityUser user) {this.user = user;}

	private LocalDateTime startDate;
	@Override public LocalDateTime getStartDate() {return startDate;}
	@Override public void setStartDate(LocalDateTime startDate) {this.startDate = startDate;}

	private LocalDateTime endDate;
	@Override public LocalDateTime getEndDate() {return endDate;}
	@Override public void setEndDate(LocalDateTime endDate) {this.endDate = endDate;}

	@Basic @Column(columnDefinition="text")
	private String remark;
	public String getRemark() {return remark;}
	public void setRemark(String remark) {this.remark = remark;}


	@Override public boolean equals(Object object){return (object instanceof IoLog) ? id == ((IoLog) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}