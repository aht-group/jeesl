package org.jeesl.model.ejb.io.cms.content;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsElement;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.cms.authoring.IoCmsVisibility;
import org.jeesl.model.ejb.io.cms.structure.IoCmsSection;
import org.jeesl.model.ejb.io.fr.IoFileContainer;

@Entity
@Table(name="IoCmsElement")
@EjbErNode(name="Element",category="system",subset="cms")
public class IoCmsElement implements JeeslIoCmsElement<IoCmsVisibility,IoCmsSection,IoCmsElementCategory,IoCmsElementType,IoCmsContent,IoFileContainer>
{
	public static final long serialVersionUID=1;	


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@Override public String resolveParentAttribute() {return JeeslIoCmsElement.Attributes.section.toString();}
	@NotNull @ManyToOne
	private IoCmsSection section;
	@Override public IoCmsSection getSection() {return section;}
	@Override public void setSection(IoCmsSection section) {this.section = section;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	private Boolean visible;
	@Override public Boolean getVisible() {return visible;}
	@Override public void setVisible(Boolean visible) {this.visible = visible;}

	@NotNull @ManyToOne
	private IoCmsElementType type;
	@Override public IoCmsElementType getType() {return type;}
	@Override public void setType(IoCmsElementType type) {this.type = type;}

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER,mappedBy="element")
	@MapKey(name="lkey")
	private Map<String,IoCmsContent> content;
	@Override public Map<String,IoCmsContent> getContent() {if(Objects.isNull(content)) {content=new HashMap<>();} return content;}
	@Override public void setContent(Map<String,IoCmsContent> content) {this.content = content;}

	@Basic @Column(columnDefinition="text")
	private String json;
	@Override public String getJson() {return json;}
	@Override public void setJson(String json) {this.json = json;}

	@OneToOne
	private IoFileContainer frContainer;
	@Override public IoFileContainer getFrContainer() {return frContainer;}
	@Override public void setFrContainer(IoFileContainer frContainer) {this.frContainer = frContainer;}


	@Override public boolean equals(Object object){return (object instanceof IoCmsElement) ? id == ((IoCmsElement) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}