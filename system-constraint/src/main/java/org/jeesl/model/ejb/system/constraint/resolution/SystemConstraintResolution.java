package org.jeesl.model.ejb.system.constraint.resolution;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.CascadeType;
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
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintResolution;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.system.constraint.core.SystemConstraint;
import org.jeesl.model.ejb.system.constraint.core.SystemConstraintType;

@Entity
@Table(name="SystemConstraintResolution")
@EjbErNode(name="Constraint",category="system",subset="handbook")
public class SystemConstraintResolution implements Serializable,EjbRemoveable,EjbPersistable,EjbWithPosition,
											JeeslConstraintResolution<IoLang,IoDescription,SystemConstraint>
{
	public static final long serialVersionUID=1;	
	

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@Override public String resolveParentAttribute() {return JeeslConstraintResolution.Attributes.constraint.toString();}
	@NotNull @ManyToOne
	private SystemConstraint constraint;
	@Override public SystemConstraint getConstraint() {return constraint;}
	@Override public void setConstraint(SystemConstraint constraint) {this.constraint = constraint;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_SystemConstraintResolution_type"))
	private SystemConstraintType type;
	public SystemConstraintType getType() {return type;}
	public void setType(SystemConstraintType type) {this.type = type;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="SystemConstraintResolutionJtLang",joinColumns={@JoinColumn(name="resolution_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="SystemConstraintResolutionJtDescription",joinColumns={@JoinColumn(name="resolution_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}


	@Override public boolean equals(Object object){return (object instanceof SystemConstraintResolution) ? id == ((SystemConstraintResolution) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}