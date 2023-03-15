package org.jeesl.model.ejb.io.label.revision;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionViewMapping;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.label.entity.IoLabelEntity;

@Entity
@Table(name="IoRevisionMappingView",uniqueConstraints = @UniqueConstraint(columnNames = {"view_id","entity_id","mapping_id"}))
@EjbErNode(name="View Mapping",category="revision",subset="revision",level=3)
public class IoRevisionViewMapping implements JeeslRevisionViewMapping<IoRevisionView,IoLabelEntity,IoRevisionEntityMapping>
{
	public static final long serialVersionUID=1;
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@NotNull @ManyToOne
	private IoRevisionView view;
	@Override public IoRevisionView getView() {return view;}
	@Override public void setView(IoRevisionView view) {this.view = view;}

	@NotNull @ManyToOne
	private IoLabelEntity entity;
	@Override public IoLabelEntity getEntity() {return entity;}
	@Override public void setEntity(IoLabelEntity entity) {this.entity = entity;}
	
	@NotNull @ManyToOne
	@JoinColumn(name="mapping_id")
	private IoRevisionEntityMapping entityMapping;
	@Override public IoRevisionEntityMapping getEntityMapping() {return entityMapping;}
	@Override public void setEntityMapping(IoRevisionEntityMapping entityMapping) {this.entityMapping = entityMapping;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}
	
	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}


	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
	
	@Override public boolean equals(Object object){return (object instanceof IoRevisionViewMapping) ? id == ((IoRevisionViewMapping) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
}