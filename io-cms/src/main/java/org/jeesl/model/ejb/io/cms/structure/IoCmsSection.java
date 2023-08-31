package org.jeesl.model.ejb.io.cms.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsSection;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoLang;

@Entity
@Table(name="IoCmsSection")
@EjbErNode(name="Section",category="system",subset="cms")
public class IoCmsSection implements JeeslIoCmsSection<IoLang,IoCmsSection>
{
	public static final long serialVersionUID=1;	


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@Override public String resolveParentAttribute() {return JeeslIoCmsSection.Attributes.section.toString();}
	@ManyToOne
	private IoCmsSection section;
	@Override public IoCmsSection getSection() {return section;}
	@Override public void setSection(IoCmsSection section) {this.section = section;}

	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="IoCmsSectionJtLang",joinColumns={@JoinColumn(name="section_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="section")
	@OrderBy("position ASC")
	private List<IoCmsSection> sections;
	@Override public List<IoCmsSection> getSections() {if(Objects.isNull(sections)) {sections = new ArrayList<>();} return sections;}
	@Override public void setSections(List<IoCmsSection> sections) {this.sections = sections;}


	@Override public boolean equals(Object object){return (object instanceof IoCmsSection) ? id == ((IoCmsSection) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}