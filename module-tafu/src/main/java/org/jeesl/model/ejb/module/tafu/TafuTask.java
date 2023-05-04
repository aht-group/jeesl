package org.jeesl.model.ejb.module.tafu;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuTask;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.cms.markup.IoMarkup;
import org.jeesl.model.ejb.system.tenant.TenantRealm;

@Table(name="TafuTask")
@EjbErNode(name="Task",category="tafu",subset="moduleTafu")
@Entity
public class TafuTask implements JeeslTafuTask<TenantRealm,TafuStatus,TafuScope,IoMarkup>
{
	public static final long serialVersionUID=1;

	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@NotNull @ManyToOne
	private TenantRealm realm;
	@Override public TenantRealm getRealm() {return realm;}
	@Override public void setRealm(TenantRealm realm) {this.realm = realm;}
	
	private long rref;
	@Override public long getRref() {return rref;}
	@Override public void setRref(long rref) {this.rref = rref;}
	
	@NotNull @ManyToOne
	private TafuStatus status;
	@Override public TafuStatus getStatus() {return status;}
	@Override public void setStatus(TafuStatus status) {this.status = status;}
	
	@ManyToOne
	private TafuScope scope;
	@Override public TafuScope getScope() {return scope;}
	@Override public void setScope(TafuScope scope) {this.scope = scope;}
	
	private String name;
	@Override public String getName() {return name;}
	@Override public void setName(String name) {this.name = name;}

	@NotNull
	private LocalDateTime recordCreated;
	@Override public LocalDateTime getRecordCreated() {return recordCreated;}
	@Override public void setRecordCreated(LocalDateTime recordCreated) {this.recordCreated = recordCreated;}
	
	@NotNull
	private LocalDateTime recordUpdated;
	@Override public LocalDateTime getRecordUpdated() {return recordUpdated;}
	@Override public void setRecordUpdated(LocalDateTime recordUpdated) {this.recordUpdated = recordUpdated;}

	private LocalDateTime recordResolved;
	@Override public LocalDateTime getRecordResolved() {return recordResolved;}
	@Override public void setRecordResolved(LocalDateTime recordResolved) {this.recordResolved = recordResolved;}
	
	@NotNull
	private LocalDate recordShow;
	@Override public LocalDate getRecordShow() {return recordShow;}
	@Override public void setRecordShow(LocalDate recordShow) {this.recordShow = recordShow;}
	
	private LocalDate recordDue;
	@Override public LocalDate getRecordDue() {return recordDue;}
	@Override public void setRecordDue(LocalDate recordDue) {this.recordDue = recordDue;}
	
	@NotNull
	@OneToOne(cascade=CascadeType.ALL)
	private IoMarkup markup;
	@Override public IoMarkup getMarkup() {return markup;}
	@Override public void setMarkup(IoMarkup markup) {this.markup = markup;}
	
	
	@Override public boolean equals(Object object) {return (object instanceof TafuTask) ? id == ((TafuTask) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(23,7).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		sb.append(" status:");if(status.getCode()!=null) {sb.append(status.getCode());}else {sb.append("id=").append(status.getId());}
		sb.append(" show:").append(recordShow.toString());
		return sb.toString();
	}
}