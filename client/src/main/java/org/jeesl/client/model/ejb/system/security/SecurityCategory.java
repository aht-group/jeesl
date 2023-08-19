package org.jeesl.client.model.ejb.system.security;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.jeesl.client.model.ejb.system.locale.Description;
import org.jeesl.client.model.ejb.system.locale.Lang;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames = {"type","code"}))
@EjbErNode(name="Category",category="security",subset="security")
public class SecurityCategory implements Serializable, EjbWithCode,EjbRemoveable,EjbPersistable,
	JeeslSecurityCategory<Lang,Description>
{
	public static final long serialVersionUID=1;
	
	public static enum Code{system,regional}
	

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@NotNull
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}
	
	@NotNull
	private String type;
	@Override public String getType() {return type;}
	@Override public void setType(String type) {this.type = type;}
	
	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}
	
	private Boolean documentation;
	@Override public Boolean getDocumentation() {return documentation;}
	@Override public void setDocumentation(Boolean documentation) {this.documentation = documentation;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name = "lkey")
	private Map<String, Lang> name;
	public Map<String, Lang> getName() {return name;}
	public void setName(Map<String, Lang> name) {this.name = name;}
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name = "lkey")
	private Map<String, Description> description;
	public Map<String, Description> getDescription() {return description;}
	public void setDescription(Map<String, Description> description) {this.description = description;}
	
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("id="+id);
		return sb.toString();
	}
	
	@Override public boolean equals(Object object) {return (object instanceof SecurityCategory) ? id == ((SecurityCategory) object).getId() : (object == this);}
}