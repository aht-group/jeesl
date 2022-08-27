package org.jeesl.client.model.ejb.system.security;

import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.client.model.ejb.system.locale.Description;
import org.jeesl.client.model.ejb.system.locale.Lang;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames = {"code"}))
@EjbErNode(name="Action Template",category="security",subset="security")
public class SecurityActionTemplate implements JeeslSecurityTemplate<Lang,Description,SecurityCategory>
{
	public static final long serialVersionUID=1;

	public static enum Code {login}
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	public long getId() {return id;}
	public void setId(long id) {this.id = id;}
			
	@NotNull
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
	
	@Override public String resolveParentAttribute() {return "category";}
	
	@NotNull @ManyToOne
	private SecurityCategory category;
	public SecurityCategory getCategory() {return category;}
	public void setCategory(SecurityCategory category) {this.category = category;}
	
	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}
	
	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}
	
	private boolean documentation;
	@Override public boolean getDocumentation() {return documentation;}
	@Override public void setDocumentation(boolean documentation) {this.documentation = documentation;}
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name = "lkey")
	private Map<String, Lang> name;
	@Override public Map<String, Lang> getName() {return name;}
	@Override public void setName(Map<String, Lang> name) {this.name = name;}
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name = "lkey")
	private Map<String, Description> description;
	@Override public Map<String, Description> getDescription() {return description;}
	@Override public void setDescription(Map<String, Description> description) {this.description = description;}
		
	@Override public boolean equals(Object object) {return (object instanceof SecurityActionTemplate) ? id == ((SecurityActionTemplate) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17, 53).append(id).toHashCode();}
}