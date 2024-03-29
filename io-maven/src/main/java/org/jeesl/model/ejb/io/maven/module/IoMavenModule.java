package org.jeesl.model.ejb.io.maven.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.maven.usage.JeeslIoMavenModule;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeEdition;

@Entity
@Table(name="IoMavenModule")
@EjbErNode(name="Module",category="ioMaven",subset="ioMaven")
public class IoMavenModule implements JeeslIoMavenModule<IoMavenModule,IoMavenStructure,IoMavenType,IoMavenEeEdition,IoGraphic>
{
	public static final long serialVersionUID=1;	


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@Override public String resolveParentAttribute() {return JeeslIoMavenModule.Attributes.parent.toString();}
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoMavenModule_parent"))
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
	
	private String abbreviation;
	@Override public String getAbbreviation() {return abbreviation;}
	@Override public void setAbbreviation(String abbreviation) {this.abbreviation = abbreviation;}
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoMavenModule_structure"))
	private IoMavenStructure structure;
	@Override public IoMavenStructure getStructure() {return structure;}
	@Override public void setStructure(IoMavenStructure structure) {this.structure = structure;}
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoMavenModule_type"))
	private IoMavenType type;
	@Override public IoMavenType getType() {return type;}
	@Override public void setType(IoMavenType type) {this.type = type;}
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private IoGraphic graphic;
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoMavenModule_graphic"))
	@Override public IoGraphic getGraphic() {return graphic;}
	@Override public void setGraphic(IoGraphic graphic) {this.graphic = graphic;}
	
	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_iomavenmodule_jdk"))
	private IoMavenJdk jdk;
	public IoMavenJdk getJdk() {return jdk;}
	public void setJdk(IoMavenJdk jdk) {this.jdk = jdk;}
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="IoMavenModuleEe",joinColumns={@JoinColumn(name="module_id")},inverseJoinColumns={@JoinColumn(name="ee_id")})
	private List<IoMavenEeEdition> enterpriseEditions;
	@Override public List<IoMavenEeEdition> getEnterpriseEditions() {if(Objects.isNull(enterpriseEditions)) {enterpriseEditions = new ArrayList<>();} return enterpriseEditions;}
	@Override public void setEnterpriseEditions(List<IoMavenEeEdition> enterpriseEditions) {this.enterpriseEditions = enterpriseEditions;}


	@Override public boolean equals(Object object){return (object instanceof IoMavenModule) ? id == ((IoMavenModule) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
	
}