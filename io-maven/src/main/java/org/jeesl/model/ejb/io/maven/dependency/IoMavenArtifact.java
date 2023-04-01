package org.jeesl.model.ejb.io.maven.dependency;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenArtifact;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.maven.classification.IoMavenSuitability;

@Entity
@Table(name="IoMavenArtifact")
@EjbErNode(name="Artifact",category="ioMaven",subset="ioMaven")
public class IoMavenArtifact implements JeeslIoMavenArtifact<IoMavenGroup,IoMavenSuitability>
{
	public static final long serialVersionUID=1;	


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	@ManyToOne @NotNull
	private IoMavenGroup group;
	@Override public IoMavenGroup getGroup() {return group;}
	@Override public void setGroup(IoMavenGroup group) {this.group = group;}

	@ManyToOne @NotNull
	private IoMavenSuitability suitability;
	@Override public IoMavenSuitability getSuitability() {return suitability;}
	@Override public void setSuitability(IoMavenSuitability suitability) {this.suitability = suitability;}


	@Override public boolean equals(Object object){return (object instanceof IoMavenArtifact) ? id == ((IoMavenArtifact) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}