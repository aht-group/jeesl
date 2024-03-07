package org.jeesl.model.ejb.system.filter;

import java.time.LocalDateTime;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
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
import org.jeesl.interfaces.model.system.filter.JeeslFilter;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.system.security.user.SecurityUser;

@Entity
@Table(name="SystemFilter")
@EjbErNode(name="Filter",category="user",subset="systemUser")
public class SystemFilter implements JeeslFilter<IoLang,SystemFilter,SystemFilterType,SystemFilterScope,SecurityUser>
										
{
	public static final long serialVersionUID=1;

	public enum Attributes{type,scope,user}
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_SystemFilter_parent"))
	private SystemFilter parent;
	public SystemFilter getParent() {return parent;}
	public void setParent(SystemFilter parent) {this.parent = parent;}

	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_SystemFilter_type"))
	private SystemFilterType type;
	@Override public SystemFilterType getType() {return type;}
	@Override public void setType(SystemFilterType type) {this.type = type;}

//jeesl.highlight:mto
@NotNull @ManyToOne
@JoinColumn(foreignKey=@ForeignKey(name="fk_SystemFilter_scope"))
private SystemFilterScope scope;
@Override public SystemFilterScope getScope() {return scope;}
@Override public void setScope(SystemFilterScope scope) {this.scope = scope;}
//jeesl.highlight:mto
	
	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_SystemFilter_user"))
	private SecurityUser user;
	public SecurityUser getUser() {return user;}
	public void setUser(SecurityUser user) {this.user = user;}
	
	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}
	
	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}
    
//jeesl.highlight:multilang
@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
@MapKey(name="lkey")
@JoinTable(name="SystemFilterJtLang",
	joinColumns={@JoinColumn(foreignKey=@ForeignKey(name="fk_SystemFilterJtLang_filter"),name="filter_id")},
	inverseJoinColumns={@JoinColumn(foreignKey=@ForeignKey(name="fk_SystemFilterJtLang_lang"),name="lang_id")},
	uniqueConstraints=@UniqueConstraint(columnNames={"lang_id"}, name = "uk_SystemFilterJtLang_lang"))
private Map<String,IoLang> name;
@Override public Map<String,IoLang> getName() {return name;}
@Override public void setName(Map<String,IoLang> name) {this.name = name;}
//jeesl.highlight:multilang
	
	@Basic @Column(columnDefinition="text")
	private String json;
	@Override public String getJson() {return json;}
	@Override public void setJson(String json) {this.json = json;}
	
	@ManyToOne @NotNull
	@JoinColumn(foreignKey=@ForeignKey(name="fk_SystemFilter_lastModifiedBy"))
	private SecurityUser lastModifiedBy;
	public SecurityUser getLastModifiedBy() {return lastModifiedBy;}
	public void setLastModifiedBy(SecurityUser lastModifiedBy) {this.lastModifiedBy = lastModifiedBy;}

	private LocalDateTime lastModifiedAt;
	public LocalDateTime getLastModifiedAt() {return lastModifiedAt;}
	public void setLastModifiedAt(LocalDateTime lastModifiedAt) {this.lastModifiedAt = lastModifiedAt;}


	@Override public boolean equals(Object object) {return (object instanceof SystemFilter) ? id == ((SystemFilter) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(21,43).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}