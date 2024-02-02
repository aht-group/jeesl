package org.jeesl.model.ejb.io.ssi.data;

import java.util.HashMap;
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
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiError;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;

@Entity
@Table(name="IoSsiError",uniqueConstraints=@UniqueConstraint(columnNames={"context_id","code"}))
@EjbErNode(name="IoSsiError")
public class IoSsiError implements JeeslIoSsiError<IoLang,IoDescription,IoSsiContext,IoGraphic>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@Override public String resolveParentAttribute() {return JeeslIoSsiError.Attributes.context.toString();}
	@NotNull @ManyToOne
	private IoSsiContext context;
	@Override public IoSsiContext getContext() {return context;}
	@Override public void setContext(IoSsiContext context) {this.context = context;}
	
	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}
	
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="IoSsiErrorJtLang",joinColumns={@JoinColumn(name="error_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	@MapKey(name="lkey")
	protected Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {if(Objects.isNull(name)) {name = new HashMap<>();} return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="IoSsiErrorJtDescription",joinColumns={@JoinColumn(name="error_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {if(Objects.isNull(description)) {description = new HashMap<>();} return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}


	@Override public boolean equals(Object object){return (object instanceof IoSsiError) ? id == ((IoSsiError) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(21,43).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}