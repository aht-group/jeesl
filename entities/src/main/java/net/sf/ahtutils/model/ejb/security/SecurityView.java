package net.sf.ahtutils.model.ejb.security;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import net.sf.ahtutils.interfaces.model.crud.EjbPersistable;
import net.sf.ahtutils.interfaces.model.crud.EjbRemoveable;
import net.sf.ahtutils.interfaces.model.security.UtilsSecurityView;
import net.sf.ahtutils.interfaces.model.with.code.EjbWithCode;
import net.sf.ahtutils.model.ejb.status.Description;
import net.sf.ahtutils.model.ejb.status.AhtUtilsLang;
import net.sf.ahtutils.model.ejb.user.AhtUtilsUser;
import net.sf.ahtutils.model.qualifier.EjbErNode;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"code"}))
@EjbErNode(name="View",category="security",subset="security")

public class SecurityView implements EjbWithCode,Serializable,EjbRemoveable,EjbPersistable,
		UtilsSecurityView<AhtUtilsLang,Description,SecurityCategory,SecurityRole,SecurityView,SecurityUsecase,SecurityAction,AhtUtilsUser>
{
	public static enum Code {welcome}
	public static enum CodeSeries {seriesAll,series,season,episode}
	public static enum CodeTag {client,info}
	
	public static final long serialVersionUID=1;
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	public long getId() {return id;}
	public void setId(long id) {this.id = id;}
	
	@NotNull @ManyToOne
	private SecurityCategory category;
	public SecurityCategory getCategory() {return category;}
	public void setCategory(SecurityCategory category) {this.category = category;}
	
	@NotNull
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
	
	@Override public String resolveParentAttribute() {return "category";}
	
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
	private Map<String, AhtUtilsLang> name;
	@Override public Map<String, AhtUtilsLang> getName() {return name;}
	@Override public void setName(Map<String, AhtUtilsLang> name) {this.name = name;}
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name = "lkey")
	private Map<String, Description> description;
	@Override public Map<String, Description> getDescription() {return description;}
	@Override public void setDescription(Map<String, Description> description) {this.description = description;}
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="view")
	private List<SecurityAction> actions;
	@Override public List<SecurityAction> getActions() {if(actions==null){actions=new ArrayList<SecurityAction>();}return actions;}
	@Override public void setActions(List<SecurityAction> actions) {this.actions = actions;}
	
	@ManyToMany(fetch=FetchType.LAZY)
	private List<SecurityUsecase> usecases;
	@Override public List<SecurityUsecase> getUsecases() {if(usecases==null){usecases = new ArrayList<SecurityUsecase>();}return usecases;}
	@Override public void setUsecases(List<SecurityUsecase> usecases) {this.usecases = usecases;}
	
	private Boolean accessPublic;
	@Override public Boolean getAccessPublic() {return accessPublic;}
	@Override public void setAccessPublic(Boolean accessPublic) {this.accessPublic = accessPublic;}
	
	private Boolean accessLogin;
	@Override public Boolean getAccessLogin() {return accessLogin;}
	@Override public void setAccessLogin(Boolean accessLogin) {this.accessLogin = accessLogin;}
	
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
	
	@ManyToMany(fetch=FetchType.LAZY)
	private List<SecurityRole> roles;
	@Override public List<SecurityRole> getRoles() {if(roles==null){roles = new ArrayList<SecurityRole>();}return roles;}
	@Override public void setRoles(List<SecurityRole> roles) {this.roles = roles;}
	
	public boolean equals(Object object)
	{
        return (object instanceof SecurityView) ? id == ((SecurityView) object).getId() : (object == this);
    }	
}