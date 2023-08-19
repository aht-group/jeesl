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
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames = {"code"}))
@EjbErNode(name="Action",category="security",subset="security")
public class SecurityAction implements JeeslSecurityAction<Lang,Description,SecurityRole,SecurityView,SecurityUsecase,SecurityActionTemplate>
{
	public static final long serialVersionUID=1;

	public static enum Code {login}
	
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
	private SecurityActionTemplate template;
	@Override public SecurityActionTemplate getTemplate() {return template;}
	@Override public void setTemplate(SecurityActionTemplate template) {this.template = template;}
		
	@NotNull
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}
	
//	@Override public String toCode2()
//	{
//		StringBuffer sb = new StringBuffer();
//		if(template==null){sb.append(code);}
//		else
//		{
//	    	sb.append(view.getCode());
//	    	sb.append(template.getCode().substring(template.getCode().lastIndexOf("."), template.getCode().length()));
//		}
//		return sb.toString();
//	}
	public Map<String,Lang> toName()
	{
		if(template==null){return name;}
		else{return template.getName();}
	}
	
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
	@Override public Map<String, Lang> getName() {return name;}
	@Override public void setName(Map<String, Lang> name) {this.name = name;}
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name = "lkey")
	private Map<String, Description> description;
	@Override public Map<String, Description> getDescription() {return description;}
	@Override public void setDescription(Map<String, Description> description) {this.description = description;}
	
	@ManyToMany(fetch=FetchType.LAZY)
	private List<SecurityRole> roles;
	@Override public List<SecurityRole> getRoles() {if(roles==null){roles = new ArrayList<SecurityRole>();}return roles;}
	@Override public void setRoles(List<SecurityRole> roles) {this.roles = roles;}
	
	@ManyToMany(fetch=FetchType.LAZY)
	private List<SecurityUsecase> usecases;
	@Override public List<SecurityUsecase> getUsecases() {if(usecases==null){usecases = new ArrayList<SecurityUsecase>();}return usecases;}
	@Override public void setUsecases(List<SecurityUsecase> usecases) {this.usecases = usecases;}

	
	@Override public boolean equals(Object object){return (object instanceof SecurityAction) ? id == ((SecurityAction) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17, 53).append(id).toHashCode();}
}