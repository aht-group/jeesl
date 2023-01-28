package org.jeesl.model.ejb.system.constraint.algorithm;

import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.constraint.algorithm.JeeslConstraintAlgorithm;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;

@Entity
@Table(name="SystemConstraintAlgorithm",uniqueConstraints=@UniqueConstraint(columnNames={"code"}))
public class SystemConstraintAlgorithm implements JeeslConstraintAlgorithm<IoLang,IoDescription,SystemConstraintAlgorithmCategory>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@Override public String resolveParentAttribute() {return JeeslConstraintAlgorithm.Attributes.category.toString();}
	@NotNull @ManyToOne
	private SystemConstraintAlgorithmCategory category;
	@Override public SystemConstraintAlgorithmCategory getCategory() {return category;}
	@Override public void setCategory(SystemConstraintAlgorithmCategory category) {this.category = category;}
	
	private String code;
	@Override public String getCode(){return code;}
	@Override public void setCode(String code){this.code = code;}
	
	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="SystemConstraintAlgorithmJtLang",joinColumns={@JoinColumn(name="algorithm_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="SystemConstraintAlgorithmJtDescription",joinColumns={@JoinColumn(name="algorithm_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}
	
	
	@Override public boolean equals(Object object){return (object instanceof SystemConstraintAlgorithm) ? id == ((SystemConstraintAlgorithm) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
}