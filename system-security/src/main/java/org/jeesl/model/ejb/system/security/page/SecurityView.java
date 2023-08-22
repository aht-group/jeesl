package org.jeesl.model.ejb.system.security.page;

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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.system.security.SecurityCategory;
import org.jeesl.model.ejb.system.security.access.SecurityRole;
import org.jeesl.model.ejb.system.security.access.SecurityUsecase;

@Entity
@Table(name="SecurityView",uniqueConstraints=@UniqueConstraint(columnNames={"code"}))
@EjbErNode(name="SecurityView")
public class SecurityView implements JeeslSecurityView<IoLang,IoDescription,SecurityCategory,SecurityRole,SecurityUsecase,SecurityAction>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@Override public String resolveParentAttribute() {return JeeslSecurityView.Attributes.category.toString();}
	@NotNull @ManyToOne
	private SecurityCategory category;
	@Override public SecurityCategory getCategory() {return category;}
	@Override public void setCategory(SecurityCategory category) {this.category = category;}

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

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="SecurityViewJtLang",joinColumns={@JoinColumn(name="view_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="SecurityViewJtDescription",joinColumns={@JoinColumn(name="view_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}

	@OneToMany(fetch=FetchType.EAGER,mappedBy="view")
	private List<SecurityAction> actions;
	@Override public List<SecurityAction> getActions() {if(Objects.isNull(actions)) {actions=new ArrayList<>();} return actions;}
	@Override public void setActions(List<SecurityAction> actions) {this.actions = actions;}

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="SecurityRoleJtView",joinColumns={@JoinColumn(name="view_id")},inverseJoinColumns={@JoinColumn(name="role_id")})
	private List<SecurityRole> roles;
	@Override public List<SecurityRole> getRoles() {if(Objects.isNull(roles)) {roles = new ArrayList<>();} return roles;}
	@Override public void setRoles(List<SecurityRole> roles) {this.roles = roles;}

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="SecurityUsecaseJtView",joinColumns={@JoinColumn(name="view_id")},inverseJoinColumns={@JoinColumn(name="usecase_id")})
	private List<SecurityUsecase> usecases;
	@Override public List<SecurityUsecase> getUsecases() {if(Objects.isNull(usecases)) {usecases = new ArrayList<>();} return usecases;}
	@Override public void setUsecases(List<SecurityUsecase> usecases) {this.usecases = usecases;}

	private Boolean accessPublic;
	@Override public Boolean getAccessPublic() {return accessPublic;}
	@Override public void setAccessPublic(Boolean accessPublic) {this.accessPublic = accessPublic;}

	private Boolean accessLogin;
	@Override public Boolean getAccessLogin() {return accessLogin;}
	@Override public void setAccessLogin(Boolean accessLogin) {this.accessLogin = accessLogin;}

	private Boolean redirect;
	@Override public Boolean getRedirect() {return redirect;}
	@Override public void setRedirect(Boolean redirect) {this.redirect = redirect;}

	private Boolean maintenance;
	@Override public Boolean getMaintenance() {return maintenance;}
	@Override public void setMaintenance(Boolean maintenance) {this.maintenance = maintenance;}

	private String packageName;
	@Override public String getPackageName() {return packageName;}
	@Override public void setPackageName(String packageName) {this.packageName = packageName;}

	private String viewPattern;
	@Override public String getViewPattern() {return viewPattern;}
	@Override public void setViewPattern(String viewPattern) {this.viewPattern = viewPattern;}

	private String urlMapping;
	@Override public String getUrlMapping() {return urlMapping;}
	@Override public void setUrlMapping(String urlMapping) {this.urlMapping = urlMapping;}

	private String urlBase;
	@Override public String getUrlBase() {return urlBase;}
	@Override public void setUrlBase(String urlBase) {this.urlBase = urlBase;}


	@Override public boolean equals(Object object){return (object instanceof SecurityView) ? id == ((SecurityView) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		sb.append(" code="+code);
		return sb.toString();
	}
}