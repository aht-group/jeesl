package org.jeesl.model.ejb.system.constraint.core;

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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraint;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.system.constraint.resolution.SystemConstraintResolution;

@Entity
@Table(name="SystemConstraint",uniqueConstraints=@UniqueConstraint(columnNames={"scope_id","code"}))
@EjbErNode(name="Constraint",category="system",subset="handbook")
public class SystemConstraint implements JeeslConstraint<IoLang,IoDescription,SystemConstraintScope,SystemConstraintCategory,SystemConstraintLevel,SystemConstraintType>
{
	public static final long serialVersionUID=1;	
	
	public enum JEESL{error,warn,info}
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	private String nr;
	@Override public String getNr() {return nr;}
	@Override public void setNr(String nr) {this.nr = nr;}
	
	@NotNull
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}
	
	@Override public String resolveParentAttribute() {return JeeslConstraint.Attributes.scope.toString();}
	@NotNull @ManyToOne
	private SystemConstraintScope scope;
	@Override public SystemConstraintScope getScope() {return scope;}
	@Override public void setScope(SystemConstraintScope scope) {this.scope = scope;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}
	
	@NotNull @ManyToOne
	private SystemConstraintType type;
	public SystemConstraintType getType() {return type;}
	public void setType(SystemConstraintType type) {this.type = type;}
	
	@NotNull @ManyToOne
	private SystemConstraintLevel level;
	public SystemConstraintLevel getLevel() {return level;}
	public void setLevel(SystemConstraintLevel level) {this.level = level;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="SystemConstraintJtLang",joinColumns={@JoinColumn(name="constraint_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="SystemConstraintJtDescription",joinColumns={@JoinColumn(name="constraint_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}
	
	@Transient
	private String contextMessage;
	@Override public String getContextMessage() {return contextMessage;}
	@Override public void setContextMessage(String contextMessage) {this.contextMessage = contextMessage;}


	@Override public boolean equals(Object object){return (object instanceof SystemConstraint) ? id == ((SystemConstraint) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}