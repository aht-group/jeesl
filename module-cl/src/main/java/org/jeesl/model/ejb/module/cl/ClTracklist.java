package org.jeesl.model.ejb.module.cl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.cl.JeeslClTracklist;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.system.tenant.TenantRealm;

@Table(name="ClTracklist")
@EjbErNode(name="Tracklist",category="tafu",subset="moduleChecklist")
@Entity
public class ClTracklist implements JeeslClTracklist<IoLang,TenantRealm>
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
	private ClTopic category;
	public ClTopic getCategory() {return category;}
	public void setCategory(ClTopic category) {this.category = category;}

	private LocalDate startDate;
	@Override public LocalDate getStartDate() {return startDate;}
	@Override public void setStartDate(LocalDate startDate) {this.startDate = startDate;}

	private LocalDate endDate;
	@Override public LocalDate getEndDate() {return endDate;}
	@Override public void setEndDate(LocalDate endDate) {this.endDate = endDate;}

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="ClTracklistJtLang",joinColumns={@JoinColumn(name="checklist_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	@MapKey(name="lkey")
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {if(Objects.isNull(name)) {name=new HashMap<>();} return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}


	@Override public boolean equals(Object object) {return (object instanceof ClTracklist) ? id == ((ClTracklist) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(23,7).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		
		return sb.toString();
	}
}