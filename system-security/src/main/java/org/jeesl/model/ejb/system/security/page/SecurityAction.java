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
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.system.security.access.SecurityRole;
import org.jeesl.model.ejb.system.security.access.SecurityUsecase;

@Entity
@Table(name="SecurityAction",uniqueConstraints=@UniqueConstraint(columnNames={"code"}))
@EjbErNode(name="SecurityAction")
public class SecurityAction implements JeeslSecurityAction<IoLang,IoDescription,SecurityRole,SecurityView,SecurityUsecase,SecurityTemplate>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@Override public String resolveParentAttribute() {return JeeslSecurityAction.Attributes.view.toString();}
	@NotNull @ManyToOne
	private SecurityView view;
	@Override public SecurityView getView() {return view;}
	@Override public void setView(SecurityView view) {this.view = view;}

	@ManyToOne
	private SecurityTemplate template;
	@Override public SecurityTemplate getTemplate() {return template;}
	@Override public void setTemplate(SecurityTemplate template) {this.template = template;}

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
	@JoinTable(name="SecurityActionJtLang",joinColumns={@JoinColumn(name="action_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="SecurityActionJtDescription",joinColumns={@JoinColumn(name="action_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="SecurityRoleJtAction",joinColumns={@JoinColumn(name="action_id")},inverseJoinColumns={@JoinColumn(name="role_id")})
	private List<SecurityRole> roles;
	@Override public List<SecurityRole> getRoles() {if(Objects.isNull(roles)) {roles = new ArrayList<>();} return roles;}
	@Override public void setRoles(List<SecurityRole> roles) {this.roles = roles;}

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="SecurityUsecaseJtAction",joinColumns={@JoinColumn(name="action_id")},inverseJoinColumns={@JoinColumn(name="usecase_id")})
	private List<SecurityUsecase> usecases;
	@Override public List<SecurityUsecase> getUsecases() {if(Objects.isNull(usecases)) {usecases = new ArrayList<>();} return usecases;}
	@Override public void setUsecases(List<SecurityUsecase> usecases) {this.usecases = usecases;}


	@Override public boolean equals(Object object){return (object instanceof SecurityAction) ? id == ((SecurityAction) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17, 53).append(id).toHashCode();}

	@Override public Map<String,IoLang> toName()
	{
		if(template==null){return name;}
		else{return template.getName();}
	}
}