package org.jeesl.model.ejb.io.maven.module;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.maven.usage.JeeslIoMavenModule;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;

@Entity
@Table(name="IoMavenModule")
@EjbErNode(name="Module",category="ioMaven",subset="ioMaven")
public class IoMavenModule implements JeeslIoMavenModule<IoMavenModule,IoMavenStructure,IoMavenType,IoGraphic>
{
	public static final long serialVersionUID=1;	


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@Override public String resolveParentAttribute() {return JeeslIoMavenModule.Attributes.parent.toString();}
	@ManyToOne
	private IoMavenModule parent;
	@Override public IoMavenModule getParent() {return parent;}
	@Override public void setParent(IoMavenModule parent) {this.parent = parent;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}
	
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}
	
	private String label;
	@Override public String getLabel() {return label;}
	@Override public void setLabel(String label) {this.label = label;}

	@ManyToOne
	private IoMavenStructure structure;
	@Override public IoMavenStructure getStructure() {return structure;}
	@Override public void setStructure(IoMavenStructure structure) {this.structure = structure;}
	
	@ManyToOne
	private IoMavenType type;
	@Override public IoMavenType getType() {return type;}
	@Override public void setType(IoMavenType type) {this.type = type;}
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private IoGraphic graphic;
	@Override public IoGraphic getGraphic() {return graphic;}
	@Override public void setGraphic(IoGraphic graphic) {this.graphic = graphic;}


	@Override public boolean equals(Object object){return (object instanceof IoMavenModule) ? id == ((IoMavenModule) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
	
}