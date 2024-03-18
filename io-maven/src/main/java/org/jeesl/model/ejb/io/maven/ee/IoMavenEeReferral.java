package org.jeesl.model.ejb.io.maven.ee;

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
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;

@Entity
@Table(name="IoMavenEeReferral")
@EjbErNode(name="Version",category="ioMaven",subset="ioMaven")
public class IoMavenEeReferral implements EjbSaveable
{
	public static final long serialVersionUID=1;	


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@ManyToOne @NotNull
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoMavenEeReferral_edition"))
	private IoMavenEeEdition edition;
	public IoMavenEeEdition getEdition() {return edition;}
	public void setEdition(IoMavenEeEdition edition) {this.edition = edition;}

	@ManyToOne @NotNull
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoMavenEeReferral_standard"))
	private IoMavenEeStandard standard;
	public IoMavenEeStandard getStandard() {return standard;}
	public void setStandard(IoMavenEeStandard standard) {this.standard = standard;}
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoMavenEeReferral_artifiact"))
	private IoMavenVersion artifact;
	public IoMavenVersion getArtifact() {return artifact;}
	public void setArtifact(IoMavenVersion artifact) {this.artifact = artifact;}
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoMavenEeReferral_bom"))
	private IoMavenVersion bom;
	public IoMavenVersion getBom() {return bom;}
	public void setBom(IoMavenVersion bom) {this.bom = bom;}

	@Basic @Column(columnDefinition="text")
	private String remark;
	public String getRemark() {return remark;}
	public void setRemark(String remark) {this.remark = remark;}
	

	@Override public boolean equals(Object object){return (object instanceof IoMavenEeReferral) ? id == ((IoMavenEeReferral) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}