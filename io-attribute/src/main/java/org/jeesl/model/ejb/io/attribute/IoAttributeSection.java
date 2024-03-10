package org.jeesl.model.ejb.io.attribute;

import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
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
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSection;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoLang;

@Entity
@Table(name="IoAttributeSection")
@EjbErNode(name="Option",category="surveyDefinition",subset="auSurvey,lcfEval,moduleTs")
public class IoAttributeSection implements JeeslAttributeSection<IoLang,IoAttributeSet>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

//	@Override public String resolveParentAttribute() {return JeeslAttributeOption.Attributes.criteria.toString();}
	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAttributeSection_set"))
	private IoAttributeSet set;
	public IoAttributeSet getSet() {return set;}
	public void setSet(IoAttributeSet set) {this.set = set;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="IoAttributeSectionJtLang",
		joinColumns={@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAttributeSectionJtLang_section"),name="section_id")},
		inverseJoinColumns={@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAttributeSectionJtLang_lang"),name="lang_id")},
		uniqueConstraints=@UniqueConstraint(columnNames={"lang_id"},name="uk_IoAttributeSectionJtLang_lang"))
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}

	@Override public boolean equals(Object object){return (object instanceof IoAttributeSection) ? id == ((IoAttributeSection) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(41,43).append(id).toHashCode();}
}