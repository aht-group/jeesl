package org.jeesl.client.model.ejb.system.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.client.model.ejb.system.locale.Description;
import org.jeesl.client.model.ejb.system.locale.Lang;
import org.jeesl.client.model.ejb.user.User;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"code"}))
@EjbErNode(name="Role",category="security",subset="security,ts")
public class SecurityRole implements JeeslSecurityRole<Lang,Description,SecurityCategory,SecurityView,SecurityUsecase,SecurityAction>
{	
	public static final long serialVersionUID=1;
	

	public static enum Code {systemAht}
	public static enum CodeRegion {regionalManager,regionalEditor}
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	public long getId() {return id;}
	public void setId(long id) {this.id = id;}
	
	@NotNull @ManyToOne
	private SecurityCategory category;
	public SecurityCategory getCategory() {return category;}
	public void setCategory(SecurityCategory category) {this.category = category;}
	
	@Override public String resolveParentAttribute() {return "category";}
	
	@NotNull
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}
	
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
	
	@ManyToMany(fetch=FetchType.EAGER)
	private List<SecurityView> views;
	public List<SecurityView> getViews() {if(views==null){views = new ArrayList<SecurityView>();}return views;}
	public void setViews(List<SecurityView> views) {this.views = views;}
	
	@ManyToMany(fetch=FetchType.EAGER)
	private List<SecurityAction> actions;
	public List<SecurityAction> getActions() {if(actions==null){actions = new ArrayList<SecurityAction>();}return actions;}
	public void setActions(List<SecurityAction> actions) {this.actions = actions;}
	
	@ManyToMany(fetch=FetchType.EAGER)
	private List<SecurityUsecase> usecases;
	public List<SecurityUsecase> getUsecases() {if(usecases==null){usecases = new ArrayList<SecurityUsecase>();}return usecases;}
	public void setUsecases(List<SecurityUsecase> usecases) {this.usecases = usecases;}
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "User_SecurityRole")
	private List<User> users;
	public List<User> getUsers() {return users;}
	public void setUsers(List<User> users) {this.users = users;}

	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append(SecurityRole.class.getSimpleName());
		sb.append("-").append(id);
		sb.append(" ").append(code);
		sb.append("]");
		return sb.toString();
	}
	
	@Override public boolean equals(Object object){return (object instanceof SecurityRole) ? id == ((SecurityRole) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17, 53).append(id).toHashCode();}
}