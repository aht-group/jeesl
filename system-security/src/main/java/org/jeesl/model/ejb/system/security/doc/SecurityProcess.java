package org.jeesl.model.ejb.system.security.doc;

import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.security.doc.JeeslSecurityProcess;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoLang;

@Entity
@Table(name="SecurityProcess",uniqueConstraints=@UniqueConstraint(columnNames={"code"}))
@EjbErNode(name="SecurityProcess")
public class SecurityProcess implements JeeslSecurityProcess<IoLang>
{
	public static final long serialVersionUID=1;

	public enum Code{xx}

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@NotNull
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="SecurityProcessJtLang",joinColumns={@JoinColumn(name="process_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}


	@Override public boolean equals(Object object){return (object instanceof SecurityProcess) ? id == ((SecurityProcess) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17, 53).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		sb.append(" code="+code);
		return sb.toString();
	}
}