package org.jeesl.model.ejb.io.maven.dependency;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenVersion;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Entity
@Table(name="IoMavenVersion")
@EjbErNode(name="Version",category="ioMaven",subset="ioMaven")
public class IoMavenVersion implements JeeslIoMavenVersion<IoMavenArtifact>
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
	private IoMavenArtifact artifact;
	@Override public IoMavenArtifact getArtifact() {return artifact;}
	@Override public void setArtifact(IoMavenArtifact artifact) {this.artifact = artifact;}


	@Override public boolean equals(Object object){return (object instanceof IoMavenVersion) ? id == ((IoMavenVersion) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}