package org.jeesl.model.ejb.io.maven.dependency;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenDependency;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Entity
@Table(name="IoMavenDependency",uniqueConstraints=@UniqueConstraint(name="uk_IoMavenDependency_artifact_depends",columnNames={"artifact_id","dependsOn_id"}))
@EjbErNode(name="Version",category="ioMaven",subset="ioMaven")
public class IoMavenDependency implements JeeslIoMavenDependency<IoMavenVersion>
{
	public static final long serialVersionUID=1;	


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@ManyToOne @NotNull
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoMavenDependency_artifact"))
	private IoMavenVersion artifact;
	@Override public IoMavenVersion getArtifact() {return artifact;}
	@Override public void setArtifact(IoMavenVersion artifact) {this.artifact = artifact;}

	@ManyToOne @NotNull
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoMavenDependency_dependsOn"))
	private IoMavenVersion dependsOn;
	@Override public IoMavenVersion getDependsOn() {return dependsOn;}
	@Override public void setDependsOn(IoMavenVersion dependsOn) {this.dependsOn = dependsOn;}
	
	
	@Override public boolean equals(Object object){return (object instanceof IoMavenDependency) ? id == ((IoMavenDependency) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}