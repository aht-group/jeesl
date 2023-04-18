package org.jeesl.model.ejb.io.mail.template;

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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplate;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;

@Entity
@Table(name="IoTemplate", uniqueConstraints=@UniqueConstraint(columnNames={"code"}))
@EjbErNode(name="Template",category="system",subset="template")
public class IoTemplate implements JeeslIoTemplate<IoLang,IoDescription,IoTemplateCategory,IoTemplateScope,IoTemplateDefinition,IoTemplateToken>
{
	public static final long serialVersionUID=1;	


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@Override public String resolveParentAttribute() {return "category";}
	@ManyToOne
	private IoTemplateCategory category;
	@Override public IoTemplateCategory getCategory() {return category;}
	@Override public void setCategory(IoTemplateCategory category) {this.category = category;}

	@ManyToOne
	private IoTemplateScope scope;
	@Override public IoTemplateScope getScope() {return scope;}
	@Override public void setScope(IoTemplateScope scope) {this.scope = scope;}

	@NotNull
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name = "lkey")
	@JoinTable(name="IoTemplateJtLang",joinColumns={@JoinColumn(name="template_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String, IoLang> name) {this.name = name;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name = "lkey")
	@JoinTable(name="IoTemplateJtDescription",joinColumns={@JoinColumn(name="template_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="template")
	@OrderBy("position")
	private List<IoTemplateToken> tokens;
	@Override public List<IoTemplateToken> getTokens() {if(tokens==null) {tokens = new ArrayList<>();} return tokens;}
	@Override public void setTokens(List<IoTemplateToken> tokens) {this.tokens = tokens;}

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="template")
	private List<IoTemplateDefinition> definitions;
	@Override public List<IoTemplateDefinition> getDefinitions() {if(definitions==null) {definitions = new ArrayList<>();} return definitions;}
	@Override public void setDefinitions(List<IoTemplateDefinition> definitions) {this.definitions = definitions;}


	@Override public boolean equals(Object object){return (object instanceof IoTemplate) ? id == ((IoTemplate) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}