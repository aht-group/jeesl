package org.jeesl.model.ejb.system.job.template;

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

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.job.template.JeeslJobTemplate;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.system.job.cache.SystemJobExpiration;
import org.jeesl.model.ejb.system.job.core.SystemJobPriority;

@Entity
@Table(name="SystemJobTemplate",uniqueConstraints={@UniqueConstraint(columnNames={"type_id","code"})})
@EjbErNode(name="Template")
public class SystemJobTemplate implements JeeslJobTemplate<IoLang,IoDescription,SystemJobCategory,SystemJobType,SystemJobPriority,SystemJobExpiration>
{
	public static final long serialVersionUID=1;
	
	public static enum Code{mail};
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	@ManyToOne
	private SystemJobType type;
	@Override public SystemJobType getType() {return type;}
	@Override public void setType(SystemJobType type) {this.type = type;}
	
	@ManyToOne
	private SystemJobCategory category;
	@Override public SystemJobCategory getCategory() {return category;}
	@Override public void setCategory(SystemJobCategory category) {this.category = category;}
	
	@ManyToOne
	private SystemJobPriority priority;
	@Override public SystemJobPriority getPriority() {return priority;}
	@Override public void setPriority(SystemJobPriority priority) {this.priority = priority;}
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="SystemJobTemplateJtLang",joinColumns={@JoinColumn(name="template_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	@MapKey(name="lkey")
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="JobTemplateJtDescription",joinColumns={@JoinColumn(name="template_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}
	
	private int timeout;
	@Override public int getTimeout() {return timeout;}
	@Override public void setTimeout(int timeout) {this.timeout = timeout;}
	
	@ManyToOne
	private SystemJobExpiration expiration;
	@Override public SystemJobExpiration getExpiration() {return expiration;}
	@Override public void setExpiration(SystemJobExpiration expiration) {this.expiration = expiration;}
	
	@Override public boolean equals(Object object){return (object instanceof SystemJobTemplate) ? id == ((SystemJobTemplate) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(21,43).append(id).toHashCode();}
}