package org.jeesl.model.ejb.io.maven.dependency;

import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenVersion;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Entity
@Table(name="IoMavenVersion")
@EjbErNode(name="Version",category="ioMaven",subset="ioMaven")
public class IoMavenVersion implements JeeslIoMavenVersion<IoMavenArtifact,IoMavenOutdate,IoMavenMaintainer>
{
	public static final long serialVersionUID=1;	


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@Override public String resolveParentAttribute() {return JeeslIoMavenVersion.Attributes.artifact.toString();}
	@ManyToOne @NotNull
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoMavenVersion_artifact"))
	private IoMavenArtifact artifact;
	@Override public IoMavenArtifact getArtifact() {return artifact;}
	@Override public void setArtifact(IoMavenArtifact artifact) {this.artifact = artifact;}
	
	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}
	
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}
	
	private String label;
	@Override public String getLabel() {return label;}
	@Override public void setLabel(String label) {this.label = label;}
	
	@Basic @Column(columnDefinition="text")
	private String remark;
	@Override public String getRemark() {return remark;}
	@Override public void setRemark(String remark) {this.remark = remark;}
	
	@ManyToOne @NotNull
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoMavenVersion_outdate"))
	private IoMavenOutdate outdate;
	@Override public IoMavenOutdate getOutdate() {return outdate;}
	@Override public void setOutdate(IoMavenOutdate outdate) {this.outdate = outdate;}
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoMavenVersion_maintainer"))
	private IoMavenMaintainer maintainer;
	@Override public IoMavenMaintainer getMaintainer() {return maintainer;}
	@Override public void setMaintainer(IoMavenMaintainer maintainer) {this.maintainer = maintainer;}


	@Override public boolean equals(Object object){return (object instanceof IoMavenVersion) ? id == ((IoMavenVersion) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("] ");
		if(Objects.nonNull(artifact))
		{
			if(Objects.nonNull(artifact.getGroup())) {sb.append(artifact.getGroup().getCode()).append(":");}
			sb.append(artifact.getCode()).append(":");
		}
		sb.append(code);
		return sb.toString();
	}
}