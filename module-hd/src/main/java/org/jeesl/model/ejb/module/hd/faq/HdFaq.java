package org.jeesl.model.ejb.module.hd.faq;

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
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdFaq;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.module.hd.HdCategory;
import org.jeesl.model.ejb.module.hd.HdScope;
import org.jeesl.model.ejb.system.tenant.TenantRealm;

@Table(name="HdFaq")
@EjbErNode(name="FAQ",category="hd",subset="moduleHd")
@Entity
public class HdFaq implements JeeslHdFaq<IoLang,IoDescription,TenantRealm,HdCategory,HdScope>
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

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}

	@NotNull @ManyToOne
	private HdCategory category;
	@Override public HdCategory getCategory() {return category;}
	@Override public void setCategory(HdCategory category) {this.category = category;}

	@NotNull @ManyToOne
	private HdScope scope;
	@Override public HdScope getScope() {return scope;}
	@Override public void setScope(HdScope scope) {this.scope = scope;}

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="HdFaqJtLang",joinColumns={@JoinColumn(name="faq_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	protected Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {if(Objects.isNull(name)) {name = new HashMap<>();} return this.name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="HdFaqJtDescription", joinColumns={@JoinColumn(name="faq_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	protected Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {if(Objects.isNull(description)) {description = new HashMap<>(); } return this.description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}


	@Override public boolean equals(Object object) {return (object instanceof HdFaq) ? id == ((HdFaq) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(23,7).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}