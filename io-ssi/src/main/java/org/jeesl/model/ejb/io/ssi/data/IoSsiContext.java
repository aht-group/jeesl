package org.jeesl.model.ejb.io.ssi.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.label.entity.IoLabelEntity;
import org.jeesl.model.ejb.io.ssi.core.IoSsiSystem;

@Entity
@Table(name="IoSsiContext")
@EjbErNode(name="Mapping",category="ssi",subset="systemSsi")
public class IoSsiContext implements JeeslIoSsiContext<IoSsiSystem,IoLabelEntity>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@Override public String resolveParentAttribute() {return JeeslIoSsiContext.Attributes.system.toString();}
	@NotNull @ManyToOne
	private IoSsiSystem system;
	@Override public IoSsiSystem getSystem(){return system;}
	@Override public void setSystem(IoSsiSystem system){this.system = system;}

	@NotNull @ManyToOne
	private IoLabelEntity json;
	@Override public IoLabelEntity getJson() {return json;}
	@Override public void setJson(IoLabelEntity json) {this.json = json;}

	@NotNull @ManyToOne
	private IoLabelEntity entity;
	@Override public IoLabelEntity getEntity() {return entity;}
	@Override public void setEntity(IoLabelEntity entity) {this.entity = entity;}

	@ManyToOne
	private IoLabelEntity classA;
	@Override public IoLabelEntity getClassA() {return classA;}
	@Override public void setClassA(IoLabelEntity classA) {this.classA = classA;}

	@ManyToOne
	private IoLabelEntity classB;
	@Override public IoLabelEntity getClassB() {return classB;}
	@Override public void setClassB(IoLabelEntity classB) {this.classB = classB;}

	@ManyToOne
	private IoLabelEntity classC;
	@Override public IoLabelEntity getClassC() {return classC;}
	@Override public void setClassC(IoLabelEntity classC) {this.classC = classC;}

	private String name;
	@Override public String getName() {return name;}
	@Override public void setName(String name) {this.name = name;}


	@Override public boolean equals(Object object){return (object instanceof IoSsiContext) ? id == ((IoSsiContext) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}