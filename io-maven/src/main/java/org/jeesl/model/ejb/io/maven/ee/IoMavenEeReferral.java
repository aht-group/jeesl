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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.maven.ee.JeeslIoMavenEeReferral;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;

@Entity
@Table(name="IoMavenEeReferral",uniqueConstraints={@UniqueConstraint(name="uk_IoMavenEeReferral_edition_standard_recommendation",columnNames={"edition_id","standard_id","recommendation"})})
@EjbErNode(name="Version",category="ioMaven",subset="ioMaven")
public class IoMavenEeReferral implements JeeslIoMavenEeReferral<IoMavenVersion,IoMavenEeEdition,IoMavenEeStandard>
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
	
	private int position;
	public int getPosition() {return position;}
	public void setPosition(int position) {this.position = position;}

	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoMavenEeReferral_artifiact"))
	private IoMavenVersion artifact;
	@Override public IoMavenVersion getArtifact() {return artifact;}
	@Override public void setArtifact(IoMavenVersion artifact) {this.artifact = artifact;}

	@Basic @Column(columnDefinition="text")
	private String remark;
	public String getRemark() {return remark;}
	public void setRemark(String remark) {this.remark = remark;}
	
	private Boolean recommendation;
	@Override public Boolean getRecommendation() {return recommendation;}
	@Override public void setRecommendation(Boolean recommendation) {this.recommendation = recommendation;}
	
	@Override public boolean equals(Object object){return (object instanceof IoMavenEeReferral) ? id == ((IoMavenEeReferral) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}