package org.jeesl.model.ejb.io.locale;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.locale.JeeslLang;

@Entity
@Table(name="IoLang2")
public class IoLang implements JeeslLang
{
	public static final long serialVersionUID=1;
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@NotNull
	private String lkey;
	@Override public String getLkey() {return lkey;}
	@Override public void setLkey(String lkey) {this.lkey = lkey;}
	
	@NotNull
	private String lang;
	@Override public String getLang() {return lang;}
	@Override public void setLang(String name) {this.lang = name;}
	
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		sb.append(" (").append(lkey).append(")");
		sb.append(" "+lang);
		return sb.toString();
	}
	
	@Override public boolean equals(Object object) {return (object instanceof IoLang) ? id == ((IoLang) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(19,21).append(id).toHashCode();}
}