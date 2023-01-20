package org.jeesl.model.ejb.io.mail.template;

import java.io.Serializable;
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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateDefinition;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoDescription;

@Entity
@Table(name="IoTemplateDefinition",uniqueConstraints=@UniqueConstraint(columnNames={"template_id","type_id"}))
@EjbErNode(name="Definition",category="system",subset="template")
public class IoTemplateDefinition implements Serializable,EjbRemoveable,EjbPersistable,
							JeeslIoTemplateDefinition<IoDescription,IoTemplateChannel,IoTemplate>
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

	@NotNull @ManyToOne
	private IoTemplateChannel type;
	public IoTemplateChannel getType() {return type;}
	public void setType(IoTemplateChannel type) {this.type = type;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="IoTemplateDefinitionJtDescription",joinColumns={@JoinColumn(name="template_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="IoTemplateDefinitionJtHeader",joinColumns={@JoinColumn(name="definition_id")},inverseJoinColumns={@JoinColumn(name="descripton_id")})
	private Map<String,IoDescription> header;
	@Override public Map<String,IoDescription> getHeader() {return header;}
	@Override public void setHeader(Map<String,IoDescription> header) {this.header = header;}


	@Override public boolean equals(Object object){return (object instanceof IoTemplateDefinition) ? id == ((IoTemplateDefinition) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}