package org.jeesl.model.ejb.io.mail.template;

import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateToken;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;

@Entity
@Table(name="IoTemplateToken",uniqueConstraints=@UniqueConstraint(columnNames={"template_id","code"}))
@EjbErNode(name="Token",category="system",subset="template")
public class IoTemplateToken implements JeeslIoTemplateToken<IoLang,IoDescription,IoTemplate,IoTemplateTokenType>
{
	public static final long serialVersionUID=1;	


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@Override public String resolveParentAttribute() {return "template";}
	@ManyToOne
	private IoTemplate template;
	public IoTemplate getTemplate() {return template;}
	public void setTemplate(IoTemplate template) {this.template = template;}

	@NotNull private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}
	
	@NotNull @ManyToOne
	private IoTemplateTokenType type;
	@Override public IoTemplateTokenType getType() {return type;}
	@Override public void setType(IoTemplateTokenType type) {this.type = type;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name = "lkey")
	@JoinTable(name="IoTemplateTokenJtLang",joinColumns={@JoinColumn(name="token_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	private Map<String,IoLang> name;
	public Map<String,IoLang> getName() {return name;}
	public void setName(Map<String, IoLang> name) {this.name = name;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name = "lkey")
	@JoinTable(name="IoTemplateTokenJtDescription",joinColumns={@JoinColumn(name="token_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	private Map<String,IoDescription> description;
	public Map<String,IoDescription> getDescription() {return description;}
	public void setDescription(Map<String,IoDescription> description) {this.description = description;}
	
	@Basic @Column(columnDefinition = "text")
	private String example;
	@Override public String getExample() {return example;}
	@Override public void setExample(String example) {this.example = example;}


	@Override public boolean equals(Object object){return (object instanceof IoTemplateToken) ? id == ((IoTemplateToken) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}