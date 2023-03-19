package org.jeesl.model.ejb.system.property;

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
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.property.JeeslProperty;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;

@Entity
@Table(name="SystemProperty")
public class SystemProperty implements JeeslProperty<IoLang,IoDescription,SystemPropertyCategory,SystemProperty>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
    @Override public long getId() {return id;}
    @Override public void setId(long id) {this.id = id;}

	@ManyToOne @NotNull
	private SystemPropertyCategory category;
	@Override public SystemPropertyCategory getCategory() {return category;}
	@Override public void setCategory(SystemPropertyCategory category) {this.category = category;}
   
	@NotNull
	private String key;
    @Override public String getKey() {return key;}
    @Override public void setKey(String key) {this.key=key;}
   
	@NotNull
	private String value;
    @Override public String getValue() {return value;}
    @Override public void setValue(String value) {this.value=value;}
   
    private boolean frozen;
	@Override public boolean isFrozen(){return frozen;}
	@Override public void setFrozen(boolean frozen) {this.frozen=frozen;}

	private Integer position;
	@Override public Integer getPosition() {return position;}
	@Override public void setPosition(Integer position) {this.position = position;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="SystemPropertyJtLang",joinColumns={@JoinColumn(name="property_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	@MapKey(name="lkey")
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="SystemPropertyJtDescription",joinColumns={@JoinColumn(name="property_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	@MapKey(name="lkey")
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}

	private Boolean documentation;
	@Override public Boolean getDocumentation() {return documentation;}
	@Override public void setDocumentation(Boolean documentation) {this.documentation = documentation;}


	@Override public boolean equals(Object object) {return (object instanceof SystemProperty) ? id == ((SystemProperty) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,51).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		sb.append(" ").append(key);
		sb.append(":"+value);
		return sb.toString();
	}
}