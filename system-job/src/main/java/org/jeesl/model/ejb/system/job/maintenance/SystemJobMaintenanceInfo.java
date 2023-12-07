package org.jeesl.model.ejb.system.job.maintenance;

import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.job.maintenance.JeeslJobMaintenanceInfo;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.system.job.core.SystemJobStatus;

@Entity
@Table(name="SystemJobMaintenanceInfo",uniqueConstraints=@UniqueConstraint(columnNames={"status_id","maintenance_id"}))
@EjbErNode(name="JobMaintenanceInfo")
public class SystemJobMaintenanceInfo implements JeeslJobMaintenanceInfo<IoDescription,SystemJobStatus,SystemJobMaintenance>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@NotNull @ManyToOne
	private SystemJobStatus status;
	@Override public SystemJobStatus getStatus() {return status;}
	@Override public void setStatus(SystemJobStatus status) {this.status = status;}
	
	@Override public String resolveParentAttribute() {return JeeslJobMaintenanceInfo.Attributes.maintenance.toString();}
	@NotNull @ManyToOne
	private SystemJobMaintenance maintenance;
	public SystemJobMaintenance getMaintenance() {return maintenance;}
	public void setMaintenance(SystemJobMaintenance maintenance) {this.maintenance = maintenance;}
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="SystemJobMaintenanceInfoJtDescription",joinColumns={@JoinColumn(name="info_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}


	@Override public boolean equals(Object object){return (object instanceof SystemJobMaintenanceInfo) ? id == ((SystemJobMaintenanceInfo) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(21,43).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}