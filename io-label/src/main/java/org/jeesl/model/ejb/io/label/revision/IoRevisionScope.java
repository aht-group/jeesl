package org.jeesl.model.ejb.io.label.revision;

import java.util.ArrayList;
import java.util.List;
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
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionScope;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.label.entity.IoLabelAttribute;
import org.jeesl.model.ejb.io.label.entity.IoLabelCategory;
import org.jeesl.model.ejb.io.locale.IoDescription;

@Entity
@Table(name="IoRevisionScope", uniqueConstraints=@UniqueConstraint(columnNames={"code"}))
@EjbErNode(name="Scope",category="revision",subset="revision")
public class IoRevisionScope implements JeeslRevisionScope<IoLang,IoDescription,IoLabelCategory,IoLabelAttribute>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@Override public String resolveParentAttribute() {return "category";}
	@NotNull @ManyToOne
	private IoLabelCategory category;
	@Override public IoLabelCategory getCategory() {return category;}
	@Override public void setCategory(IoLabelCategory category) {this.category = category;}

	@NotNull private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name = "lkey")
	@JoinTable(name="IoRevisionScopeJtLang",joinColumns={@JoinColumn(name="scope_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name = "lkey")
	@JoinTable(name="IoRevisionScopeJtDescription",joinColumns={@JoinColumn(name="scope_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}

	@OneToMany(fetch=FetchType.LAZY)
	@JoinTable(name="IoRevisionScopeJtAttribute",joinColumns={@JoinColumn(name="scope_id")},inverseJoinColumns={@JoinColumn(name="attribute_id")})
	private List<IoLabelAttribute> attributes;
	@Override public List<IoLabelAttribute> getAttributes() {if(attributes==null){attributes=new ArrayList<IoLabelAttribute>();} return attributes;}
	@Override public void setAttributes(List<IoLabelAttribute> attributes) {this.attributes = attributes;}


	@Override public boolean equals(Object object){return (object instanceof IoRevisionScope) ? id == ((IoRevisionScope) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}