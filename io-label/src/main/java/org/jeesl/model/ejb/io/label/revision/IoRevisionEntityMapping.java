package org.jeesl.model.ejb.io.label.revision;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.label.entity.IoLabelEntity;

@Entity
@Table(name="IoRevisionMappingEntity",uniqueConstraints = @UniqueConstraint(columnNames = {"entity_id","scope_id"}))
@EjbErNode(name="Entity Mapping",category="revision",subset="revision",level=3)
public class IoRevisionEntityMapping implements JeeslRevisionEntityMapping<IoRevisionScope,IoRevisionScopeType,IoLabelEntity>
{
	public static final long serialVersionUID=1;
	
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@NotNull @ManyToOne
	private IoLabelEntity entity;
	@Override public IoLabelEntity getEntity() {return entity;}
	@Override public void setEntity(IoLabelEntity entity) {this.entity = entity;}
	
	@NotNull @ManyToOne
	private IoRevisionScope scope;
	@Override public IoRevisionScope getScope() {return scope;}
	@Override public void setScope(IoRevisionScope scope) {this.scope = scope;}
	
	@NotNull
	@ManyToOne
	private IoRevisionScopeType type;
	@Override public IoRevisionScopeType getType() {return type;}
	@Override public void setType(IoRevisionScopeType type) {this.type = type;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}
	
	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}

	private String xpath;
	@Override public String getXpath() {return xpath;}
	@Override public void setXpath(String xpath) {this.xpath = xpath;}
	
	private String jpqlTree;
	@Override public String getJpqlTree() {return jpqlTree;}
	@Override public void setJpqlTree(String jpqlTree) {this.jpqlTree = jpqlTree;}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
	
	@Override public boolean equals(Object object){return (object instanceof IoRevisionEntityMapping) ? id == ((IoRevisionEntityMapping) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
}