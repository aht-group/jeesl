package org.jeesl.model.ejb.io.ssi.data;

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
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiAttribute;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.label.entity.IoLabelEntity;

@Entity
@Table(name="IoSsiMapping",uniqueConstraints={@UniqueConstraint(columnNames = {"context_id","entity_id","localCode","remoteCode"})})
@EjbErNode(name="Attribute",category="ssi",subset="systemSsi")
public class IoSsiMapping implements JeeslIoSsiAttribute<IoSsiContext,IoLabelEntity>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@Override public String resolveParentAttribute() {return JeeslIoSsiData.Attributes.mapping.toString();}
	@NotNull @ManyToOne
	@JoinColumn(name="context_id")
	private IoSsiContext mapping;
	@Override public IoSsiContext getMapping() {return mapping;}
	@Override public void setMapping(IoSsiContext mapping) {this.mapping = mapping;}

	@NotNull @ManyToOne
	private IoLabelEntity entity;
	@Override public IoLabelEntity getEntity() {return entity;}
	@Override public void setEntity(IoLabelEntity entity) {this.entity = entity;}

	@NotNull
	private String localCode;
	public String getLocalCode() {return localCode;}
	public void setLocalCode(String localCode) {this.localCode = localCode;}

	@NotNull
	private String remoteCode;
	public String getRemoteCode() {return remoteCode;}
	public void setRemoteCode(String remoteCode) {this.remoteCode = remoteCode;}


	@Override public boolean equals(Object object){return (object instanceof IoSsiMapping) ? id == ((IoSsiMapping) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,57).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}